package tools.mdsd.jamopp.parser.interfaces.jamopp;

import org.eclipse.jdt.core.dom.ASTParser;

public interface JamoppJavaParserFactory {

	ASTParser getJavaParser(String version);

}