package jamopp.parser.jdt.converter.implementation.converter;

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

import jamopp.parser.jdt.converter.interfaces.converter.Converter;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.converter.interfaces.helper.IUtilNamedElement;

public class ToOnDemandStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ModifiersFactory modifiersFactory;
	private final ImportsFactory importsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilNamedElement utilNamedElement;
	private final IUtilJdtResolver jdtResolverUtility;

	@Inject
	public ToOnDemandStaticConverter(IUtilNamedElement utilNamedElement, ModifiersFactory modifiersFactory,
			IUtilLayout layoutInformationConverter, IUtilJdtResolver jdtResolverUtility,
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
