package jamopp.parser.jdt.implementation.converter;

import java.util.Collections;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationInstance;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilJdtResolver;

@SuppressWarnings("restriction")
public class BindingToPackageConverter
		implements Converter<IPackageBinding, org.emftext.language.java.containers.Package> {

	private final IUtilJdtResolver jdtTResolverUtility;
	private final Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter;

	@Inject
	BindingToPackageConverter(IUtilJdtResolver jdtTResolverUtility,
			Converter<IAnnotationBinding, AnnotationInstance> bindingToAnnotationInstanceConverter) {
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	public org.emftext.language.java.containers.Package convert(IPackageBinding binding) {
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
				pack.getAnnotations().add(bindingToAnnotationInstanceConverter.convert(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		return pack;
	}

}
