package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.commons.NamedElement;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;

public class ReferenceableElementResolver {

	private final Set<ITypeBinding> typeBindings;
	private final Set<IMethodBinding> methodBindings;
	private final Set<IVariableBinding> variableBindings;
	private final AnnotationResolver annotationResolver;
	private final EnumerationResolver enumerationResolver;
	private final InterfaceResolver interfaceResolver;
	private final ClassResolver classResolver;
	private final TypeParameterResolver typeParameterResolver;
	private final ClassMethodResolver classMethodResolver;
	private final FieldResolver fieldResolver;
	private final EnumConstantResolver enumConstantResolver;
	private final AdditionalFieldResolver additionalFieldResolver;
	private final CatchParameterResolver catchParameterResolver;
	private final OrdinaryParameterResolver ordinaryParameterResolver;
	private final AdditionalLocalVariableResolver additionalLocalVariableResolver;
	private final VariableLengthParameterResolver variableLengthParameterResolver;
	private final LocalVariableResolver localVariableResolver;
	private final InterfaceMethodResolver interfaceMethodResolver;
	private final ClassifierResolver classifierResolver;
	private final MethodResolver methodResolver;

	private final ToFieldNameConverter toFieldNameConverter;
	private final ToParameterNameConverter toParameterNameConverter;

	@Inject
	public ReferenceableElementResolver(final VariableLengthParameterResolver variableLengthParameterResolver,
			final Set<IVariableBinding> variableBindings, final TypeParameterResolver typeParameterResolver,
			final Set<ITypeBinding> typeBindings, final OrdinaryParameterResolver ordinaryParameterResolver,
			final Set<IMethodBinding> methodBindings, final LocalVariableResolver localVariableResolver,
			final InterfaceResolver interfaceResolver, final InterfaceMethodResolver interfaceMethodResolver,
			final FieldResolver fieldResolver, final EnumerationResolver enumerationResolver,
			final EnumConstantResolver enumConstantResolver, final ClassResolver classResolver,
			final ClassMethodResolver classMethodResolver, final CatchParameterResolver catchParameterResolver,
			final AnnotationResolver annotationResolver,
			final AdditionalLocalVariableResolver additionalLocalVariableResolver,
			final AdditionalFieldResolver additionalFieldResolver,
			final ToParameterNameConverter toParameterNameConverter, final ToFieldNameConverter toFieldNameConverter,
			final MethodResolver methodResolver, final ClassifierResolver classifierResolver) {
		this.typeBindings = typeBindings;
		this.methodBindings = methodBindings;
		this.variableBindings = variableBindings;
		this.annotationResolver = annotationResolver;
		this.enumerationResolver = enumerationResolver;
		this.interfaceResolver = interfaceResolver;
		this.classResolver = classResolver;
		this.typeParameterResolver = typeParameterResolver;
		this.classMethodResolver = classMethodResolver;
		this.fieldResolver = fieldResolver;
		this.enumConstantResolver = enumConstantResolver;
		this.additionalFieldResolver = additionalFieldResolver;
		this.catchParameterResolver = catchParameterResolver;
		this.ordinaryParameterResolver = ordinaryParameterResolver;
		this.additionalLocalVariableResolver = additionalLocalVariableResolver;
		this.variableLengthParameterResolver = variableLengthParameterResolver;
		this.localVariableResolver = localVariableResolver;
		this.interfaceMethodResolver = interfaceMethodResolver;
		this.classifierResolver = classifierResolver;
		this.methodResolver = methodResolver;
		this.toFieldNameConverter = toFieldNameConverter;
		this.toParameterNameConverter = toParameterNameConverter;
	}

	public ReferenceableElement getByBinding(final IVariableBinding binding) {
		ReferenceableElement referenceableElement;
		if (binding.isEnumConstant()) {
			referenceableElement = handleIsEnumConstant(binding);
		} else if (binding.isField()) {
			referenceableElement = handleIsField(binding);
		} else if (binding.isParameter()) {
			referenceableElement = handleIsParameter(binding);
		} else {
			final String paramName = toParameterNameConverter.convertToParameterName(binding, false);
			if (catchParameterResolver.getBindings().containsKey(paramName)) {
				referenceableElement = catchParameterResolver.getBindings().get(paramName);
			} else if (localVariableResolver.getBindings().containsKey(paramName)) {
				referenceableElement = localVariableResolver.getBindings().get(paramName);
			} else if (additionalLocalVariableResolver.getBindings().containsKey(paramName)) {
				referenceableElement = additionalLocalVariableResolver.getBindings().get(paramName);
			} else if (ordinaryParameterResolver.getBindings().containsKey(paramName)) {
				referenceableElement = ordinaryParameterResolver.getBindings().get(paramName);
			} else {
				referenceableElement = localVariableResolver.getByBinding(binding);
			}
		}
		return referenceableElement;
	}

	private ReferenceableElement handleIsEnumConstant(final IVariableBinding binding) {
		return enumConstantResolver.getByBinding(binding);
	}

	private ReferenceableElement handleIsParameter(final IVariableBinding binding) {
		final String paramName = toParameterNameConverter.convertToParameterName(binding, false);
		ReferenceableElement referenceableElement;
		if (ordinaryParameterResolver.getBindings().containsKey(paramName)) {
			referenceableElement = ordinaryParameterResolver.getBindings().get(paramName);
		} else if (variableLengthParameterResolver.getBindings().containsKey(paramName)) {
			referenceableElement = variableLengthParameterResolver.getBindings().get(paramName);
		} else {
			referenceableElement = ordinaryParameterResolver.getByBinding(binding);
		}
		return referenceableElement;
	}

	private ReferenceableElement handleIsField(final IVariableBinding binding) {
		ReferenceableElement referenceableElement;
		final String fieldName = toFieldNameConverter.convertToFieldName(binding);
		if (fieldResolver.getBindings().containsKey(fieldName)) {
			referenceableElement = fieldResolver.getBindings().get(fieldName);
		} else if (additionalFieldResolver.getBindings().containsKey(fieldName)) {
			referenceableElement = additionalFieldResolver.getBindings().get(fieldName);
		} else {
			referenceableElement = fieldResolver.getByBinding(binding);
		}
		return referenceableElement;
	}

	public ReferenceableElement getByName(final String name) {
		final List<Stream<? extends ReferenceableElement>> streams = new ArrayList<>();
		streams.add(variableBindings.stream().filter(bindingsFilter(name)).map(this::getByBinding));
		streams.add(methodBindings.stream().filter(methodFilter(name)).map(methodResolver::getMethod));
		streams.add(typeBindings.stream().filter(typeFilter(name)).map(classifierResolver::getClassifier));
		streams.add(catchParameterResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(localVariableResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(variableLengthParameterResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(ordinaryParameterResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(additionalLocalVariableResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(enumConstantResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(fieldResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(additionalFieldResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(classMethodResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(interfaceMethodResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(typeParameterResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(enumerationResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(annotationResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(classResolver.getBindings().values().stream().filter(filter(name)));
		streams.add(interfaceResolver.getBindings().values().stream().filter(filter(name)));

		ReferenceableElement referenceableElement;
		final Optional<? extends ReferenceableElement> optionalRefElement = streams.stream().flatMap(s -> s)
				.findFirst();
		if (optionalRefElement.isPresent()) {
			referenceableElement = optionalRefElement.get();
		} else {
			referenceableElement = classResolver.getByName(name);
		}
		return referenceableElement;
	}

	private Predicate<? super IVariableBinding> bindingsFilter(final String name) {
		return param -> param != null && name.equals(param.getName());
	}

	private Predicate<? super IMethodBinding> methodFilter(final String name) {
		return param -> !param.isConstructor() && name.equals(param.getName());
	}

	private Predicate<? super ITypeBinding> typeFilter(final String name) {
		return param -> param != null && name.equals(param.getName());
	}

	private Predicate<? super NamedElement> filter(final String name) {
		return param -> param != null && name.equals(param.getName());
	}

}
