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
	private final Map<String, Pair> mapping;

	@SuppressWarnings("deprecation")
	@Inject
	public JamoppJavaParserFactoryImpl(Logger logger) {
		this.logger = logger;
		mapping = new HashMap<>();

		mapping.put("1.1", new Pair(JavaCore.VERSION_1_1, AST.JLS2));
		mapping.put("1.2", new Pair(JavaCore.VERSION_1_2, AST.JLS2));
		mapping.put("1.3", new Pair(JavaCore.VERSION_1_3, AST.JLS3));
		mapping.put("1.4", new Pair(JavaCore.VERSION_1_4, AST.JLS4));
		mapping.put("1.5", new Pair(JavaCore.VERSION_1_5, AST.JLS8));
		mapping.put("1.6", new Pair(JavaCore.VERSION_1_6, AST.JLS8));
		mapping.put("1.7", new Pair(JavaCore.VERSION_1_7, AST.JLS8));
		mapping.put("1.8", new Pair(JavaCore.VERSION_1_8, AST.JLS8));
		mapping.put("9", new Pair(JavaCore.VERSION_9, AST.JLS9));
		mapping.put("10", new Pair(JavaCore.VERSION_10, AST.JLS10));
		mapping.put("11", new Pair(JavaCore.VERSION_11, AST.JLS11));
		mapping.put("12", new Pair(JavaCore.VERSION_12, AST.JLS12));
		mapping.put("13", new Pair(JavaCore.VERSION_13, AST.JLS13));
		mapping.put("14", new Pair(JavaCore.VERSION_14, AST.JLS14));
		mapping.put("15", new Pair(JavaCore.VERSION_15, AST.JLS15));
		mapping.put("16", new Pair(JavaCore.VERSION_16, AST.JLS16));
		mapping.put("17", new Pair(JavaCore.VERSION_17, AST.JLS17));
	}

	@Override
	@SuppressWarnings("deprecation")
	public ASTParser getJavaParser(String version) {
		String javaCoreVersion;
		int astParserLevel;
		final String stripedVersion = String.valueOf(version).strip();

		Pair pair = mapping.get(stripedVersion);
		if (pair != null) {
			javaCoreVersion = pair.getJavaCoreVersion();
			astParserLevel = pair.getAstParserLevel();
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

	public static class Pair {
		private final String javaCoreVersion;
		private final int astParserLevel;

		public Pair(String javaCoreVersion, int astParserLevel) {
			this.javaCoreVersion = javaCoreVersion;
			this.astParserLevel = astParserLevel;
		}

		public int getAstParserLevel() {
			return astParserLevel;
		}

		public String getJavaCoreVersion() {
			return javaCoreVersion;
		}
	}

}
