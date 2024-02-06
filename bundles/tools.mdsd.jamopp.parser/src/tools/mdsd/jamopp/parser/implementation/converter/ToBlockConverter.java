package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Modifier;

import tools.mdsd.jamopp.model.java.statements.Block;
import tools.mdsd.jamopp.model.java.statements.StatementsFactory;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilTypeInstructionSeparation;

public class ToBlockConverter implements Converter<Initializer, Block> {

	private final StatementsFactory statementsFactory;
	private final UtilTypeInstructionSeparation toInstructionSeparation;
	private final Converter<Modifier, tools.mdsd.jamopp.model.java.modifiers.Modifier> toModifierConverter;

	@Inject
	public ToBlockConverter(
			final Converter<Modifier, tools.mdsd.jamopp.model.java.modifiers.Modifier> toModifierConverter,
			final UtilTypeInstructionSeparation toInstructionSeparation, final StatementsFactory statementsFactory) {
		this.statementsFactory = statementsFactory;
		this.toInstructionSeparation = toInstructionSeparation;
		this.toModifierConverter = toModifierConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Block convert(final Initializer init) {
		final Block result = statementsFactory.createBlock();
		result.setName("");
		toInstructionSeparation.addInitializer(init.getBody(), result);
		init.modifiers().forEach(obj -> result.getModifiers().add(toModifierConverter.convert((Modifier) obj)));
		return result;
	}

}
