package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.List;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.generics.TypeParameter;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

@SuppressWarnings("restriction")
public class BindingToTypeParameterConverter implements Converter<ITypeBinding, TypeParameter> {

	private final UtilNamedElement utilNamedElement;
	private final UtilJdtResolver jdtTResolverUtility;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	BindingToTypeParameterConverter(UtilNamedElement utilNamedElement,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			UtilJdtResolver jdtTResolverUtility,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
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
