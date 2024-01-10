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

public class AnonymousClassExtension {

	/**
	 * @param context to check protected visibility
	 * @return a view on all members including super classifiers' members
	 */
	public static EList<Member> getAllMembers(AnonymousClass me, Commentable context) {
		EList<Member> memberList = new UniqueEList<>();
		memberList.addAll(me.getMembers());
		memberList.addAll(me.getDefaultMembers());

		NewConstructorCall ncCall = null;
		EObject eContainer = me.eContainer();
		if (eContainer instanceof NewConstructorCall) {
			ncCall = (NewConstructorCall) eContainer;
		}

		if (ncCall == null) {
			return memberList;
		}
		TypeReference typeReference = ncCall.getTypeReference();
		Type target = typeReference.getTarget();
		ConcreteClassifier classifier = (ConcreteClassifier) target;
		if (classifier != null) {
			EList<Member> superMemberList = classifier.getAllMembers(context);
			for (Member superMember : superMemberList) {
				// Exclude private members
				if (superMember instanceof AnnotableAndModifiable) {
					if (superMember.eIsProxy()) {
						superMember = (Member) EcoreUtil.resolve(superMember, me);
					}
					AnnotableAndModifiable modifiable = (AnnotableAndModifiable) superMember;
					if (!modifiable.isHidden(context)) {
						memberList.add(superMember);
					}
				} else {
					memberList.add(superMember);
				}
			}
		}
		return memberList;
	}

	/**
	 * @return a view on all super classifiers
	 */
	public static EList<ConcreteClassifier> getAllSuperClassifiers(AnonymousClass me) {
		EList<ConcreteClassifier> superClassifierList = new UniqueEList<>();

		ConcreteClassifier superClassifier = me.getSuperClassifier();

		if (superClassifier != null) {
			superClassifierList.add(superClassifier);
			superClassifierList.addAll(superClassifier.getAllSuperClassifiers());
		} else {
			superClassifierList.add(me.getObjectClass());
		}
		return superClassifierList;
	}

	/**
	 * @return the direct super classifier
	 */
	public static ConcreteClassifier getSuperClassifier(EObject me) {
		NewConstructorCall ncCall = null;
		EObject eContainer = me.eContainer();
		if (eContainer instanceof NewConstructorCall) {
			ncCall = (NewConstructorCall) eContainer;
			TypeReference typeReference = ncCall.getTypeReference();
			Type target = typeReference.getTarget();
			return (ConcreteClassifier) target;
		}
		if (eContainer instanceof EnumConstant && eContainer.eContainer() instanceof Enumeration) {
			return (Enumeration) eContainer.eContainer();
		}
		return null;
	}
}
