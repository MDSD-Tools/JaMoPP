/*******************************************************************************
 * Copyright (c) 2006-2014
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Berlin, Amtsgericht Charlottenburg, HRB 140026
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Berlin, Germany
 *      - initial API and implementation
 ******************************************************************************/
package tools.mdsd.jamopp.model.java.extensions.classifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

public final class ClassExtension {

	private static final Map<String, Supplier<PrimitiveType>> MAPPINGS;

	static {
		MAPPINGS = new HashMap<>();
		MAPPINGS.put("Boolean", () -> TypesFactory.eINSTANCE.createBoolean());
		MAPPINGS.put("Byte", () -> TypesFactory.eINSTANCE.createByte());
		MAPPINGS.put("Character", () -> TypesFactory.eINSTANCE.createChar());
		MAPPINGS.put("Float", () -> TypesFactory.eINSTANCE.createFloat());
		MAPPINGS.put("Double", () -> TypesFactory.eINSTANCE.createDouble());
		MAPPINGS.put("Integer", () -> TypesFactory.eINSTANCE.createInt());
		MAPPINGS.put("Long", () -> TypesFactory.eINSTANCE.createLong());
		MAPPINGS.put("Short", () -> TypesFactory.eINSTANCE.createShort());
		MAPPINGS.put("Void", () -> TypesFactory.eINSTANCE.createVoid());
	}

	private ClassExtension() {
		// Should not be initiated.
	}

	/**
	 * Recursively collects all super types (extended classes and implemented
	 * interfaces) of the given class.
	 */
	public static EList<ConcreteClassifier> getAllSuperClassifiers(
			tools.mdsd.jamopp.model.java.classifiers.Class clazz) {

		EList<ConcreteClassifier> result = new UniqueEList<>();

		// Collects all super classes first
		tools.mdsd.jamopp.model.java.classifiers.Class superClass = clazz;
		while (superClass != null && !superClass.eIsProxy() && !clazz.isJavaLangObject(superClass)) {
			superClass = superClass.getSuperClass();
			if (superClass != null) {
				result.add(superClass);
			}
		}

		collectAllImplementedInterfaces(clazz, result);
		collectImplementedInterfacesOfSuperClass(clazz, result);

		return result;
	}

	private static void collectImplementedInterfacesOfSuperClass(tools.mdsd.jamopp.model.java.classifiers.Class clazz,
			EList<ConcreteClassifier> result) {
		tools.mdsd.jamopp.model.java.classifiers.Class superClass;
		superClass = clazz.getSuperClass();
		if (superClass != null && !superClass.eIsProxy() && !clazz.isJavaLangObject(superClass)) {
			result.addAll(superClass.getAllSuperClassifiers());
		}
	}

	private static void collectAllImplementedInterfaces(tools.mdsd.jamopp.model.java.classifiers.Class clazz,
			EList<ConcreteClassifier> result) {
		for (TypeReference typeArg : clazz.getImplements()) {
			ConcreteClassifier superInterface = (ConcreteClassifier) typeArg.getTarget();
			if (superInterface != null) {
				result.add(superInterface);
				if (superInterface instanceof Interface) {
					result.addAll(((Interface) superInterface).getAllSuperClassifiers());
				}
			}
		}
	}

	/**
	 * @return the direct super class
	 */
	public static tools.mdsd.jamopp.model.java.classifiers.Class getSuperClass(
			tools.mdsd.jamopp.model.java.classifiers.Class clazz) {

		TypeReference superClassReference = clazz.getExtends();
		if (superClassReference == null) {
			superClassReference = clazz.getDefaultExtends();
		}

		tools.mdsd.jamopp.model.java.classifiers.Class result = null;
		if (superClassReference != null) {
			Type target = superClassReference.getTarget();
			if (target instanceof tools.mdsd.jamopp.model.java.classifiers.Class) {
				result = (tools.mdsd.jamopp.model.java.classifiers.Class) target;
			}
		}
		return result;
	}

	/**
	 * @return primitive type, if the class can be wrapped
	 */
	public static PrimitiveType unWrapPrimitiveType(tools.mdsd.jamopp.model.java.classifiers.Class clazz) {
		PrimitiveType result = null;
		for (Entry<String, Supplier<PrimitiveType>> entry : MAPPINGS.entrySet()) {
			String key = entry.getKey();
			Supplier<PrimitiveType> val = entry.getValue();
			if (clazz.getLibClass(key).equals(clazz)) {
				result = val.get();
				break;
			}
		}

		return result;
	}
}
