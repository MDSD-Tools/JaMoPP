package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.emftext.language.java.imports.Import;
import org.emftext.language.java.imports.ImportsFactory;
import org.emftext.language.java.imports.PackageImport;
import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.interfaces.helper.IUtilNamedElement;

public class ToOnDemandNonStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ImportsFactory importsFactory;
	private final IUtilLayout layoutInformationConverter;
	private final IUtilNamedElement utilNamedElement;

	@Inject
	public ToOnDemandNonStaticConverter(IUtilNamedElement utilNamedElement, IUtilLayout layoutInformationConverter,
			ImportsFactory importsFactory) {
		this.importsFactory = importsFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.utilNamedElement = utilNamedElement;
	}

	@Override
	public Import convert(ImportDeclaration importDecl) {
		PackageImport convertedImport = importsFactory.createPackageImport();
		utilNamedElement.addNameToNameSpace(importDecl.getName(), convertedImport);
		layoutInformationConverter.convertToMinimalLayoutInformation(convertedImport, importDecl);
		return convertedImport;
	}

}
