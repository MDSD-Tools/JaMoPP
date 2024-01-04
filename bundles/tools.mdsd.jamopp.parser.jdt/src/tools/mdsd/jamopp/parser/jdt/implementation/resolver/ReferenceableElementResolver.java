package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import tools.mdsd.jamopp.model.java.references.ReferenceableElement;

public class ReferenceableElementResolver {

	private final HashSet<ITypeBinding> typeBindings;
	private final HashSet<IMethodBinding> methodBindings;
	private final HashSet<IVariableBinding> variableBindings;
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
			HashSet<IVariableBinding> variableBindings, TypeParameterResolver typeParameterResolver,
			HashSet<ITypeBinding> typeBindings, OrdinaryParameterResolver ordinaryParameterResolver,
			HashSet<IMethodBinding> methodBindings, LocalVariableResolver localVariableResolver,
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
			return enumConstantResolver.getByBinding(binding);
		}
		if (binding.isField()) {
			String fieldName = toFieldNameConverter.convertToFieldName(binding);
			if (fieldResolver.getBindings().containsKey(fieldName)) {
				return fieldResolver.getBindings().get(fieldName);
			}
			if (additionalFieldResolver.getBindings().containsKey(fieldName)) {
				return additionalFieldResolver.getBindings().get(fieldName);
			}
			return fieldResolver.getByBinding(binding);
		}
		if (binding.isParameter()) {
			String paramName = toParameterNameConverter.convertToParameterName(binding, false);
			if (ordinaryParameterResolver.getBindings().containsKey(paramName)) {
				return ordinaryParameterResolver.getBindings().get(paramName);
			}
			if (variableLengthParameterResolver.getBindings().containsKey(paramName)) {
				return variableLengthParameterResolver.getBindings().get(paramName);
			}
			return ordinaryParameterResolver.getByBinding(binding);
		}
		String paramName = toParameterNameConverter.convertToParameterName(binding, false);
		if (catchParameterResolver.getBindings().containsKey(paramName)) {
			return catchParameterResolver.getBindings().get(paramName);
		}
		if (localVariableResolver.getBindings().containsKey(paramName)) {
			return localVariableResolver.getBindings().get(paramName);
		}
		if (additionalLocalVariableResolver.getBindings().containsKey(paramName)) {
			return additionalLocalVariableResolver.getBindings().get(paramName);
		}
		if (ordinaryParameterResolver.getBindings().containsKey(paramName)) {
			return ordinaryParameterResolver.getBindings().get(paramName);
		}
		return localVariableResolver.getByBinding(binding);
	}

	public ReferenceableElement getByName(String name) {
		List<Optional<? extends ReferenceableElement>> optionals = new ArrayList<>();

		optionals.add(variableBindings.stream().filter(binding -> binding != null && binding.getName().equals(name))
				.findFirst().map(this::getByBinding));
		optionals.add(methodBindings.stream().filter(meth -> !meth.isConstructor() && meth.getName().equals(name))
				.findFirst().map(methodResolver::getMethod));
		optionals.add(typeBindings.stream().filter(type -> type != null && type.getName().equals(name)).findFirst()
				.map(classifierResolver::getClassifier));
		optionals.add(catchParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst());
		optionals.add(localVariableResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst());
		optionals.add(variableLengthParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst());
		optionals.add(ordinaryParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst());
		optionals.add(additionalLocalVariableResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst());
		optionals.add(enumConstantResolver.getBindings().values().stream().filter(param -> param.getName().equals(name))
				.findFirst());
		optionals.add(fieldResolver.getBindings().values().stream()
				.filter(param -> param != null && param.getName().equals(name)).findFirst());
		optionals.add(additionalFieldResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst());
		optionals.add(classMethodResolver.getBindings().values().stream().filter(param -> param.getName().equals(name))
				.findFirst());
		optionals.add(interfaceMethodResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst());
		optionals.add(typeParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst());
		optionals.add(enumerationResolver.getBindings().values().stream().filter(param -> name.equals(param.getName()))
				.findFirst());
		optionals.add(annotationResolver.getBindings().values().stream().filter(param -> name.equals(param.getName()))
				.findFirst());
		optionals.add(classResolver.getBindings().values().stream().filter(param -> name.equals(param.getName()))
				.findFirst());
		optionals.add(interfaceResolver.getBindings().values().stream().filter(param -> name.equals(param.getName()))
				.findFirst());

		for (Optional<? extends ReferenceableElement> optional : optionals) {
			if (optional.isPresent()) {
				return optional.get();
			}
		}

		return classResolver.getByName(name);
	}

}
