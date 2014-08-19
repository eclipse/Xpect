package org.xpect.state;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.xtext.util.IAcceptor;
import org.xpect.parameter.IParameterAdapter;
import org.xpect.parameter.IParameterParser;
import org.xpect.parameter.IParameterProvider;
import org.xpect.parameter.ParameterParser;
import org.xpect.parameter.XpectParameterAdapter;
import org.xpect.runner.XpectRunner;
import org.xpect.state.ResolvedConfiguration.DerivedValue;
import org.xpect.state.ResolvedConfiguration.Factory;
import org.xpect.state.ResolvedConfiguration.PrimaryValue;
import org.xpect.state.ResolvedConfiguration.Value;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Similar to a Guice injector. It is created from the {@link Configuration} by the Xpect runners. It can inject using key (annotation) and type (of object). If the type is unique,
 * the default annotation is used and only the type is used as a key. This injector is aware of dependencies, that is, if an object is to be created which needs other objects,
 * these other objects are created as well.
 * <p>
 * The state container is also used to retrieve arguments for a test method call, see {@link #tryGet(Class, Object...)} for details.
 * 
 */
public class StateContainer {
	protected class FactoryInstance {
		private final List<Instance> dependees = Lists.newArrayList();
		private final Factory factory;
		private final Object instance;

		public FactoryInstance(Factory factory, Object instance) {
			super();
			this.factory = factory;
			this.instance = instance;
		}

	}

	protected abstract class Instance implements Managed<Object> {
		private final List<FactoryInstance> dependees = Lists.newArrayList();
		private final Value key;

		public Instance(Value key) {
			super();
			this.key = key;
		}

		public void invalidate() {
			StateContainer.this.invalidate(this);
		}
	}

	protected class ManagedInstance extends Instance {
		private final Managed<?> value;

		public ManagedInstance(Value key, Managed<?> value) {
			super(key);
			this.value = value;
		}

		public Object get() {
			return value.get();
		}
	}

	protected static class State {
		private final Map<Factory, FactoryInstance> factory2instance = Maps.newLinkedHashMap();

		private final Map<Value, Instance> value2instance = Maps.newLinkedHashMap();
	}

	protected class ValueInstance extends Instance {
		private final Object value;

		public ValueInstance(Value key, Object value) {
			super(key);
			this.value = value;
		}

		public Object get() {
			return value;
		}
	}

	private final ResolvedConfiguration config;

	private final State state;

	public StateContainer(ResolvedConfiguration config) {
		this.config = config;
		this.state = new State();
	}

	public StateContainer(IAcceptor<Configuration> config) {
		Configuration cfg = new Configuration();
		config.accept(cfg);
		this.config = new ResolvedConfiguration(cfg);
		this.state = new State();
	}

	public StateContainer(StateContainer parent, ResolvedConfiguration config) {
		this.config = config;
		this.state = parent.state;
		if (parent.config != this.config.getParent())
			throw new IllegalStateException("Parent configs must be the same");
	}

	public StateContainer(StateContainer parent, IAcceptor<Configuration> config) {
		Configuration cfg = new Configuration();
		config.accept(cfg);
		this.config = new ResolvedConfiguration(cfg);
		this.state = parent.state;
		if (parent.config != this.config.getParent())
			throw new IllegalStateException("Parent configs must be the same");
	}

	protected void collectDependees(FactoryInstance fact, Set<FactoryInstance> factories, Set<Instance> instances) {
		for (Instance i : fact.dependees)
			if (instances.add(i))
				collectDependees(i, factories, instances);
	}

	protected void collectDependees(Instance instance, Set<FactoryInstance> factories, Set<Instance> instances) {
		for (FactoryInstance fi : instance.dependees)
			if (factories.add(fi))
				collectDependees(fi, factories, instances);
	}

	protected FactoryInstance createFactory(Factory factory) {
		Constructor<?> constructor = factory.getConstructor();
		Class<?>[] paramTypes = constructor.getParameterTypes();
		Object[] paramsObjects = new Object[paramTypes.length];
		Instance[] paramsInstances = new Instance[paramTypes.length];
		Annotation[][] paramAnnot = constructor.getParameterAnnotations();
		for (int i = 0; i < paramTypes.length; i++) {
			paramsInstances[i] = (Instance) get(paramTypes[i], (Object[]) paramAnnot[i]);
			paramsObjects[i] = paramsInstances[i].get();
		}
		try {
			Object instance = constructor.newInstance(paramsObjects);
			FactoryInstance result = new FactoryInstance(factory, instance);
			for (Instance inst : paramsInstances)
				inst.dependees.add(result);
			return result;
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	protected Instance createInstance(DerivedValue value) {
		FactoryInstance factory = getFactory(value.getFactory());
		try {
			Object instance = value.getMethod().invoke(factory.instance);
			Instance result = createInstance(value, instance);
			factory.dependees.add(result);
			return result;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	protected Instance createInstance(PrimaryValue value) {
		return createInstance(value, value.getValue());
	}

	protected Instance createInstance(Value value) {
		if (value instanceof PrimaryValue)
			return createInstance((PrimaryValue) value);
		if (value instanceof DerivedValue)
			return createInstance((DerivedValue) value);
		throw new IllegalStateException();
	}

	@SuppressWarnings("unchecked")
	protected Instance createInstance(Value key, Object value) {
		if (value instanceof Managed<?>)
			return new ManagedInstance(key, (Managed<Object>) value);
		return new ValueInstance(key, value);
	}

	public <T> Managed<T> get(Class<T> expectedType, Object... annotations) {
		return get(expectedType, true, annotations);
	}

	/**
	 * The following workflow is performed to get the argument of expected type (annotated with given annotations):
	 * <ol>
	 * <li>get "raw" value:
	 * <ol>
	 * <li>get the parameter from the parser defined by {@link ParameterParser} if possible
	 * <li>if this fails, get value identified via annotations (used as key) and type
	 * </ol>
	 * <li>for all parsers (some parameters may be parsers, e.g., expectations), get the {@link IParameterProvider} via {@code parseRegion()} defined similarly (not equally) in
	 * {@link IParameterParser} subtypes.
	 * <li>if necessary, also create {@link IParameterAdapter}, configured via {@link XpectParameterAdapter}.
	 * <li>use {@link IParameterProvider} or {@link IParameterAdapter} to get argument
	 * </ol>
	 */
	public <T> T tryGet(Class<T> expectedType, Object... annotations) {
		Managed<T> managed = get(expectedType, false, annotations);
		if (managed != null)
			return managed.get();
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T> Managed<T> get(Class<T> expectedType, boolean throwException, Object... annotations) {
		if (expectedType == StateContainer.class)
			return (Managed<T>) new ManagedInstance(null, new ManagedImpl<T>((T) this));
		Class<? extends Annotation> annotatedWith = getAnnotation(annotations);
		Value value = config.getValue(annotatedWith, expectedType);
		if (value == null) {
			if (throwException)
				throw new IllegalStateException("Unknown key @" + annotatedWith.getName() + " " + expectedType.getName());
			else
				return null;
		}
		Instance instance = state.value2instance.get(value);
		if (instance == null)
			state.value2instance.put(value, instance = createInstance(value));
		return (Managed<T>) instance;
	}

	@SuppressWarnings("unchecked")
	protected Class<? extends Annotation> getAnnotation(Object... annotations) {
		if (annotations.length == 0)
			return Default.class;
		Object annotation = annotations[0];
		if (annotation instanceof Class<?>)
			return (Class<? extends Annotation>) annotation;
		if (annotation instanceof Annotation)
			return ((Annotation) annotation).annotationType();
		throw new IllegalStateException();
	}

	public ResolvedConfiguration getConfiguration() {
		return config;
	}

	protected FactoryInstance getFactory(Factory factory) {
		FactoryInstance instance = state.factory2instance.get(factory);
		if (instance == null)
			state.factory2instance.put(factory, instance = createFactory(factory));
		return instance;
	}

	public void invalidate() {
		Set<FactoryInstance> factories = Sets.newLinkedHashSet();
		Set<Instance> instances = Sets.newLinkedHashSet();
		for (PrimaryValue fact : config.getPrimaryValues()) {
			Instance instance = state.value2instance.get(fact);
			if (instance != null && instances.add(instance))
				collectDependees(instance, factories, instances);
		}
		for (Factory fact : config.getResolvedFactories()) {
			FactoryInstance instance = state.factory2instance.get(fact);
			if (instance != null && factories.add(instance))
				collectDependees(instance, factories, instances);
		}
		invalidate(factories, instances);
	}

	protected void invalidate(FactoryInstance instance, DerivedValue key, Object val) {
		Method invalidator = key.getInvalidator();
		try {
			invalidator.invoke(instance.instance, val);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	protected void invalidate(Instance instance) {
		Set<FactoryInstance> factories = Sets.newLinkedHashSet();
		Set<Instance> instances = Sets.newLinkedHashSet();
		instances.add(instance);
		collectDependees(instance, factories, instances);
		invalidate(factories, instances);
	}

	protected void invalidate(Set<FactoryInstance> factories, Set<Instance> instances) {
		for (Instance inst : instances) {
			if (inst instanceof ManagedInstance) {
				ManagedInstance mi = (ManagedInstance) inst;
				mi.value.invalidate();
			}
			if (inst.key instanceof DerivedValue) {
				DerivedValue derivedValue = (DerivedValue) inst.key;
				Method invalidator = derivedValue.getInvalidator();
				if (invalidator != null) {
					FactoryInstance factoryInstance = state.factory2instance.get(derivedValue.getFactory());
					invalidate(factoryInstance, derivedValue, inst.get());
				}
			}
		}
		for (Instance inst : instances)
			state.value2instance.remove(inst.key);
		for (FactoryInstance fact : factories)
			state.factory2instance.remove(fact.factory);
	}

	@Override
	public String toString() {
		List<String> values = Lists.newArrayList();
		List<String> factories = Lists.newArrayList();
		for (Map.Entry<Value, Instance> e : state.value2instance.entrySet())
			values.add(e.getKey() + " -> " + e.getValue().toString().replace("\n", "\n  "));
		for (Map.Entry<Factory, FactoryInstance> e : state.factory2instance.entrySet())
			factories.add(e.getKey() + " -> " + e.getValue().toString().replace("\n", "\n  "));
		Collections.sort(values);
		Collections.sort(factories);
		StringBuilder result = new StringBuilder();
		result.append(getClass().getSimpleName());
		result.append(" {\n  Values {\n    ");
		result.append(Joiner.on("\n").join(values).replace("\n", "\n    "));
		result.append("\n  } Factories {\n    ");
		result.append(Joiner.on("\n").join(factories).replace("\n", "\n    "));
		result.append("\n  } Configuration {\n    ");
		result.append(config.toString().replace("\n", "\n    "));
		return result.toString();
	}
}
