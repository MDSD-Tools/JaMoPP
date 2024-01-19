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
package tools.mdsd.jamopp.model.java.extensions.imports;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.imports.Import;

public final class ImportExtension {

	private ImportExtension() {
		// Should not be initiated.
	}

	/**
	 * Returns the classifier with the given name located in the namespace defined
	 * by the import.
	 *
	 * @param name the name of the classifier
	 * @return imported classifier (proxy)
	 */
	public static ConcreteClassifier getImportedClassifier(Import imp, String name) {
		String containerName = imp.getNamespacesAsString();
		ConcreteClassifier result = null;
		if (containerName != null) {
			String fullQualifiedName = containerName + "." + name;
			result = imp.getConcreteClassifier(fullQualifiedName);
		}
		return result;
	}

	/**
	 * Returns a list of imported classifiers assuming the import's namespace
	 * identifies a package.
	 *
	 * @param _this
	 * @return imported classifier (proxy)
	 */
	public static EList<ConcreteClassifier> getImportedClassifiers(Import imp) {
		String containerName = imp.getNamespacesAsString();
		EList<ConcreteClassifier> result;
		if (containerName == null) {
			result = ECollections.emptyEList();
		} else {
			result = imp.getConcreteClassifiers(containerName, "*");
		}
		return result;
	}
}
