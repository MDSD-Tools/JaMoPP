package jamopp.parser.jdt.binding;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.generics.TypeParameter;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.ToTypeReferencesConverter;
import jamopp.parser.jdt.resolver.UtilJdtResolver;
import jamopp.parser.jdt.util.UtilNamedElement;

public class BindingToTypeParameterConverter {

	private final UtilNamedElement utilNamedElement;
	private final ToTypeReferencesConverter toTypeReferencesConverter;
	private final UtilJdtResolver jdtTResolverUtility;
	private final BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter;

	@Inject
	BindingToTypeParameterConverter(UtilNamedElement utilNamedElement,
			ToTypeReferencesConverter toTypeReferencesConverter, UtilJdtResolver jdtTResolverUtility,
			BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter) {
		this.utilNamedElement = utilNamedElement;
		this.toTypeReferencesConverter = toTypeReferencesConverter;
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	TypeParameter convertToTypeParameter(ITypeBinding binding) {
		TypeParameter result = jdtTResolverUtility.getTypeParameter(binding);
		if (result.eContainer() != null) {
			return result;
		}
		try {
			for (IAnnotationBinding annotBind : binding.getAnnotations()) {
				result.getAnnotations()
						.add(bindingToAnnotationInstanceConverter.convertToAnnotationInstance(annotBind));
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
