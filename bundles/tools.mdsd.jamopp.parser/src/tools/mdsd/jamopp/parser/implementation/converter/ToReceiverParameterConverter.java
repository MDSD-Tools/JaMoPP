package tools.mdsd.jamopp.parser.implementation.converter;

import com.google.inject.Inject;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;

import tools.mdsd.jamopp.model.java.literals.LiteralsFactory;
import tools.mdsd.jamopp.model.java.parameters.ParametersFactory;
import tools.mdsd.jamopp.model.java.parameters.ReceiverParameter;
import tools.mdsd.jamopp.model.java.types.ClassifierReference;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;

public class ToReceiverParameterConverter implements Converter<MethodDeclaration, ReceiverParameter> {

	private final LiteralsFactory literalsFactory;
	private final ParametersFactory parametersFactory;
	private final Converter<Type, TypeReference> toTypeReferenceConverter;
	private final Converter<SimpleName, ClassifierReference> toClassifierReferenceConverter;

	@Inject
	public ToReceiverParameterConverter(final Converter<Type, TypeReference> toTypeReferenceConverter,
			final Converter<SimpleName, ClassifierReference> toClassifierReferenceConverter,
			final ParametersFactory parametersFactory, final LiteralsFactory literalsFactory) {
		this.literalsFactory = literalsFactory;
		this.parametersFactory = parametersFactory;
		this.toTypeReferenceConverter = toTypeReferenceConverter;
		this.toClassifierReferenceConverter = toClassifierReferenceConverter;
	}

	@Override
	public ReceiverParameter convert(final MethodDeclaration methodDecl) {
		final ReceiverParameter result = parametersFactory.createReceiverParameter();
		result.setName("");
		result.setTypeReference(toTypeReferenceConverter.convert(methodDecl.getReceiverType()));
		if (methodDecl.getReceiverQualifier() != null) {
			result.setOuterTypeReference(toClassifierReferenceConverter.convert(methodDecl.getReceiverQualifier()));
		}
		result.setThisReference(literalsFactory.createThis());
		return result;
	}

}
