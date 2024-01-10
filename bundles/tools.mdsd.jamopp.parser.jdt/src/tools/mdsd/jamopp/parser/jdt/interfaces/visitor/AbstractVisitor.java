package tools.mdsd.jamopp.parser.jdt.interfaces.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import tools.mdsd.jamopp.model.java.containers.JavaRoot;

public abstract class AbstractVisitor extends ASTVisitor {

	@Override
	public abstract boolean visit(CompilationUnit node);

	public abstract void setSource(String src);

	public abstract String getSource();

	public abstract void setConvertedElement(JavaRoot root);

	public abstract JavaRoot getConvertedElement();

}