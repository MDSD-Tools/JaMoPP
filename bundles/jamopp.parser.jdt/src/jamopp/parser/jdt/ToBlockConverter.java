package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.StatementsFactory;

class ToBlockConverter {

	private final UtilTypeInstructionSeparation toInstructionSeparation;
	private final ToModifierConverter toModifierConverter;

	ToBlockConverter(ToModifierConverter toModifierConverter, UtilTypeInstructionSeparation toInstructionSeparation) {
		this.toInstructionSeparation = toInstructionSeparation;
		this.toModifierConverter = toModifierConverter;
	}

	@SuppressWarnings("unchecked")
	Block convertToBlock(Initializer init) {
		Block result = StatementsFactory.eINSTANCE.createBlock();
		result.setName("");
		toInstructionSeparation.addInitializer(init.getBody(), result);
		init.modifiers()
				.forEach(obj -> result.getModifiers().add(toModifierConverter.convertToModifier((Modifier) obj)));
		return result;
	}

}
