package jamopp.parser.jdt.converter.implementation.converter;

import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.Modifier;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.StatementsFactory;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilTypeInstructionSeparation;

public class ToBlockConverter
		implements Converter<org.eclipse.jdt.core.dom.Initializer, org.emftext.language.java.statements.Block> {

	private final StatementsFactory statementsFactory;
	private final IUtilTypeInstructionSeparation toInstructionSeparation;
	private final Converter<Modifier, org.emftext.language.java.modifiers.Modifier> toModifierConverter;

	@Inject
	ToBlockConverter(Converter<Modifier, org.emftext.language.java.modifiers.Modifier> toModifierConverter,
			IUtilTypeInstructionSeparation toInstructionSeparation, StatementsFactory statementsFactory) {
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
