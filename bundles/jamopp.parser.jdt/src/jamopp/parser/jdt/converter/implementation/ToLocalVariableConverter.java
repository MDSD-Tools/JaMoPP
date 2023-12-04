package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.ToArrayDimensionAfterAndSetConverter;
import jamopp.parser.jdt.converter.helper.ToArrayDimensionsAndSetConverter;
import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;
import jamopp.parser.jdt.util.UtilLayout;
import jamopp.parser.jdt.util.UtilNamedElement;

public class ToLocalVariableConverter
		implements ToConverter<VariableDeclarationExpression, org.emftext.language.java.variables.LocalVariable> {

	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final ToExpressionConverter expressionConverterUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter;
	private final ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToAdditionalLocalVariableConverter toAdditionalLocalVariableConverter;
	private final ToArrayDimensionsAndSetConverter toArrayDimensionsAndSetConverter;

	@Inject
	ToLocalVariableConverter(UtilNamedElement utilNamedElement, ToTypeReferenceConverter toTypeReferenceConverter,
			ToModifierOrAnnotationInstanceConverter toModifierOrAnnotationInstanceConverter,
			ToArrayDimensionAfterAndSetConverter toArrayDimensionAfterAndSetConverter,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ToExpressionConverter expressionConverterUtility,
			ToAdditionalLocalVariableConverter toAdditionalLocalVariableConverter, ToArrayDimensionsAndSetConverter toArrayDimensionsAndSetConverter) {
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.utilNamedElement = utilNamedElement;
		this.toArrayDimensionAfterAndSetConverter = toArrayDimensionAfterAndSetConverter;
		this.toModifierOrAnnotationInstanceConverter = toModifierOrAnnotationInstanceConverter;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toAdditionalLocalVariableConverter = toAdditionalLocalVariableConverter;
		this.toArrayDimensionsAndSetConverter = toArrayDimensionsAndSetConverter;
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
		toArrayDimensionsAndSetConverter.convertToArrayDimensionsAndSet(expr.getType(), loc);
		frag.extraDimensions().forEach(obj -> toArrayDimensionAfterAndSetConverter.convertToArrayDimensionAfterAndSet((Dimension) obj, loc));
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
