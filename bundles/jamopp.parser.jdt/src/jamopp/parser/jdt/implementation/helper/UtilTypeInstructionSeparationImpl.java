package jamopp.parser.jdt.implementation.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.emftext.language.java.annotations.AnnotationValue;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import jamopp.parser.jdt.interfaces.converter.Converter;
import jamopp.parser.jdt.interfaces.helper.UtilJdtResolver;
import jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;

@Singleton
public class UtilTypeInstructionSeparationImpl implements UtilTypeInstructionSeparation {

	private final UtilJdtResolver jdtResolverUtility;
	private final Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Expression, AnnotationValue> toAnnotationValueConverter;
	private final Converter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter;
	private final Converter<Statement, org.emftext.language.java.statements.Statement> statementToStatementConverter;

	private final HashMap<Block, org.emftext.language.java.members.Method> methods = new HashMap<>();
	private final HashMap<Block, org.emftext.language.java.members.Constructor> constructors = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.members.Field> fields = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.members.AdditionalField> addFields = new HashMap<>();
	private final HashMap<Block, org.emftext.language.java.statements.Block> initializers = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.members.InterfaceMethod> annotationMethods = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.annotations.SingleAnnotationParameter> singleAnnotations = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.annotations.AnnotationAttributeSetting> annotationSetting = new HashMap<>();
	private final HashSet<EObject> visitedObjects = new HashSet<>();

	@Inject
	UtilTypeInstructionSeparationImpl(Converter<Expression, AnnotationValue> toAnnotationValueConverter,
			Converter<Statement, org.emftext.language.java.statements.Statement> statementToStatementConverter,
			UtilJdtResolver jdtResolverUtility,
			Converter<org.eclipse.jdt.core.dom.Expression, org.emftext.language.java.expressions.Expression> expressionConverterUtility,
			Converter<Block, org.emftext.language.java.statements.Block> blockToBlockConverter) {
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toAnnotationValueConverter = toAnnotationValueConverter;
		this.blockToBlockConverter = blockToBlockConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@Override
	public void convertAll() {
		int oldSize;
		int newSize = this.methods.size() + this.constructors.size() + this.fields.size() + this.addFields.size()
				+ this.initializers.size() + this.annotationMethods.size() + this.singleAnnotations.size()
				+ this.annotationSetting.size();
		do {
			oldSize = newSize;
			handleMethods();
			handleConstructors();
			handleFields();
			handleAddFields();
			handleInitializers();
			handleAnnotationMethods();
			handleSingleAnnotations();
			handleAnnotationSetting();
			newSize = this.methods.size() + this.constructors.size() + this.fields.size() + this.addFields.size()
					+ this.initializers.size() + this.annotationMethods.size() + this.singleAnnotations.size()
					+ this.annotationSetting.size();
		} while (newSize != oldSize);

		this.methods.clear();
		this.constructors.clear();
		this.fields.clear();
		this.addFields.clear();
		this.initializers.clear();
		this.annotationMethods.clear();
		this.singleAnnotations.clear();
		this.annotationSetting.clear();
		this.visitedObjects.clear();
	}

	@SuppressWarnings("unchecked")
	private void handleAnnotationSetting() {
		Iterator<Expression> exprIter;
		HashMap<Expression, org.emftext.language.java.annotations.AnnotationAttributeSetting> clonedAnnotationSetting = (HashMap<Expression, org.emftext.language.java.annotations.AnnotationAttributeSetting>) this.annotationSetting
				.clone();
		exprIter = clonedAnnotationSetting.keySet().iterator();
		while (exprIter.hasNext()) {
			if (this.visitedObjects.contains(clonedAnnotationSetting.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clonedAnnotationSetting.forEach((expr, aas) -> {
			this.visitedObjects.add(aas);
			aas.setValue(this.toAnnotationValueConverter.convert(expr));
		});
	}

	@SuppressWarnings("unchecked")
	private void handleSingleAnnotations() {
		Iterator<Expression> exprIter;
		HashMap<Expression, org.emftext.language.java.annotations.SingleAnnotationParameter> clonedSingleAnnotations = (HashMap<Expression, org.emftext.language.java.annotations.SingleAnnotationParameter>) this.singleAnnotations
				.clone();
		exprIter = clonedSingleAnnotations.keySet().iterator();
		while (exprIter.hasNext()) {
			if (this.visitedObjects.contains(clonedSingleAnnotations.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clonedSingleAnnotations.forEach((expr, sap) -> {
			this.visitedObjects.add(sap);
			sap.setValue(this.toAnnotationValueConverter.convert(expr));
		});
	}

	@SuppressWarnings("unchecked")
	private void handleAnnotationMethods() {
		Iterator<Expression> exprIter;
		HashMap<Expression, org.emftext.language.java.members.InterfaceMethod> clonedAnnotationMethods = (HashMap<Expression, org.emftext.language.java.members.InterfaceMethod>) this.annotationMethods
				.clone();
		exprIter = clonedAnnotationMethods.keySet().iterator();
		while (exprIter.hasNext()) {
			if (this.visitedObjects.contains(clonedAnnotationMethods.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clonedAnnotationMethods.forEach((expr, m) -> {
			this.visitedObjects.add(m);
			m.setDefaultValue(this.toAnnotationValueConverter.convert(expr));
		});
	}

	@SuppressWarnings("unchecked")
	private void handleInitializers() {
		Iterator<Block> iter;
		HashMap<Block, org.emftext.language.java.statements.Block> clonedInitializers = (HashMap<Block, org.emftext.language.java.statements.Block>) this.initializers
				.clone();
		iter = clonedInitializers.keySet().iterator();
		while (iter.hasNext()) {
			if (this.visitedObjects.contains(clonedInitializers.get(iter.next()))) {
				iter.remove();
			}
		}
		clonedInitializers.forEach((b1, b2) -> {
			this.visitedObjects.add(b2);
			this.jdtResolverUtility.prepareNextUid();
			b1.statements().forEach(
					obj -> b2.getStatements().add(this.statementToStatementConverter.convert((Statement) obj)));
		});
	}

	@SuppressWarnings("unchecked")
	private void handleAddFields() {
		Iterator<Expression> exprIter;
		HashMap<Expression, org.emftext.language.java.members.AdditionalField> clonedAddFields = (HashMap<Expression, org.emftext.language.java.members.AdditionalField>) this.addFields
				.clone();
		exprIter = clonedAddFields.keySet().iterator();
		while (exprIter.hasNext()) {
			if (this.visitedObjects.contains(clonedAddFields.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clonedAddFields.forEach((expr, f) -> {
			this.visitedObjects.add(f);
			f.setInitialValue(this.expressionConverterUtility.convert(expr));
		});
	}

	@SuppressWarnings("unchecked")
	private void handleFields() {
		HashMap<Expression, org.emftext.language.java.members.Field> clonedFields = (HashMap<Expression, org.emftext.language.java.members.Field>) this.fields
				.clone();
		Iterator<Expression> exprIter = clonedFields.keySet().iterator();
		while (exprIter.hasNext()) {
			if (this.visitedObjects.contains(clonedFields.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clonedFields.forEach((expr, f) -> {
			this.visitedObjects.add(f);
			f.setInitialValue(this.expressionConverterUtility.convert(expr));
		});
	}

	@SuppressWarnings("unchecked")
	private void handleConstructors() {
		Iterator<Block> iter;
		HashMap<Block, org.emftext.language.java.members.Constructor> clonedConstructors = (HashMap<Block, org.emftext.language.java.members.Constructor>) this.constructors
				.clone();
		iter = clonedConstructors.keySet().iterator();
		while (iter.hasNext()) {
			if (this.visitedObjects.contains(clonedConstructors.get(iter.next()))) {
				iter.remove();
			}
		}
		clonedConstructors.forEach((b, c) -> {
			this.visitedObjects.add(c);
			c.setBlock(this.blockToBlockConverter.convert(b));
		});
	}

	@SuppressWarnings("unchecked")
	private void handleMethods() {
		HashMap<Block, org.emftext.language.java.members.Method> clonedMethods = (HashMap<Block, org.emftext.language.java.members.Method>) this.methods
				.clone();
		Iterator<Block> iter = clonedMethods.keySet().iterator();
		while (iter.hasNext()) {
			if (this.visitedObjects.contains(clonedMethods.get(iter.next()))) {
				iter.remove();
			}
		}
		clonedMethods.forEach((b, m) -> {
			this.visitedObjects.add(m);
			m.setStatement(this.blockToBlockConverter.convert(b));
		});
	}

	@Override
	public void addMethod(Block block, org.emftext.language.java.members.Method method) {
		this.methods.put(block, method);
	}

	@Override
	public void addConstructor(Block block, org.emftext.language.java.members.Constructor constructor) {
		this.constructors.put(block, constructor);
	}

	@Override
	public void addField(Expression initializer, org.emftext.language.java.members.Field field) {
		this.fields.put(initializer, field);
	}

	@Override
	public void addAdditionalField(Expression initializer, org.emftext.language.java.members.AdditionalField field) {
		this.addFields.put(initializer, field);
	}

	@Override
	public void addInitializer(Block block, org.emftext.language.java.statements.Block correspondingBlock) {
		this.initializers.put(block, correspondingBlock);
	}

	@Override
	public void addAnnotationMethod(Expression value, org.emftext.language.java.members.InterfaceMethod method) {
		this.annotationMethods.put(value, method);
	}

	@Override
	public void addSingleAnnotationParameter(Expression value,
			org.emftext.language.java.annotations.SingleAnnotationParameter param) {
		this.singleAnnotations.put(value, param);
	}

	@Override
	public void addAnnotationAttributeSetting(Expression value,
			org.emftext.language.java.annotations.AnnotationAttributeSetting setting) {
		this.annotationSetting.put(value, setting);
	}

}
