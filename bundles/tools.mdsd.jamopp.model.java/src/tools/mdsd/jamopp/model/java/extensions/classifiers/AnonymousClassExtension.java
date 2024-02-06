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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import tools.mdsd.jamopp.model.java.classifiers.AnonymousClass;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.commons.Commentable;
import tools.mdsd.jamopp.model.java.instantiations.NewConstructorCall;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.types.Type;
import tools.mdsd.jamopp.model.java.types.TypeReference;

public final class AnonymousClassExtension {

	private AnonymousClassExtension() {
		// Should not be initiated.
	}

	/**
	 * @param context to check protected visibility
	 * @return a view on all members including super classifiers' members
	 */
	public static EList<Member> getAllMembers(final AnonymousClass anonymousClass, final Commentable context) {
		final EList<Member> memberList = new UniqueEList<>();
		memberList.addAll(anonymousClass.getMembers());
		memberList.addAll(anonymousClass.getDefaultMembers());

		NewConstructorCall ncCall = null;
		final EObject eContainer = anonymousClass.eContainer();
		if (eContainer instanceof NewConstructorCall) {
			ncCall = (NewConstructorCall) eContainer;
		}

		if (ncCall != null) {
			final TypeReference typeReference = ncCall.getTypeReference();
			final Type target = typeReference.getTarget();
			final ConcreteClassifier classifier = (ConcreteClassifier) target;
			if (classifier != null) {
				final EList<Member> superMemberList = classifier.getAllMembers(context);
				for (final Member superMember : superMemberList) {
					addToList(anonymousClass, context, memberList, superMember);
				}
			}
		}
		return memberList;
	}

	private static void addToList(final AnonymousClass anonymousClass, final Commentable context,
			final EList<Member> memberList, final Member superMember) {
		Member newMember = superMember;
		// Exclude private members
		if (newMember instanceof AnnotableAndModifiable) {
			if (newMember.eIsProxy()) {
				newMember = (Member) EcoreUtil.resolve(newMember, anonymousClass);
			}
			final AnnotableAndModifiable modifiable = (AnnotableAndModifiable) newMember;
			if (!modifiable.isHidden(context)) {
				memberList.add(newMember);
			}
		} else {
			memberList.add(newMember);
		}
	}

	/**
	 * @return a view on all super classifiers
	 */
	public static EList<ConcreteClassifier> getAllSuperClassifiers(final AnonymousClass anonymousClass) {
		final EList<ConcreteClassifier> superClassifierList = new UniqueEList<>();

		final ConcreteClassifier superClassifier = anonymousClass.getSuperClassifier();

		if (superClassifier != null) {
			superClassifierList.add(superClassifier);
			superClassifierList.addAll(superClassifier.getAllSuperClassifiers());
		} else {
			superClassifierList.add(anonymousClass.getObjectClass());
		}
		return superClassifierList;
	}

	/**
	 * @return the direct super classifier
	 */
	public static ConcreteClassifier getSuperClassifier(final EObject eObject) {
		NewConstructorCall ncCall;
		final EObject eContainer = eObject.eContainer();
		ConcreteClassifier result = null;
		if (eContainer instanceof NewConstructorCall) {
			ncCall = (NewConstructorCall) eContainer;
			final TypeReference typeReference = ncCall.getTypeReference();
			final Type target = typeReference.getTarget();
			result = (ConcreteClassifier) target;
		} else if (eContainer instanceof EnumConstant && eContainer.eContainer() instanceof Enumeration) {
			result = (Enumeration) eContainer.eContainer();
		}
		return result;
	}
}
