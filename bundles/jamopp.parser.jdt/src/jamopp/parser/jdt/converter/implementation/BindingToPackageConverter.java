package jamopp.parser.jdt.converter.implementation;

import java.util.Collections;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import com.google.inject.Inject;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;

public class BindingToPackageConverter
		implements ToConverter<IPackageBinding, org.emftext.language.java.containers.Package> {

	private final UtilJdtResolver jdtTResolverUtility;
	private final BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter;

	@Inject
	BindingToPackageConverter(UtilJdtResolver jdtTResolverUtility,
			BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter) {
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
