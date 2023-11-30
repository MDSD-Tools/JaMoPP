package jamopp.parser.jdt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IModuleBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.classifiers.Annotation;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.classifiers.Interface;
import org.emftext.language.java.commons.NamedElement;
import org.emftext.language.java.commons.NamespaceAwareElement;
import org.emftext.language.java.generics.ExtendsTypeArgument;
import org.emftext.language.java.generics.GenericsFactory;
import org.emftext.language.java.generics.QualifiedTypeArgument;
import org.emftext.language.java.generics.SuperTypeArgument;
import org.emftext.language.java.generics.TypeArgument;
import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.literals.BooleanLiteral;
import org.emftext.language.java.literals.CharacterLiteral;
import org.emftext.language.java.literals.DecimalDoubleLiteral;
import org.emftext.language.java.literals.DecimalFloatLiteral;
import org.emftext.language.java.literals.DecimalIntegerLiteral;
import org.emftext.language.java.literals.DecimalLongLiteral;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.EnumConstant;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ExportsModuleDirective;
import org.emftext.language.java.modules.ModuleReference;
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.modules.OpensModuleDirective;
import org.emftext.language.java.modules.ProvidesModuleDirective;
import org.emftext.language.java.modules.RequiresModuleDirective;
import org.emftext.language.java.modules.UsesModuleDirective;
import org.emftext.language.java.parameters.Parameter;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.parameters.ReceiverParameter;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.Reference;
import org.emftext.language.java.references.ReferenceableElement;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.references.ReflectiveClassReference;
import org.emftext.language.java.references.StringReference;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
class UtilJDTBindingConverter {

	private UtilJdtResolver jdtTResolverUtility;
	
	@Inject
	UtilJDTBindingConverter() {
	}

	List<TypeReference> convertToTypeReferences(ITypeBinding binding) {
		List<TypeReference> result = new ArrayList<>();
		if (binding.isPrimitive()) {
			handlePrimitive(binding, result);
		} else if (binding.isArray()) {
			return convertToTypeReferences(binding.getElementType());
		} else if (binding.isIntersectionType()) {
			for (ITypeBinding b : binding.getTypeBounds()) {
				result.addAll(convertToTypeReferences(b));
			}
		} else {
			Classifier classifier = jdtTResolverUtility.getClassifier(binding);
			convertToNameAndSet(binding, classifier);
			ClassifierReference ref = TypesFactory.eINSTANCE.createClassifierReference();
			if (binding.isParameterizedType()) {
				for (ITypeBinding b : binding.getTypeArguments()) {
					ref.getTypeArguments().add(convertToTypeArgument(b));
				}
			}
			ref.setTarget(classifier);
			result.add(ref);
		}
		return result;
	}

	private void handlePrimitive(ITypeBinding binding, List<TypeReference> result) {
		if ("int".equals(binding.getName())) {
			result.add(TypesFactory.eINSTANCE.createInt());
		} else if ("byte".equals(binding.getName())) {
			result.add(TypesFactory.eINSTANCE.createByte());
		} else if ("short".equals(binding.getName())) {
			result.add(TypesFactory.eINSTANCE.createShort());
		} else if ("long".equals(binding.getName())) {
			result.add(TypesFactory.eINSTANCE.createLong());
		} else if ("boolean".equals(binding.getName())) {
			result.add(TypesFactory.eINSTANCE.createBoolean());
		} else if ("double".equals(binding.getName())) {
			result.add(TypesFactory.eINSTANCE.createDouble());
		} else if ("float".equals(binding.getName())) {
			result.add(TypesFactory.eINSTANCE.createFloat());
		} else if ("void".equals(binding.getName())) {
			result.add(TypesFactory.eINSTANCE.createVoid());
		} else if ("char".equals(binding.getName())) {
			result.add(TypesFactory.eINSTANCE.createChar());
		}
	}

	void convertToNameAndSet(ITypeBinding binding, NamedElement element) {
		String name = binding.getName();
		if (binding.isParameterizedType()) {
			name = name.substring(0, name.indexOf("<"));
		} else if (binding.isArray()) {
			name = name.substring(0, name.indexOf("["));
		}
		element.setName(name);
	}

	TypeArgument convertToTypeArgument(ITypeBinding binding) {
		if (!binding.isWildcardType()) {
			QualifiedTypeArgument result = GenericsFactory.eINSTANCE.createQualifiedTypeArgument();
			result.setTypeReference(convertToTypeReferences(binding).get(0));
			convertToArrayDimensionsAndSet(binding, result);
			return result;
		}
		if (binding.getBound() == null) {
			return GenericsFactory.eINSTANCE.createUnknownTypeArgument();
		}
		if (binding.isUpperbound()) {
			ExtendsTypeArgument result = GenericsFactory.eINSTANCE.createExtendsTypeArgument();
			result.setExtendType(convertToTypeReferences(binding.getBound()).get(0));
			convertToArrayDimensionsAndSet(binding, result);
			return result;
		}
		SuperTypeArgument result = GenericsFactory.eINSTANCE.createSuperTypeArgument();
		result.setSuperType(convertToTypeReferences(binding.getBound()).get(0));
		convertToArrayDimensionsAndSet(binding, result);
		return result;
	}

	void convertToArrayDimensionsAndSet(ITypeBinding binding,
			org.emftext.language.java.arrays.ArrayTypeable arrDimContainer) {
		if (binding.isArray()) {
			for (int i = 0; i < binding.getDimensions(); i++) {
				arrDimContainer.getArrayDimensionsBefore().add(ArraysFactory.eINSTANCE.createArrayDimension());
			}
		}
	}

	ConcreteClassifier convertToConcreteClassifier(ITypeBinding binding, boolean extractAdditionalInformation) {
		binding = binding.getTypeDeclaration();

		ConcreteClassifier result = getConcreteClassifier(binding, extractAdditionalInformation);

		result.setPackage(jdtTResolverUtility.getPackage(binding.getPackage()));
		if (result.eContainer() == null) {
			handleEmptyContainer(binding, extractAdditionalInformation, result);
		}
		if (extractAdditionalInformation) {
			extractAdditionalInformation(binding, result);
		}
		return result;
	}

	private void handleEmptyContainer(ITypeBinding binding, boolean extractAdditionalInformation,
			ConcreteClassifier result) {
		if (extractAdditionalInformation) {
			try {
				for (IAnnotationBinding annotBind : binding.getAnnotations()) {
					result.getAnnotationsAndModifiers().add(convertToAnnotationInstance(annotBind));
				}
				for (ITypeBinding typeBind : binding.getTypeParameters()) {
					result.getTypeParameters().add(convertToTypeParameter(typeBind));
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
		}
		result.getAnnotationsAndModifiers().addAll(convertToModifiers(binding.getModifiers()));
		convertToNameAndSet(binding, result);
	}

	private void extractAdditionalInformation(ITypeBinding binding, ConcreteClassifier result) {
		try {
			Member member;
			for (IVariableBinding varBind : binding.getDeclaredFields()) {
				if (varBind.isEnumConstant()) {
					continue;
				}
				member = convertToField(varBind);
				if (!result.getMembers().contains(member)) {
					result.getMembers().add(member);
				}
			}
			for (IMethodBinding methBind : binding.getDeclaredMethods()) {
				if (methBind.isDefaultConstructor()) {
					continue;
				}
				if (methBind.isConstructor()) {
					member = convertToConstructor(methBind);
				} else {
					member = convertToMethod(methBind);
				}
				if (!result.getMembers().contains(member)) {
					result.getMembers().add(member);
				}
			}
			for (ITypeBinding typeBind : binding.getDeclaredTypes()) {
				member = convertToConcreteClassifier(typeBind, true);
				if (!result.getMembers().contains(member)) {
					result.getMembers().add(member);
				}
			}
		} catch (AbortCompilation e) {
			// Ignore
		}
	}

	private ConcreteClassifier getConcreteClassifier(ITypeBinding binding, boolean extractAdditionalInformation) {
		ConcreteClassifier result = null;
		if (binding.isAnnotation()) {
			result = jdtTResolverUtility.getAnnotation(binding);
		} else if (binding.isClass()) {
			result = handleClass(binding);
		} else if (binding.isInterface()) {
			result = handleInterface(binding);
		} else {
			result = handleElse(binding, extractAdditionalInformation);
		}
		return result;
	}

	private ConcreteClassifier handleElse(ITypeBinding binding, boolean extractAdditionalInformation) {
		ConcreteClassifier result;
		org.emftext.language.java.classifiers.Enumeration resultEnum = jdtTResolverUtility.getEnumeration(binding);
		if (resultEnum.eContainer() == null) {
			try {
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultEnum.getImplements().addAll(convertToTypeReferences(typeBind));
				}
				if (extractAdditionalInformation) {
					for (IVariableBinding varBind : binding.getDeclaredFields()) {
						if (varBind.isEnumConstant()) {
							resultEnum.getConstants().add(convertToEnumConstant(varBind));
						}
					}
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
		}
		result = resultEnum;
		return result;
	}

	private ConcreteClassifier handleInterface(ITypeBinding binding) {
		ConcreteClassifier result;
		Interface resultInterface = jdtTResolverUtility.getInterface(binding);
		if (resultInterface.eContainer() == null) {
			try {
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultInterface.getExtends().addAll(convertToTypeReferences(typeBind));
				}
			} catch (AbortCompilation e) {
			}
		}
		result = resultInterface;
		return result;
	}

	private ConcreteClassifier handleClass(ITypeBinding binding) {
		ConcreteClassifier result;
		org.emftext.language.java.classifiers.Class resultClass = jdtTResolverUtility.getClass(binding);
		if (resultClass.eContainer() == null) {
			try {
				if (binding.getSuperclass() != null) {
					resultClass.setExtends(convertToTypeReferences(binding.getSuperclass()).get(0));
				}
				for (ITypeBinding typeBind : binding.getInterfaces()) {
					resultClass.getImplements().addAll(convertToTypeReferences(typeBind));
				}
			} catch (AbortCompilation e) {
			}
		}
		result = resultClass;
		return result;
	}

	private TypeParameter convertToTypeParameter(ITypeBinding binding) {
		TypeParameter result = jdtTResolverUtility.getTypeParameter(binding);
		if (result.eContainer() != null) {
			return result;
		}
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotations().add(convertToAnnotationInstance(annotBind));
			}
			for (ITypeBinding typeBind : binding.getTypeBounds()) {
				result.getExtendTypes().addAll(convertToTypeReferences(typeBind));
			}
		} catch (AbortCompilation e) {
		}
		convertToNameAndSet(binding, result);
		return result;
	}

	private Reference internalConvertToReference(ITypeBinding binding) {
		IdentifierReference idRef = ReferencesFactory.eINSTANCE.createIdentifierReference();
		idRef.setTarget(jdtTResolverUtility.getClassifier(binding));
		if (binding.isNested()) {
			Reference parentRef = internalConvertToReference(binding.getDeclaringClass());
			parentRef.setNext(idRef);
		}
		return idRef;
	}

	private Reference getTopReference(Reference ref) {
		Reference currentRef = ref;
		Reference parentRef = ref.getPrevious();
		while (parentRef != null) {
			currentRef = parentRef;
			parentRef = currentRef.getPrevious();
		}
		return currentRef;
	}

	private Field convertToField(IVariableBinding binding) {
		ReferenceableElement refElement = jdtTResolverUtility.getReferencableElement(binding);
		if (refElement.eContainer() != null) {
			if (refElement instanceof AdditionalField) {
				return (Field) ((AdditionalField) refElement).eContainer();
			}
			return (Field) refElement;
		}
		Field result = (Field) refElement;
		result.getAnnotationsAndModifiers().addAll(convertToModifiers(binding.getModifiers()));
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotationsAndModifiers().add(convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		result.setTypeReference(convertToTypeReferences(binding.getType()).get(0));
		convertToArrayDimensionsAndSet(binding.getType(), result);
		if (binding.getConstantValue() != null) {
			result.setInitialValue(convertToPrimaryExpression(binding.getConstantValue()));
		}
		return result;
	}

	private EnumConstant convertToEnumConstant(IVariableBinding binding) {
		EnumConstant result = jdtTResolverUtility.getEnumConstant(binding);
		if (result.eContainer() != null) {
			return result;
		}
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotations().add(convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		return result;
	}

	private Constructor convertToConstructor(IMethodBinding binding) {
		Constructor result = jdtTResolverUtility.getConstructor(binding);
		if (result.eContainer() != null) {
			return result;
		}
		result.getAnnotationsAndModifiers().addAll(convertToModifiers(binding.getModifiers()));
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotationsAndModifiers().add(convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		try {
			for (ITypeBinding typeBind : binding.getTypeParameters()) {
				result.getTypeParameters().add(convertToTypeParameter(typeBind));
			}
		} catch (AbortCompilation e) {
		}
		if (binding.getDeclaredReceiverType() != null) {
			ReceiverParameter param = ParametersFactory.eINSTANCE.createReceiverParameter();
			param.setName("");
			param.setTypeReference(convertToTypeReferences(binding.getDeclaredReceiverType()).get(0));
			param.setOuterTypeReference(param.getTypeReference());
			param.setThisReference(LiteralsFactory.eINSTANCE.createThis());
			result.getParameters().add(param);
		}
		for (int index = 0; index < binding.getParameterTypes().length; index++) {
			ITypeBinding typeBind = binding.getParameterTypes()[index];
			Parameter param;
			if (binding.isVarargs() && index == binding.getParameterTypes().length - 1) {
				param = ParametersFactory.eINSTANCE.createVariableLengthParameter();
			} else {
				param = ParametersFactory.eINSTANCE.createOrdinaryParameter();
			}
			param.setName("param" + index);
			param.setTypeReference(convertToTypeReferences(typeBind).get(0));
			convertToArrayDimensionsAndSet(typeBind, param);
			IAnnotationBinding[] binds = binding.getParameterAnnotations(index);
			try {
				for (IAnnotationBinding annotBind : binds) {
					param.getAnnotationsAndModifiers().add(convertToAnnotationInstance(annotBind));
				}
			} catch (AbortCompilation e) {
			}
			result.getParameters().add(param);
		}
		for (ITypeBinding typeBind : binding.getExceptionTypes()) {
			result.getExceptions().add(convertToNamespaceClassifierReference(typeBind));
		}
		return result;
	}

	private Method convertToMethod(IMethodBinding binding) {
		Method result = jdtTResolverUtility.getMethod(binding);
		if (result.eContainer() != null) {
			return result;
		}
		result.getAnnotationsAndModifiers().addAll(convertToModifiers(binding.getModifiers()));
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotationsAndModifiers().add(convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		result.setName(binding.getName());
		result.setTypeReference(convertToTypeReferences(binding.getReturnType()).get(0));
		convertToArrayDimensionsAndSet(binding.getReturnType(), result);
		try {
			for (ITypeBinding typeBind : binding.getTypeParameters()) {
				result.getTypeParameters().add(convertToTypeParameter(typeBind));
			}
		} catch (AbortCompilation e) {
		}
		if (binding.getDeclaredReceiverType() != null) {
			ReceiverParameter param = ParametersFactory.eINSTANCE.createReceiverParameter();
			param.setTypeReference(convertToTypeReferences(binding.getDeclaredReceiverType()).get(0));
			param.setName("");
			param.setThisReference(LiteralsFactory.eINSTANCE.createThis());
			result.getParameters().add(param);
		}
		for (int index = 0; index < binding.getParameterTypes().length; index++) {
			ITypeBinding typeBind = binding.getParameterTypes()[index];
			Parameter param;
			if (binding.isVarargs() && index == binding.getParameterTypes().length - 1) {
				param = ParametersFactory.eINSTANCE.createVariableLengthParameter();
			} else {
				param = ParametersFactory.eINSTANCE.createOrdinaryParameter();
			}
			param.setName("param" + index);
			param.setTypeReference(convertToTypeReferences(typeBind).get(0));
			convertToArrayDimensionsAndSet(typeBind, param);
			try {
				IAnnotationBinding[] binds = binding.getParameterAnnotations(index);
				for (IAnnotationBinding annotBind : binds) {
					param.getAnnotationsAndModifiers().add(convertToAnnotationInstance(annotBind));
				}
			} catch (AbortCompilation e) {
			}
			result.getParameters().add(param);
		}
		if (binding.getDefaultValue() != null) {
			((InterfaceMethod) result).setDefaultValue(convertToAnnotationValue(binding.getDefaultValue()));
		}
		try {
			for (ITypeBinding typeBind : binding.getExceptionTypes()) {
				result.getExceptions().add(convertToNamespaceClassifierReference(typeBind));
			}
		} catch (AbortCompilation e) {
		}
		if (binding.getDeclaringClass().isInterface()) {
			boolean hasDefaultImpl = false;
			for (org.emftext.language.java.modifiers.Modifier mod : result.getModifiers()) {
				if (mod instanceof org.emftext.language.java.modifiers.Default) {
					hasDefaultImpl = true;
					break;
				}
			}
			if (!hasDefaultImpl) {
				result.setStatement(StatementsFactory.eINSTANCE.createEmptyStatement());
			}
		}
		return result;
	}

	private NamespaceClassifierReference convertToNamespaceClassifierReference(ITypeBinding binding) {
		NamespaceClassifierReference ref = TypesFactory.eINSTANCE.createNamespaceClassifierReference();
		if (binding.getPackage() != null) {
			Collections.addAll(ref.getNamespaces(), binding.getPackage().getNameComponents());
		}
		ClassifierReference classRef = TypesFactory.eINSTANCE.createClassifierReference();
		classRef.setTarget(jdtTResolverUtility.getClassifier(binding));
		ref.getClassifierReferences().add(classRef);
		return ref;
	}

	private AnnotationInstance convertToAnnotationInstance(IAnnotationBinding binding) {
		AnnotationInstance result = org.emftext.language.java.annotations.AnnotationsFactory.eINSTANCE
				.createAnnotationInstance();
		Annotation resultClass = jdtTResolverUtility.getAnnotation(binding.getAnnotationType());
		convertToNameAndSet(binding.getAnnotationType(), resultClass);
		result.setAnnotation(resultClass);
		if (binding.getDeclaredMemberValuePairs().length > 0) {
			org.emftext.language.java.annotations.AnnotationParameterList params = org.emftext.language.java.annotations.AnnotationsFactory.eINSTANCE
					.createAnnotationParameterList();
			for (IMemberValuePairBinding memBind : binding.getDeclaredMemberValuePairs()) {
				params.getSettings().add(convertToAnnotationAttributeSetting(memBind));
			}
			result.setParameter(params);
		}
		return result;
	}

	private org.emftext.language.java.annotations.AnnotationAttributeSetting convertToAnnotationAttributeSetting(
			IMemberValuePairBinding binding) {
		AnnotationAttributeSetting result = org.emftext.language.java.annotations.AnnotationsFactory.eINSTANCE
				.createAnnotationAttributeSetting();
		result.setAttribute(jdtTResolverUtility.getInterfaceMethod(binding.getMethodBinding()));
		result.setValue(convertToAnnotationValue(binding.getValue()));
		return result;
	}

	private AnnotationValue convertToAnnotationValue(Object value) {
		if (value instanceof IVariableBinding varBind) {
			Reference parentRef = internalConvertToReference(varBind.getDeclaringClass());
			IdentifierReference varRef = ReferencesFactory.eINSTANCE.createIdentifierReference();
			varRef.setTarget(jdtTResolverUtility.getEnumConstant(varBind));
			parentRef.setNext(varRef);
			return getTopReference(varRef);
		}
		if (value instanceof IAnnotationBinding) {
			return convertToAnnotationInstance((IAnnotationBinding) value);
		}
		if (value instanceof Object[] values) {
			org.emftext.language.java.arrays.ArrayInitializer initializer = ArraysFactory.eINSTANCE
					.createArrayInitializer();
			for (Object value2 : values) {
				initializer.getInitialValues().add(
						(org.emftext.language.java.arrays.ArrayInitializationValue) convertToAnnotationValue(value2));
			}
			return initializer;
		}
		if (value instanceof ITypeBinding) {
			Reference parentRef = internalConvertToReference((ITypeBinding) value);
			ReflectiveClassReference classRef = ReferencesFactory.eINSTANCE.createReflectiveClassReference();
			parentRef.setNext(classRef);
			return getTopReference(classRef);
		}
		return convertToPrimaryExpression(value);
	}

	private org.emftext.language.java.expressions.PrimaryExpression convertToPrimaryExpression(Object value) {
		if (value instanceof String) {
			StringReference ref = ReferencesFactory.eINSTANCE.createStringReference();
			ref.setValue((String) value);
			return ref;
		}
		if (value instanceof Boolean) {
			BooleanLiteral literal = LiteralsFactory.eINSTANCE.createBooleanLiteral();
			literal.setValue((boolean) value);
			return literal;
		}
		if (value instanceof Character) {
			CharacterLiteral literal = LiteralsFactory.eINSTANCE.createCharacterLiteral();
			literal.setValue("\\u" + Integer.toHexString((Character) value));
			return literal;
		}
		if (value instanceof Byte) {
			DecimalIntegerLiteral literal = LiteralsFactory.eINSTANCE.createDecimalIntegerLiteral();
			literal.setDecimalValue(BigInteger.valueOf((byte) value));
			return literal;
		}
		if (value instanceof Short) {
			DecimalIntegerLiteral literal = LiteralsFactory.eINSTANCE.createDecimalIntegerLiteral();
			literal.setDecimalValue(BigInteger.valueOf((short) value));
			return literal;
		}
		if (value instanceof Integer) {
			DecimalIntegerLiteral literal = LiteralsFactory.eINSTANCE.createDecimalIntegerLiteral();
			literal.setDecimalValue(BigInteger.valueOf((int) value));
			return literal;
		}
		if (value instanceof Long) {
			DecimalLongLiteral literal = LiteralsFactory.eINSTANCE.createDecimalLongLiteral();
			literal.setDecimalValue(BigInteger.valueOf((long) value));
			return literal;
		}
		if (value instanceof Float) {
			DecimalFloatLiteral literal = LiteralsFactory.eINSTANCE.createDecimalFloatLiteral();
			literal.setDecimalValue((float) value);
			return literal;
		}
		if (value instanceof Double) {
			DecimalDoubleLiteral literal = LiteralsFactory.eINSTANCE.createDecimalDoubleLiteral();
			literal.setDecimalValue((double) value);
			return literal;
		}
		return LiteralsFactory.eINSTANCE.createNullLiteral();
	}

	private List<org.emftext.language.java.modifiers.Modifier> convertToModifiers(int modifiers) {
		ArrayList<org.emftext.language.java.modifiers.Modifier> result = new ArrayList<>();
		if (Modifier.isAbstract(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createAbstract());
		}
		if (Modifier.isDefault(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createDefault());
		}
		if (Modifier.isFinal(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createFinal());
		}
		if (Modifier.isNative(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createNative());
		}
		if (Modifier.isPrivate(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createPrivate());
		}
		if (Modifier.isProtected(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createProtected());
		}
		if (Modifier.isPublic(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createPublic());
		}
		if (Modifier.isStatic(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createStatic());
		}
		if (Modifier.isStrictfp(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createStrictfp());
		}
		if (Modifier.isSynchronized(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createSynchronized());
		}
		if (Modifier.isTransient(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createTransient());
		}
		if (Modifier.isVolatile(modifiers)) {
			result.add(ModifiersFactory.eINSTANCE.createVolatile());
		}
		return result;
	}

	org.emftext.language.java.containers.Package convertToPackage(IPackageBinding binding) {
		org.emftext.language.java.containers.Package pack = jdtTResolverUtility.getPackage(binding);
		pack.setModule(jdtTResolverUtility.getModule(binding.getModule()));
		if (!pack.getAnnotations().isEmpty()) {
			return pack;
		}
		pack.getNamespaces().clear();
		Collections.addAll(pack.getNamespaces(), binding.getNameComponents());
		pack.setName("");
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				pack.getAnnotations().add(convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		return pack;
	}

	org.emftext.language.java.containers.Module convertToModule(IModuleBinding binding) {
		org.emftext.language.java.containers.Module result = jdtTResolverUtility.getModule(binding);
		if (!result.eContents().isEmpty()) {
			return result;
		}
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotations().add(convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		if (binding.isOpen()) {
			result.setOpen(ModifiersFactory.eINSTANCE.createOpen());
		}
		convertToNamespacesAndSet(binding.getName(), result);
		result.setName("");
		try {
			for (IPackageBinding packBind : binding.getExportedPackages()) {
				ExportsModuleDirective dir = ModulesFactory.eINSTANCE.createExportsModuleDirective();
				dir.setAccessablePackage(jdtTResolverUtility.getPackage(packBind));
				String[] mods = binding.getExportedTo(packBind);
				for (String modName : mods) {
					ModuleReference ref = ModulesFactory.eINSTANCE.createModuleReference();
					ref.setTarget(jdtTResolverUtility.getModule(modName));
					dir.getModules().add(ref);
				}
				result.getTarget().add(dir);
			}
			for (IPackageBinding packBind : binding.getOpenedPackages()) {
				OpensModuleDirective dir = ModulesFactory.eINSTANCE.createOpensModuleDirective();
				dir.setAccessablePackage(jdtTResolverUtility.getPackage(packBind));
				String[] mods = binding.getOpenedTo(packBind);
				for (String modName : mods) {
					ModuleReference ref = ModulesFactory.eINSTANCE.createModuleReference();
					ref.setTarget(jdtTResolverUtility.getModule(modName));
					dir.getModules().add(ref);
				}
				result.getTarget().add(dir);
			}
			for (IModuleBinding modBind : binding.getRequiredModules()) {
				RequiresModuleDirective dir = ModulesFactory.eINSTANCE.createRequiresModuleDirective();
				org.emftext.language.java.containers.Module reqMod = jdtTResolverUtility.getModule(modBind);
				ModuleReference ref = ModulesFactory.eINSTANCE.createModuleReference();
				ref.setTarget(reqMod);
				dir.setRequiredModule(ref);
				result.getTarget().add(dir);
			}
			for (ITypeBinding typeBind : binding.getUses()) {
				UsesModuleDirective dir = ModulesFactory.eINSTANCE.createUsesModuleDirective();
				dir.setTypeReference(convertToTypeReferences(typeBind).get(0));
				result.getTarget().add(dir);
			}
			for (ITypeBinding typeBind : binding.getServices()) {
				ProvidesModuleDirective dir = ModulesFactory.eINSTANCE.createProvidesModuleDirective();
				dir.setTypeReference(convertToTypeReferences(typeBind).get(0));
				for (ITypeBinding service : binding.getImplementations(typeBind)) {
					dir.getServiceProviders().addAll(convertToTypeReferences(service));
				}
				result.getTarget().add(dir);
			}
		} catch (AbortCompilation e) {
		}
		return result;
	}

	private void convertToNamespacesAndSet(String namespaces, NamespaceAwareElement ele) {
		ele.getNamespaces().clear();
		String[] singleNamespaces = namespaces.split("\\.");
		Collections.addAll(ele.getNamespaces(), singleNamespaces);
	}
	
	void setJDTResolverUtility(UtilJdtResolver jDTResolverUtility) {
		jdtTResolverUtility = jDTResolverUtility;
	}
}
