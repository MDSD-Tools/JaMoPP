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
package tools.mdsd.jamopp.model.java.extensions.references;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.expressions.NestedExpression;
import tools.mdsd.jamopp.model.java.literals.Literal;
import tools.mdsd.jamopp.model.java.literals.Super;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.references.ElementReference;
import tools.mdsd.jamopp.model.java.references.PrimitiveTypeReference;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;
import tools.mdsd.jamopp.model.java.references.ReflectiveClassReference;
import tools.mdsd.jamopp.model.java.references.SelfReference;
import tools.mdsd.jamopp.model.java.references.StringReference;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.types.TypedElement;

public final class ReferenceExtension {

	private ReferenceExtension() {
		// Should not be initiated.
	}

	public static Reference getPrevious(Reference reference) {
		Reference result = null;
		if (reference.eContainer() instanceof Reference
				&& reference.equals(((Reference) reference.eContainer()).getNext())) {
			result = (Reference) reference.eContainer();
		}
		return result;
	}

	/**
	 * Determines the {@link Type} of the reference. That is, either the type to
	 * which the reference points directly, or the type of the element to which the
	 * reference points.
	 *
	 * @return the determined type
	 */
	public static Type getReferencedType(Reference reference) {
		Type type = null;
		if (reference instanceof Literal) {
			type = handeLiteral(reference);
		} else if (reference instanceof TypedElement) {
			type = handleTypedElement(reference);
		} else if (reference instanceof SelfReference) {
			type = handleSelfReference(reference);
		} else if (reference instanceof ReflectiveClassReference) {
			type = handleReflectiveClassReference(reference);
		} else if (reference instanceof ElementReference) {
			type = handleElementReference(reference, type);
		} else if (reference instanceof StringReference) {
			type = handleStringReference(reference);
		} else if (reference instanceof NestedExpression) {
			type = handleNestedExpression(reference);
		} else if (reference instanceof PrimitiveTypeReference) {
			type = handlePrimitiveTypeReference(reference);
		} else {
			assert false;
		}

		return type;
	}

	private static Type handleElementReference(Reference reference, Type type) {
		// Referenced element points to an element with a type
		ReferenceableElement target = ((ElementReference) reference).getTarget();
		Type newType = null;
		if (target != null && !target.eIsProxy()) {
			newType = type;
		}

		// Navigate through AdditionalLocalVariable or Field
		if (target instanceof TypedElement) {
			TypeReference typeRef = ((TypedElement) target).getTypeReference();
			if (typeRef != null) {
				newType = typeRef.getBoundTarget(reference);
			}
		} else if (target instanceof Type/* e.g. Annotation */ ) {
			newType = (Type) target;
		} else if (target instanceof EnumConstant) {
			newType = (Enumeration) target.eContainer();
		}
		return newType;
	}

	private static Type handlePrimitiveTypeReference(Reference reference) {
		return ((PrimitiveTypeReference) reference).getPrimitiveType();
	}

	private static Type handleNestedExpression(Reference reference) {
		return ((NestedExpression) reference).getExpression().getType();
	}

	private static Type handleStringReference(Reference reference) {
		// Strings may also appear as reference
		return reference.getStringClass();
	}

	private static Type handleReflectiveClassReference(Reference reference) {
		// Element points to the object's class object
		return reference.getClassClass();
	}

	private static Type handleSelfReference(Reference reference) {
		// Element points to this or super
		Type thisClass;
		if (reference.getPrevious() != null) {
			thisClass = reference.getPrevious().getReferencedType();
		} else {
			AnonymousClass anonymousContainer = reference.getContainingAnonymousClass();
			if (anonymousContainer != null) {
				thisClass = anonymousContainer;
			} else {
				thisClass = reference.getContainingConcreteClassifier();
			}
		}

		Type result;
		if (((SelfReference) reference).getSelf() instanceof Super) {
			if (thisClass instanceof tools.mdsd.jamopp.model.java.classifiers.Class) {
				result = ((tools.mdsd.jamopp.model.java.classifiers.Class) thisClass).getSuperClass();
			} else if (thisClass instanceof AnonymousClass) {
				result = ((AnonymousClass) thisClass).getSuperClassifier();
			} else {
				result = thisClass;
			}
		} else {
			result = thisClass;
		}
		return result;
	}

	private static Type handleTypedElement(Reference reference) {
		// Referenced element points to a type
		TypeReference typeRef = ((TypedElement) reference).getTypeReference();
		return typeRef.getBoundTarget(reference);
	}

	private static Type handeLiteral(Reference reference) {
		return ((Literal) reference).getType();
	}

}
