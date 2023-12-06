package jamopp.parser.jdt.converter.implementation;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.classifiers.AnonymousClass;
import org.emftext.language.java.members.Member;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import jamopp.parser.jdt.converter.helper.UtilJdtResolver;
import jamopp.parser.jdt.converter.interfaces.ToConverter;
import jamopp.parser.jdt.util.UtilLayout;

public class ToAnonymousClassConverter implements ToConverter<AnonymousClassDeclaration, AnonymousClass> {

	private final UtilJdtResolver utilJDTResolver;
	private final UtilLayout utilLayout;
	private final ToConverter<BodyDeclaration, Member> toClassMemberConverter;

	@Inject
	ToAnonymousClassConverter(UtilLayout utilLayout, UtilJdtResolver utilJDTResolver,
			@Named("ToClassMemberConverter") ToConverter<BodyDeclaration, Member> toClassMemberConverter) {
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
