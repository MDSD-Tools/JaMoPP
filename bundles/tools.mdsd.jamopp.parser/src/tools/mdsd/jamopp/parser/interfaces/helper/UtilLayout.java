package tools.mdsd.jamopp.parser.interfaces.helper;

import org.eclipse.jdt.core.dom.ASTNode;
import tools.mdsd.jamopp.model.java.commons.Commentable;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;

public interface UtilLayout {

	void convertJavaRootLayoutInformation(JavaRoot root, ASTNode rootSource, String sourceCode);

	void convertToMinimalLayoutInformation(Commentable target, ASTNode source);

}