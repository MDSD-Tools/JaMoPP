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

import java.util.List;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.commons.Commentable;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.members.Member;
import tools.mdsd.jamopp.model.java.modifiers.AnnotableAndModifiable;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;

public final class ConcreteClassifierExtension {

	private static final String OBJECT = "Object";

	private ConcreteClassifierExtension() {
		// Should not be initiated.
	}

	public static EList<ConcreteClassifier> getAllInnerClassifiers(final ConcreteClassifier concreteClassifier) {
		final EList<ConcreteClassifier> innerClassifierList = new UniqueEList<>();

		innerClassifierList.addAll(concreteClassifier.getInnerClassifiers());

		for (final ConcreteClassifier superClassifier : concreteClassifier.getAllSuperClassifiers()) {
			final List<ConcreteClassifier> superInnerList = superClassifier.getInnerClassifiers();

			for (final ConcreteClassifier superInner : superInnerList) {
				ConcreteClassifier newSuperInner = superInner;
				if (newSuperInner.eIsProxy()) {
					final EObject resolved = EcoreUtil.resolve(newSuperInner, concreteClassifier);
					newSuperInner = (ConcreteClassifier) resolved;
				}

				final boolean isVisible = !newSuperInner.isHidden(concreteClassifier);
				if (isVisible) {
					innerClassifierList.add(newSuperInner);
				}
			}
		}
		return innerClassifierList;
	}

	public static EList<ConcreteClassifier> getInnerClassifiers(final ConcreteClassifier concreteClassifier) {
		EList<ConcreteClassifier> result;
		if (concreteClassifier.eIsProxy()) {
			result = ECollections.emptyEList();
		} else {
			final StringBuilder suffix = new StringBuilder();
			ConcreteClassifier containingClass = concreteClassifier;
			while (containingClass.eContainer() instanceof ConcreteClassifier) {
				containingClass = (ConcreteClassifier) containingClass.eContainer();
				suffix.insert(0, '.').insert(0, containingClass.getName());
			}
			if (containingClass.eContainer() instanceof CompilationUnit) {
				final CompilationUnit compilationUnit = (CompilationUnit) containingClass.eContainer();
				final String fullName = compilationUnit.getNamespacesAsString() + suffix + concreteClassifier.getName();
				result = concreteClassifier.getConcreteClassifiers(fullName, "*");
			} else {
				result = handleClassedDeclaredInsideMethods(concreteClassifier);
			}
		}
		return result;
	}

	private static EList<ConcreteClassifier> handleClassedDeclaredInsideMethods(
			final ConcreteClassifier concreteClassifier) {
		// For classes declared locally inside methods that are not registered
		// in the class path
		final EList<ConcreteClassifier> result = new UniqueEList<>();
		// Can not call ClassifierUtil.getAllMembers, because it will try to
		// call this method!
		for (final Member member : concreteClassifier.getMembers()) {
			if (member instanceof ConcreteClassifier) {
				result.add((ConcreteClassifier) member);
			}
		}
		for (final ConcreteClassifier superClassifier : concreteClassifier.getAllSuperClassifiers()) {
			for (final Member member : superClassifier.getMembers()) {
				if (member instanceof ConcreteClassifier) {
					result.add((ConcreteClassifier) member);
				}
			}
		}

		return result;
	}

	public static EList<ClassifierReference> getSuperTypeReferences(final ConcreteClassifier concreteClassifier) {
		final EList<ClassifierReference> typeReferenceList = new UniqueEList<>();
		if (concreteClassifier instanceof final tools.mdsd.jamopp.model.java.classifiers.Class javaClass) {
			// Add super type of class to super type list
			final TypeReference superClass = javaClass.getExtends();
			if (superClass != null) {
				final ClassifierReference classifierReference = superClass.getPureClassifierReference();
				typeReferenceList.add(classifierReference);
				final Classifier target = classifierReference.getTarget();
				final ConcreteClassifier concreteTarget = (ConcreteClassifier) target;
				if (!concreteClassifier.isJavaLangObject(concreteTarget)) {
					typeReferenceList.addAll(concreteTarget.getSuperTypeReferences());
				}
			}

			// Add all implemented interfaces to super type list
			addSuperTypes(javaClass.getImplements(), typeReferenceList);

		} else if (concreteClassifier instanceof final Interface javaInterface) {
			// Add all super interfaces to super type list
			addSuperTypes(javaInterface.getExtends(), typeReferenceList);
		}
		return typeReferenceList;
	}

	private static void addSuperTypes(final List<TypeReference> typeReferences,
			final List<ClassifierReference> superTypeReferences) {

		for (final TypeReference interfaceReference : typeReferences) {
			addSuperType(interfaceReference, superTypeReferences);
		}
	}

	private static void addSuperType(final TypeReference typeReference,
			final List<ClassifierReference> superTypeReferences) {

		final ClassifierReference classifierReference = typeReference.getPureClassifierReference();
		superTypeReferences.add(classifierReference);
		final Classifier target = classifierReference.getTarget();
		final ConcreteClassifier concreteTarget = (ConcreteClassifier) target;
		superTypeReferences.addAll(concreteTarget.getSuperTypeReferences());
	}

	/**
	 * Returns all members of the given classifier including inner classes and all
	 * members of super types (extended classes and implemented interfaces).
	 *
	 * @param context to check protected visibility
	 * @return member list
	 */
	public static EList<Member> getAllMembers(final ConcreteClassifier classifier, final Commentable context) {
		final EList<Member> memberList = new UniqueEList<>();

		final ConcreteClassifier concreteClassifier = classifier;
		memberList.addAll(concreteClassifier.getMembers());
		memberList.addAll(concreteClassifier.getDefaultMembers());
		// Because inner classes are found in separate class files
		memberList.addAll(concreteClassifier.getAllInnerClassifiers());

		for (final ConcreteClassifier superClassifier : classifier.getAllSuperClassifiers()) {
			for (final Member member : superClassifier.getMembers()) {
				if (member instanceof final AnnotableAndModifiable modifiable) {
					final boolean isVisible = !modifiable.isHidden(context);
					if (isVisible) {
						memberList.add(member);
					}
				} else {
					memberList.add(member);
				}
			}
			memberList.addAll(superClassifier.getDefaultMembers());
		}
		return memberList;
	}

	/**
	 * Returns the qualified name of this concrete classifier.
	 */
	public static String getQualifiedName(final ConcreteClassifier concreteClassifier) {
		final StringBuilder qualifiedName = new StringBuilder();
		if (concreteClassifier.eContainer() instanceof ConcreteClassifier) {
			qualifiedName.append(((ConcreteClassifier) concreteClassifier.eContainer()).getQualifiedName());
			qualifiedName.append('.');
		} else {
			final List<String> packageParts = concreteClassifier.getContainingPackageName();
			if (packageParts != null) {
				for (final String packagePart : packageParts) {
					qualifiedName.append(packagePart);
					qualifiedName.append('.');
				}
			}
		}
		qualifiedName.append(concreteClassifier.getName());
		return qualifiedName.toString();
	}

	/**
	 * Returns <code>true</code> if the given {@link ConcreteClassifier} is
	 * <code>java.lang.Object</code>. Attention: This method does not take the
	 * {@link ConcreteClassifier} on which the method is called (<code>me</code> )
	 * as argument as this is not used in the methods implementation.
	 *
	 * @param clazz the class to check
	 * @return <code>true</code> if <code>clazz</code> represents
	 *         <code>java.lang.Object</code>, otherwise <code>false</code>
	 */
	public static boolean isJavaLangObject(final ConcreteClassifier clazz) {
		final String name = clazz.getName();
		boolean result;
		if (OBJECT.equals(name)) {
			final List<String> packageName = clazz.getContainingPackageName();
			result = packageName.size() != 2 || !"java".equals(packageName.get(0))
					|| !"lang".equals(packageName.get(1));
		} else {
			result = false;
		}
		return result;
	}
}
