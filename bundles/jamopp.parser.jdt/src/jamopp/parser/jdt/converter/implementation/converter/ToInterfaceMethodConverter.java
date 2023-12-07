package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;

public class ToInterfaceMethodConverter implements Converter<AnnotationTypeMemberDeclaration, InterfaceMethod> {

	private final StatementsFactory statementsFactory;
	private final IUtilJdtResolver iUtilJdtResolver;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final IUtilLayout utilLayout;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	ToInterfaceMethodConverter(IUtilTypeInstructionSeparation utilTypeInstructionSeparation,
			IUtilNamedElement utilNamedElement, IUtilLayout utilLayout, IUtilJdtResolver iUtilJdtResolver,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			StatementsFactory statementsFactory,
			IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.statementsFactory = statementsFactory;
		this.iUtilJdtResolver = iUtilJdtResolver;
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
			result = iUtilJdtResolver.getInterfaceMethod(binding);
		} else {
			result = iUtilJdtResolver.getInterfaceMethod(annDecl.getName().getIdentifier());
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
