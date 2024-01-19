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
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.types.TypeReference;

public final class EnumerationExtension {

	private EnumerationExtension() {
		// Should not be initiated.
	}

	/**
	 * @return all interfaces extended by this enumeration.
	 */
	public static EList<ConcreteClassifier> getAllSuperClassifiers(Enumeration enumeration) {
		EList<ConcreteClassifier> result = new UniqueEList<>();

		// Enumerations inherit from java.lang.Enum
		tools.mdsd.jamopp.model.java.classifiers.Class enumClass = enumeration.getLibClass("Enum");
		result.add(enumClass);
		result.addAll(enumClass.getAllSuperClassifiers());

		// Collect all implemented interfaces
		for (TypeReference typeArg : enumeration.getImplements()) {
			ConcreteClassifier superInterface = (ConcreteClassifier) typeArg.getTarget();
			if (superInterface != null) {
				result.add(superInterface);
				if (superInterface instanceof Interface) {
					result.addAll(((Interface) superInterface).getAllSuperClassifiers());
				}
			}
		}

		return result;
	}

	public static EnumConstant getContainedConstant(Enumeration enumeration, String name) {
		EnumConstant result = null;
		for (EnumConstant constant : enumeration.getConstants()) {
			if (name.equals(constant.getName())) {
				result = constant;
				break;
			}
		}
		return result;
	}
}
