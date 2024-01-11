package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

@SuppressWarnings("restriction")
public class BindingToTypeParameterConverter implements Converter<ITypeBinding, TypeParameter> {

	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtTResolverUtility;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	public BindingToTypeParameterConverter(UtilNamedElement utilNamedElement,
			Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter, JdtResolver jdtTResolverUtility,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	@Override
	public TypeParameter convert(ITypeBinding binding) {
		TypeParameter result = jdtTResolverUtility.getTypeParameter(binding);
		if (result.eContainer() == null) {
			try {
				for (IAnnotationBinding annotBind : binding.getAnnotations()) {
					result.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
				for (ITypeBinding typeBind : binding.getTypeBounds()) {
					result.getExtendTypes().addAll(toTypeReferencesConverter.convert(typeBind));
				}
			} catch (AbortCompilation e) {
				// Ignore
			}
			utilNamedElement.convertToNameAndSet(binding, result);

		}
		return result;
	}

}
