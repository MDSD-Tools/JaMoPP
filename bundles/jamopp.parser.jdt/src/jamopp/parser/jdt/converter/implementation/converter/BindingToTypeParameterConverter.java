package jamopp.parser.jdt.converter.implementation.converter;

import java.util.List;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.converter.interfaces.converter.ToConverter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

@SuppressWarnings("restriction")
public class BindingToTypeParameterConverter implements ToConverter<ITypeBinding, TypeParameter> {

	private final IUtilNamedElement utilNamedElement;
	private final IUtilJdtResolver jdtTResolverUtility;
	private final ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final ToConverter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	BindingToTypeParameterConverter(IUtilNamedElement utilNamedElement,
			ToConverter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			IUtilJdtResolver jdtTResolverUtility,
			ToConverter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	@Override
	public TypeParameter convert(ITypeBinding binding) {
		TypeParameter result = jdtTResolverUtility.getTypeParameter(binding);
		if (result.eContainer() != null) {
			return result;
		}
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
			}
			for (ITypeBinding typeBind : binding.getTypeBounds()) {
				result.getExtendTypes().addAll(toTypeReferencesConverter.convert(typeBind));
			}
		} catch (AbortCompilation e) {
		}
		utilNamedElement.convertToNameAndSet(binding, result);
		return result;
	}

}
