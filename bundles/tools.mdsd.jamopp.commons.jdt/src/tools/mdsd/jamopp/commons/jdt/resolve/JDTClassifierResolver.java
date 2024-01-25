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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;

import tools.mdsd.jamopp.commons.jdt.JDTJavaClassifier;
import tools.mdsd.jamopp.commons.jdt.JdtPackage;

/**
 * This class can be used to find all Java classifiers that are available in a
 * given JDT Java project.
 */
public class JDTClassifierResolver {

	/**
	 * Returns the Java project that is located at the given URI. If the project at
	 * this locations is not a Java project or if there is no project at all,
	 * <code>null</code> is returned.
	 */
	public IJavaProject getJavaProject(final URI uri) {
		final IProject project = getProject(uri);
		IJavaProject javaProject = null;
		if (project != null) {
			javaProject = getJavaProject(project);
		}
		return javaProject;
	}

	private static boolean isJavaProject(final IProject project) {
		boolean result = false;
		if (project != null) {
			try {
				result = project.isNatureEnabled("org.eclipse.jdt.core.javanature");
			} catch (final CoreException e) {
				// Ignore
			}
		}
		return result;
	}

	private static IProject getProject(final URI uri) {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (uri.isPlatformResource() && uri.segmentCount() > 2) {
			final String segment = uri.segment(1);
			// We must decode the URI segment to make sure the escaped
			// characters (e.g., whitespace that is represented as %20) are
			// correctly replaced.
			final String decoded = URI.decode(segment);
			return root.getProject(decoded);
		}
		throw new IllegalArgumentException("Can't handle URIs that do not reference platform resources: " + uri);
	}

	private static IJavaProject getJavaProject(final IProject project) {
		IJavaProject javaProject = null;
		if (isJavaProject(project)) {
			javaProject = JavaCore.create(project);
		}
		return javaProject;
	}

	/**
	 * Returns a list of all Java classifiers that are available in the classpath of
	 * the given project.
	 */
	public List<JDTJavaClassifier> getAllClassifiersInClassPath(final IJavaProject project) {
		return getAllClassifiersForPackageInClassPath(null, project);
	}

	/**
	 * Returns a list of all Java classifiers that are available in the classpath of
	 * the given project within the given package. If <code>packageName</code> is
	 * null, all classifiers in the project are returned.
	 */
	public List<JDTJavaClassifier> getAllClassifiersForPackageInClassPath(final String packageName,
			final IJavaProject project) {

		List<JDTJavaClassifier> classes = new ArrayList<>();
		try {
			final SearchEngine searchEngine = new SearchEngine();
			final ClassifierVisitor visitor = new ClassifierVisitor(project);

			// prepare search parameters
			char[][] packages = null;
			if (packageName != null) {
				packages = new char[][] { packageName.toCharArray() };
			}
			final IJavaProject[] projects = { project };
			final IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(projects);
			final int waitingPolicy = IJavaSearchConstants.FORCE_IMMEDIATE_SEARCH;
			// perform search
			searchEngine.searchAllTypeNames(packages, null, searchScope, visitor, waitingPolicy, null);

			classes = visitor.getClassifiersInClasspath();
		} catch (final JavaModelException e) {
			logWarning("Search for Java classifiers failed.", e);
		}
		return classes;
	}

	/**
	 * Logs the given exception.
	 */
	private static void logWarning(final String message, final JavaModelException exception) {
		final String pluginName = JdtPackage.class.getPackage().getName();
		final Status status = new Status(IStatus.WARNING, pluginName, message, exception);
		ResourcesPlugin.getPlugin().getLog().log(status);
	}
}
