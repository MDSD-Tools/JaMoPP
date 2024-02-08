package tools.mdsd.jamopp.parser.implementation.converter.statement;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.converter.StatementHandler;

public class TypeDeclarationStatementHandler implements StatementHandler {

	private final Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility;

	@Inject
	public TypeDeclarationStatementHandler(
			final Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility) {
		this.classifierConverterUtility = classifierConverterUtility;
	}

	@Override
	public tools.mdsd.jamopp.model.java.statements.Statement handle(final Statement statement) {
		final TypeDeclarationStatement declSt = (TypeDeclarationStatement) statement;
		return classifierConverterUtility.convert(declSt.getDeclaration());
	}

}
