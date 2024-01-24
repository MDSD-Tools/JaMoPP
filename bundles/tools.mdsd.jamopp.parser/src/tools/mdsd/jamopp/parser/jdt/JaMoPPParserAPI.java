/*******************************************************************************
 * Copyright (c) 2020, Martin Armbruster
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Martin Armbruster
 *      - Initial implementation
 ******************************************************************************/

package tools.mdsd.jamopp.parser.jdt;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import tools.mdsd.jamopp.model.java.containers.JavaRoot;

// TODO: Auto-generated Javadoc (partial)
/**
 * This interface provides an API for parsers that read Java source files and
 * convert them to Java model instances.
 */
public interface JaMoPPParserAPI {

	/**
	 * Reads an InputStream and parses its content into a Java model instance.
	 *
	 * @param fileName name of the Java source file which will be read.
	 * @param input    the InputStream to read.
	 * @return the created Java model instance or null if the InputStream could not
	 *         be read.
	 */
	JavaRoot parse(String fileName, InputStream input);

	/**
	 * Reads a file and parses its content into a Java model instance.
	 *
	 * @param file the Java source file.
	 * @return the created Java model instance contained in its associated Resource
	 *         instance or null if the file could not be read.
	 */
	Resource parseFile(Path file);

	/**
	 * Visits all files and directories in a directory and parses all found Java
	 * source files. It is assumed that the given directory is a Java source folder
	 * containing sub-directories representing a Java package hierarchy.
	 *
	 * @param directory the directory to search for Java source files.
	 * @return a ResourceSet containing all parsed source files with their
	 *         associated Resources.
	 */
	ResourceSet parseDirectory(Path directory);

	/**
	 * Sets the ResourceSet that is used to create Resources if new Resource
	 * instances are needed. If no ResourceSet is provided, a ResourceSet is
	 * created.
	 *
	 * @param set the ResourceSet.
	 */
	void setResourceSet(ResourceSet set);

	/**
	 * Parses the uri.
	 *
	 * @param uri the uri
	 * @return the resource set
	 */
	default ResourceSet parseUri(final URI uri) {
		ResourceSet returnValue = null;
		if (uri.isFile()) {
			returnValue = parseFile(Path.of(uri.toFileString())).getResourceSet();
		} else if (uri.hasPath()) {
			returnValue = parseDirectory(Path.of(uri.path()));
		}
		return returnValue;
	}

	/**
	 * Convert compilation units.
	 *
	 * @param compilationUnits the compilation units
	 * @return the list
	 */
	List<JavaRoot> convertCompilationUnits(Map<String, CompilationUnit> compilationUnits);

	/**
	 * Gets the.
	 *
	 * @param <T>  the generic type
	 * @param type the type
	 * @return the sets the
	 */
	<T> Set<T> get(Class<T> type);

	/**
	 * Gets the resource set.
	 *
	 * @return the resource set
	 */
	ResourceSet getResourceSet();

	/**
	 * Parses the directory.
	 *
	 * @param parser the parser
	 * @param dir    the dir
	 * @return the resource set
	 */
	ResourceSet parseDirectory(ASTParser parser, Path dir);

	/**
	 * Parses the package.
	 *
	 * @param javaPackage the java package
	 * @return the resource set
	 */
	ResourceSet parsePackage(IPackageFragment javaPackage);

	/**
	 * Parses the project.
	 *
	 * @param javaProject the java project
	 * @return the resource set
	 */
	ResourceSet parseProject(IJavaProject javaProject);

	/**
	 * Gets the sourcepath entries.
	 *
	 * @param dir the dir
	 * @return the sourcepath entries
	 */
	String[] getSourcepathEntries(Path dir);
}
