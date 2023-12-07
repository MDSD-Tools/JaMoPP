package jamopp.parser.jdt.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.containers.JavaRoot;

public abstract class MyAbstractVisitor extends ASTVisitor {

	public abstract boolean visit(CompilationUnit node);

	public abstract JavaRoot getConvertedElement();

	public abstract void setSource(String src);

}