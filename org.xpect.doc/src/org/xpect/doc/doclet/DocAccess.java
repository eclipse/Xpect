/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.xpect.doc.doclet;


public class DocAccess {
//	private final RootDoc root;
//	private final Multimap<String, ClassDoc> byAnnotation;
//	private final Multimap<String, ClassDoc> bySuperclass;
//	private final List<ClassDoc> tests;
//
//	public DocAccess(RootDoc root) {
//		super();
//		this.root = root;
//		this.byAnnotation = groupByAnnotation(root);
//		this.bySuperclass = groupBySuperClass(root);
//		this.tests = collectTests(root);
//	}
//
//	public RootDoc getRoot() {
//		return root;
//	}
//
//	private Multimap<String, ClassDoc> groupBySuperClass(RootDoc root) {
//		Multimap<String, ClassDoc> result = LinkedHashMultimap.create();
//		for (ClassDoc cls : root.classes())
//			groupBySuperClass(result, cls, cls);
//		return result;
//	}
//
//	private void groupBySuperClass(Multimap<String, ClassDoc> result, ClassDoc cls, ClassDoc subclass) {
//		ClassDoc superclass = subclass.superclass();
//		if (superclass != null) {
//			result.put(superclass.qualifiedName(), cls);
//			groupBySuperClass(result, cls, superclass);
//		}
//		for (ClassDoc i : subclass.interfaces()) {
//			result.put(i.qualifiedName(), cls);
//			groupBySuperClass(result, cls, i);
//		}
//	}
//
//	private Multimap<String, ClassDoc> groupByAnnotation(RootDoc root) {
//		Multimap<String, ClassDoc> result = LinkedHashMultimap.create();
//		for (ClassDoc cls : root.classes())
//			for (AnnotationDesc a : cls.annotations())
//				result.put(a.annotationType().qualifiedName(), cls);
//		return result;
//	}
//
//	private List<ClassDoc> collectTests(RootDoc root) {
//		List<ClassDoc> result = Lists.newArrayList();
//		CLS: for (ClassDoc cls : root.classes()) {
//			for (AnnotationDesc a : cls.annotations()) {
//				String name = a.annotationType().qualifiedName();
//				if (XpectSuiteClasses.class.getName().equals(name) || XpectSetup.class.getName().equals(name)) {
//					result.add(cls);
//					continue CLS;
//				}
//			}
//			for (MethodDoc m : cls.methods())
//				for (AnnotationDesc a : m.annotations()) {
//					String name = a.annotationType().qualifiedName();
//					if (Test.class.getName().equals(name) || Xpect.class.getName().equals(name)) {
//						result.add(cls);
//						continue CLS;
//					}
//				}
//		}
//		return result;
//	}
//
//	public List<AnnotationTypeDoc> findAnnotationsAnnotatedWith(Class<? extends Annotation> type) {
//		List<AnnotationTypeDoc> result = Lists.newArrayList();
//		for (ClassDoc cls : byAnnotation.get(qualifiedName(type)))
//			if (cls instanceof AnnotationTypeDoc)
//				result.add((AnnotationTypeDoc) cls);
//		return result;
//	}
//
//	public AnnotationTypeDoc findAnnotation(Class<? extends Annotation> type) {
//		return (AnnotationTypeDoc) root.classNamed(qualifiedName(type));
//	}
//
//	public Collection<ClassDoc> findSubclassesOf(Class<?> cls) {
//		return bySuperclass.get(qualifiedName(cls));
//	}
//
//	public List<ClassDoc> findXpectTests() {
//		return tests;
//	}
//
//	private String qualifiedName(Class<?> cls) {
//		return cls.getName().replace('$', '.');
//	}

}
