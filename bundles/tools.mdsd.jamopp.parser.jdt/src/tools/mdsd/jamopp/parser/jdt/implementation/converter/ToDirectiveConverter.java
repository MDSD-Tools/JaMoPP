package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ModuleDirective;
import org.eclipse.jdt.core.dom.ModuleModifier;
import org.eclipse.jdt.core.dom.ModulePackageAccess;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ProvidesDirective;
import org.eclipse.jdt.core.dom.RequiresDirective;
import org.eclipse.jdt.core.dom.UsesDirective;

import tools.mdsd.jamopp.model.java.modifiers.ModifiersFactory;
import tools.mdsd.jamopp.model.java.modules.ModuleReference;
import tools.mdsd.jamopp.model.java.modules.ModulesFactory;
import tools.mdsd.jamopp.model.java.types.TypeReference;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

public class ToDirectiveConverter
		implements Converter<ModuleDirective, tools.mdsd.jamopp.model.java.modules.ModuleDirective> {

	private final ModulesFactory modulesFactory;
	private final ModifiersFactory modifiersFactory;
	private final UtilLayout layoutInformationConverter;
	private final JdtResolver jdtResolverUtility;
	private final Converter<Name, TypeReference> utilBaseConverter;
	private final Converter<Name, ModuleReference> toModuleReferenceConverter;

	@Inject
	public ToDirectiveConverter(Converter<Name, TypeReference> utilBaseConverter,
			Converter<Name, ModuleReference> toModuleReferenceConverter, ModulesFactory modulesFactory,
			ModifiersFactory modifiersFactory, UtilLayout layoutInformationConverter, JdtResolver jdtResolverUtility) {
		this.modulesFactory = modulesFactory;
		this.modifiersFactory = modifiersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.jdtResolverUtility = jdtResolverUtility;
		this.utilBaseConverter = utilBaseConverter;
		this.toModuleReferenceConverter = toModuleReferenceConverter;
	}

	@Override
	public tools.mdsd.jamopp.model.java.modules.ModuleDirective convert(ModuleDirective directive) {
		if (directive.getNodeType() == ASTNode.REQUIRES_DIRECTIVE) {
			return handleRequiresDirective(directive);
		} else if (directive.getNodeType() == ASTNode.EXPORTS_DIRECTIVE
				|| directive.getNodeType() == ASTNode.OPENS_DIRECTIVE) {
			return handlesExportsAndOpensDirective(directive);
		} else if (directive.getNodeType() == ASTNode.PROVIDES_DIRECTIVE) {
			return handleProvidesDirective(directive);
		} else {
			return handleUsesDirective(directive);
		}
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.modules.ModuleDirective handlesExportsAndOpensDirective(
			ModuleDirective directive) {
		ModulePackageAccess accessDir = (ModulePackageAccess) directive;
		tools.mdsd.jamopp.model.java.modules.AccessProvidingModuleDirective convertedDir;
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

	private tools.mdsd.jamopp.model.java.modules.ModuleDirective handleUsesDirective(ModuleDirective directive) {
		UsesDirective usDir = (UsesDirective) directive;
		tools.mdsd.jamopp.model.java.modules.UsesModuleDirective result = modulesFactory.createUsesModuleDirective();
		result.setTypeReference(utilBaseConverter.convert(usDir.getName()));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, directive);
		return result;
	}

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.modules.ModuleDirective handleRequiresDirective(ModuleDirective directive) {
		RequiresDirective reqDir = (RequiresDirective) directive;
		tools.mdsd.jamopp.model.java.modules.RequiresModuleDirective result = modulesFactory
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

	@SuppressWarnings("unchecked")
	private tools.mdsd.jamopp.model.java.modules.ModuleDirective handleProvidesDirective(ModuleDirective directive) {
		ProvidesDirective provDir = (ProvidesDirective) directive;
		tools.mdsd.jamopp.model.java.modules.ProvidesModuleDirective result = modulesFactory
				.createProvidesModuleDirective();
		result.setTypeReference(utilBaseConverter.convert(provDir.getName()));
		provDir.implementations()
				.forEach(obj -> result.getServiceProviders().add(utilBaseConverter.convert((Name) obj)));
		layoutInformationConverter.convertToMinimalLayoutInformation(result, directive);
		return result;
	}

}
