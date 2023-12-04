package jamopp.parser.jdt.converter.interfaces;

import org.eclipse.jdt.core.dom.Block;

public interface BlockToBlockConverter extends ToConverter<Block, org.emftext.language.java.statements.Block> {

	public org.emftext.language.java.statements.Block convert(Block block);

}
