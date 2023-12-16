package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.ContainersFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.IUtilLayout;
import jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

public class ToCompilationUnitConverter
		implements Converter<CompilationUnit, org.emftext.language.java.containers.CompilationUnit> {

	private final ContainersFactory containersFactory;
	private final IUtilLayout layoutInformationConverter;
	private final AbstractVisitor visitor;
	private final Converter<AbstractTypeDeclaration, ConcreteClassifier> ClassifierConverterUtility;

	@Inject
	public ToCompilationUnitConverter(AbstractVisitor visitor, IUtilLayout layoutInformationConverter,
			ContainersFactory containersFactory,
			Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility) {
		this.containersFactory = containersFactory;
		this.layoutInformationConverter = layoutInformationConverter;
		this.ClassifierConverterUtility = classifierConverterUtility;
		this.visitor = visitor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.containers.CompilationUnit convert(CompilationUnit cu) {
		org.emftext.language.java.containers.CompilationUnit result = containersFactory.createCompilationUnit();
		result.setName("");
		layoutInformationConverter.convertJavaRootLayoutInformation(result, cu, visitor.getSource());
		cu.types().forEach(
				obj -> result.getClassifiers().add(ClassifierConverterUtility.convert((AbstractTypeDeclaration) obj)));
		return result;
	}

}
