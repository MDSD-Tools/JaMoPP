package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.variables.AdditionalLocalVariable;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.interfaces.helper.IUtilToArrayDimensionAfterAndSetConverter;

public class ToAdditionalLocalVariableConverter
		implements Converter<VariableDeclarationFragment, AdditionalLocalVariable> {

	private final IUtilLayout layoutInformationConverter;
	private final IUtilJdtResolver jdtResolverUtility;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;

	@Inject
	ToAdditionalLocalVariableConverter(IUtilNamedElement utilNamedElement,
			IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			IUtilLayout layoutInformationConverter, IUtilJdtResolver jdtResolverUtility,
			Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility) {
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
