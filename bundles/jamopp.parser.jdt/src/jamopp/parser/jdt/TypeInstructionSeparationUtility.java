package jamopp.parser.jdt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;

class TypeInstructionSeparationUtility {

	private StatementConverterUtility statementConverterUtility;
	private final JDTResolverUtility jdtResolverUtility;
	private final ExpressionConverterUtility expressionConverterUtility;
	private final AnnotationInstanceOrModifierConverterUtility annotationInstanceOrModifierConverterUtility;

	private final HashMap<Block, org.emftext.language.java.members.Method> methods = new HashMap<>();
	private final HashMap<Block, org.emftext.language.java.members.Constructor> constructors = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.members.Field> fields = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.members.AdditionalField> addFields = new HashMap<>();
	private final HashMap<Block, org.emftext.language.java.statements.Block> initializers = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.members.InterfaceMethod> annotationMethods = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.annotations.SingleAnnotationParameter> singleAnnotations = new HashMap<>();
	private final HashMap<Expression, org.emftext.language.java.annotations.AnnotationAttributeSetting> annotationSetting = new HashMap<>();
	private final HashSet<EObject> visitedObjects = new HashSet<>();

	TypeInstructionSeparationUtility(JDTResolverUtility jdtResolverUtility,
			ExpressionConverterUtility expressionConverterUtility,
			AnnotationInstanceOrModifierConverterUtility annotationInstanceOrModifierConverterUtility) {

		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.annotationInstanceOrModifierConverterUtility = annotationInstanceOrModifierConverterUtility;
	}

	void addMethod(Block block, org.emftext.language.java.members.Method method) {
		methods.put(block, method);
	}

	void addConstructor(Block block, org.emftext.language.java.members.Constructor constructor) {
		constructors.put(block, constructor);
	}

	void addField(Expression initializer, org.emftext.language.java.members.Field field) {
		fields.put(initializer, field);
	}

	void addAdditionalField(Expression initializer, org.emftext.language.java.members.AdditionalField field) {
		addFields.put(initializer, field);
	}

	void addInitializer(Block block, org.emftext.language.java.statements.Block correspondingBlock) {
		initializers.put(block, correspondingBlock);
	}

	void addAnnotationMethod(Expression value, org.emftext.language.java.members.InterfaceMethod method) {
		annotationMethods.put(value, method);
	}

	void addSingleAnnotationParameter(Expression value,
			org.emftext.language.java.annotations.SingleAnnotationParameter param) {
		singleAnnotations.put(value, param);
	}

	void addAnnotationAttributeSetting(Expression value,
			org.emftext.language.java.annotations.AnnotationAttributeSetting setting) {
		annotationSetting.put(value, setting);
	}

	@SuppressWarnings("unchecked")
	void convertAll() {
		int oldSize;
		int newSize = methods.size() + constructors.size() + fields.size() + addFields.size() + initializers.size()
				+ annotationMethods.size() + singleAnnotations.size() + annotationSetting.size();
		do {
			oldSize = newSize;
			HashMap<Block, org.emftext.language.java.members.Method> clonedMethods = (HashMap<Block, org.emftext.language.java.members.Method>) methods
					.clone();
			Iterator<Block> iter = clonedMethods.keySet().iterator();
			while (iter.hasNext()) {
				if (visitedObjects.contains(clonedMethods.get(iter.next()))) {
					iter.remove();
				}
			}
			clonedMethods.forEach((b, m) -> {
				visitedObjects.add(m);
				m.setStatement(statementConverterUtility.convertToBlock(b));
			});
			HashMap<Block, org.emftext.language.java.members.Constructor> clonedConstructors = (HashMap<Block, org.emftext.language.java.members.Constructor>) constructors
					.clone();
			iter = clonedConstructors.keySet().iterator();
			while (iter.hasNext()) {
				if (visitedObjects.contains(clonedConstructors.get(iter.next()))) {
					iter.remove();
				}
			}
			clonedConstructors.forEach((b, c) -> {
				visitedObjects.add(c);
				c.setBlock(statementConverterUtility.convertToBlock(b));
			});
			HashMap<Expression, org.emftext.language.java.members.Field> clonedFields = (HashMap<Expression, org.emftext.language.java.members.Field>) fields
					.clone();
			Iterator<Expression> exprIter = clonedFields.keySet().iterator();
			while (exprIter.hasNext()) {
				if (visitedObjects.contains(clonedFields.get(exprIter.next()))) {
					exprIter.remove();
				}
			}
			clonedFields.forEach((expr, f) -> {
				visitedObjects.add(f);
				f.setInitialValue(expressionConverterUtility.convertToExpression(expr));
			});
			HashMap<Expression, org.emftext.language.java.members.AdditionalField> clonedAddFields = (HashMap<Expression, org.emftext.language.java.members.AdditionalField>) addFields
					.clone();
			exprIter = clonedAddFields.keySet().iterator();
			while (exprIter.hasNext()) {
				if (visitedObjects.contains(clonedAddFields.get(exprIter.next()))) {
					exprIter.remove();
				}
			}
			clonedAddFields.forEach((expr, f) -> {
				visitedObjects.add(f);
				f.setInitialValue(expressionConverterUtility.convertToExpression(expr));
			});
			HashMap<Block, org.emftext.language.java.statements.Block> clonedInitializers = (HashMap<Block, org.emftext.language.java.statements.Block>) initializers
					.clone();
			iter = clonedInitializers.keySet().iterator();
			while (iter.hasNext()) {
				if (visitedObjects.contains(clonedInitializers.get(iter.next()))) {
					iter.remove();
				}
			}
			clonedInitializers.forEach((b1, b2) -> {
				visitedObjects.add(b2);
				jdtResolverUtility.prepareNextUid();
				b1.statements().forEach(
						obj -> b2.getStatements().add(statementConverterUtility.convertToStatement((Statement) obj)));
			});
			HashMap<Expression, org.emftext.language.java.members.InterfaceMethod> clonedAnnotationMethods = (HashMap<Expression, org.emftext.language.java.members.InterfaceMethod>) annotationMethods
					.clone();
			exprIter = clonedAnnotationMethods.keySet().iterator();
			while (exprIter.hasNext()) {
				if (visitedObjects.contains(clonedAnnotationMethods.get(exprIter.next()))) {
					exprIter.remove();
				}
			}
			clonedAnnotationMethods.forEach((expr, m) -> {
				visitedObjects.add(m);
				m.setDefaultValue(annotationInstanceOrModifierConverterUtility.convertToAnnotationValue(expr));
			});
			HashMap<Expression, org.emftext.language.java.annotations.SingleAnnotationParameter> clonedSingleAnnotations = (HashMap<Expression, org.emftext.language.java.annotations.SingleAnnotationParameter>) singleAnnotations
					.clone();
			exprIter = clonedSingleAnnotations.keySet().iterator();
			while (exprIter.hasNext()) {
				if (visitedObjects.contains(clonedSingleAnnotations.get(exprIter.next()))) {
					exprIter.remove();
				}
			}
			clonedSingleAnnotations.forEach((expr, sap) -> {
				visitedObjects.add(sap);
				sap.setValue(annotationInstanceOrModifierConverterUtility.convertToAnnotationValue(expr));
			});
			HashMap<Expression, org.emftext.language.java.annotations.AnnotationAttributeSetting> clonedAnnotationSetting = (HashMap<Expression, org.emftext.language.java.annotations.AnnotationAttributeSetting>) annotationSetting
					.clone();
			exprIter = clonedAnnotationSetting.keySet().iterator();
			while (exprIter.hasNext()) {
				if (visitedObjects.contains(clonedAnnotationSetting.get(exprIter.next()))) {
					exprIter.remove();
				}
			}
			clonedAnnotationSetting.forEach((expr, aas) -> {
				visitedObjects.add(aas);
				aas.setValue(annotationInstanceOrModifierConverterUtility.convertToAnnotationValue(expr));
			});
			newSize = methods.size() + constructors.size() + fields.size() + addFields.size() + initializers.size()
					+ annotationMethods.size() + singleAnnotations.size() + annotationSetting.size();
		} while (newSize != oldSize);

		methods.clear();
		constructors.clear();
		fields.clear();
		addFields.clear();
		initializers.clear();
		annotationMethods.clear();
		singleAnnotations.clear();
		annotationSetting.clear();
		visitedObjects.clear();
	}
	
	void setStatementConverterUtility(StatementConverterUtility statementConverterUtility) {
		this.statementConverterUtility = statementConverterUtility;
	}
}
