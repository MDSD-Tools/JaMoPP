package jamopp.parser.jdt.interfaces.jamopp;

import java.util.Map;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public interface JamoppCompilationUnitsFactory {

	Map<String, CompilationUnit> getCompilationUnits(ASTParser parser, String[] classpathEntries, String[] sources,
			String[] encodings);

}