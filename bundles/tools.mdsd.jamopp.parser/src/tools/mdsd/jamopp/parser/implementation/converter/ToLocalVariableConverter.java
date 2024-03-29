package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class ToLocalVariableConverter
		implements Converter<VariableDeclarationExpression, tools.mdsd.jamopp.model.java.variables.LocalVariable> {

	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<VariableDeclarationFragment, AdditionalLocalVariable> toAdditionalLocalVariableConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;

	@Inject
	public ToLocalVariableConverter(final UtilNamedElement utilNamedElement,
			final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<IExtendedModifier, AnnotationInstanceOrModifier> toModifierOrAnnotationInstanceConverter,
			final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			final UtilLayout layoutInformationConverter, final JdtResolver jdtResolverUtility,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			final Converter<VariableDeclarationFragment, AdditionalLocalVariable> toAdditionalLocalVariableConverter,
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter) {
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
	public tools.mdsd.jamopp.model.java.variables.LocalVariable convert(final VariableDeclarationExpression expr) {
		final VariableDeclarationFragment frag = (VariableDeclarationFragment) expr.fragments().get(0);
		tools.mdsd.jamopp.model.java.variables.LocalVariable loc;
		final IVariableBinding binding = frag.resolveBinding();
		if (binding == null) {
			loc = jdtResolverUtility.getLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
		} else {
			loc = jdtResolverUtility.getLocalVariable(binding);
		}
		utilNamedElement.setNameOfElement(frag.getName(), loc);
		expr.modifiers().forEach(obj -> loc.getAnnotationsAndModifiers()
				.add(toModifierOrAnnotationInstanceConverter.convert((IExtendedModifier) obj)));
		loc.setTypeReference(toTypeReferenceConverter.convert(expr.getType()));
		utilToArrayDimensionsAndSetConverter.convert(expr.getType(), loc);
		frag.extraDimensions().forEach(obj -> utilToArrayDimensionAfterAndSetConverter.convert((Dimension) obj, loc));
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
