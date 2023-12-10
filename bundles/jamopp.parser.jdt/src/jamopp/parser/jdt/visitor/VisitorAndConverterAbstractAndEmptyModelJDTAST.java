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

package jamopp.parser.jdt.visitor;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.ModuleDeclaration;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.containers.JavaRoot;
import org.emftext.language.java.imports.Import;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

@Singleton
public class VisitorAndConverterAbstractAndEmptyModelJDTAST extends AbstractVisitor {

	private final ContainersFactory containersFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilJdtResolver jdtResolverUtility;
	private final Converter<Annotation, AnnotationInstance> annotationInstanceConverter;
	private final Converter<ImportDeclaration, Import> toImportConverter;
	private final Converter<CompilationUnit, org.emftext.language.java.containers.CompilationUnit> toCompilationUnitConverter;
	private final Converter<ModuleDeclaration, org.emftext.language.java.containers.Module> toModuleConverter;

	private JavaRoot convertedRootElement;
	private String originalSource;

	@Inject
	public VisitorAndConverterAbstractAndEmptyModelJDTAST(IUtilNamedElement utilNamedElement,
			@Named("ToImportConverter") Converter<ImportDeclaration, Import> toImportConverter,
			IUtilLayout layoutInformationConverter, IUtilJdtResolver jdtResolverUtility,
			ContainersFactory containersFactory, Converter<Annotation, AnnotationInstance> annotationInstanceConverter,
			Converter<ModuleDeclaration, org.emftext.language.java.containers.Module> toModuleConverter,
			Converter<CompilationUnit, org.emftext.language.java.containers.CompilationUnit> toCompilationUnitConverter) {
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
	public void setSource(String src) {
		originalSource = src;
	}

	@Override
	public JavaRoot getConvertedElement() {
		return convertedRootElement;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(CompilationUnit node) {
		convertedRootElement = null;
		if (!node.types().isEmpty()) {
			convertedRootElement = toCompilationUnitConverter.convert(node);
			layoutInformationConverter.convertJavaRootLayoutInformation(getConvertedElement(), node, originalSource);
		}
		if (node.getModule() != null) {
			org.emftext.language.java.containers.Module module = toModuleConverter.convert(node.getModule());
			layoutInformationConverter.convertJavaRootLayoutInformation(module, node, originalSource);
			convertedRootElement = module;
		}
		org.emftext.language.java.containers.JavaRoot root = this.getConvertedElement();
		if (root == null && node.getPackage() != null) {
			root = jdtResolverUtility.getPackage(node.getPackage().resolveBinding());
			root.setName("");
			layoutInformationConverter.convertJavaRootLayoutInformation(root, node, originalSource);
			convertedRootElement = root;
		}
		org.emftext.language.java.containers.JavaRoot finalRoot = root;
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

}
