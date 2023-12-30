package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import tools.mdsd.jamopp.model.java.modifiers.AnnotationInstanceOrModifier;
import tools.mdsd.jamopp.model.java.statements.CatchBlock;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.model.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToCatchblockConverter implements Converter<CatchClause, CatchBlock> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter;
	private final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter;

	@Inject
	ToCatchblockConverter(UtilNamedElement utilNamedElement, Converter<Type, TypeReference> toTypeReferenceConverter,
			StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			JdtResolver jdtResolverUtility,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter,
			Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter) {
		this.statementsFactory = statementsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.annotationInstanceConverter = annotationInstanceConverter;
		this.blockToBlockConverter = blockToBlockConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CatchBlock convert(CatchClause block) {
		tools.mdsd.jamopp.model.java.statements.CatchBlock result = statementsFactory.createCatchBlock();
		SingleVariableDeclaration decl = block.getException();
		tools.mdsd.jamopp.model.java.parameters.CatchParameter param;
		IVariableBinding binding = decl.resolveBinding();
		if (binding == null) {
			param = jdtResolverUtility.getCatchParameter(decl.getName().getIdentifier() + "-" + block.hashCode());
		} else {
			param = jdtResolverUtility.getCatchParameter(binding);
		}
		decl.modifiers().forEach(obj -> param.getAnnotationsAndModifiers()
				.add(annotationInstanceConverter.convert((IExtendedModifier) obj)));
		if (decl.getType().isUnionType()) {
			UnionType un = (UnionType) decl.getType();
			param.setTypeReference(toTypeReferenceConverter.convert((Type) un.types().get(0)));
			for (int index = 1; index < un.types().size(); index++) {
				param.getTypeReferences().add(toTypeReferenceConverter.convert((Type) un.types().get(index)));
			}
		} else {
			param.setTypeReference(toTypeReferenceConverter.convert(decl.getType()));
		}
		utilNamedElement.setNameOfElement(decl.getName(), param);
		result.setParameter(param);
		result.setBlock(blockToBlockConverter.convert(block.getBody()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, block);
		return result;
	}

}
