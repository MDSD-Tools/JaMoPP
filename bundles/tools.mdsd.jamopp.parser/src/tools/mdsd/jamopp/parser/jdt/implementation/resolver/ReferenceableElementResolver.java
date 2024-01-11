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
	public ReferenceableElementResolver(VariableLengthParameterResolver variableLengthParameterResolver,
			Set<IVariableBinding> variableBindings, TypeParameterResolver typeParameterResolver,
			Set<ITypeBinding> typeBindings, OrdinaryParameterResolver ordinaryParameterResolver,
			Set<IMethodBinding> methodBindings, LocalVariableResolver localVariableResolver,
			InterfaceResolver interfaceResolver, InterfaceMethodResolver interfaceMethodResolver,
			FieldResolver fieldResolver, EnumerationResolver enumerationResolver,
			EnumConstantResolver enumConstantResolver, ClassResolver classResolver,
			ClassMethodResolver classMethodResolver, CatchParameterResolver catchParameterResolver,
			AnnotationResolver annotationResolver, AdditionalLocalVariableResolver additionalLocalVariableResolver,
			AdditionalFieldResolver additionalFieldResolver, ToParameterNameConverter toParameterNameConverter,
			ToFieldNameConverter toFieldNameConverter, MethodResolver methodResolver,
			ClassifierResolver classifierResolver) {
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

	public ReferenceableElement getByBinding(IVariableBinding binding) {
		if (binding.isEnumConstant()) {
			return handleIsEnumConstant(binding);
		} else if (binding.isField()) {
			return handleIsField(binding);
		} else if (binding.isParameter()) {
			return handleIsParameter(binding);
		}
		String paramName = toParameterNameConverter.convertToParameterName(binding, false);
		if (catchParameterResolver.getBindings().containsKey(paramName)) {
			return catchParameterResolver.getBindings().get(paramName);
		} else if (localVariableResolver.getBindings().containsKey(paramName)) {
			return localVariableResolver.getBindings().get(paramName);
		} else if (additionalLocalVariableResolver.getBindings().containsKey(paramName)) {
			return additionalLocalVariableResolver.getBindings().get(paramName);
		} else if (ordinaryParameterResolver.getBindings().containsKey(paramName)) {
			return ordinaryParameterResolver.getBindings().get(paramName);
		} else {
			return localVariableResolver.getByBinding(binding);
		}
	}

	private ReferenceableElement handleIsEnumConstant(IVariableBinding binding) {
		return enumConstantResolver.getByBinding(binding);
	}

	private ReferenceableElement handleIsParameter(IVariableBinding binding) {
		String paramName = toParameterNameConverter.convertToParameterName(binding, false);
		if (ordinaryParameterResolver.getBindings().containsKey(paramName)) {
			return ordinaryParameterResolver.getBindings().get(paramName);
		} else if (variableLengthParameterResolver.getBindings().containsKey(paramName)) {
			return variableLengthParameterResolver.getBindings().get(paramName);
		} else {
			return ordinaryParameterResolver.getByBinding(binding);
		}
	}

	private ReferenceableElement handleIsField(IVariableBinding binding) {
		String fieldName = toFieldNameConverter.convertToFieldName(binding);
		if (fieldResolver.getBindings().containsKey(fieldName)) {
			return fieldResolver.getBindings().get(fieldName);
		} else if (additionalFieldResolver.getBindings().containsKey(fieldName)) {
			return additionalFieldResolver.getBindings().get(fieldName);
		} else {
			return fieldResolver.getByBinding(binding);
		}
	}

	public ReferenceableElement getByName(String name) {
		List<Stream<? extends ReferenceableElement>> streams = new ArrayList<>();
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

		Optional<? extends ReferenceableElement> a = streams.stream().flatMap(s -> s).findFirst();
		if (a.isPresent()) {
			return a.get();
		}

		return classResolver.getByName(name);
	}

	private Predicate<? super IVariableBinding> bindingsFilter(String name) {
		return param -> param != null && name.equals(param.getName());
	}

	private Predicate<? super IMethodBinding> methodFilter(String name) {
		return param -> !param.isConstructor() && name.equals(param.getName());
	}

	private Predicate<? super ITypeBinding> typeFilter(String name) {
		return param -> param != null && name.equals(param.getName());
	}

	private Predicate<? super NamedElement> filter(String name) {
		return param -> param != null && name.equals(param.getName());
	}

}
