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

package tools.mdsd.jamopp.model.java.extensions.commons;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.commons.NamespaceAwareElement;

public final class NamespaceAwareElementExtension {

	private NamespaceAwareElementExtension() {
		// Should not be initiated.
	}

	/**
	 * Converts the namespaces array of the given namespace aware element into a
	 * String representation using . delimiters.
	 *
	 * @param namespaceAwareElement the given namespace aware element.
	 * @return single string representation of namespace.
	 */
	public static String getNamespacesAsString(NamespaceAwareElement namespaceAwareElement) {
		StringBuilder builder = new StringBuilder();
		for (String part : namespaceAwareElement.getNamespaces()) {
			builder.append(part);
			builder.append('.');
		}
		if (builder.length() > 0) {
			builder.delete(builder.length() - 1, builder.length());
		}
		return builder.toString();
	}

	/**
	 * Assuming the namespace identifies a classifier, that classifier is returned.
	 *
	 * @param namespaceAwareElement the namespace aware element.
	 * @return classifier at namespace.
	 */
	public static ConcreteClassifier getClassifierAtNamespaces(NamespaceAwareElement namespaceAwareElement) {
		return JavaClasspath.get().getConcreteClassifier(namespaceAwareElement.getNamespacesAsString());
	}
}
