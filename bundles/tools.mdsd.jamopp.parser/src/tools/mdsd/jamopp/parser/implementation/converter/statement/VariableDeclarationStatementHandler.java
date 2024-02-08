package tools.mdsd.jamopp.parser.implementation.converter.statement;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Dimension;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionAfterAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.converter.ToArrayDimensionsAndSetConverter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

public class VariableDeclarationStatementHandler implements StatementHandler {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter;
	private final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter;
	private final Converter<VariableDeclarationFragment, AdditionalLocalVariable> toAdditionalLocalVariableConverter;

	@Inject
	public VariableDeclarationStatementHandler(
			final ToArrayDimensionsAndSetConverter utilToArrayDimensionsAndSetConverter,
			final ToArrayDimensionAfterAndSetConverter utilToArrayDimensionAfterAndSetConverter,
			final UtilNamedElement utilNamedElement, final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<VariableDeclarationFragment, AdditionalLocalVariable> toAdditionalLocalVariableConverter,
			final StatementsFactory statementsFactory, final UtilLayout layoutInformationConverter,
			final JdtResolver jdtResolverUtility,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			final Converter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilNamedElement = utilNamedElement;
		this.utilToArrayDimensionsAndSetConverter = utilToArrayDimensionsAndSetConverter;
		this.utilToArrayDimensionAfterAndSetConverter = utilToArrayDimensionAfterAndSetConverter;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.toAdditionalLocalVariableConverter = toAdditionalLocalVariableConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final VariableDeclarationStatement varSt = (VariableDeclarationStatement) statement;
		final tools.mdsd.jamopp.model.java.statements.LocalVariableStatement result = statementsFactory
				.createLocalVariableStatement();
		final VariableDeclarationFragment frag = (VariableDeclarationFragment) varSt.fragments().get(0);
		tools.mdsd.jamopp.model.java.variables.LocalVariable locVar;
		final IVariableBinding binding = frag.resolveBinding();
		if (binding == null) {
			locVar = jdtResolverUtility.getLocalVariable(frag.getName().getIdentifier() + "-" + frag.hashCode());
		} else {
			locVar = jdtResolverUtility.getLocalVariable(binding);
		}
		utilNamedElement.setNameOfElement(frag.getName(), locVar);
		varSt.modifiers().forEach(obj -> locVar.getAnnotationsAndModifiers()
				.add(annotationInstanceConverter.convert((IExtendedModifier) obj)));
		locVar.setTypeReference(toTypeReferenceConverter.convert(varSt.getType()));
		utilToArrayDimensionsAndSetConverter.convert(varSt.getType(), locVar);
		frag.extraDimensions()
				.forEach(obj -> utilToArrayDimensionAfterAndSetConverter.convert((Dimension) obj, locVar));
		if (frag.getInitializer() != null) {
			locVar.setInitialValue(expressionConverterUtility.convert(frag.getInitializer()));
		}
		for (int index = 1; index < varSt.fragments().size(); index++) {
			locVar.getAdditionalLocalVariables().add(toAdditionalLocalVariableConverter
					.convert((VariableDeclarationFragment) varSt.fragments().get(index)));
		}
		result.setVariable(locVar);
		layoutInformationConverter.convertToMinimalLayoutInformation(result, varSt);
		return result;
	}

}
