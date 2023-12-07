package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.variables.AdditionalLocalVariable;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilToArrayDimensionsAndSetConverter;

public class ToLocalVariableConverter
		implements Converter<VariableDeclarationExpression, org.emftext.language.java.variables.LocalVariable> {

	private final IUtilLayout layoutInformationConverter;
	private final IUtilJdtResolver jdtResolverUtility;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<VariableDeclarationFragment, AdditionalLocalVariable> toAdditionalLocalVariableConverter;
	private final Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;

	@Inject
	ToLocalVariableConverter(IUtilNamedElement utilNamedElement,
			Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			IUtilToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			IUtilLayout layoutInformationConverter, IUtilJdtResolver jdtResolverUtility,
			Converter<Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility,
			Converter<VariableDeclarationFragment, AdditionalLocalVariable> toAdditionalLocalVariableConverter,
			IUtilToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.utilNamedElement = utilNamedElement;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toAdditionalLocalVariableConverter = toAdditionalLocalVariableConverter;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.variables.LocalVariable convert(VariableDeclarationExpression expr) {
		VariableDeclarationFragment frag = (VariableDeclarationFragment) expr.fragments().get(0);
		org.emftext.language.java.variables.LocalVariable loc;
		IVariableBinding binding = frag.resolveBinding();
		if (binding == null) {
			loc = jdtResolverUtility.getLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
		} else {
			loc = jdtResolverUtility.getLocalVariable(binding);
		}
		utilNamedElement.setNameOfElement(frag.getName(), loc);
		expr.modifiers().forEach(obj -> loc.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		loc.setTypeReference(toTypeReferenceConverter.convert(expr.getType()));
		utilToArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(expr.getType(), loc);
		frag.extraDimensions().forEach(obj -> utilToArrayDimensionAfterAndSetConverter
				.convertToArrayDimensionAfterAndSet((Dimension) obj, loc));
		if (frag.getInitializer() != null) {
			loc.setInitialValue(expressionConverterUtility.convert(frag.getInitializer()));
		}
		for (int index = 1; index < expr.fragments().size(); index++) {
			loc.getAdditionalLocalVariables().add(toAdditionalLocalVariableConverter
					.convert((VariableDeclarationFragment) expr.fragments().get(index)));
		}
		layoutInformationConverter.convertToMinimalLayoutInformation(loc, expr);
		return loc;
	}

}
