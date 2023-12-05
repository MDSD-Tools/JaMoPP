package jamopp.parser.jdt.converter.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import jamopp.parser.jdt.converter.implementation.ToAnnotationValueConverter;
import jamopp.parser.jdt.converter.interfaces.BlockToBlockConverter;
import jamopp.parser.jdt.converter.interfaces.StatementToStatementConverter;
import jamopp.parser.jdt.converter.interfaces.ToExpressionConverter;

@Singleton
public class UtilTypeInstructionSeparation implements IUtilTypeInstructionSeparation {

	private final UtilJdtResolver jdtResolverUtility;
	private final ToExpressionConverter expressionConverterUtility;
	private final ToAnnotationValueConverter toAnnotationValueConverter;
	private final BlockToBlockConverter blockToBlockConverter;
	private final StatementToStatementConverter statementToStatementConverter;

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
	UtilTypeInstructionSeparation(ToAnnotationValueConverter toAnnotationValueConverter,
			StatementToStatementConverter statementToStatementConverter, UtilJdtResolver jdtResolverUtility,
			ToExpressionConverter expressionConverterUtility, BlockToBlockConverter blockToBlockConverter) {
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toAnnotationValueConverter = toAnnotationValueConverter;
		this.blockToBlockConverter = blockToBlockConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void convertAll() {
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
				m.setStatement(blockToBlockConverter.convert(b));
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
				c.setBlock(blockToBlockConverter.convert(b));
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
				f.setInitialValue(expressionConverterUtility.convert(expr));
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
				f.setInitialValue(expressionConverterUtility.convert(expr));
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
				b1.statements()
						.forEach(obj -> b2.getStatements().add(statementToStatementConverter.convert((Statement) obj)));
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
				m.setDefaultValue(toAnnotationValueConverter.convert(expr));
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
				sap.setValue(toAnnotationValueConverter.convert(expr));
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
				aas.setValue(toAnnotationValueConverter.convert(expr));
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

	@Override
	public void addMethod(Block block, org.emftext.language.java.members.Method method) {
		methods.put(block, method);
	}

	@Override
	public void addConstructor(Block block, org.emftext.language.java.members.Constructor constructor) {
		constructors.put(block, constructor);
	}

	@Override
	public void addField(Expression initializer, org.emftext.language.java.members.Field field) {
		fields.put(initializer, field);
	}

	@Override
	public void addAdditionalField(Expression initializer, org.emftext.language.java.members.AdditionalField field) {
		addFields.put(initializer, field);
	}

	@Override
	public void addInitializer(Block block, org.emftext.language.java.statements.Block correspondingBlock) {
		initializers.put(block, correspondingBlock);
	}

	@Override
	public void addAnnotationMethod(Expression value, org.emftext.language.java.members.InterfaceMethod method) {
		annotationMethods.put(value, method);
	}

	@Override
	public void addSingleAnnotationParameter(Expression value,
			org.emftext.language.java.annotations.SingleAnnotationParameter param) {
		singleAnnotations.put(value, param);
	}

	@Override
	public void addAnnotationAttributeSetting(Expression value,
			org.emftext.language.java.annotations.AnnotationAttributeSetting setting) {
		annotationSetting.put(value, setting);
	}

}
