package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;

import com.google.inject.Inject;

class ToReceiverParameterConverter {

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

	ReceiverParameter convertToReceiverParameter(MethodDeclaration methodDecl) {
		ReceiverParameter result = parametersFactory.createReceiverParameter();
		result.setName("");
		result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(methodDecl.getReceiverType()));
		if (methodDecl.getReceiverQualifier() != null) {
			result.setOuterTypeReference(
					toClassifierReferenceConverter.convertToClassifierReference(methodDecl.getReceiverQualifier()));
		}
		result.setThisReference(literalsFactory.createThis());
		return result;
	}

}
