package tools.mdsd.jamopp.parser.jdt.implementation.resolver;

import java.util.HashSet;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import com.google.inject.Inject;

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
		IVariableBinding vBinding = variableBindings.stream().filter(var -> var != null && var.getName().equals(name))
				.findFirst().orElse(null);
		if (vBinding != null) {
			return getByBinding(vBinding);
		}
		IMethodBinding mBinding = methodBindings.stream()
				.filter(meth -> !meth.isConstructor() && meth.getName().equals(name)).findFirst().orElse(null);
		if (mBinding != null) {
			return methodResolver.getMethod(mBinding);
		}
		ITypeBinding tBinding = typeBindings.stream().filter(type -> type != null && type.getName().equals(name))
				.findFirst().orElse(null);
		if (tBinding != null) {
			return classifierResolver.getClassifier(tBinding);
		}
		tools.mdsd.jamopp.model.java.variables.Variable par = catchParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		par = localVariableResolver.getBindings().values().stream().filter(param -> param.getName().equals(name))
				.findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		tools.mdsd.jamopp.model.java.variables.AdditionalLocalVariable addLocVar = additionalLocalVariableResolver
				.getBindings().values().stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (addLocVar != null) {
			return addLocVar;
		}
		par = variableLengthParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		par = ordinaryParameterResolver.getBindings().values().stream().filter(param -> param.getName().equals(name))
				.findFirst().orElse(null);
		if (par != null) {
			return par;
		}
		tools.mdsd.jamopp.model.java.members.EnumConstant enumConst = enumConstantResolver.getBindings().values()
				.stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (enumConst != null) {
			return enumConst;
		}
		tools.mdsd.jamopp.model.java.members.Field field = fieldResolver.getBindings().values().stream()
				.filter(param -> param != null && param.getName().equals(name)).findFirst().orElse(null);
		if (field != null) {
			return field;
		}
		tools.mdsd.jamopp.model.java.members.AdditionalField addField = additionalFieldResolver.getBindings().values()
				.stream().filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (addField != null) {
			return addField;
		}
		tools.mdsd.jamopp.model.java.members.Method meth = classMethodResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (meth != null) {
			return meth;
		}
		meth = interfaceMethodResolver.getBindings().values().stream().filter(param -> param.getName().equals(name))
				.findFirst().orElse(null);
		if (meth != null) {
			return meth;
		}
		tools.mdsd.jamopp.model.java.classifiers.Classifier c = typeParameterResolver.getBindings().values().stream()
				.filter(param -> param.getName().equals(name)).findFirst().orElse(null);
		if (c != null) {
			return c;
		}
		c = enumerationResolver.getBindings().values().stream().filter(param -> name.equals(param.getName()))
				.findFirst().orElse(null);
		if (c != null) {
			return c;
		}
		c = annotationResolver.getBindings().values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		c = classResolver.getBindings().values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		c = interfaceResolver.getBindings().values().stream().filter(param -> name.equals(param.getName())).findFirst()
				.orElse(null);
		if (c != null) {
			return c;
		}
		return classResolver.getByName(name);
	}

}
