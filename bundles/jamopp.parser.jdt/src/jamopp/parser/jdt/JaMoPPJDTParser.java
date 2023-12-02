package jamopp.parser.jdt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.emftext.language.java.JavaClasspath;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.containers.JavaRoot;

import jamopp.parser.api.JaMoPPParserAPI;

public final class JaMoPPJDTParser implements JaMoPPParserAPI {

	private static final JaMoPPJDTParserAdapter jaMoPPJDTParserAdapterStatic = new JaMoPPJDTParserAdapter();
	private final JaMoPPJDTParserAdapter jaMoPPJDTParserAdapter;

	public static String[] getClasspathEntries(Path dir) {
		return jaMoPPJDTParserAdapterStatic.getClasspathEntries(dir);
	}

	public static Map<String, CompilationUnit> getCompilationUnits(ASTParser parser, String[] classpathEntries,
			String[] sources, String[] encodings) {
		return jaMoPPJDTParserAdapterStatic.getCompilationUnits(parser, classpathEntries, sources, encodings);
	}

	@SuppressWarnings("deprecation")
	public static ASTParser getJavaParser(String version) {
		return jaMoPPJDTParserAdapterStatic.getJavaParser(version);
	}

	public JaMoPPJDTParser() {
		this.jaMoPPJDTParserAdapter = new JaMoPPJDTParserAdapter();
	}

	public List<JavaRoot> convertCompilationUnits(Map<String, CompilationUnit> compilationUnits) {
		return jaMoPPJDTParserAdapter.convertCompilationUnits(compilationUnits);
	}

	@Override
	public boolean equals(Object obj) {
		return jaMoPPJDTParserAdapter.equals(obj);
	}

	public <T> Set<T> get(Class<T> type) {
		return jaMoPPJDTParserAdapter.get(type);
	}

	/**
	 * @return the resourceSet
	 */
	public ResourceSet getResourceSet() {
		return jaMoPPJDTParserAdapter.getResourceSet();
	}

	public String[] getSourcepathEntries(Path dir) {
		return jaMoPPJDTParserAdapter.getSourcepathEntries(dir);
	}

	@Override
	public int hashCode() {
		return jaMoPPJDTParserAdapter.hashCode();
	}

	@Override
	public JavaRoot parse(String fileName, InputStream input) {
		return jaMoPPJDTParserAdapter.parse(fileName, input);
	}

	public ResourceSet parseDirectory(ASTParser parser, Path dir) {
		return jaMoPPJDTParserAdapter.parseDirectory(parser, dir);
	}

	@Override
	public ResourceSet parseDirectory(Path dir) {
		return jaMoPPJDTParserAdapter.parseDirectory(dir);
	}

	@Override
	public Resource parseFile(Path file) {
		return jaMoPPJDTParserAdapter.parseFile(file);
	}

	public ResourceSet parsePackage(IPackageFragment javaPackage) {
		return jaMoPPJDTParserAdapter.parsePackage(javaPackage);
	}

	public ResourceSet parseProject(IJavaProject javaProject) {
		return jaMoPPJDTParserAdapter.parseProject(javaProject);
	}

	@Override
	public void setResourceSet(ResourceSet set) {
		jaMoPPJDTParserAdapter.setResourceSet(set);
	}

	@Override
	public String toString() {
		return jaMoPPJDTParserAdapter.toString();
	}

}