package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.other.UtilJdtResolver;
import jamopp.parser.jdt.other.UtilTypeInstructionSeparation;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToInterfaceMethodConverter extends ToConverter<AnnotationTypeMemberDeclaration, InterfaceMethod> {

	private final StatementsFactory statementsFactory;
	private final UtilJdtResolver utilJdtResolver;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final UtilNamedElement utilNamedElement;
	private final UtilTypeInstructionSeparation utilTypeInstructionSeparation;
	private final UtilLayout utilLayout;

	@Inject
	ToInterfaceMethodConverter(UtilTypeInstructionSeparation utilTypeInstructionSeparation,
			UtilNamedElement utilNamedElement, UtilLayout utilLayout, UtilJdtResolver utilJdtResolver,
			ToTypeReferenceConverter toTypeReferenceConverter,
			ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.utilJdtResolver = utilJdtResolver;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.utilNamedElement = utilNamedElement;
		this.utilTypeInstructionSeparation = utilTypeInstructionSeparation;
		this.utilLayout = utilLayout;
	}

	@SuppressWarnings("unchecked")
	public
	InterfaceMethod convert(AnnotationTypeMemberDeclaration annDecl) {
		IMethodBinding binding = annDecl.resolveBinding();
		InterfaceMethod result;
		if (binding != null) {
			result = utilJdtResolver.getInterfaceMethod(binding);
		} else {
			result = utilJdtResolver.getInterfaceMethod(annDecl.getName().getIdentifier());
		}
		annDecl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers().add(
				toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		result.setTypeReference(toTypeReferenceConverter.convert(annDecl.getType()));
		toTypeReferenceConverter.convertToArrayDimensionsAndSet(annDecl.getType(), result);
		utilNamedElement.setNameOfElement(annDecl.getName(), result);
		if (annDecl.getDefault() != null) {
			utilTypeInstructionSeparation.addAnnotationMethod(annDecl.getDefault(), result);
		}
		result.setStatement(statementsFactory.createEmptyStatement());
		utilLayout.convertToMinimalLayoutInformation(result, annDecl);
		return result;
	}

}
