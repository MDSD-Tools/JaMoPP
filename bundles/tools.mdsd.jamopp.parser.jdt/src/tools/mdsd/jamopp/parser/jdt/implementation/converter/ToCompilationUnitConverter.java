package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import tools.mdsd.jamopp.model.java.classifiers.ConcreteClassifier;
import tools.mdsd.jamopp.model.java.containers.ContainersFactory;

import com.google.inject.Inject;

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
	public tools.mdsd.jamopp.model.java.containers.CompilationUnit convert(CompilationUnit cu) {
		tools.mdsd.jamopp.model.java.containers.CompilationUnit result = this.containersFactory.createCompilationUnit();
		result.setName("");
		this.layoutInformationConverter.convertJavaRootLayoutInformation(result, cu, this.visitor.getSource());
		cu.types().forEach(obj -> result.getClassifiers()
				.add(this.classifierConverterUtility.convert((AbstractTypeDeclaration) obj)));
		return result;
	}

}
