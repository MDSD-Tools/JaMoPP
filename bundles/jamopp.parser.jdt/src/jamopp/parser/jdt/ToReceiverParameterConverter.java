package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;

import com.google.inject.Inject;

class ToReceiverParameterConverter {

	private final ToTypeReferenceConverter toTypeReferenceConverter;
	private final ToClassifierReferenceConverter toClassifierReferenceConverter;

	@Inject
	ToReceiverParameterConverter(ToTypeReferenceConverter toTypeReferenceConverter,
			ToClassifierReferenceConverter toClassifierReferenceConverter) {
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toClassifierReferenceConverter = toClassifierReferenceConverter;
	}

	ReceiverParameter convertToReceiverParameter(MethodDeclaration methodDecl) {
		ReceiverParameter result = ParametersFactory.eINSTANCE.createReceiverParameter();
		result.setName("");
		result.setTypeReference(toTypeReferenceConverter.convertToTypeReference(methodDecl.getReceiverType()));
		if (methodDecl.getReceiverQualifier() != null) {
			result.setOuterTypeReference(
					toClassifierReferenceConverter.convertToClassifierReference(methodDecl.getReceiverQualifier()));
		}
		result.setThisReference(LiteralsFactory.eINSTANCE.createThis());
		return result;
	}

}
