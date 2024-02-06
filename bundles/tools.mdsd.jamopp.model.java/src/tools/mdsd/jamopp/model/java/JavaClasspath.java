/*******************************************************************************
 * Copyright (c) 2006-2015
 * Software Technology Group, Dresden University of Technology
 * DevBoost GmbH, Dresden, Amtsgericht Dresden, HRB 34001
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Software Technology Group - TU Dresden, Germany;
 *   DevBoost GmbH - Dresden, Germany
 *      - initial API and implementation
 *   Martin Armbruster
 *      - Extension for loading the standard library in Java 9+
 ******************************************************************************/

package tools.mdsd.jamopp.model.java;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.CompilationUnit;

/**
 * This class is responsible for managing and retrieving Java resources to
 * establish inter-model references between different Java classes represented
 * as EMF-models.
 */
public final class JavaClasspath {
	/**
	 * Singleton instance.
	 */
	private static JavaClasspath globalClasspath;
	private final Set<tools.mdsd.jamopp.model.java.containers.Module> modules = new HashSet<>();
	private final Set<tools.mdsd.jamopp.model.java.containers.Package> packages = new HashSet<>();
	private final Set<ConcreteClassifier> classifiers = new HashSet<>();

	static {
		globalClasspath = new JavaClasspath();
	}

	private JavaClasspath() {
		// Is singleton.
	}

	public static JavaClasspath get() {
		return globalClasspath;
	}

	public void clear() {
		modules.clear();
		packages.clear();
		classifiers.clear();
	}

	public void registerPackage(final tools.mdsd.jamopp.model.java.containers.Package pack) {
		packages.add(pack);
	}

	public void registerModule(final tools.mdsd.jamopp.model.java.containers.Module module) {
		modules.add(module);
	}

	public void registerConcreteClassifier(final ConcreteClassifier classifier) {
		classifiers.add(classifier);
	}

	public tools.mdsd.jamopp.model.java.containers.Package getPackage(final String packageName) {
		return packages.stream().filter(p -> p.getNamespacesAsString().equals(packageName)).findFirst().orElse(null);
	}

	public tools.mdsd.jamopp.model.java.containers.Module getModule(final String moduleName) {
		return modules.stream().filter(m -> m.getNamespacesAsString().equals(moduleName)).findFirst().orElse(null);
	}

	public ConcreteClassifier getConcreteClassifier(final URI uri) {
		return classifiers.stream()
				.filter(c -> c.eResource() != null && c.eResource().getURI().toString().equals(uri.toString()))
				.findFirst().orElse(null);
	}

	public ConcreteClassifier getConcreteClassifier(final String fullQualifiedClassifierName) {
		return classifiers.stream().filter(c -> c.getQualifiedName().equals(fullQualifiedClassifierName)).findFirst()
				.orElse(null);
	}

	public ConcreteClassifier getFirstConcreteClassifier(final String simpleClassifierName) {
		return classifiers.stream().filter(c -> c.getName().equals(simpleClassifierName)).findFirst().orElse(null);
	}

	public CompilationUnit getCompilationUnit(final String fullQualifiedClassifierName) {
		ConcreteClassifier classifier = getConcreteClassifier(fullQualifiedClassifierName);
		CompilationUnit result = null;
		if (classifier != null) {
			while (classifier.eContainer() != null && !(classifier.eContainer() instanceof CompilationUnit)) {
				classifier = (ConcreteClassifier) classifier.eContainer();
			}
			if (classifier.eContainer() != null) {
				result = (CompilationUnit) classifier.eContainer();
			}
		}
		return result;
	}

	public Resource getResource(final URI resourceURI) {
		final ConcreteClassifier classifier = getConcreteClassifier(resourceURI);
		final tools.mdsd.jamopp.model.java.containers.Package pack = packages.stream()
				.filter(p -> p.eResource() != null)
				.filter(p -> p.eResource().getURI().toString().equals(resourceURI.toString())).findFirst().orElse(null);
		final tools.mdsd.jamopp.model.java.containers.Module mod = modules.stream().filter(m -> m.eResource() != null)
				.filter(m -> m.eResource().getURI().toString().equals(resourceURI.toString())).findFirst().orElse(null);

		Resource result = null;
		if (classifier != null) {
			result = classifier.eResource();
		} else if (pack != null) {
			result = pack.eResource();
		} else if (mod != null) {
			result = mod.eResource();
		}
		return result;
	}
}
