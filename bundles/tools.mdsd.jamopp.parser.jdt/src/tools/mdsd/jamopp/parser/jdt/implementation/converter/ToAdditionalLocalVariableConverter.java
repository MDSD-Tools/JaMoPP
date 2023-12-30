package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToAdditionalLocalVariableConverter
		implements Converter<VariableDeclarationFragment, AdditionalLocalVariable> {

	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	ToAdditionalLocalVariableConverter(UtilNamedElement utilNamedElement,
			ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			UtilLayout layoutInformationConverter, JdtResolver jdtResolverUtility,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility) {
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
				.convert((Dimension) obj, result));
		if (frag.getInitializer() != null) {
			result.setInitialValue(expressionConverterUtility.convert(frag.getInitializer()));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(result, frag);
		return result;
	}
}
