package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.members.Member;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToAnonymousClassConverter implements Converter<AnonymousClassDeclaration, AnonymousClass> {

	private final UtilJdtResolver utilJDTResolver;
	private final UtilLayout utilLayout;
	private final Converter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	ToAnonymousClassConverter(UtilLayout utilLayout, UtilJdtResolver utilJDTResolver,
			@Named("ToClassMemberConverter") Converter<BodyDeclaration, Member> toClassMemberConverter) {
		this.utilJDTResolver = utilJDTResolver;
		this.utilLayout = utilLayout;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AnonymousClass convert(AnonymousClassDeclaration anon) {
		ITypeBinding binding = anon.resolveBinding();
		AnonymousClass result;
		if (binding != null) {
			result = utilJDTResolver.getAnonymousClass(binding);
		} else {
			result = utilJDTResolver.getAnonymousClass("" + anon.hashCode());
		}
		anon.bodyDeclarations()
				.forEach(obj -> result.getMembers().add(toClassMemberConverter.convert((BodyDeclaration) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, anon);
		return result;
	}

}
