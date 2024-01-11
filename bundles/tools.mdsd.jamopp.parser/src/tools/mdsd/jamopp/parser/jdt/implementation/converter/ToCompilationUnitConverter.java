package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import javax.inject.Inject;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;

import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

public class ToCompilationUnitConverter
		implements Converter<CompilationUnit, tools.mdsd.jamopp.model.java.containers.CompilationUnit> {

	private final ContainersFactory containersFactory;
	private final UtilLayout layoutInformationConverter;
	private final AbstractVisitor visitor;
	private final Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility;

	@Inject
	public ToCompilationUnitConverter(AbstractVisitor visitor, UtilLayout layoutInformationConverter,
			ContainersFactory containersFactory,
			Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility) {
		this.containersFactory = containersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.classifierConverterUtility = classifierConverterUtility;
		this.visitor = visitor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public tools.mdsd.jamopp.model.java.containers.CompilationUnit convert(CompilationUnit unit) {
		tools.mdsd.jamopp.model.java.containers.CompilationUnit result = containersFactory.createCompilationUnit();
		result.setName("");
		layoutInformationConverter.convertJavaRootLayoutInformation(result, unit, visitor.getSource());
		unit.types().forEach(
				obj -> result.getClassifiers().add(classifierConverterUtility.convert((AbstractTypeDeclaration) obj)));
		return result;
	}

}
