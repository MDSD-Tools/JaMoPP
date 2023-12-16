package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.emftext.language.java.imports.Import;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.interfaces.converter.Converter;

public class ToImportConverter implements Converter<ImportDeclaration, Import> {

	private final Converter<ImportDeclaration, Import> toOnDemandStaticConverter;
	private final Converter<ImportDeclaration, Import> toOnDemandNonStaticConverter;
	private final Converter<ImportDeclaration, Import> toNonOnDemandStaticConverter;
	private final Converter<ImportDeclaration, Import> toNonOnDemandNonStaticConverter;

	@Inject
	public ToImportConverter(
			@Named("ToOnDemandStaticConverter") Converter<ImportDeclaration, Import> toOnDemandStaticConverter,
			@Named("ToOnDemandNonStaticConverter") Converter<ImportDeclaration, Import> toOnDemandNonStaticConverter,
			@Named("ToNonOnDemandStaticConverter") Converter<ImportDeclaration, Import> toNonOnDemandStaticConverter,
			@Named("ToNonOnDemandNonStaticConverter") Converter<ImportDeclaration, Import> toNonOnDemandNonStaticConverter) {
		this.toOnDemandStaticConverter = toOnDemandStaticConverter;
		this.toOnDemandNonStaticConverter = toOnDemandNonStaticConverter;
		this.toNonOnDemandStaticConverter = toNonOnDemandStaticConverter;
		this.toNonOnDemandNonStaticConverter = toNonOnDemandNonStaticConverter;
	}

	@Override
	public Import convert(ImportDeclaration declaration) {
		if (declaration.isOnDemand()) {
			if (declaration.isStatic()) {
				return toOnDemandStaticConverter.convert(declaration);
			} else {
				return toOnDemandNonStaticConverter.convert(declaration);
			}
		} else {
			if (declaration.isStatic()) {
				return toNonOnDemandStaticConverter.convert(declaration);
			} else {
				return toNonOnDemandNonStaticConverter.convert(declaration);
			}
		}
	}
}
