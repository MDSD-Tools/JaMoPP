package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.emftext.language.java.parameters.OrdinaryParameter;

import com.google.inject.Inject;

public class ToOrdinaryParameterConverter {

	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;

	@Inject
	ToOrdinaryParameterConverter(UtilNamedElement utilNamedElement, ToTypeReferenceConverter toTypeReferenceConverter,
			ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilNamedElement = utilNamedElement;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	OrdinaryParameter convertToOrdinaryParameter(SingleVariableDeclaration decl) {
		OrdinaryParameter result = jdtResolverUtility.getOrdinaryParameter(decl.resolveBinding());
		decl.modifiers().forEach(obj -> result.getAnnotationsAndModifiers().add(
				toModifierOrAnnotationInstanceConverter.converToModifierOrAnnotationInstance((IExtendedModifier) obj)));
		result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(decl.getType()));
		toTypeReferenceConverter.convertToArrayDimensionsAndSet(decl.getType(), result);
		utilNamedElement.setNameOfElement(decl.getName(), result);
		decl.extraDimensions().forEach(obj -> toArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, decl);
		return result;
	}

}
