package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.variables.AdditionalLocalVariable;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.implementation.helper.UtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public class ToAdditionalLocalVariableConverter
		implements ToConverter<VariableDeclarationFragment, AdditionalLocalVariable> {

	private final IUtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final IUtilNamedElement utilNamedElement;
	private final UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;

	@Inject
	ToAdditionalLocalVariableConverter(IUtilNamedElement utilNamedElement,
			UtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			IUtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ToConverter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.utilNamedElement = utilNamedElement;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	public AdditionalLocalVariable convert(VariableDeclarationFragment frag) {
		AdditionalLocalVariable result;
		IVariableBinding binding = frag.resolveBinding();
		if (binding == null) {
			result = jdtResolverUtility
					.getAdditionalLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
		} else {
			result = jdtResolverUtility.getAdditionalLocalVariable(frag.resolveBinding());
		}
		utilNamedElement.setNameOfElement(frag.getName(), result);
		frag.extraDimensions().forEach(obj -> utilToArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, result));
		if (frag.getInitializer() != null) {
			result.setInitialValue(expressionConverterUtility.convert(frag.getInitializer()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, frag);
		return result;
	}
}
