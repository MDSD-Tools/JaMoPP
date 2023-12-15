package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;

public class ToReceiverParameterConverter implements Converter<MethodDeclaration, ReceiverParameter> {

	private final LiteralsFactory literalsFactory;
	private final ParametersFactory parametersFactory;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<SimpleName, ClassifierReference> toClassifierReferenceConverter;

	@Inject
	ToReceiverParameterConverter(Converter<Type, TypeReference> toTypeReferenceConverter,
			Converter<SimpleName, ClassifierReference> toClassifierReferenceConverter,
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
