package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.emftext.language.java.references.Reference;

public interface ReferenceConverter<From extends ASTNode> {

	public Reference convert(From expr);

}
