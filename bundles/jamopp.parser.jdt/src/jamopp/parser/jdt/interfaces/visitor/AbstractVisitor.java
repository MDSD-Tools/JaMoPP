package jamopp.parser.jdt.interfaces.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.containers.JavaRoot;

public abstract class AbstractVisitor extends ASTVisitor {

	public abstract JavaRoot getConvertedElement();

	public abstract void setSource(String src);

	@Override
	public abstract boolean visit(CompilationUnit node);

}