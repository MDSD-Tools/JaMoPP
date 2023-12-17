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

package tools.mdsd.jamopp.parser.jdt.implementation.visitor;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.model.java.imports.Import;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

@Singleton
public class VisitorAndConverterAbstractAndEmptyModelJDTAST extends AbstractVisitor {

	private final ContainersFactory containersFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final UtilJdtResolver jdtResolverUtility;
	private final Converter<Annotation, AnnotationInstance> annotationInstanceConverter;
	private final Converter<ImportDeclaration, Import> toImportConverter;

	private Converter<CompilationUnit, tools.mdsd.jamopp.model.java.containers.CompilationUnit> toCompilationUnitConverter;
	private Converter<ModuleDeclaration, tools.mdsd.jamopp.model.java.containers.Module> toModuleConverter;

	private JavaRoot convertedRootElement;
	private String originalSource;

	@Inject
	public VisitorAndConverterAbstractAndEmptyModelJDTAST(UtilNamedElement utilNamedElement,
			@Named("ToImportConverter") Converter<ImportDeclaration, Import> toImportConverter,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ContainersFactory containersFactory,
			Converter<Annotation, AnnotationInstance> annotationInstanceConverter) {
		this.containersFactory = containersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.toImportConverter = toImportConverter;
	}

	@Override
	public void setSource(String src) {
		originalSource = src;
	}

	@Override
	public String getSource() {
		return originalSource;
	}

	@Override
	public void setConvertedElement(JavaRoot root) {
		convertedRootElement = root;
	}

	@Override
	public JavaRoot getConvertedElement() {
		return convertedRootElement;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(CompilationUnit node) {
		this.setConvertedElement(null);
		if (!node.types().isEmpty()) {
			this.setConvertedElement(toCompilationUnitConverter.convert(node));
		}
		if (node.getModule() != null) {
			tools.mdsd.jamopp.model.java.containers.Module module = toModuleConverter.convert(node.getModule());
			this.setConvertedElement(module);
		}
		tools.mdsd.jamopp.model.java.containers.JavaRoot root = this.getConvertedElement();
		if (root == null && node.getPackage() != null) {
			root = jdtResolverUtility.getPackage(node.getPackage().resolveBinding());
			root.setName("");
			layoutInformationConverter.convertJavaRootLayoutInformation(root, node, this.getSource());
			this.setConvertedElement(root);
		}
		tools.mdsd.jamopp.model.java.containers.JavaRoot finalRoot = root;
		if (node.getPackage() != null) {
			node.getPackage().annotations().forEach(
					obj -> finalRoot.getAnnotations().add(annotationInstanceConverter.convert((Annotation) obj)));
			root.getNamespaces().clear();
			utilNamedElement.addNameToNameSpace(node.getPackage().getName(), root);
		}
		if (convertedRootElement == null) {
			convertedRootElement = containersFactory.createEmptyModel();
			convertedRootElement.setName("");
		}
		for (Object obj : node.imports()) {
			convertedRootElement.getImports().add(toImportConverter.convert((ImportDeclaration) obj));
		}
		return false;
	}

	@Inject
	public void setToModuleConverter(
			Converter<ModuleDeclaration, tools.mdsd.jamopp.model.java.containers.Module> toModuleConverter) {
		this.toModuleConverter = toModuleConverter;
	}

	@Inject
	public void setToCompilationUnitConverter(
			Converter<CompilationUnit, tools.mdsd.jamopp.model.java.containers.CompilationUnit> toCompilationUnitConverter) {
		this.toCompilationUnitConverter = toCompilationUnitConverter;
	}

}
