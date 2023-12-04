package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToReceiverParameterConverter implements ToConverter<MethodDeclaration, ReceiverParameter> {

	private final LiteralsFactory literalsFactory;
	private final ParametersFactory parametersFactory;
	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToClassifierReferenceConverter toClassifierReferenceConverter;

	@Inject
	ToReceiverParameterConverter(ToTypeReferenceConverter toTypeReferenceConverter,
			ToClassifierReferenceConverter toClassifierReferenceConverter, ParametersFactory parametersFactory,
			LiteralsFactory literalsFactory) {
		this.literalsFactory = literalsFactory;
		this.parametersFactory = parametersFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toClassifierReferenceConverter = toClassifierReferenceConverter;
	}

	@Override
	public ReceiverParameter convert(MethodDeclaration methodDecl) {
		ReceiverParameter result = parametersFactory.createReceiverParameter();
		result.setName("");
		result.setTypeReference(toTypeReferenceConverter.convert(methodDecl.getReceiverType()));
		if (methodDecl.getReceiverQualifier() != null) {
			result.setOuterTypeReference(
					toClassifierReferenceConverter.convert(methodDecl.getReceiverQualifier()));
		}
		result.setThisReference(literalsFactory.createThis());
		return result;
	}

}
