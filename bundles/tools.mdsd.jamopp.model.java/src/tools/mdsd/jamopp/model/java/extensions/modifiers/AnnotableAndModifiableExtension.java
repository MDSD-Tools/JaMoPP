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
package tools.mdsd.jamopp.model.java.extensions.modifiers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.commons.Commentable;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.modifiers.Modifier;
import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.model.java.modifiers.Private;
import tools.mdsd.jamopp.model.java.modifiers.Protected;
import tools.mdsd.jamopp.model.java.modifiers.Public;
import tools.mdsd.jamopp.model.java.modifiers.Static;
import tools.mdsd.jamopp.model.java.references.Reference;
import tools.mdsd.jamopp.model.java.references.SelfReference;
import tools.mdsd.jamopp.model.java.types.Type;

public final class AnnotableAndModifiableExtension {

	private AnnotableAndModifiableExtension() {
		// Should not be initiated.
	}

	/**
	 * Sets the visibility of this element to <code>private</code>.
	 */
	public static void makePrivate(AnnotableAndModifiable annotableAndModifiable) {
		if (annotableAndModifiable.isPrivate()) {
			return;
		}
		annotableAndModifiable.removeModifier(Public.class);
		annotableAndModifiable.removeModifier(Protected.class);
		annotableAndModifiable.getAnnotationsAndModifiers().add(ModifiersFactory.eINSTANCE.createPrivate());
	}

	/**
	 * Sets the visibility of this element to <code>public</code>.
	 */
	public static void makePublic(AnnotableAndModifiable me) {
		if (me.isPublic()) {
			return;
		}
		me.removeModifier(Private.class);
		me.removeModifier(Protected.class);
		me.getAnnotationsAndModifiers().add(ModifiersFactory.eINSTANCE.createPublic());
	}

	/**
	 * Sets the visibility of this element to <code>protected</code>.
	 */
	public static void makeProtected(AnnotableAndModifiable annotableAndModifiable) {
		if (annotableAndModifiable.isProtected()) {
			return;
		}
		annotableAndModifiable.removeModifier(Private.class);
		annotableAndModifiable.removeModifier(Public.class);
		annotableAndModifiable.getAnnotationsAndModifiers().add(ModifiersFactory.eINSTANCE.createProtected());
	}

	/**
	 * Removes all modifiers from this element.
	 */
	public static void removeAllModifiers(AnnotableAndModifiable annotableAndModifiable) {
		List<Modifier> modifiers = annotableAndModifiable.getModifiers();
		EList<AnnotationInstanceOrModifier> elements = annotableAndModifiable.getAnnotationsAndModifiers();
		elements.removeAll(modifiers);
	}

	/**
	 * Returns an unmodifiable list of the modifiers that apply to this element.
	 */
	public static EList<Modifier> getModifiers(AnnotableAndModifiable annotableAndModifiable) {
		EList<AnnotationInstanceOrModifier> elements = annotableAndModifiable.getAnnotationsAndModifiers();
		EList<Modifier> modifiers = new BasicEList<>();
		for (AnnotationInstanceOrModifier next : elements) {
			if (next instanceof Modifier) {
				modifiers.add((Modifier) next);
			}
		}
		return ECollections.unmodifiableEList(modifiers);
	}

	/**
	 * Returns an unmodifiable list of the annotations that apply to this element.
	 */
	public static EList<AnnotationInstance> getAnnotationInstances(AnnotableAndModifiable annotableAndModifiable) {
		EList<AnnotationInstanceOrModifier> elements = annotableAndModifiable.getAnnotationsAndModifiers();
		EList<AnnotationInstance> annotations = new BasicEList<>();
		for (AnnotationInstanceOrModifier next : elements) {
			if (next instanceof AnnotationInstance) {
				annotations.add((AnnotationInstance) next);
			}
		}
		return ECollections.unmodifiableEList(annotations);
	}

	/**
	 * Adds the given type of modifier to this element. This method does not check
	 * for duplicate modifiers!
	 *
	 * @param newModifier the modifier to add
	 */
	public static void addModifier(AnnotableAndModifiable annotableAndModifiable, Modifier newModifier) {
		annotableAndModifiable.getAnnotationsAndModifiers().add(newModifier);
	}

	/**
	 * Removes the given type of modifier from this element.
	 *
	 * @param modifierType
	 */
	public static void removeModifier(AnnotableAndModifiable me, Class<?> modifierType) {
		List<AnnotationInstanceOrModifier> modifiers = me.getAnnotationsAndModifiers();
		List<AnnotationInstanceOrModifier> modifiersToRemove = new ArrayList<>();
		for (AnnotationInstanceOrModifier modifier : modifiers) {
			if (modifierType.isInstance(modifier)) {
				modifiersToRemove.add(modifier);
			}
		}
		modifiers.removeAll(modifiersToRemove);
	}

	public static boolean isPublic(AnnotableAndModifiable annotableAndModifiable) {
		return annotableAndModifiable.hasModifier(Public.class);
	}

	public static boolean isPrivate(AnnotableAndModifiable annotableAndModifiable) {
		return annotableAndModifiable.hasModifier(Private.class);
	}

	public static boolean isProtected(AnnotableAndModifiable annotableAndModifiable) {
		return annotableAndModifiable.hasModifier(Protected.class);
	}

	/**
	 * Checks whether this element has an modifier of the given type.
	 *
	 * @param type
	 */
	public static boolean hasModifier(AnnotableAndModifiable annotableAndModifiable, Class<?> type) {
		boolean result = false;
		List<AnnotationInstanceOrModifier> modifiers = annotableAndModifiable.getAnnotationsAndModifiers();
		for (AnnotationInstanceOrModifier modifier : modifiers) {
			if (type.isInstance(modifier)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Returns <code>true</code> if this element is static (either by an explicit
	 * modifier <code>static</code> or because this element is part of an
	 * interface).
	 */
	public static boolean isStatic(AnnotableAndModifiable annotableAndModifiable) {
		// all members of an interface are static by default
		boolean result = false;
		if (annotableAndModifiable.eContainer() instanceof Interface) {
			result = true;
		}

		for (AnnotationInstanceOrModifier modifier : annotableAndModifiable.getAnnotationsAndModifiers()) {
			if (modifier instanceof Static) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean isHidden(AnnotableAndModifiable annotableAndModifiable, Commentable context) {
		// all members of an interface are public by default
		Commentable lContext = context;
		if (annotableAndModifiable.eIsProxy() || annotableAndModifiable.eContainer() instanceof Interface) {
			return false;
		} else if (lContext.eIsProxy()) {
			lContext = (Commentable) EcoreUtil.resolve(lContext, annotableAndModifiable);
		} else if (!(annotableAndModifiable.eContainer() instanceof Commentable)) {
			return true;
		}
		ConcreteClassifier iContextClassifier = lContext.getContainingConcreteClassifier();
		ConcreteClassifier myClassifier = ((Commentable) annotableAndModifiable.eContainer())
				.getParentConcreteClassifier();
		if (lContext instanceof Reference && ((Reference) lContext).getPrevious() instanceof SelfReference) {
			SelfReference selfReference = (SelfReference) ((Reference) lContext).getPrevious();
			if (selfReference.getPrevious() != null) {
				Type type = selfReference.getPrevious().getReferencedType();
				if (type instanceof ConcreteClassifier) {
					iContextClassifier = (ConcreteClassifier) type;
				}
			}
		}

		for (AnnotationInstanceOrModifier modifier : annotableAndModifiable.getAnnotationsAndModifiers()) {
			if (modifier instanceof Private) {
				return myClassifier.equalsType(0, iContextClassifier, 0);
			} else if (modifier instanceof Public) {
				return false;
			} else if (modifier instanceof Protected) {
				return checkProtected(myClassifier, myClassifier, lContext, lContext);
			}
		}

		// Probably package visibility
		return annotableAndModifiable.getContainingPackageName() == null
				|| !annotableAndModifiable.getContainingPackageName().equals(lContext.getContainingPackageName());
	}

	private static boolean checkProtected(ConcreteClassifier iContextClassifier, ConcreteClassifier myClassifier,
			Commentable me, Commentable lContext) {
		ConcreteClassifier concreteClassifier = iContextClassifier;
		// package visibility
		if (me.getContainingPackageName() != null
				&& me.getContainingPackageName().equals(lContext.getContainingPackageName())) {
			return false;
		}
		// try outer classifiers as well
		while (concreteClassifier != null) {
			if (concreteClassifier.isSuperType(0, myClassifier, null)) {
				return false;
			} else if (concreteClassifier.eContainer() instanceof Commentable) {
				concreteClassifier = ((Commentable) concreteClassifier.eContainer()).getParentConcreteClassifier();
			} else {
				concreteClassifier = null;
			}

			if (concreteClassifier != null && !concreteClassifier.eIsProxy()
					&& concreteClassifier.isSuperType(0, myClassifier, null)) {
				return false;
			}
		}
		// visibility through anonymous subclass
		AnonymousClass anonymousClass = lContext.getContainingAnonymousClass();
		while (anonymousClass != null) {
			if (anonymousClass.getSuperClassifier() == null) {
				return true;
			} else if (anonymousClass.getSuperClassifier().isSuperType(0, myClassifier, null)) {
				return false;
			}

			if (anonymousClass.eContainer() instanceof Commentable) {
				anonymousClass = ((Commentable) anonymousClass.eContainer()).getContainingAnonymousClass();
			} else {
				anonymousClass = null;
			}
		}
		return true;
	}
}
