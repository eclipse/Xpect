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

package org.eclipse.xpect.util;

import java.lang.reflect.Field;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class ReflectionUtil {

	public static Class<?> getMemberClass(Class<?> owner, String className) {
		for (Class<?> c : owner.getDeclaredClasses())
			if (c.getSimpleName().equals(className))
				return c;
		throw new RuntimeException("Class " + className + " not found inside " + owner);
	}

	public static Object getParentObject(Object obj) {
		try {
			Field parent = obj.getClass().getDeclaredField("this$0");
			parent.setAccessible(true);
			return parent.get(obj);
		} catch (Throwable e) {
			return null;
		}
	}

	public static <T> T newInstanceUnchecked(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T readField(Class<?> declaredIn, Object owner, String fieldName, Class<T> type) {
		try {
			Field field = declaredIn.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(owner);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T readField(Object owner, String fieldName, Class<T> type) {
		return readField(owner.getClass(), owner, fieldName, type);
	}

	public static void writeField(Class<?> declaredIn, Object owner, String fieldName, Object value) {
		try {
			Field field = declaredIn.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(owner, value);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
}
