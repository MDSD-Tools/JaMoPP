package tools.mdsd.jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.ASTNode;
import org.emftext.language.java.commons.Commentable;
import org.emftext.language.java.containers.JavaRoot;

public interface UtilLayout {

	void convertJavaRootLayoutInformation(JavaRoot root, ASTNode rootSource, String sourceCode);

	void convertToMinimalLayoutInformation(Commentable target, ASTNode source);

}