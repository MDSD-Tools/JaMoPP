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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

public final class ClassExtension {

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
		if (clazz.getLibClass("Boolean").equals(clazz)) {
			result = TypesFactory.eINSTANCE.createBoolean();
		} else if (clazz.getLibClass("Byte").equals(clazz)) {
			result = TypesFactory.eINSTANCE.createByte();
		} else if (clazz.getLibClass("Character").equals(clazz)) {
			result = TypesFactory.eINSTANCE.createChar();
		} else if (clazz.getLibClass("Float").equals(clazz)) {
			result = TypesFactory.eINSTANCE.createFloat();
		} else if (clazz.getLibClass("Double").equals(clazz)) {
			result = TypesFactory.eINSTANCE.createDouble();
		} else if (clazz.getLibClass("Integer").equals(clazz)) {
			result = TypesFactory.eINSTANCE.createInt();
		} else if (clazz.getLibClass("Long").equals(clazz)) {
			result = TypesFactory.eINSTANCE.createLong();
		} else if (clazz.getLibClass("Short").equals(clazz)) {
			result = TypesFactory.eINSTANCE.createShort();
		} else if (clazz.getLibClass("Void").equals(clazz)) {
			result = TypesFactory.eINSTANCE.createVoid();
		}
		return result;
	}
}
