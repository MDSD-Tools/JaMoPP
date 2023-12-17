package tools.mdsd.jamopp.parser.jdt.interfaces.jamopp;

import org.eclipse.jdt.core.dom.ASTNode;

public interface JamoppFileWithJDTParser {

	ASTNode parseFileWithJDT(String fileContent, String fileName);

}