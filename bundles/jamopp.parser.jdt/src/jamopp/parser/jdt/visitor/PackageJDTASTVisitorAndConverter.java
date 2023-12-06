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

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ModulesFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.converter.ToAnnotationInstanceConverter;
import jamopp.parser.jdt.converter.implementation.converter.ToClassifierOrNamespaceClassifierReferenceConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public abstract class PackageJDTASTVisitorAndConverter extends VisitorAndConverterAbstractAndEmptyModelJDTAST {

	@Inject
	protected PackageJDTASTVisitorAndConverter(IUtilLayout layoutInformationConverter,
			IUtilJdtResolver jdtResolverUtility, ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter,
			ModifiersFactory modifiersFactory, ImportsFactory importsFactory, IUtilNamedElement utilNamedElement,
			ToAnnotationInstanceConverter annotationInstanceConverter,
			ToConverter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility,
			ContainersFactory containersFactory, ModulesFactory modulesFactory) {
		super(layoutInformationConverter, jdtResolverUtility, utilBaseConverter, modifiersFactory, importsFactory,
				utilNamedElement, annotationInstanceConverter, classifierConverterUtility, containersFactory,
				modulesFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(CompilationUnit node) {
		org.emftext.language.java.containers.JavaRoot root = this.getConvertedElement();
		if (root == null && node.getPackage() != null) {
			root = jdtResolverUtility.getPackage(node.getPackage().resolveBinding());
			root.setName("");
			layoutInformationConverter.convertJavaRootLayoutInformation(root, node, this.getSource());
			this.setConvertedElement(root);
		}
		org.emftext.language.java.containers.JavaRoot finalRoot = root;
		if (node.getPackage() != null) {
			node.getPackage().annotations().forEach(
					obj -> finalRoot.getAnnotations().add(annotationInstanceConverter.convert((Annotation) obj)));
			root.getNamespaces().clear();
			utilNamedElement.addNameToNameSpace(node.getPackage().getName(), root);
		}
		super.visit(node);
		return false;
	}
}
