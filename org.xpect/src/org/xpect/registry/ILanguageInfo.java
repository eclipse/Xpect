package org.xpect.registry;

import static org.eclipse.emf.common.EMFPlugin.IS_ECLIPSE_RUNNING;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public interface ILanguageInfo {
	public interface Registry {
		public static final Registry INSTANCE = IS_ECLIPSE_RUNNING ? new DelegatingLanguageRegistry() : new StandaloneLanguageRegistry();

		public abstract ILanguageInfo getLanguageByFileExtension(String fileExtension);

		public abstract ILanguageInfo getLanguageByName(String languageName);

		public abstract Collection<ILanguageInfo> getLanguages();
	}

	public Set<String> getFileExtensions();

	public Injector getInjector();

	public Injector getInjector(List<Class<? extends Module>> moduleClasses);

	public String getLanguageName();

	public Class<? extends Module> getRuntimeModuleClass();

	public Class<? extends Module> getUIModuleClass();
}
