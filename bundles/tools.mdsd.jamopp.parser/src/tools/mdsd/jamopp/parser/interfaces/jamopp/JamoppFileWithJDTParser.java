package tools.mdsd.jamopp.parser.interfaces.jamopp;

import org.eclipse.jdt.core.dom.ASTNode;

public interface JamoppFileWithJDTParser {

	ASTNode parseFileWithJDT(String fileContent, String fileName);

}