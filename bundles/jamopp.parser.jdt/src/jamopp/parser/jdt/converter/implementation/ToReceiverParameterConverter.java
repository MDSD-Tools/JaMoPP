package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class ToReceiverParameterConverter implements ToConverter<MethodDeclaration, ReceiverParameter> {

	private final LiteralsFactory literalsFactory;
	private final ParametersFactory parametersFactory;
	private final ToConverter<Type, TypeReference> toTypeReferenceConverter;
	private final ToConverter<SimpleName, ClassifierReference> toClassifierReferenceConverter;

	@Inject
	ToReceiverParameterConverter(ToConverter<Type, TypeReference> toTypeReferenceConverter,
			ToConverter<SimpleName, ClassifierReference> toClassifierReferenceConverter,
			ParametersFactory parametersFactory, LiteralsFactory literalsFactory) {
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
			result.setOuterTypeReference(toClassifierReferenceConverter.convert(methodDecl.getReceiverQualifier()));
		}
		result.setThisReference(literalsFactory.createThis());
		return result;
	}

}
