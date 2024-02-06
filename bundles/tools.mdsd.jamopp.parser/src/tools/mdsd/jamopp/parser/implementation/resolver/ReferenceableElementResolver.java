package tools.mdsd.jamopp.parser.implementation.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

import tools.mdsd.jamopp.model.java.classifiers.Annotation;
import tools.mdsd.jamopp.model.java.classifiers.Class;
import tools.mdsd.jamopp.model.java.classifiers.Classifier;
import tools.mdsd.jamopp.model.java.classifiers.Enumeration;
import tools.mdsd.jamopp.model.java.classifiers.Interface;
import tools.mdsd.jamopp.model.java.commons.NamedElement;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.ClassMethod;
import tools.mdsd.jamopp.model.java.members.EnumConstant;
import tools.mdsd.jamopp.model.java.members.Field;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.model.java.parameters.CatchParameter;
import tools.mdsd.jamopp.model.java.parameters.OrdinaryParameter;
import tools.mdsd.jamopp.model.java.parameters.VariableLengthParameter;
import tools.mdsd.jamopp.model.java.references.ReferenceableElement;
import tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable;
import tools.mdsd.jamopp.model.java.variables.LocalVariable;
import tools.mdsd.jamopp.parser.interfaces.resolver.ToStringConverter;
import tools.mdsd.jamopp.parser.interfaces.resolver.ConverterWithBoolean;
import tools.mdsd.jamopp.parser.interfaces.resolver.MethodResolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.Resolver;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithCache;
import tools.mdsd.jamopp.parser.interfaces.resolver.ResolverWithName;

public class ReferenceableElementResolver implements ResolverWithName<ReferenceableElement, IVariableBinding> {

	private final Set<ITypeBinding> typeBindings;
	private final Set<IMethodBinding> methodBindings;
	private final Set<IVariableBinding> variableBindings;

	private final ToStringConverter<IVariableBinding> toFieldNameConverter;
	private final ConverterWithBoolean<IVariableBinding> toParameterNameConverter;

	private final MethodResolver methodResolver;
	private final Resolver<Classifier, ITypeBinding> classifierResolver;

	private final ResolverWithCache<Annotation, ITypeBinding> annotationResolver;
	private final ResolverWithCache<Enumeration, ITypeBinding> enumerationResolver;
	private final ResolverWithCache<Interface, ITypeBinding> interfaceResolver;
	private final ResolverWithCache<Class, ITypeBinding> classResolver;
	private final ResolverWithCache<TypeParameter, ITypeBinding> typeParameterResolver;
	private final ResolverWithCache<ClassMethod, IMethodBinding> classMethodResolver;
	private final ResolverWithCache<Field, IVariableBinding> fieldResolver;
	private final ResolverWithCache<EnumConstant, IVariableBinding> enumConstantResolver;
	private final ResolverWithCache<AdditionalField, IVariableBinding> additionalFieldResolver;
	private final ResolverWithCache<CatchParameter, IVariableBinding> catchParameterResolver;
	private final ResolverWithCache<OrdinaryParameter, IVariableBinding> ordinaryParameterResolver;
	private final ResolverWithCache<AdditionalLocalVariable, IVariableBinding> additionalLocalVariableResolver;
	private final ResolverWithCache<VariableLengthParameter, IVariableBinding> variableLengthParameterResolver;
	private final ResolverWithCache<LocalVariable, IVariableBinding> localVariableResolver;
	private final ResolverWithCache<InterfaceMethod, IMethodBinding> interfaceMethodResolver;

	@Inject
	public ReferenceableElementResolver(final Set<ITypeBinding> typeBindings, final Set<IMethodBinding> methodBindings,
			final Set<IVariableBinding> variableBindings, final ToStringConverter<IVariableBinding> toFieldNameConverter,
			final ConverterWithBoolean<IVariableBinding> toParameterNameConverter, final MethodResolver methodResolver,
			final Resolver<Classifier, ITypeBinding> classifierResolver,
			final ResolverWithCache<Annotation, ITypeBinding> annotationResolver,
			final ResolverWithCache<Enumeration, ITypeBinding> enumerationResolver,
			final ResolverWithCache<Interface, ITypeBinding> interfaceResolver,
			final ResolverWithCache<Class, ITypeBinding> classResolver,
			final ResolverWithCache<TypeParameter, ITypeBinding> typeParameterResolver,
			final ResolverWithCache<ClassMethod, IMethodBinding> classMethodResolver,
			final ResolverWithCache<Field, IVariableBinding> fieldResolver,
			final ResolverWithCache<EnumConstant, IVariableBinding> enumConstantResolver,
			final ResolverWithCache<AdditionalField, IVariableBinding> additionalFieldResolver,
			final ResolverWithCache<CatchParameter, IVariableBinding> catchParameterResolver,
			final ResolverWithCache<OrdinaryParameter, IVariableBinding> ordinaryParameterResolver,
			final ResolverWithCache<AdditionalLocalVariable, IVariableBinding> additionalLocalVariableResolver,
			final ResolverWithCache<VariableLengthParameter, IVariableBinding> variableLengthParameterResolver,
			final ResolverWithCache<LocalVariable, IVariableBinding> localVariableResolver,
			final ResolverWithCache<InterfaceMethod, IMethodBinding> interfaceMethodResolver) {
		this.typeBindings = typeBindings;
		this.methodBindings = methodBindings;
		this.variableBindings = variableBindings;
		this.toFieldNameConverter = toFieldNameConverter;
		this.toParameterNameConverter = toParameterNameConverter;
		this.methodResolver = methodResolver;
		this.classifierResolver = classifierResolver;
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
	}

	@Override
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
			if (catchParameterResolver.containsKey(paramName)) {
				referenceableElement = catchParameterResolver.get(paramName);
			} else if (localVariableResolver.containsKey(paramName)) {
				referenceableElement = localVariableResolver.get(paramName);
			} else if (additionalLocalVariableResolver.containsKey(paramName)) {
				referenceableElement = additionalLocalVariableResolver.getByName(paramName);
			} else if (ordinaryParameterResolver.containsKey(paramName)) {
				referenceableElement = ordinaryParameterResolver.get(paramName);
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
		if (ordinaryParameterResolver.containsKey(paramName)) {
			referenceableElement = ordinaryParameterResolver.get(paramName);
		} else if (variableLengthParameterResolver.containsKey(paramName)) {
			referenceableElement = variableLengthParameterResolver.get(paramName);
		} else {
			referenceableElement = ordinaryParameterResolver.getByBinding(binding);
		}
		return referenceableElement;
	}

	private ReferenceableElement handleIsField(final IVariableBinding binding) {
		ReferenceableElement referenceableElement;
		final String fieldName = toFieldNameConverter.convert(binding);
		if (fieldResolver.containsKey(fieldName)) {
			referenceableElement = fieldResolver.get(fieldName);
		} else if (additionalFieldResolver.containsKey(fieldName)) {
			referenceableElement = additionalFieldResolver.get(fieldName);
		} else {
			referenceableElement = fieldResolver.getByBinding(binding);
		}
		return referenceableElement;
	}

	@Override
	public ReferenceableElement getByName(final String name) {
		final List<Stream<? extends ReferenceableElement>> streams = new ArrayList<>();
		streams.add(variableBindings.stream().filter(bindingsFilter(name)).map(this::getByBinding));
		streams.add(methodBindings.stream().filter(methodFilter(name)).map(methodResolver::getMethod));
		streams.add(typeBindings.stream().filter(typeFilter(name)).map(classifierResolver::getByBinding));
		streams.add(catchParameterResolver.getBindings().stream().filter(filter(name)));
		streams.add(localVariableResolver.getBindings().stream().filter(filter(name)));
		streams.add(variableLengthParameterResolver.getBindings().stream().filter(filter(name)));
		streams.add(ordinaryParameterResolver.getBindings().stream().filter(filter(name)));
		streams.add(additionalLocalVariableResolver.getBindings().stream().filter(filter(name)));
		streams.add(enumConstantResolver.getBindings().stream().filter(filter(name)));
		streams.add(fieldResolver.getBindings().stream().filter(filter(name)));
		streams.add(additionalFieldResolver.getBindings().stream().filter(filter(name)));
		streams.add(classMethodResolver.getBindings().stream().filter(filter(name)));
		streams.add(interfaceMethodResolver.getBindings().stream().filter(filter(name)));
		streams.add(typeParameterResolver.getBindings().stream().filter(filter(name)));
		streams.add(enumerationResolver.getBindings().stream().filter(filter(name)));
		streams.add(annotationResolver.getBindings().stream().filter(filter(name)));
		streams.add(classResolver.getBindings().stream().filter(filter(name)));
		streams.add(interfaceResolver.getBindings().stream().filter(filter(name)));

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
