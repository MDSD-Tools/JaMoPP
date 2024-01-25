package tools.mdsd.jamopp.parser.implementation.converter;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;

import tools.mdsd.jamopp.model.java.annotations.AnnotationInstance;
import tools.mdsd.jamopp.model.java.generics.TypeParameter;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilNamedElement;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

@SuppressWarnings("restriction")
public class BindingToTypeParameterConverter implements Converter<ITypeBinding, TypeParameter> {

	private final UtilNamedElement utilNamedElement;
	private final JdtResolver jdtTResolverUtility;
	private final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	public BindingToTypeParameterConverter(final UtilNamedElement utilNamedElement,
			final Converter<ITypeBinding, List<TypeReference>> toTypeReferencesConverter,
			final JdtResolver jdtTResolverUtility,
			final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	@Override
	public TypeParameter convert(final ITypeBinding binding) {
		final TypeParameter result = jdtTResolverUtility.getTypeParameter(binding);
		if (result.eContainer() == null) {
			try {
				for (final IAnnotationBinding annotBind : binding.getAnnotations()) {
					result.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
				}
				for (final ITypeBinding typeBind : binding.getTypeBounds()) {
					result.getExtendTypes().addAll(toTypeReferencesConverter.convert(typeBind));
				}
			} catch (final AbortCompilation e) {
				// Ignore
			}
			utilNamedElement.convertToNameAndSet(binding, result);

		}
		return result;
	}

}
