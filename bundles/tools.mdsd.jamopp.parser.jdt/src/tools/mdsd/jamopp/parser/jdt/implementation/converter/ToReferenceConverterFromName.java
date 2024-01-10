package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

import tools.mdsd.jamopp.model.java.references.IdentifierReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToReferenceConverterFromName implements Converter<Name, IdentifierReference> {

	private final Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName;

	@Inject
	ToReferenceConverterFromName(Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName) {
		this.toReferenceConverterFromSimpleName = toReferenceConverterFromSimpleName;
	}

	@Override
	public IdentifierReference convert(Name name) {
		if (name.isSimpleName()) {
			return toReferenceConverterFromSimpleName.convert((SimpleName) name);
		}
		QualifiedName qualifiedName = (QualifiedName) name;
		IdentifierReference parent = convert(qualifiedName.getQualifier());
		IdentifierReference child = toReferenceConverterFromSimpleName.convert(qualifiedName.getName());
		parent.setNext(child);
		return child;
	}

}
