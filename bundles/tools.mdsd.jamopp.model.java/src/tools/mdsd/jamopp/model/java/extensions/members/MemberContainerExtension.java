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
package tools.mdsd.jamopp.model.java.extensions.members;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.members.Constructor;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.members.MemberContainer;
import tools.mdsd.jamopp.model.java.members.MembersFactory;
import tools.mdsd.jamopp.model.java.members.Method;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypesFactory;

/**
 * A utility class that provides methods that belong to class MemberContainer,
 * but can not go there, because MemberContainer is generated.
 */
public final class MemberContainerExtension {

	private MemberContainerExtension() {
		// Should not be initiated.
	}

	public static EList<Method> getMethods(final MemberContainer memberContainer) {
		final EList<Method> methodList = new BasicEList<>();

		for (final Member member : memberContainer.getMembers()) {
			if (member instanceof Method) {
				methodList.add((Method) member);
			}
		}
		return ECollections.unmodifiableEList(methodList);
	}

	public static EList<Field> getFields(final MemberContainer memberContainer) {
		final EList<Field> fieldList = new BasicEList<>();

		for (final Member member : memberContainer.getMembers()) {
			if (member instanceof Field) {
				fieldList.add((Field) member);
			}
		}
		return ECollections.unmodifiableEList(fieldList);
	}

	public static EList<Constructor> getConstructors(final MemberContainer memberContainer) {
		final EList<Constructor> constructorList = new BasicEList<>();

		for (final Member member : memberContainer.getMembers()) {
			if (member instanceof Constructor) {
				constructorList.add((Constructor) member);
			}
		}
		return ECollections.unmodifiableEList(constructorList);
	}

	public static EList<Member> getMembersByName(final MemberContainer memberContainer, final String name) {
		final EList<Member> matchingMembers = new BasicEList<>();

		for (final Member member : memberContainer.getMembers()) {
			if (name.equals(member.getName())) {
				matchingMembers.add(member);
			}
		}
		return ECollections.unmodifiableEList(matchingMembers);
	}

	public static void removeMethods(final MemberContainer memberContainer, final String name) {
		final EList<Method> methodsToRemove = new BasicEList<>();

		for (final Member member : memberContainer.getMembers()) {
			if (member instanceof Method && name.equals(member.getName())) {
				methodsToRemove.add((Method) member);
			}
		}
		memberContainer.getMembers().removeAll(methodsToRemove);
	}

	/**
	 * @param name
	 * @param name
	 * @return classifier with the given name defined in this member container
	 */
	public static ConcreteClassifier getContainedClassifier(final MemberContainer memberContainer, final String name) {
		for (final Member member : memberContainer.getMembers()) {
			if (member instanceof ConcreteClassifier && name.equals(member.getName())) {
				return (ConcreteClassifier) member;
			}
		}
		for (final Member member : memberContainer.getDefaultMembers()) {
			if (member instanceof ConcreteClassifier && name.equals(member.getName())) {
				return (ConcreteClassifier) member;
			}
		}
		return null;
	}

	/**
	 * @param name
	 * @param name
	 * @return field with the given name defined in this member container
	 */
	public static Field getContainedField(final MemberContainer memberContainer, final String name) {
		for (final Member member : memberContainer.getMembers()) {
			if (member instanceof Field && name.equals(member.getName())) {
				return (Field) member;
			}
		}
		for (final Member member : memberContainer.getDefaultMembers()) {
			if (member instanceof Field && name.equals(member.getName())) {
				return (Field) member;
			}
		}
		return null;
	}

	/**
	 * @param name the method name to search for
	 *
	 * @return method with the given name defined in this member container; null, if
	 *         there is no such method or if there are multiple methods with the
	 *         same name
	 */
	public static Method getContainedMethod(final MemberContainer memberContainer, final String name) {
		Method found = null;
		for (final Member member : memberContainer.getMembers()) {
			if (member instanceof Method && name.equals(member.getName())) {
				if (found != null) {
					return null;
				}
				found = (Method) member;
			}
		}
		for (final Member member : memberContainer.getDefaultMembers()) {
			if (member instanceof Method && name.equals(member.getName())) {
				if (found != null) {
					return null;
				}
				found = (Method) member;
			}
		}
		return found;
	}

	public static Field createField(final MemberContainer memberContainer, final String name,
			final String qualifiedTypeName) {
		final Field field = MembersFactory.eINSTANCE.createField();
		field.setName(name);
		final ClassifierReference typeRef = TypesFactory.eINSTANCE.createClassifierReference();
		typeRef.setTarget(memberContainer.getConcreteClassifier(qualifiedTypeName));
		field.setTypeReference(typeRef);
		memberContainer.getMembers().add(field);
		return field;
	}
}
