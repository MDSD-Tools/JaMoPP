package tools.mdsd.jamopp.parser.jdt.implementation.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;

import com.google.inject.Singleton;

import tools.mdsd.jamopp.model.java.annotations.AnnotationAttributeSetting;
import tools.mdsd.jamopp.model.java.annotations.AnnotationValue;
import tools.mdsd.jamopp.model.java.annotations.SingleAnnotationParameter;
import tools.mdsd.jamopp.model.java.members.AdditionalField;
import tools.mdsd.jamopp.model.java.members.InterfaceMethod;
import tools.mdsd.jamopp.parser.jdt.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.jdt.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.jdt.interfaces.resolver.JdtResolver;

@Singleton
public class UtilTypeInstructionSeparationImpl implements UtilTypeInstructionSeparation {

	private final JdtResolver jdtResolverUtility;
	private final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility;
	private final Converter<Expression, AnnotationValue> toAnnotationValueConverter;
	private final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter;
	private final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter;

	private final Map<Block, tools.mdsd.jamopp.model.java.members.Method> methods = new ConcurrentHashMap<>();
	private final Map<Block, tools.mdsd.jamopp.model.java.members.Constructor> constructors = new ConcurrentHashMap<>();
	private final Map<Expression, tools.mdsd.jamopp.model.java.members.Field> fields = new ConcurrentHashMap<>();
	private final Map<Expression, AdditionalField> addFields = new ConcurrentHashMap<>();
	private final Map<Block, tools.mdsd.jamopp.model.java.statements.Block> initializers = new ConcurrentHashMap<>();
	private final Map<Expression, InterfaceMethod> annotationMethods = new ConcurrentHashMap<>();
	private final Map<Expression, SingleAnnotationParameter> singleAnnotations = new ConcurrentHashMap<>();
	private final Map<Expression, AnnotationAttributeSetting> annotationSetting = new ConcurrentHashMap<>();
	private final Set<EObject> visitedObjects = new HashSet<>();

	@Inject
	public UtilTypeInstructionSeparationImpl(Converter<Expression, AnnotationValue> toAnnotationValueConverter,
			Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter,
			JdtResolver jdtResolverUtility,
			Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter) {
		this.jdtResolverUtility = jdtResolverUtility;
		this.expressionConverterUtility = expressionConverterUtility;
		this.toAnnotationValueConverter = toAnnotationValueConverter;
		this.blockToBlockConverter = blockToBlockConverter;
		this.statementToStatementConverter = statementToStatementConverter;
	}

	@Override
	public void convertAll() {
		int oldSize;
		int newSize = methods.size() + constructors.size() + fields.size() + addFields.size() + initializers.size()
				+ annotationMethods.size() + singleAnnotations.size() + annotationSetting.size();
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

	private void handleAnnotationSetting() {
		Iterator<Expression> exprIter;
		HashMap<Expression, AnnotationAttributeSetting> clonedAnnotationSetting = new HashMap<>(annotationSetting);
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
	}

	private void handleSingleAnnotations() {
		Iterator<Expression> exprIter;
		HashMap<Expression, SingleAnnotationParameter> clonedSingleAnnotations = new HashMap<>(singleAnnotations);
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
	}

	private void handleAnnotationMethods() {
		Iterator<Expression> exprIter;
		HashMap<Expression, InterfaceMethod> clonedAnnotationMethods = new HashMap<>(annotationMethods);
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
	}

	@SuppressWarnings("unchecked")
	private void handleInitializers() {
		Iterator<Block> iter;
		HashMap<Block, tools.mdsd.jamopp.model.java.statements.Block> clonedInitializers = new HashMap<>(initializers);
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
	}

	private void handleAddFields() {
		Iterator<Expression> exprIter;
		HashMap<Expression, AdditionalField> clonedAddFields = new HashMap<>(addFields);
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
	}

	private void handleFields() {
		HashMap<Expression, tools.mdsd.jamopp.model.java.members.Field> clonedFields = new HashMap<>(fields);
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
	}

	private void handleConstructors() {
		Iterator<Block> iter;
		HashMap<Block, tools.mdsd.jamopp.model.java.members.Constructor> clonedConstructors = new HashMap<>(
				constructors);
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
	}

	private void handleMethods() {
		HashMap<Block, tools.mdsd.jamopp.model.java.members.Method> clonedMethods = new HashMap<>(methods);
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
	}

	@Override
	public void addMethod(Block block, tools.mdsd.jamopp.model.java.members.Method method) {
		methods.put(block, method);
	}

	@Override
	public void addConstructor(Block block, tools.mdsd.jamopp.model.java.members.Constructor constructor) {
		constructors.put(block, constructor);
	}

	@Override
	public void addField(Expression initializer, tools.mdsd.jamopp.model.java.members.Field field) {
		fields.put(initializer, field);
	}

	@Override
	public void addAdditionalField(Expression initializer, AdditionalField field) {
		addFields.put(initializer, field);
	}

	@Override
	public void addInitializer(Block block, tools.mdsd.jamopp.model.java.statements.Block correspondingBlock) {
		initializers.put(block, correspondingBlock);
	}

	@Override
	public void addAnnotationMethod(Expression value, InterfaceMethod method) {
		annotationMethods.put(value, method);
	}

	@Override
	public void addSingleAnnotationParameter(Expression value, SingleAnnotationParameter param) {
		singleAnnotations.put(value, param);
	}

	@Override
	public void addAnnotationAttributeSetting(Expression value, AnnotationAttributeSetting setting) {
		annotationSetting.put(value, setting);
	}

}
