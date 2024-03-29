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

import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;

public final class ClassifierExtension {

	private ClassifierExtension() {
		// Should not be initiated.
	}

	public static EList<ConcreteClassifier> getAllSuperClassifiers(final Classifier classifier) {
		// Method has to be specified in subclasses
		throw new UnsupportedOperationException("Not implemented in superclass for: " + classifier);
	}

	/**
	 * Adds an import of the given class to the compilation unit that contains this
	 * classifier.
	 */
	public static void addImport(final Classifier classifier, final String nameOfClassToImport) {
		final CompilationUnit compilationUnit = classifier.getParentByType(CompilationUnit.class);
		compilationUnit.addImport(nameOfClassToImport);
	}

	/**
	 * Adds an import of the given package to the compilation unit that contains
	 * this classifier.
	 */
	public static void addPackageImport(final Classifier classifier, final String packageName) {
		final CompilationUnit compilationUnit = classifier.getParentByType(CompilationUnit.class);
		compilationUnit.addPackageImport(packageName);
	}
}
