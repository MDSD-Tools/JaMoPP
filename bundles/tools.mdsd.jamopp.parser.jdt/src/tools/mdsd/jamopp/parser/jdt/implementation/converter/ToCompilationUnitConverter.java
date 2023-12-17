package tools.mdsd.jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.ContainersFactory;

import com.google.inject.Inject;

import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilLayout;
import tools.mdsd.jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

public class ToCompilationUnitConverter
implements Converter<CompilationUnit, org.emftext.language.java.containers.CompilationUnit> {

	private final ContainersFactory containersFactory;
	private final UtilLayout layoutInformationConverter;
	private final AbstractVisitor visitor;
	private final Converter<AbstractTypeDeclaration, ConcreteClassifier> ClassifierConverterUtility;

	@Inject
	public ToCompilationUnitConverter(AbstractVisitor visitor, UtilLayout layoutInformationConverter,
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
		org.emftext.language.java.containers.CompilationUnit result = this.containersFactory.createCompilationUnit();
		result.setName("");
		this.layoutInformationConverter.convertJavaRootLayoutInformation(result, cu, this.visitor.getSource());
		cu.types().forEach(obj -> result.getClassifiers()
				.add(this.ClassifierConverterUtility.convert((AbstractTypeDeclaration) obj)));
		return result;
	}

}
