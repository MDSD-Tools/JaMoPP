package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.emftext.language.java.references.IdentifierReference;
import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;

public class ToReferenceConverterFromName implements Converter<Name, IdentifierReference> {

	private final Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName;

	@Inject
	ToReferenceConverterFromName(Converter<SimpleName, IdentifierReference> toReferenceConverterFromSimpleName) {
		this.toReferenceConverterFromSimpleName = toReferenceConverterFromSimpleName;
	}

	@Override
	public org.emftext.language.java.references.IdentifierReference convert(Name name) {
		if (name.isSimpleName()) {
			return toReferenceConverterFromSimpleName.convert((SimpleName) name);
		}
		QualifiedName qualifiedName = (QualifiedName) name;
		org.emftext.language.java.references.IdentifierReference parent = convert(qualifiedName.getQualifier());
		org.emftext.language.java.references.IdentifierReference child = toReferenceConverterFromSimpleName
				.convert(qualifiedName.getName());
		parent.setNext(child);
		return child;
	}

}
