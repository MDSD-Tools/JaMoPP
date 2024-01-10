package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

import javax.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.StatementHandler;

public class TypeDeclarationStatementHandler implements StatementHandler {

	private final Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility;

	@Inject
	public TypeDeclarationStatementHandler(
			Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility) {
		this.classifierConverterUtility = classifierConverterUtility;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		TypeDeclarationStatement declSt = (TypeDeclarationStatement) statement;
		return classifierConverterUtility.convert(declSt.getDeclaration());
	}

}
