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
package tools.mdsd.jamopp.model.java.extensions.types;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;
import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypedElement;
import tools.mdsd.jamopp.model.java.util.TemporalCompositeClassifier;

public final class TypeExtension {

	private TypeExtension() {
		// Should not initiated.
	}

	/**
	 * @param arrayDimension
	 * @param otherType
	 * @param otherArrayDimension
	 *
	 * @return if both type are equal
	 */
	public static boolean equalsType(Type type, long arrayDimension, Type otherType, long otherArrayDimension) {
		Type lOtherType = otherType;
		Type thisType = type;
		// comparison for type parameters
		if (thisType instanceof TypeParameter typeParameter) {
			for (TypeReference referencedType : typeParameter.getExtendTypes()) {
				if (referencedType.getTarget() != null && !referencedType.getTarget().eIsProxy()
						&& referencedType.getTarget().equalsType(arrayDimension, lOtherType, otherArrayDimension)) {
					return true;
				}
			}
			if (typeParameter.getExtendTypes().isEmpty()
					&& type.getObjectClass().equalsType(arrayDimension, lOtherType, otherArrayDimension)) {
				return true;
			}
		}
		if (lOtherType instanceof TypeParameter typeParameter) {
			for (TypeReference referencedType : typeParameter.getExtendTypes()) {
				if (referencedType.getTarget() != null && !referencedType.getTarget().eIsProxy()
						&& type.equalsType(arrayDimension, referencedType.getTarget(), otherArrayDimension)) {
					return true;
				}
			}
			if (typeParameter.getExtendTypes().isEmpty()
					&& type.equalsType(arrayDimension, type.getObjectClass(), otherArrayDimension)) {
				return true;
			}
		}

		// do comparison on the classifier level
		if (thisType instanceof PrimitiveType) {
			thisType = ((PrimitiveType) thisType).wrapPrimitiveType();
		}
		if (lOtherType instanceof PrimitiveType) {
			lOtherType = ((PrimitiveType) lOtherType).wrapPrimitiveType();
		}

		return arrayDimension == otherArrayDimension && lOtherType instanceof Classifier
				&& thisType instanceof Classifier && lOtherType.equals(thisType);
	}

	/**
	 * @param arrayDimension
	 * @param otherType
	 * @param otherArrayType
	 * @param otherArrayType
	 * @return if the other type is equal to me or a super type of me
	 */
	public static boolean isSuperType(Type me, long arrayDimension, Type otherType, ArrayTypeable otherArrayType) {
		Type lOtherType = otherType;

		if (lOtherType == null) {
			return false;
		}

		Type thisType = me;

		if (thisType instanceof TemporalCompositeClassifier || lOtherType instanceof TemporalCompositeClassifier) {
			EList<Type> thisTypeList = new UniqueEList<>();
			EList<Type> otherTypeList = new UniqueEList<>();
			if (thisType instanceof TemporalCompositeClassifier) {
				for (EObject aType : ((TemporalCompositeClassifier) thisType).getSuperTypes()) {
					thisTypeList.add((Type) aType);
				}
			} else {
				thisTypeList.add(thisType);
			}
			if (lOtherType instanceof TemporalCompositeClassifier) {
				for (EObject aType : ((TemporalCompositeClassifier) lOtherType).getSuperTypes()) {
					otherTypeList.add((Type) aType);
				}
			} else {
				otherTypeList.add(thisType);
			}

			for (Type one_thisType : thisTypeList) {
				for (Type oneOtherType : otherTypeList) {
					boolean result = one_thisType.isSuperType(arrayDimension, oneOtherType, otherArrayType);
					if (result) {
						return true;
					}
				}
			}
			return false;
		}

		// if I am a void, I am of every type
		// if the other is Object I am a subtype in any case (also array dimensions do
		// not matter)
		if (thisType.equals(me.getLibClass("Void")) || lOtherType.equals(me.getObjectClass())) {
			return true;
		}

		// String, primitives, and arrays are serializable
		ConcreteClassifier serializableClass = JavaClasspath.get().getConcreteClassifier("java.io.Serializable");
		if (lOtherType.equals(serializableClass) && thisType.equals(serializableClass)
				|| thisType.equals(me.getStringClass()) || thisType instanceof PrimitiveType || arrayDimension > 0) {
			return true;
		}

		// if one of us is a parameter to the best of my knowledge, we might match
		if (thisType instanceof TypeParameter || lOtherType instanceof TypeParameter) {
			return true;
		}

		// if array dimensions do not match, I am no subtype
		boolean isTypeParameter = false;
		if (otherArrayType instanceof TypedElement) {
			Type type = ((TypedElement) otherArrayType).getTypeReference().getTarget();
			isTypeParameter = type instanceof TypeParameter;
		}
		boolean isVariableLengthParameter = otherArrayType instanceof VariableLengthParameter;

		long otherArrayDim = 0;
		if (otherArrayType != null) {
			otherArrayDim = otherArrayType.getArrayDimension();
		}

		if (isTypeParameter && isVariableLengthParameter) {
			if (arrayDimension != otherArrayDim && arrayDimension != otherArrayDim - 1
					&& arrayDimension < otherArrayDim) {
				return false;
			}
		} else if (isTypeParameter) {
			if (arrayDimension < otherArrayDim) {
				return false;
			}
		} else if (isVariableLengthParameter) {
			if (arrayDimension != otherArrayDim && arrayDimension != otherArrayDim - 1) {
				return false;
			}
		} else if (arrayDimension != otherArrayDim) {
			return false;
		}

		// annotations
		if (thisType instanceof Annotation && (lOtherType.equals(me.getAnnotationInterface())
				|| ((ConcreteClassifier) thisType).getAllSuperClassifiers().contains(me.getAnnotationInterface()))) {
			return true;
		}

		// do comparison on the classifier level
		if (thisType instanceof PrimitiveType) {
			thisType = ((PrimitiveType) thisType).wrapPrimitiveType();
		}
		if (lOtherType instanceof PrimitiveType) {
			lOtherType = ((PrimitiveType) lOtherType).wrapPrimitiveType();
		}

		// compare in type hierarchy
		if (lOtherType instanceof ConcreteClassifier && thisType instanceof ConcreteClassifier
				&& (lOtherType.equals(thisType)
						|| ((ConcreteClassifier) thisType).getAllSuperClassifiers().contains(lOtherType))) {
			return true;
		}

		if (lOtherType instanceof ConcreteClassifier && thisType instanceof AnonymousClass
				&& ((AnonymousClass) thisType).getAllSuperClassifiers().contains(lOtherType)) {
			return true;
		}

		// everything can be implicitly casted to CharSequence, so I match when the
		// other type is a CharSequence
		Interface charSequenceClass = me.getLibInterface("CharSequence");

		if (lOtherType instanceof ConcreteClassifier && (lOtherType.equals(charSequenceClass)
				|| ((ConcreteClassifier) lOtherType).getAllSuperClassifiers().contains(charSequenceClass))) {
			return true;
		}

		// there are some specifics for primitive types not reflected in the type
		// hierarchy
		if (lOtherType instanceof tools.mdsd.jamopp.model.java.classifiers.Class) {
			if (((tools.mdsd.jamopp.model.java.classifiers.Class) lOtherType)
					.unWrapPrimitiveType() == null) {
				return false;
			}
			lOtherType = ((tools.mdsd.jamopp.model.java.classifiers.Class) lOtherType)
					.unWrapPrimitiveType();
		}

		if (thisType instanceof tools.mdsd.jamopp.model.java.classifiers.Class) {
			PrimitiveType primitiveType = ((tools.mdsd.jamopp.model.java.classifiers.Class) thisType)
					.unWrapPrimitiveType();
			if (primitiveType == null) {
				return false;
			}
			thisType = primitiveType;
		}

		if (thisType instanceof tools.mdsd.jamopp.model.java.types.Boolean) {
			return lOtherType instanceof tools.mdsd.jamopp.model.java.types.Boolean;
		}

		if (thisType instanceof tools.mdsd.jamopp.model.java.types.Byte
				|| thisType instanceof tools.mdsd.jamopp.model.java.types.Int
				|| thisType instanceof tools.mdsd.jamopp.model.java.types.Short
				|| thisType instanceof tools.mdsd.jamopp.model.java.types.Long
				|| thisType instanceof tools.mdsd.jamopp.model.java.types.Char) {

			return lOtherType instanceof tools.mdsd.jamopp.model.java.types.Byte
					|| lOtherType instanceof tools.mdsd.jamopp.model.java.types.Int
					|| lOtherType instanceof tools.mdsd.jamopp.model.java.types.Short
					|| lOtherType instanceof tools.mdsd.jamopp.model.java.types.Long
					|| lOtherType instanceof tools.mdsd.jamopp.model.java.types.Char
					|| lOtherType instanceof tools.mdsd.jamopp.model.java.types.Float
					|| lOtherType instanceof tools.mdsd.jamopp.model.java.types.Double;
		}

		return (thisType instanceof tools.mdsd.jamopp.model.java.types.Float
				|| thisType instanceof tools.mdsd.jamopp.model.java.types.Double)
				&& (lOtherType instanceof tools.mdsd.jamopp.model.java.types.Float
						|| lOtherType instanceof tools.mdsd.jamopp.model.java.types.Double);
	}

	public static EList<Member> getAllMembers(Type type) {
		// method has to be specified in subclasses
		throw new UnsupportedOperationException();
	}
}
