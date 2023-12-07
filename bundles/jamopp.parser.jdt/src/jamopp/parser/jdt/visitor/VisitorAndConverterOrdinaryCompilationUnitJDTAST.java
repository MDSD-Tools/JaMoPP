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
import org.eclipse.jdt.core.dom.Name;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.ContainersFactory;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public class VisitorAndConverterOrdinaryCompilationUnitJDTAST extends VisitorAndConverterModuleJDTAST {

	@Inject
	protected VisitorAndConverterOrdinaryCompilationUnitJDTAST(IUtilLayout layoutInformationConverter,
			IUtilJdtResolver jdtResolverUtility, Converter<Name, TypeReference> utilBaseConverter,
			ModifiersFactory modifiersFactory, ImportsFactory importsFactory, IUtilNamedElement utilNamedElement,
			Converter<Annotation, AnnotationInstance> annotationInstanceConverter,
			Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility,
			ContainersFactory containersFactory, ModulesFactory modulesFactory) {
		super(layoutInformationConverter, jdtResolverUtility, utilBaseConverter, modifiersFactory, importsFactory,
				utilNamedElement, annotationInstanceConverter, classifierConverterUtility, containersFactory,
				modulesFactory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean visit(CompilationUnit node) {
		this.setConvertedElement(null);
		if (!node.types().isEmpty()) {
			this.setConvertedElement(this.convertToCompilationUnit(node));
		}
		super.visit(node);
		return false;
	}

	@SuppressWarnings("unchecked")
	private org.emftext.language.java.containers.CompilationUnit convertToCompilationUnit(CompilationUnit cu) {
		org.emftext.language.java.containers.CompilationUnit result = containersFactory.createCompilationUnit();
		result.setName("");
		layoutInformationConverter.convertJavaRootLayoutInformation(result, cu, getSource());
		cu.types().forEach(
				obj -> result.getClassifiers().add(ClassifierConverterUtility.convert((AbstractTypeDeclaration) obj)));
		return result;
	}
}
