package jamopp.parser.jdt.implementation.converter;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.emftext.language.java.classifiers.ConcreteClassifier;
import org.emftext.language.java.containers.ContainersFactory;
import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.converter.Converter;

public class ToCompilationUnitConverter
		implements Converter<CompilationUnit, org.emftext.language.java.containers.CompilationUnit> {

	private final ContainersFactory containersFactory;
	private final Converter<AbstractTypeDeclaration, ConcreteClassifier> ClassifierConverterUtility;

	@Inject
	public ToCompilationUnitConverter(ContainersFactory containersFactory,
			Converter<AbstractTypeDeclaration, ConcreteClassifier> classifierConverterUtility) {
		this.containersFactory = containersFactory;
		this.ClassifierConverterUtility = classifierConverterUtility;
	}

	@SuppressWarnings("unchecked")
	@Override
	public org.emftext.language.java.containers.CompilationUnit convert(CompilationUnit cu) {
		org.emftext.language.java.containers.CompilationUnit result = containersFactory.createCompilationUnit();
		result.setName("");
		cu.types().forEach(
				obj -> result.getClassifiers().add(ClassifierConverterUtility.convert((AbstractTypeDeclaration) obj)));
		return result;
	}

}
