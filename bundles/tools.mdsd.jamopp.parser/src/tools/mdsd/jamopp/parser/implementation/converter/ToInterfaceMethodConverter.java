package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.Type;

import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToInterfaceMethodConverter implements Converter<AnnotationTypeMemberDeclaration, InterfaceMethod> {

	private final StatementsFactory statementsFactory;
	private final JdtResolver iUtilJdtResolver;
	private final UtilNamedElement utilNamedElement;
	private final UtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final UtilLayout utilLayout;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;

	@Inject
	public ToInterfaceMethodConverter(final UtilTypeInstructionSeparation utilTypeInstructionSeparation,
			final UtilNamedElement utilNamedElement, final UtilLayout utilLayout, final JdtResolver iUtilJdtResolver,
			final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			final StatementsFactory statementsFactory,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.statementsFactory = statementsFactory;
		this.iUtilJdtResolver = iUtilJdtResolver;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilNamedElement = utilNamedElement;
		this.utilTypeInstructionSeparation = utilTypeInstructionSeparation;
		this.utilLayout = utilLayout;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public InterfaceMethod convert(final AnnotationTypeMemberDeclaration annDecl) {
		final IMethodBinding binding = annDecl.resolveBinding();
		InterfaceMethod result;
		if (binding != null) {
			result = iUtilJdtResolver.getInterfaceMethod(binding);
		} else {
			result = iUtilJdtResolver.getInterfaceMethod(annDecl.getName().getIdentifier());
		}
		annDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		result.setTypeReference(toTypeReferenceConverter.convert(annDecl.getType()));
		utilToArrayDimensionsAndSetConverter.convert(annDecl.getType(), result);
		utilNamedElement.setNameOfElement(annDecl.getName(), result);
		if (annDecl.getDefault() != null) {
			utilTypeInstructionSeparation.addAnnotationMethod(annDecl.getDefault(), result);
		}
		result.setStatement(statementsFactory.createEmptyStatement());
		utilLayout.convertToMinimalLayoutInformation(result, annDecl);
		return result;
	}

}
