package tools.mdsd.jamopp.parser.jdt.interfaces.jamopp;

import org.eclipse.jdt.core.dom.ASTParser;

public interface JamoppJavaParserFactory {

	ASTParser getJavaParser(String version);

}