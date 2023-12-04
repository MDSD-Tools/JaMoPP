package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToAdditionalLocalVariableConverter {

	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final ToExpressionConverter expressionConverterUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;

	@Inject
	ToAdditionalLocalVariableConverter(UtilNamedElement utilNamedElement,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ToExpressionConverter expressionConverterUtility) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.utilNamedElement = utilNamedElement;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	org.emftext.language.java.variables.AdditionalLocalVariable convertToAdditionalLocalVariable(
			VariableDeclarationFragment frag) {
		org.emftext.language.java.variables.AdditionalLocalVariable result;
		IVariableBinding binding = frag.resolveBinding();
		if (binding == null) {
			result = jdtResolverUtility
					.getAdditionalLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
		} else {
			result = jdtResolverUtility.getAdditionalLocalVariable(frag.resolveBinding());
		}
		utilNamedElement.setNameOfElement(frag.getName(), result);
		frag.extraDimensions().forEach(obj -> toArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		if (frag.getInitializer() != null) {
			result.setInitialValue(expressionConverterUtility.convert(frag.getInitializer()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, frag);
		return result;
	}
}
