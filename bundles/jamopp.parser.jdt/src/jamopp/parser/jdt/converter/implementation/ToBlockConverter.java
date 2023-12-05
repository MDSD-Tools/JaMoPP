package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.IUtilTypeInstructionSeparation;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToBlockConverter implements ToConverter<org.eclipse.jdt.core.dom.Initializer, org.emftext.language.java.statements.Block> {

	private final StatementsFactory statementsFactory;
	private final IUtilTypeInstructionSeparation toInstructionSeparation;
	private final ToModifierConverter toModifierConverter;

	@Inject
	ToBlockConverter(ToModifierConverter toModifierConverter, IUtilTypeInstructionSeparation toInstructionSeparation,
			StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.toInstructionSeparation = toInstructionSeparation;
		this.toModifierConverter = toModifierConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Block convert(Initializer init) {
		Block result = statementsFactory.createBlock();
		result.setName("");
		toInstructionSeparation.addInitializer(init.getBody(), result);
		init.modifiers().forEach(obj -> result.getModifiers().add(toModifierConverter.convert((Modifier) obj)));
		return result;
	}

}
