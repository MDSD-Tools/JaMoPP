package tools.mdsd.jamopp.parser.jdt.implementation.jamopp;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import tools.mdsd.jamopp.parser.jdt.interfaces.jamopp.JamoppJavaParserFactory;

public class JamoppJavaParserFactoryImpl implements JamoppJavaParserFactory {

	private final Logger logger;
	private final Map<String, Mapping> mappings;

	@SuppressWarnings("deprecation")
	@Inject
	public JamoppJavaParserFactoryImpl(Logger logger) {
		this.logger = logger;
		mappings = new HashMap<>();

		mappings.put("1.1", new Mapping(JavaCore.VERSION_1_1, AST.JLS2));
		mappings.put("1.2", new Mapping(JavaCore.VERSION_1_2, AST.JLS2));
		mappings.put("1.3", new Mapping(JavaCore.VERSION_1_3, AST.JLS3));
		mappings.put("1.4", new Mapping(JavaCore.VERSION_1_4, AST.JLS4));
		mappings.put("1.5", new Mapping(JavaCore.VERSION_1_5, AST.JLS8));
		mappings.put("1.6", new Mapping(JavaCore.VERSION_1_6, AST.JLS8));
		mappings.put("1.7", new Mapping(JavaCore.VERSION_1_7, AST.JLS8));
		mappings.put("1.8", new Mapping(JavaCore.VERSION_1_8, AST.JLS8));
		mappings.put("9", new Mapping(JavaCore.VERSION_9, AST.JLS9));
		mappings.put("10", new Mapping(JavaCore.VERSION_10, AST.JLS10));
		mappings.put("11", new Mapping(JavaCore.VERSION_11, AST.JLS11));
		mappings.put("12", new Mapping(JavaCore.VERSION_12, AST.JLS12));
		mappings.put("13", new Mapping(JavaCore.VERSION_13, AST.JLS13));
		mappings.put("14", new Mapping(JavaCore.VERSION_14, AST.JLS14));
		mappings.put("15", new Mapping(JavaCore.VERSION_15, AST.JLS15));
		mappings.put("16", new Mapping(JavaCore.VERSION_16, AST.JLS16));
		mappings.put("17", new Mapping(JavaCore.VERSION_17, AST.JLS17));
	}

	@Override
	public ASTParser getJavaParser(String version) {
		String javaCoreVersion;
		int astParserLevel;
		final String stripedVersion = String.valueOf(version).strip();

		Mapping mapping = mappings.get(stripedVersion);
		if (mapping != null) {
			javaCoreVersion = mapping.getJavaCoreVersion();
			astParserLevel = mapping.getAstParserLevel();
		} else {
			javaCoreVersion = JavaCore.latestSupportedJavaVersion();
			astParserLevel = AST.getJLSLatest();
			logger.warn(String.format("No version was found for %s, so the last supported version is used.",
					stripedVersion));
		}

		final ASTParser parser = ASTParser.newParser(astParserLevel);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setCompilerOptions(Map.of(JavaCore.COMPILER_SOURCE, javaCoreVersion, JavaCore.COMPILER_COMPLIANCE,
				javaCoreVersion, JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, javaCoreVersion));
		return parser;
	}

	private static class Mapping {
		private final String javaCoreVersion;
		private final int astParserLevel;

		private Mapping(String javaCoreVersion, int astParserLevel) {
			this.javaCoreVersion = javaCoreVersion;
			this.astParserLevel = astParserLevel;
		}

		private int getAstParserLevel() {
			return astParserLevel;
		}

		private String getJavaCoreVersion() {
			return javaCoreVersion;
		}
	}

}
