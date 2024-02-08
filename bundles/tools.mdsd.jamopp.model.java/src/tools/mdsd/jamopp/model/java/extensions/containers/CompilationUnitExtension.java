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
package tools.mdsd.jamopp.model.java.extensions.containers;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;

import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.Class;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;
import tools.mdsd.jamopp.model.java.imports.ClassifierImport;
import tools.mdsd.jamopp.model.java.imports.ImportsFactory;
import tools.mdsd.jamopp.model.java.imports.PackageImport;

public final class CompilationUnitExtension {

	private CompilationUnitExtension() {
		// Should not be initiated.
	}

	/**
	 * Returns the first {@link ConcreteClassifier} that is contained in this
	 * {@link CompilationUnit} and which has the given name.
	 *
	 * @param name the name of the classifier to search for
	 * @return the classifier if one is found, otherwise <code>null</code>
	 */
	public static ConcreteClassifier getContainedClassifier(final CompilationUnit compilationUnit, final String name) {
		ConcreteClassifier result = null;
		for (final ConcreteClassifier candidate : compilationUnit.getClassifiers()) {
			if (name.equals(candidate.getName())) {
				result = candidate;
			}
		}
		return result;
	}

	/**
	 * @return all classes in the same package imports
	 */
	public static EList<ConcreteClassifier> getClassifiersInSamePackage(final CompilationUnit compilationUnit) {
		final EList<ConcreteClassifier> defaultImportList = new UniqueEList<>();

		final String packageName = compilationUnit.getNamespacesAsString();

		// locally defined in this container
		defaultImportList.addAll(compilationUnit.getClassifiers());

		defaultImportList.addAll(compilationUnit.getConcreteClassifiers(packageName, "*"));

		return defaultImportList;
	}

	/**
	 * Returns the class that is directly contained in the compilation unit (if
	 * exactly one exists). If the {@link CompilationUnit} contains multiple
	 * classifiers or if the contained classifier is not a {@link Class},
	 * <code>null</code> is returned.
	 *
	 * @return the class directly contained in the compilation unit (if there is
	 *         exactly one contained classifier that is of type {@link Class})
	 */
	public static Class getContainedClass(final CompilationUnit compilationUnit) {
		final List<ConcreteClassifier> classifiers = compilationUnit.getClassifiers();
		Class result = null;
		if (classifiers.get(0) instanceof final Class clazz && classifiers.size() == 1) {
			result = clazz;
		}
		return result;
	}

	/**
	 * Returns the interface that is directly contained in the compilation unit (if
	 * exactly one exists). If the {@link CompilationUnit} contains multiple
	 * classifiers or if the contained classifier is not an {@link Interface},
	 * <code>null</code> is returned.
	 *
	 * @return the interface directly contained in the compilation unit (if there is
	 *         exactly one contained classifier that is of type {@link Interface})
	 */
	public static Interface getContainedInterface(final CompilationUnit compilationUnit) {
		final List<ConcreteClassifier> classifiers = compilationUnit.getClassifiers();
		Interface result = null;
		if (classifiers.get(0) instanceof final Interface interfaze && classifiers.size() == 1) {
			result = interfaze;
		}
		return result;
	}

	/**
	 * Returns the annotation that is directly contained in the compilation unit (if
	 * exactly one exists). If the {@link CompilationUnit} contains multiple
	 * classifiers or if the contained classifier is not an {@link Annotation},
	 * <code>null</code> is returned.
	 *
	 * @return the annotation directly contained in the compilation unit (if there
	 *         is exactly one contained classifier that is of type
	 *         {@link Annotation})
	 */
	public static Annotation getContainedAnnotation(final CompilationUnit compilationUnit) {
		final List<ConcreteClassifier> classifiers = compilationUnit.getClassifiers();
		Annotation result = null;
		if (classifiers.get(0) instanceof final Annotation annotation && classifiers.size() == 1) {
			result = annotation;
		}
		return result;
	}

	/**
	 * Returns the enumeration that is directly contained in the compilation unit
	 * (if exactly one exists). If the {@link CompilationUnit} contains multiple
	 * classifiers or if the contained classifier is not an {@link Enumeration},
	 * <code>null</code> is returned.
	 *
	 * @return the enumeration directly contained in the compilation unit (if there
	 *         is exactly one contained classifier that is of type
	 *         {@link Enumeration})
	 */
	public static Enumeration getContainedEnumeration(final CompilationUnit compilationUnit) {
		final List<ConcreteClassifier> classifiers = compilationUnit.getClassifiers();
		Enumeration result = null;
		if (classifiers.get(0) instanceof final Enumeration enumeration && classifiers.size() == 1) {
			result = enumeration;
		}
		return result;
	}

	/**
	 * Adds an import of the given class to this compilation unit.
	 */
	public static void addImport(final CompilationUnit compilationUnit, final String nameOfClassToImport) {
		final ClassifierImport classifierImport = ImportsFactory.eINSTANCE.createClassifierImport();
		final ConcreteClassifier classToImport = compilationUnit.getConcreteClassifier(nameOfClassToImport);
		classifierImport.setClassifier(classToImport);
		classifierImport.getNamespaces().addAll(classToImport.getContainingCompilationUnit().getNamespaces());
		compilationUnit.getImports().add(classifierImport);
	}

	/**
	 * Adds an import of the given package to this compilation unit.
	 */
	public static void addPackageImport(final CompilationUnit compilationUnit, final String packageName) {
		final PackageImport nsImport = ImportsFactory.eINSTANCE.createPackageImport();
		Collections.addAll(nsImport.getNamespaces(), packageName.split("\\."));
		compilationUnit.getImports().add(nsImport);
	}
}
