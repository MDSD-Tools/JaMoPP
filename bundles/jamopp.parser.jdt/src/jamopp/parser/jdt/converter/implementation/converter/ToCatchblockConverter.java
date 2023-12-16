package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.emftext.language.java.modifiers.AnnotationInstanceOrModifier;
import org.emftext.language.java.statements.CatchBlock;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public class ToCatchblockConverter implements Converter<CatchClause, CatchBlock> {

	private final StatementsFactory statementsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilJdtResolver jdtResolverUtility;
	private final IUtilNamedElement utilNamedElement;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter;
	private final Converter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter;

	@Inject
	ToCatchblockConverter(IUtilNamedElement utilNamedElement, Converter<Type, TypeReference> toTypeReferenceConverter,
			StatementsFactory statementsFactory, IUtilLayout layoutInformationConverter,
			IUtilJdtResolver jdtResolverUtility,
			Converter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter,
			Converter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter) {
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
		org.emftext.language.java.statements.CatchBlock result = statementsFactory.createCatchBlock();
		SingleVariableDeclaration decl = block.getException();
		org.emftext.language.java.parameters.CatchParameter param;
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
