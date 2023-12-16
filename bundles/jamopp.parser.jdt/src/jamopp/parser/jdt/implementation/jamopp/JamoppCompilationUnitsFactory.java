package jamopp.parser.jdt.implementation.jamopp;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import com.google.inject.Inject;

public class JamoppCompilationUnitsFactory {

	private final Logger logger;

	@Inject
	public
	JamoppCompilationUnitsFactory(Logger logger) {
		this.logger = logger;
	}

	public Map<String, CompilationUnit> getCompilationUnits(ASTParser parser, String[] classpathEntries, String[] sources,
			String[] encodings) {
		final Map<String, CompilationUnit> compilationUnits = new HashMap<>();
		try {
			parser.setEnvironment(classpathEntries, new String[0], new String[0], true);
			parser.createASTs(sources, encodings, new String[0], new FileASTRequestor() {
				@Override
				public void acceptAST(final String sourceFilePath, final CompilationUnit ast) {
					compilationUnits.put(sourceFilePath, ast);
				}
			}, new NullProgressMonitor());
		} catch (IllegalArgumentException | IllegalStateException e) {
			logger.error(String.valueOf(sources), e);
		}
		return compilationUnits;
	}

}
