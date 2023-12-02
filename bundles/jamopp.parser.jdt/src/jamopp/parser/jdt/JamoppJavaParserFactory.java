package jamopp.parser.jdt;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import com.google.inject.Inject;

class JamoppJavaParserFactory {

	private final Logger logger;

	@Inject
	JamoppJavaParserFactory(Logger logger) {
		this.logger = logger;
	}

	@SuppressWarnings("deprecation")
	ASTParser getJavaParser(String version) {
		final String javaCoreVersion;
		int astParserLevel;
		final String stripedVersion = String.valueOf(version).strip();
		switch (stripedVersion) {
		case "1.1":
			javaCoreVersion = JavaCore.VERSION_1_1;
			astParserLevel = AST.JLS2;
			break;
		case "1.2":
			javaCoreVersion = JavaCore.VERSION_1_2;
			astParserLevel = AST.JLS2;
			break;
		case "1.3":
			javaCoreVersion = JavaCore.VERSION_1_3;
			astParserLevel = AST.JLS3;
			break;
		case "1.4":
			javaCoreVersion = JavaCore.VERSION_1_4;
			astParserLevel = AST.JLS4;
			break;
		case "1.5":
			javaCoreVersion = JavaCore.VERSION_1_5;
			astParserLevel = AST.JLS8;
			break;
		case "1.6":
			javaCoreVersion = JavaCore.VERSION_1_6;
			astParserLevel = AST.JLS8;
			break;
		case "1.7":
			javaCoreVersion = JavaCore.VERSION_1_7;
			astParserLevel = AST.JLS8;
			break;
		case "1.8":
			javaCoreVersion = JavaCore.VERSION_1_8;
			astParserLevel = AST.JLS8;
			break;
		case "9":
			javaCoreVersion = JavaCore.VERSION_9;
			astParserLevel = AST.JLS9;
			break;
		case "10":
			javaCoreVersion = JavaCore.VERSION_10;
			astParserLevel = AST.JLS10;
			break;
		case "11":
			javaCoreVersion = JavaCore.VERSION_11;
			astParserLevel = AST.JLS11;
			break;
		case "12":
			javaCoreVersion = JavaCore.VERSION_12;
			astParserLevel = AST.JLS12;
			break;
		case "13":
			javaCoreVersion = JavaCore.VERSION_13;
			astParserLevel = AST.JLS13;
			break;
		case "14":
			javaCoreVersion = JavaCore.VERSION_14;
			astParserLevel = AST.JLS14;
			break;
		case "15":
			javaCoreVersion = JavaCore.VERSION_15;
			astParserLevel = AST.JLS15;
			break;
		case "16":
			javaCoreVersion = JavaCore.VERSION_16;
			astParserLevel = AST.JLS16;
			break;
		case "17":
			javaCoreVersion = JavaCore.VERSION_17;
			astParserLevel = AST.JLS17;
			break;
		default:
			javaCoreVersion = JavaCore.latestSupportedJavaVersion();
			astParserLevel = AST.getJLSLatest();
			logger.warn(String.format("No version was found for %s, so the last supported version is used.",
					stripedVersion));
			break;
		}
		final ASTParser parser = ASTParser.newParser(astParserLevel);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		parser.setCompilerOptions(Map.of(JavaCore.COMPILER_SOURCE, javaCoreVersion, JavaCore.COMPILER_COMPLIANCE,
				javaCoreVersion, JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, javaCoreVersion));
		return parser;
	}

}
