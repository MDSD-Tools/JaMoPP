package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.imports.StaticClassifierImport;
import org.emftext.language.java.modifiers.ModifiersFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;
import jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class ToOnDemandStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ModifiersFactory modifiersFactory;
	private final ImportsFactory importsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;
	private final UtilJdtResolver jdtResolverUtility;

	@Inject
	public ToOnDemandStaticConverter(UtilNamedElement utilNamedElement, ModifiersFactory modifiersFactory,
			UtilLayout layoutInformationConverter, UtilJdtResolver jdtResolverUtility,
			ImportsFactory importsFactory) {
		this.modifiersFactory = modifiersFactory;
		this.importsFactory = importsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
		this.jdtResolverUtility = jdtResolverUtility;
	}

	@Override
	public Import convert(ImportDeclaration importDecl) {
		StaticClassifierImport convertedImport = importsFactory.createStaticClassifierImport();
		convertedImport.setStatic(modifiersFactory.createStatic());
		IBinding binding = importDecl.getName().resolveBinding();
		Classifier proxyClass = null;
		if (binding == null || binding.isRecovered() || !(binding instanceof ITypeBinding)) {
			proxyClass = jdtResolverUtility.getClass(importDecl.getName().getFullyQualifiedName());
		} else {
			proxyClass = jdtResolverUtility.getClassifier((ITypeBinding) binding);
		}
		convertedImport.setClassifier((ConcreteClassifier) proxyClass);
		utilNamedElement.addNameToNameSpaceAndElement(importDecl.getName(), convertedImport, proxyClass);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

}
