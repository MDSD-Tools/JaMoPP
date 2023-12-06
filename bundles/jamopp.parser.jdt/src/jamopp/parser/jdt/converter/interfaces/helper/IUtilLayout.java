package jamopp.parser.jdt.converter.interfaces.helper;

import org.eclipse.jdt.core.dom.ASTNode;
import org.emftext.language.java.commons.Commentable;
import org.emftext.language.java.containers.JavaRoot;

public interface IUtilLayout {

	void convertJavaRootLayoutInformation(JavaRoot root, ASTNode rootSource, String sourceCode);

	void convertToMinimalLayoutInformation(Commentable target, ASTNode source);

}