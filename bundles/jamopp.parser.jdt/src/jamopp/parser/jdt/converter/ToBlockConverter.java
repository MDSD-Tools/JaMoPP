package jamopp.parser.jdt.converter;

import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

public class ToBlockConverter {

	private final StatementsFactory statementsFactory;
	private final UtilTypeInstructionSeparation toInstructionSeparation;
	private final ToModifierConverter toModifierConverter;

	@Inject
	ToBlockConverter(ToModifierConverter toModifierConverter, UtilTypeInstructionSeparation toInstructionSeparation, StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.toInstructionSeparation = toInstructionSeparation;
		this.toModifierConverter = toModifierConverter;
	}

	@SuppressWarnings("unchecked")
	Block convertToBlock(Initializer init) {
		Block result = statementsFactory.createBlock();
		result.setName("");
		toInstructionSeparation.addInitializer(init.getBody(), result);
		init.modifiers()
				.forEach(obj -> result.getModifiers().add(toModifierConverter.convert((Modifier) obj)));
		return result;
	}

}
