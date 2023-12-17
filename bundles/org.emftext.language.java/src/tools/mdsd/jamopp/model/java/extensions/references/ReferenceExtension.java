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
import tools.mdsd.jamopp.model.java.members.AdditionalField;
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
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;

public class ReferenceExtension {

	public static Reference getPrevious(Reference me) {
		if (me.eContainer() instanceof Reference) {
			Reference container = (Reference) me.eContainer();
			if (me.equals(container.getNext())) {
				return container;
			}
		}
		return null;
	}

	/**
	 * Determines the {@link Type} of the reference. That is, either the type to
	 * which the reference points directly, or the type of the element to which the
	 * reference points.
	 *
	 * @return the determined type
	 */
	public static Type getReferencedType(Reference me) {
		if (me instanceof Literal) {
			return handeLiteral(me);
		}

		Type type = null;
		if (me instanceof TypedElement) {
			type = handleTypedElement(me);
		} else if (me instanceof SelfReference) {
			return handleSelfReference(me);
		} else if (me instanceof ReflectiveClassReference) {
			return handleReflectiveClassReference(me);
		} else if (me instanceof ElementReference) {
			type = handleElementReference(me, type);
		} else if (me instanceof StringReference) {
			return handleStringReference(me);
		} else if (me instanceof NestedExpression) {
			type = handleNestedExpression(me);
		} else if (me instanceof PrimitiveTypeReference) {
			type = handlePrimitiveTypeReference(me);
		} else {
			assert false;
		}
		return type;
	}

	private static Type handleElementReference(Reference me, Type type) {
		// Referenced element points to an element with a type
		ReferenceableElement target = ((ElementReference) me).getTarget();

		if (target == null) {
			type = null;
		}
		if (target.eIsProxy()) {
			type = null;
		}

		// Navigate through AdditionalLocalVariable or Field
		if (target instanceof AdditionalLocalVariable || target instanceof AdditionalField) {
			target = (ReferenceableElement) target.eContainer();
		} else if (target instanceof TypedElement) {
			TypeReference typeRef = ((TypedElement) target).getTypeReference();
			if (typeRef != null) {
				type = typeRef.getBoundTarget(me);
			}
		} else if (target instanceof Type/* e.g. Annotation */ ) {
			type = (Type) target;
		} else if (target instanceof EnumConstant) {
			type = (Enumeration) target.eContainer();
		}
		return type;
	}

	private static Type handlePrimitiveTypeReference(Reference me) {
		return ((PrimitiveTypeReference) me).getPrimitiveType();
	}

	private static Type handleNestedExpression(Reference me) {
		return ((NestedExpression) me).getExpression().getType();
	}

	private static Type handleStringReference(Reference me) {
		// Strings may also appear as reference
		return me.getStringClass();
	}

	private static Type handleReflectiveClassReference(Reference me) {
		// Element points to the object's class object
		return me.getClassClass();
	}

	private static Type handleSelfReference(Reference me) {
		// Element points to this or super
		Type thisClass = null;
		if (me.getPrevious() != null) {
			thisClass = me.getPrevious().getReferencedType();
		} else {
			AnonymousClass anonymousContainer = me.getContainingAnonymousClass();
			if (anonymousContainer != null) {
				thisClass = anonymousContainer;
			} else {
				thisClass = me.getContainingConcreteClassifier();
			}
		}

		// Find super class if "self" is "super"
		if (((SelfReference) me).getSelf() instanceof Super) {
			if (thisClass instanceof tools.mdsd.jamopp.model.java.classifiers.Class) {
				return ((tools.mdsd.jamopp.model.java.classifiers.Class) thisClass).getSuperClass();
			}
			if (thisClass instanceof AnonymousClass) {
				return ((AnonymousClass) thisClass).getSuperClassifier();
			}
		}

		return thisClass;
	}

	private static Type handleTypedElement(Reference me) {
		// Referenced element points to a type
		TypeReference typeRef = ((TypedElement) me).getTypeReference();
		return typeRef.getBoundTarget(me);
	}

	private static Type handeLiteral(Reference me) {
		return ((Literal) me).getType();
	}
}
