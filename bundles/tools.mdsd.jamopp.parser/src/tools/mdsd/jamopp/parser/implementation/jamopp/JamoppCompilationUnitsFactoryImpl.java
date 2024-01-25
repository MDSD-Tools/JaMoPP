package tools.mdsd.jamopp.parser.implementation.jamopp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppCompilationUnitsFactory;

public class JamoppCompilationUnitsFactoryImpl implements JamoppCompilationUnitsFactory {

	private final Logger logger;

	@Inject
	public JamoppCompilationUnitsFactoryImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public Map<String, CompilationUnit> getCompilationUnits(final ASTParser parser, final String[] classpathEntries,
			final String[] sources, final String[] encodings) {
		final Map<String, CompilationUnit> compilationUnits = new ConcurrentHashMap<>();
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
