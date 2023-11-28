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

package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;

abstract class PackageJDTASTVisitorAndConverter extends AbstractAndEmptyModelJDTASTVisitorAndConverter {

	PackageJDTASTVisitorAndConverter(UtilLayout layoutInformationConverter, UtilJDTResolver jdtResolverUtility,
			ToClassifierOrNamespaceClassifierReferenceConverter utilBaseConverter, ModifiersFactory modifiersFactory, ImportsFactory importsFactory,
			UtilNamedElement utilNamedElement, ToAnnotationInstanceConverter annotationInstanceConverter,
			UtilClassifierConverter classifierConverterUtility) {
		super(layoutInformationConverter, jdtResolverUtility, utilBaseConverter, modifiersFactory, importsFactory,
				utilNamedElement, annotationInstanceConverter, classifierConverterUtility);
		// TODO Auto-generated constructor stub
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
			node.getPackage().annotations().forEach(obj -> finalRoot.getAnnotations()
					.add(annotationInstanceConverter.convertToAnnotationInstance((Annotation) obj)));
			root.getNamespaces().clear();
			utilNamedElement.addNameToNameSpace(node.getPackage().getName(), root);
		}
		super.visit(node);
		return false;
	}
}
