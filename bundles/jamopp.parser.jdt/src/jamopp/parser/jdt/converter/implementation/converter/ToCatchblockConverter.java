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

import jamopp.parser.jdt.converter.implementation.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.implementation.helper.UtilLayout;
import jamopp.parser.jdt.converter.implementation.helper.UtilNamedElement;
import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;

public class ToCatchblockConverter implements ToConverter<CatchClause, CatchBlock> {

	private final StatementsFactory statementsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final UtilNamedElement utilNamedElement;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private final ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter;
	private final ToConverter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter;

	@Inject
	ToCatchblockConverter(UtilNamedElement utilNamedElement, ToConverter<Type, TypeReference> toTypeReferenceConverter,
			StatementsFactory statementsFactory, UtilLayout layoutInformationConverter,
			UtilJdtResolver jdtResolverUtility,
			ToConverter<IExtendedModifier, AnnotationInstanceOrModifier> annotationInstanceConverter,
			ToConverter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter) {
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
