package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ImportDeclaration;
import tools.mdsd.jamopp.model.java.imports.Import;
import tools.mdsd.jamopp.model.java.imports.ImportsFactory;
import tools.mdsd.jamopp.model.java.imports.PackageImport;
import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilNamedElement;

public class ToOnDemandNonStaticConverter implements Converter<ImportDeclaration, Import> {

	private final ImportsFactory importsFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilNamedElement utilNamedElement;

	@Inject
	public ToOnDemandNonStaticConverter(UtilNamedElement utilNamedElement, UtilLayout layoutInformationConverter,
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
