/*******************************************************************************
 * Copyright (c) 2006-2013
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import tools.mdsd.jamopp.commons.jdt.JDTJavaClassifier;

public abstract class JDTJavaClassifierUtil {

	/**
	 * Returns the {@link IType} that corresponds to the given classifier.
	 */
	public IType getIType(final JDTJavaClassifier classifier) {
		final String projectName = classifier.getProjectName();
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();
		final IProject project = root.getProject(projectName);
		final IJavaProject javaProject = JavaCore.create(project);
		IType result = null;
		if (javaProject != null && javaProject.exists()) {
			final String qualifiedName = classifier.getQualifiedName();
			try {
				result = javaProject.findType(qualifiedName);
			} catch (final JavaModelException e) {
				handleException(e);
			}
		}
		return result;
	}

	/**
	 * This is a template method that must be implemented by concrete subclasses to
	 * handle exceptions that are thrown while executing
	 * {@link #getIType(JDTJavaClassifier)}.
	 */
	public abstract void handleException(Exception exception);
}
