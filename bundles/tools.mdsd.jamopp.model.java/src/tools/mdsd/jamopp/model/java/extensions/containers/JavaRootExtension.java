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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;

public final class JavaRootExtension {

	private JavaRootExtension() {
		// Should not be initiated.
	}

	/**
	 * @return all classes in the same package imports
	 */
	public static EList<ConcreteClassifier> getClassifiersInSamePackage(final JavaRoot javaRoot) {
		final EList<ConcreteClassifier> defaultImportList = new UniqueEList<>();

		final String packageName = javaRoot.getNamespacesAsString();

		defaultImportList.addAll(javaRoot.getConcreteClassifiers(packageName, "*"));

		return defaultImportList;
	}
}
