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

import tools.mdsd.jamopp.model.java.arrays.ArrayTypeable;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.references.MethodCall;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.InferableType;
import tools.mdsd.jamopp.model.java.types.NamespaceClassifierReference;
import tools.mdsd.jamopp.model.java.types.PrimitiveType;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

public final class TypeReferenceExtension {

	private static final String CLONE = "clone";

	private TypeReferenceExtension() {
		// Should not initiated.
	}

	/**
	 * Returns the type referenced by this <code>TypeReference</code> considering
	 * all concrete subclasses of <code>TypeReference</code> used by the Java
	 * metamodel.
	 *
	 * @return the referenced type
	 */
	public static Type getTarget(TypeReference typeReference) {
		return typeReference.getBoundTarget(null);
	}

	/**
	 * Sets the type targeted by this type reference
	 *
	 * @param type the new type to set as target.
	 */
	public static void setTarget(TypeReference typeReference, Classifier type) {
		if (type == null || type.eIsProxy()) {
			return;
		}

		if (typeReference instanceof NamespaceClassifierReference nsClassifierReference) {
			nsClassifierReference.getClassifierReferences().clear();
			nsClassifierReference.getNamespaces().clear();
			nsClassifierReference.getNamespaces().addAll(type.getContainingContainerName());
			ClassifierReference classifierRef = TypesFactory.eINSTANCE.createClassifierReference();
			classifierRef.setTarget(type);
			nsClassifierReference.getClassifierReferences().add(classifierRef);
		} else if (typeReference instanceof ClassifierReference ref) {
			ref.setTarget(type);
		} else if (typeReference instanceof InferableType ref) {
			ref.getArrayDimensionsBefore().clear();
			ref.getActualTargets().clear();
			ClassifierReference newRef = TypesFactory.eINSTANCE.createClassifierReference();
			newRef.setTarget(type);
			ref.getActualTargets().add(newRef);
		}
	}

	/**
	 * Returns the type referenced by this <code>TypeReference</code> considering
	 * all concrete subclasses of <code>TypeReference</code> used by the Java
	 * metamodel. If type parameters are bound in the given reference, the bound
	 * type will be returned instead of the parameter.
	 *
	 * @param reference.
	 *
	 * @return the referenced type.
	 */
	public static Type getBoundTarget(TypeReference typeReference, Reference reference) {
		Type type = null;
		if (typeReference instanceof ClassifierReference || typeReference instanceof NamespaceClassifierReference) {
			type = getType(typeReference);
		} else if (typeReference instanceof PrimitiveType) {
			return (PrimitiveType) typeReference;
		} else if (typeReference instanceof InferableType t && !t.getActualTargets().isEmpty()) {
			return t.getActualTargets().get(0).getBoundTarget(reference);
		}

		// resolve parameter to real type
		if (type instanceof TypeParameter) {
			type = ((TypeParameter) type).getBoundType(typeReference, reference);
		}

		Type result;
		if (type != null && type.eIsProxy()) {
			// this may happen, when e.g. a super type is resolved. It is ok.
			result = null;
		} else {
			result = type;
		}
		return result;
	}

	private static Type getType(TypeReference typeReference) {
		Type newType = null;
		ClassifierReference classifierRef = typeReference.getPureClassifierReference();
		if (classifierRef != null) {
			newType = classifierRef.getTarget();
		}

		if (typeReference instanceof MethodCall potentialCloneCall) {
			// clone returns the type of the cloned in the case of arrays
			ReferenceableElement potentialCloneCallTarget = potentialCloneCall.getTarget();
			if (potentialCloneCallTarget != null && !potentialCloneCallTarget.eIsProxy()
					&& CLONE.equals(potentialCloneCallTarget.getName())
					&& potentialCloneCall.getPrevious() instanceof ElementReference) {
				ElementReference prevRef = (ElementReference) potentialCloneCall.getPrevious();
				if (prevRef.getTarget() instanceof ArrayTypeable
						&& ((ArrayTypeable) prevRef.getTarget()).getArrayDimension() > 0) {
					newType = prevRef.getReferencedType();
				}
			}
		}
		return newType;
	}

	/**
	 * Extracts the (possibly nested) classifier reference (if any) from this type
	 * references.
	 *
	 * @return
	 */
	public static ClassifierReference getPureClassifierReference(TypeReference typeReference) {
		ClassifierReference classifierReference = null;
		if (typeReference instanceof ClassifierReference) {
			classifierReference = (ClassifierReference) typeReference;
		}

		if (typeReference instanceof NamespaceClassifierReference nsClassifierReference
				&& !nsClassifierReference.getClassifierReferences().isEmpty()) {
			int lastIndex = nsClassifierReference.getClassifierReferences().size() - 1;
			classifierReference = nsClassifierReference.getClassifierReferences().get(lastIndex);
		}

		ClassifierReference result;
		if (typeReference instanceof InferableType type && !type.getActualTargets().isEmpty()) {
			result = type.getActualTargets().get(0).getPureClassifierReference();
		} else {
			result = classifierReference;
		}
		return result;
	}
}
