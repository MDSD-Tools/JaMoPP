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
package tools.mdsd.jamopp.commons.jdt.resolve;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.search.TypeNameRequestor;

import tools.mdsd.jamopp.commons.jdt.JDTJavaClassifier;
import tools.mdsd.jamopp.commons.jdt.JdtFactory;

/**
 * A ClassifierVisitor can be used to visit all search result items when using
 * the JDT SearchEngine class to find types on the classpath. It simply converts
 * every JDT Type to an instance of {@link JDTJavaClassifier} and makes a list
 * of all these instances available via {@link #getClassifiersInClasspath()}.
 *
 * This class is intended for internal use only.
 */
class ClassifierVisitor extends TypeNameRequestor {

	private final List<JDTJavaClassifier> classifiersInClasspath = new ArrayList<>();
	private final IJavaProject project;

	public ClassifierVisitor(final IJavaProject project) {
		this.project = project;
	}

	@Override
	public void acceptType(final int modifiers, final char[] packageName, final char[] simpleTypeName,
			final char[][] enclosingTypeNames, final String path) {

		final JDTJavaClassifier javaClass = JdtFactory.eINSTANCE.createJDTJavaClassifier();
		javaClass.setProjectName(project.getProject().getName());
		javaClass.setPackageName(String.valueOf(packageName));
		for (final char[] enclosingType : enclosingTypeNames) {
			javaClass.getEnclosingTypeNames().add(String.valueOf(enclosingType));
		}
		javaClass.setSimpleName(String.valueOf(simpleTypeName));
		javaClass.setPath(path);

		// Set modifiers (flags)
		javaClass.setAbstract(Flags.isAbstract(modifiers));
		javaClass.setInterface(Flags.isInterface(modifiers));
		javaClass.setEnum(Flags.isEnum(modifiers));

		classifiersInClasspath.add(javaClass);
	}

	public List<JDTJavaClassifier> getClassifiersInClasspath() {
		return classifiersInClasspath;
	}
}
