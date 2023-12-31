package tools.mdsd.jamopp.parser.jdt.implementation.converter.statement;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class TypeDeclarationStatementHandler {

	private final Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility;

	@Inject
	public TypeDeclarationStatementHandler(
			Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility) {
		this.classifierConverterUtility = classifierConverterUtility;
	}

	public tools.mdsd.jamopp.model.java.statements.Statement handle(Statement statement) {
		TypeDeclarationStatement declSt = (TypeDeclarationStatement) statement;
		return classifierConverterUtility.convert(declSt.getDeclaration());
	}

}
