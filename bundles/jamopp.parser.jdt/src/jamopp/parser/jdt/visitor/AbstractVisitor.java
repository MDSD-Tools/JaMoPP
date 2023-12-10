package jamopp.parser.jdt.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.containers.JavaRoot;

public abstract class AbstractVisitor extends ASTVisitor {

	public abstract boolean visit(CompilationUnit node);

	public abstract void setSource(String src);

	public abstract JavaRoot getConvertedElement();

}