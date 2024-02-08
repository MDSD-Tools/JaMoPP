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

package tools.mdsd.jamopp.parser.implementation.visitor;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.ModuleDeclaration;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.model.java.containers.Module;
import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;
import tools.mdsd.jamopp.parser.interfaces.visitor.AbstractVisitor;

@Singleton
public class VisitorAndConverterAbstractAndEmptyModelJDTAST extends AbstractVisitor {

	private final ContainersFactory containersFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtResolverUtility;
	private final Converter<Annotation, AnnotationInstance> annotationInstanceConverter;
	private final Converter<ImportDeclaration, Import> toImportConverter;

	private final Provider<Converter<CompilationUnit, tools.mdsd.jamopp.model.java.containers.CompilationUnit>> toCompilationUnitConverter;
	private final Provider<Converter<ModuleDeclaration, Module>> toModuleConverter;

	private JavaRoot convertedRootElement;
	private String originalSource;

	@Inject
	public VisitorAndConverterAbstractAndEmptyModelJDTAST(final UtilNamedElement utilNamedElement,
			@Named("ToImportConverter") final Converter<ImportDeclaration, Import> toImportConverter,
			final UtilLayout layoutInformationConverter, final JdtResolver jdtResolverUtility,
			final ContainersFactory containersFactory,
			final Converter<Annotation, AnnotationInstance> annotationInstanceConverter,
			final Provider<Converter<ModuleDeclaration, Module>> toModuleConverter,
			final Provider<Converter<CompilationUnit, tools.mdsd.jamopp.model.java.containers.CompilationUnit>> toCompilationUnitConverter) {
		this.containersFactory = containersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.toImportConverter = toImportConverter;
		this.toCompilationUnitConverter = toCompilationUnitConverter;
		this.toModuleConverter = toModuleConverter;
	}

	@Override
	public void setSource(final String src) {
		originalSource = src;
	}

	@Override
	public String getSource() {
		return originalSource;
	}

	@Override
	public void setConvertedElement(final JavaRoot root) {
		convertedRootElement = root;
	}

	@Override
	public JavaRoot getConvertedElement() {
		return convertedRootElement;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(final CompilationUnit node) {
		setConvertedElement(null);
		if (!node.types().isEmpty()) {
			setConvertedElement(toCompilationUnitConverter.get().convert(node));
		}

		if (node.getModule() != null) {
			final Module module = toModuleConverter.get().convert(node.getModule());
			setConvertedElement(module);
		}

		if (convertedRootElement == null && node.getPackage() != null) {
			convertedRootElement = jdtResolverUtility.getPackage(node.getPackage().resolveBinding());
			convertedRootElement.setName("");
			layoutInformationConverter.convertJavaRootLayoutInformation(convertedRootElement, node, getSource());
			setConvertedElement(convertedRootElement);
		}

		if (convertedRootElement != null && node.getPackage() != null) {
			node.getPackage().annotations().forEach(obj -> convertedRootElement.getAnnotations()
					.add(annotationInstanceConverter.convert((Annotation) obj)));
			convertedRootElement.getNamespaces().clear();
			utilNamedElement.addNameToNameSpace(node.getPackage().getName(), convertedRootElement);
		}

		if (convertedRootElement == null) {
			convertedRootElement = containersFactory.createEmptyModel();
			convertedRootElement.setName("");
		}

		for (final Object obj : node.imports()) {
			convertedRootElement.getImports().add(toImportConverter.convert((ImportDeclaration) obj));
		}
		return false;
	}

}
