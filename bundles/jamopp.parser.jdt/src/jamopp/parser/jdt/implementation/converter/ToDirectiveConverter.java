package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ModuleDirective;
import org.eclipse.jdt.core.dom.ModuleModifier;
import org.eclipse.jdt.core.dom.ModulePackageAccess;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ProvidesDirective;
import org.eclipse.jdt.core.dom.RequiresDirective;
import org.eclipse.jdt.core.dom.UsesDirective;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modules.ModuleReference;
import org.emftext.language.java.modules.ModulesFactory;
import org.emftext.language.java.types.TypeReference;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilLayout;

public class ToDirectiveConverter
		implements Converter<ModuleDirective, org.emftext.language.java.modules.ModuleDirective> {

	private final ModulesFactory modulesFactory;
	private final ModifiersFactory modifiersFactory;
	private final UtilLayout layoutInformationConverter;
	private final UtilJdtResolver jdtResolverUtility;
	private final Converter<Name, TypeReference> utilBaseConverter;
	private final Converter<Name, ModuleReference> toModuleReferenceConverter;

	@Inject
	public ToDirectiveConverter(Converter<Name, TypeReference> utilBaseConverter,
			Converter<Name, ModuleReference> toModuleReferenceConverter, ModulesFactory modulesFactory,
			ModifiersFactory modifiersFactory, UtilLayout layoutInformationConverter,
			UtilJdtResolver jdtResolverUtility) {
		this.modulesFactory = modulesFactory;
		this.modifiersFactory = modifiersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilBaseConverter = utilBaseConverter;
		this.toModuleReferenceConverter = toModuleReferenceConverter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.modules.ModuleDirective convert(ModuleDirective directive) {
		if (directive.getNodeType() == ASTNode.REQUIRES_DIRECTIVE) {
			RequiresDirective reqDir = (RequiresDirective) directive;
			org.emftext.language.java.modules.RequiresModuleDirective result = modulesFactory
					.createRequiresModuleDirective();
			reqDir.modifiers().forEach(obj -> {
				ModuleModifier modifier = (ModuleModifier) obj;
				if (modifier.isStatic()) {
					result.setModifier(modifiersFactory.createStatic());
				} else if (modifier.isTransitive()) {
					result.setModifier(modifiersFactory.createTransitive());
				}
			});
			result.setRequiredModule(toModuleReferenceConverter.convert(reqDir.getName()));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, directive);
			return result;
		}
		if (directive.getNodeType() == ASTNode.EXPORTS_DIRECTIVE
				|| directive.getNodeType() == ASTNode.OPENS_DIRECTIVE) {
			ModulePackageAccess accessDir = (ModulePackageAccess) directive;
			org.emftext.language.java.modules.AccessProvidingModuleDirective convertedDir;
			if (directive.getNodeType() == ASTNode.OPENS_DIRECTIVE) {
				convertedDir = modulesFactory.createOpensModuleDirective();
			} else { // directive.getNodeType() == ASTNode.EXPORTS_DIRECTIVE
				convertedDir = modulesFactory.createExportsModuleDirective();
			}
			IPackageBinding binding = (IPackageBinding) accessDir.getName().resolveBinding();
			convertedDir.setAccessablePackage(jdtResolverUtility.getPackage(binding));
			accessDir.modules()
					.forEach(obj -> convertedDir.getModules().add(toModuleReferenceConverter.convert((Name) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(convertedDir, directive);
			return convertedDir;
		}
		if (directive.getNodeType() == ASTNode.PROVIDES_DIRECTIVE) {
			ProvidesDirective provDir = (ProvidesDirective) directive;
			org.emftext.language.java.modules.ProvidesModuleDirective result = modulesFactory
					.createProvidesModuleDirective();
			result.setTypeReference(utilBaseConverter.convert(provDir.getName()));
			provDir.implementations()
					.forEach(obj -> result.getServiceProviders().add(utilBaseConverter.convert((Name) obj)));
			layoutInformationConverter.convertToMinimalLayoutInformation(result, directive);
			return result;
		}
		UsesDirective usDir = (UsesDirective) directive;
		org.emftext.language.java.modules.UsesModuleDirective result = modulesFactory.createUsesModuleDirective();
		result.setTypeReference(utilBaseConverter.convert(usDir.getName()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, directive);
		return result;
	}

}
