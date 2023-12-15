package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.UtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;

public class ToInterfaceMethodConverter implements Converter<AnnotationTypeMemberDeclaration, InterfaceMethod> {

	private final StatementsFactory statementsFactory;
	private final UtilJdtResolver utilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final UtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final UtilLayout utilLayout;
	private final UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	ToInterfaceMethodConverter(UtilTypeInstructionSeparation utilTypeInstructionSeparation,
			UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJdtResolver,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			StatementsFactory statementsFactory,
			UtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.statementsFactory = statementsFactory;
		this.utilJdtResolver = utilJdtResolver;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilNamedElement = utilNamedElement;
		this.utilTypeInstructionSeparation = utilTypeInstructionSeparation;
		this.utilLayout = utilLayout;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	public InterfaceMethod convert(AnnotationTypeMemberDeclaration annDecl) {
		IMethodBinding binding = annDecl.resolveBinding();
		InterfaceMethod result;
		if (binding != null) {
			result = utilJdtResolver.getInterfaceMethod(binding);
		} else {
			result = utilJdtResolver.getInterfaceMethod(annDecl.getName().getIdentifier());
		}
		annDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		result.setTypeReference(toTypeReferenceConverter.convert(annDecl.getType()));
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(annDecl.getType(), result);
		utilNamedElement.setNameOfElement(annDecl.getName(), result);
		if (annDecl.getDefault() != null) {
			utilTypeInstructionSeparation.addAnnotationMethod(annDecl.getDefault(), result);
		}
		result.setStatement(statementsFactory.createEmptyStatement());
		utilLayout.convertToMinimalLayoutInformation(result, annDecl);
		return result;
	}

}