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

package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.emftext.language.java.classifiers.ConcreteClassifier;

import com.google.inject.Inject;

import jamopp.parser.jdt.other.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToConcreteClassifierConverter {

	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final ToInterfaceMemberConverter toInterfaceMember;
	private final ToClassOrInterfaceConverter toClassOrInterface;
	private final ToEnumConverter toEnumConverter;
	private final UtilNamedElement utilNamedElement;

	@Inject
	ToConcreteClassifierConverter(ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			UtilNamedElement utilNamedElement, ToInterfaceMemberConverter toInterfaceMember,
			ToEnumConverter toEnumConverter, ToClassOrInterfaceConverter toClassOrInterface) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toInterfaceMember = toInterfaceMember;
		this.toClassOrInterface = toClassOrInterface;
		this.toEnumConverter = toEnumConverter;
		this.utilNamedElement = utilNamedElement;
	}

	@SuppressWarnings("unchecked")
	public ConcreteClassifier convertToConcreteClassifier(AbstractTypeDeclaration typeDecl) {
		ConcreteClassifier result = null;
		if (typeDecl.getNodeType() == ASTNode.TYPE_DECLARATION) {
			result = toClassOrInterface.convertToClassOrInterface((TypeDeclaration) typeDecl);
		} else if (typeDecl.getNodeType() == ASTNode.ANNOTATION_TYPE_DECLARATION) {
			result = jdtResolverUtility.getAnnotation(typeDecl.resolveBinding());
			ConcreteClassifier fR = result;
			typeDecl.bodyDeclarations()
					.forEach(obj -> fR.getMembers().add(toInterfaceMember.convert((BodyDeclaration) obj)));
		} else {
			result = toEnumConverter.convert((EnumDeclaration) typeDecl);
		}
		ConcreteClassifier finalResult = result;
		typeDecl.modifiers().forEach(obj -> finalResult.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		utilNamedElement.setNameOfElement(typeDecl.getName(), result);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, typeDecl);
		return result;
	}

}
