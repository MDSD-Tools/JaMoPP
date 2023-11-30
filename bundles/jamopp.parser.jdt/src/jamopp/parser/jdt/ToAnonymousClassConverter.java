package jamopp.parser.jdt;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.emftext.language.java.classifiers.AnonymousClass;

import com.google.inject.Inject;

class ToAnonymousClassConverter {

	private final UtilJdtResolver utilJDTResolver;
	private final UtilLayout utilLayout;
	private final ToClassMemberConverter toClassMemberConverter;

	@Inject
	ToAnonymousClassConverter(UtilLayout utilLayout, UtilJdtResolver utilJDTResolver,
			ToClassMemberConverter toClassMemberConverter) {
		this.utilJDTResolver = utilJDTResolver;
		this.utilLayout = utilLayout;
		this.toClassMemberConverter = toClassMemberConverter;
	}

	@SuppressWarnings("unchecked")
	AnonymousClass convertToAnonymousClass(AnonymousClassDeclaration anon) {
		ITypeBinding binding = anon.resolveBinding();
		AnonymousClass result;
		if (binding != null) {
			result = utilJDTResolver.getAnonymousClass(binding);
		} else {
			result = utilJDTResolver.getAnonymousClass("" + anon.hashCode());
		}
		anon.bodyDeclarations().forEach(
				obj -> result.getMembers().add(toClassMemberConverter.convertToClassMember((BodyDeclaration) obj)));
		utilLayout.convertToMinimalLayoutInformation(result, anon);
		return result;
	}

}
