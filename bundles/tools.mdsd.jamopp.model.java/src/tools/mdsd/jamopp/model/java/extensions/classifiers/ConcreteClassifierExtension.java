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

	public static EList<ConcreteClassifier> getAllInnerClassifiers(ConcreteClassifier concreteClassifier) {
		EList<ConcreteClassifier> innerClassifierList = new UniqueEList<>();

		innerClassifierList.addAll(concreteClassifier.getInnerClassifiers());

		for (ConcreteClassifier superClassifier : concreteClassifier.getAllSuperClassifiers()) {
			List<ConcreteClassifier> superInnerList = superClassifier.getInnerClassifiers();

			for (ConcreteClassifier superInner : superInnerList) {
				if (superInner.eIsProxy()) {
					EObject resolved = EcoreUtil.resolve(superInner, concreteClassifier);
					superInner = (ConcreteClassifier) resolved;
				}

				boolean isVisible = !superInner.isHidden(concreteClassifier);
				if (isVisible) {
					innerClassifierList.add(superInner);
				}
			}
		}
		return innerClassifierList;
	}

	public static EList<ConcreteClassifier> getInnerClassifiers(ConcreteClassifier me) {
		if (me.eIsProxy()) {
			return ECollections.emptyEList();
		}
		String suffix = "";
		ConcreteClassifier containingClass = me;
		while (containingClass.eContainer() instanceof ConcreteClassifier) {
			containingClass = (ConcreteClassifier) containingClass.eContainer();
			suffix = containingClass.getName() + "." + suffix;
		}
		if (containingClass.eContainer() instanceof CompilationUnit) {
			CompilationUnit compilationUnit = (CompilationUnit) containingClass.eContainer();
			String fullName = compilationUnit.getNamespacesAsString() + suffix + me.getName();
			return me.getConcreteClassifiers(fullName, "*");
		}

		// For classes declared locally inside methods that are not registered
		// in the class path
		EList<ConcreteClassifier> result = new UniqueEList<>();
		// Can not call ClassifierUtil.getAllMembers, because it will try to
		// call this method!
		for (Member member : me.getMembers()) {
			if (member instanceof ConcreteClassifier) {
				result.add((ConcreteClassifier) member);
			}
		}
		for (ConcreteClassifier superClassifier : me.getAllSuperClassifiers()) {
			for (Member member : superClassifier.getMembers()) {
				if (member instanceof ConcreteClassifier) {
					result.add((ConcreteClassifier) member);
				}
			}
		}

		return result;
	}

	public static EList<ClassifierReference> getSuperTypeReferences(ConcreteClassifier concreteClassifier) {
		EList<ClassifierReference> typeReferenceList = new UniqueEList<>();
		if (concreteClassifier instanceof tools.mdsd.jamopp.model.java.classifiers.Class javaClass) {
			// Add super type of class to super type list
			TypeReference superClass = javaClass.getExtends();
			if (superClass != null) {
				ClassifierReference classifierReference = superClass.getPureClassifierReference();
				typeReferenceList.add(classifierReference);
				Classifier target = classifierReference.getTarget();
				ConcreteClassifier concreteTarget = (ConcreteClassifier) target;
				if (!concreteClassifier.isJavaLangObject(concreteTarget)) {
					typeReferenceList.addAll(concreteTarget.getSuperTypeReferences());
				}
			}

			// Add all implemented interfaces to super type list
			addSuperTypes(javaClass.getImplements(), typeReferenceList);

		} else if (concreteClassifier instanceof Interface javaInterface) {
			// Add all super interfaces to super type list
			addSuperTypes(javaInterface.getExtends(), typeReferenceList);
		}
		return typeReferenceList;
	}

	private static void addSuperTypes(List<TypeReference> typeReferences,
			List<ClassifierReference> superTypeReferences) {

		for (TypeReference interfaceReference : typeReferences) {
			addSuperType(interfaceReference, superTypeReferences);
		}
	}

	private static void addSuperType(TypeReference typeReference, List<ClassifierReference> superTypeReferences) {

		ClassifierReference classifierReference = typeReference.getPureClassifierReference();
		superTypeReferences.add(classifierReference);
		Classifier target = classifierReference.getTarget();
		ConcreteClassifier concreteTarget = (ConcreteClassifier) target;
		superTypeReferences.addAll(concreteTarget.getSuperTypeReferences());
	}

	/**
	 * Returns all members of the given classifier including inner classes and all
	 * members of super types (extended classes and implemented interfaces).
	 *
	 * @param context to check protected visibility
	 * @return member list
	 */
	public static EList<Member> getAllMembers(ConcreteClassifier classifier, Commentable context) {
		EList<Member> memberList = new UniqueEList<>();

		ConcreteClassifier concreteClassifier = classifier;
		memberList.addAll(concreteClassifier.getMembers());
		memberList.addAll(concreteClassifier.getDefaultMembers());
		// Because inner classes are found in separate class files
		memberList.addAll(concreteClassifier.getAllInnerClassifiers());

		for (ConcreteClassifier superClassifier : classifier.getAllSuperClassifiers()) {
			for (Member member : superClassifier.getMembers()) {
				if (member instanceof AnnotableAndModifiable modifiable) {
					boolean isVisible = !modifiable.isHidden(context);
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
	public static String getQualifiedName(ConcreteClassifier concreteClassifier) {
		StringBuilder qualifiedName = new StringBuilder();
		if (concreteClassifier.eContainer() instanceof ConcreteClassifier) {
			qualifiedName.append(((ConcreteClassifier) concreteClassifier.eContainer()).getQualifiedName());
			qualifiedName.append('.');
		} else {
			List<String> packageParts = concreteClassifier.getContainingPackageName();
			if (packageParts != null) {
				for (String packagePart : packageParts) {
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
	public static boolean isJavaLangObject(ConcreteClassifier clazz) {
		String name = clazz.getName();
		boolean result;
		if (OBJECT.equals(name)) {
			List<String> packageName = clazz.getContainingPackageName();
			result = packageName.size() != 2 || !"java".equals(packageName.get(0))
					|| !"lang".equals(packageName.get(1));
		} else {
			result = false;
		}
		return result;
	}
}
