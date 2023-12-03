package jamopp.parser.jdt.converter.binding;

import java.util.Collections;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.emftext.language.java.annotations.AnnotationsFactory;
import org.emftext.language.java.arrays.ArraysFactory;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.parameters.ParametersFactory;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.types.TypesFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.parser.jdt.converter.resolver.UtilJdtResolver;

public class BindingToPackageConverter {

	private final UtilJdtResolver jdtTResolverUtility;
	private final BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter;

	@Inject
	BindingToPackageConverter(UtilJdtResolver jdtTResolverUtility,
			BindingToAnnotationInstanceConverter bindingToAnnotationInstanceConverter) {
		this.jdtTResolverUtility = jdtTResolverUtility;
		this.bindingToAnnotationInstanceConverter = bindingToAnnotationInstanceConverter;
	}

	public org.emftext.language.java.containers.Package convertToPackage(IPackageBinding binding) {
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
				pack.getAnnotations().add(bindingToAnnotationInstanceConverter.convertToAnnotationInstance(annotBind));
			}
		} catch (AbortCompilation e) {
		}
		return pack;
	}

}
