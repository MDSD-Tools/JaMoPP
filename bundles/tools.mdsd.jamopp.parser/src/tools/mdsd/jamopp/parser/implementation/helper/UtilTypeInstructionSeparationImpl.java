package tools.mdsd.jamopp.parser.implementation.helper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.inject.Inject;

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
import tools.mdsd.jamopp.parser.interfaces.converter.Converter;
import tools.mdsd.jamopp.parser.interfaces.helper.UtilTypeInstructionSeparation;
import tools.mdsd.jamopp.parser.interfaces.resolver.JdtResolver;

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
	public UtilTypeInstructionSeparationImpl(final Converter<Expression, AnnotationValue> toAnnotationValueConverter,
			final Converter<Statement, tools.mdsd.jamopp.model.java.statements.Statement> statementToStatementConverter,
			final JdtResolver jdtResolverUtility,
			final Converter<Expression, tools.mdsd.jamopp.model.java.expressions.Expression> expressionConverterUtility,
			final Converter<Block, tools.mdsd.jamopp.model.java.statements.Block> blockToBlockConverter) {
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
		final HashMap<Expression, AnnotationAttributeSetting> clone = new HashMap<>(annotationSetting);
		final Iterator<Expression> exprIter = clone.keySet().iterator();
		while (exprIter.hasNext()) {
			if (visitedObjects.contains(clone.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clone.forEach((expr, aas) -> {
			visitedObjects.add(aas);
			aas.setValue(toAnnotationValueConverter.convert(expr));
		});
	}

	private void handleSingleAnnotations() {
		final HashMap<Expression, SingleAnnotationParameter> clone = new HashMap<>(singleAnnotations);
		final Iterator<Expression> exprIter = clone.keySet().iterator();
		while (exprIter.hasNext()) {
			if (visitedObjects.contains(clone.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clone.forEach((expr, sap) -> {
			visitedObjects.add(sap);
			sap.setValue(toAnnotationValueConverter.convert(expr));
		});
	}

	private void handleAnnotationMethods() {
		final HashMap<Expression, InterfaceMethod> clone = new HashMap<>(annotationMethods);
		final Iterator<Expression> exprIter = clone.keySet().iterator();
		while (exprIter.hasNext()) {
			if (visitedObjects.contains(clone.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clone.forEach((expr, m) -> {
			visitedObjects.add(m);
			m.setDefaultValue(toAnnotationValueConverter.convert(expr));
		});
	}

	@SuppressWarnings("unchecked")
	private void handleInitializers() {
		final HashMap<Block, tools.mdsd.jamopp.model.java.statements.Block> clone = new HashMap<>(initializers);
		final Iterator<Block> iter = clone.keySet().iterator();
		while (iter.hasNext()) {
			if (visitedObjects.contains(clone.get(iter.next()))) {
				iter.remove();
			}
		}
		clone.forEach((b1, b2) -> {
			visitedObjects.add(b2);
			jdtResolverUtility.prepareNextUid();
			b1.statements()
					.forEach(obj -> b2.getStatements().add(statementToStatementConverter.convert((Statement) obj)));
		});
	}

	private void handleAddFields() {
		final HashMap<Expression, AdditionalField> clone = new HashMap<>(addFields);
		final Iterator<Expression> exprIter = clone.keySet().iterator();
		while (exprIter.hasNext()) {
			if (visitedObjects.contains(clone.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clone.forEach((expr, f) -> {
			visitedObjects.add(f);
			f.setInitialValue(expressionConverterUtility.convert(expr));
		});
	}

	private void handleFields() {
		final HashMap<Expression, tools.mdsd.jamopp.model.java.members.Field> clone = new HashMap<>(fields);
		final Iterator<Expression> exprIter = clone.keySet().iterator();
		while (exprIter.hasNext()) {
			if (visitedObjects.contains(clone.get(exprIter.next()))) {
				exprIter.remove();
			}
		}
		clone.forEach((expr, f) -> {
			visitedObjects.add(f);
			f.setInitialValue(expressionConverterUtility.convert(expr));
		});
	}

	private void handleConstructors() {
		final HashMap<Block, tools.mdsd.jamopp.model.java.members.Constructor> clone = new HashMap<>(constructors);
		final Iterator<Block> iter = clone.keySet().iterator();
		while (iter.hasNext()) {
			if (visitedObjects.contains(clone.get(iter.next()))) {
				iter.remove();
			}
		}
		clone.forEach((b, c) -> {
			visitedObjects.add(c);
			c.setBlock(blockToBlockConverter.convert(b));
		});
	}

	private void handleMethods() {
		final HashMap<Block, tools.mdsd.jamopp.model.java.members.Method> clone = new HashMap<>(methods);
		final Iterator<Block> iter = clone.keySet().iterator();
		while (iter.hasNext()) {
			if (visitedObjects.contains(clone.get(iter.next()))) {
				iter.remove();
			}
		}
		clone.forEach((b, m) -> {
			visitedObjects.add(m);
			m.setStatement(blockToBlockConverter.convert(b));
		});
	}

	@Override
	public void addMethod(final Block block, final tools.mdsd.jamopp.model.java.members.Method method) {
		methods.put(block, method);
	}

	@Override
	public void addConstructor(final Block block, final tools.mdsd.jamopp.model.java.members.Constructor constructor) {
		constructors.put(block, constructor);
	}

	@Override
	public void addField(final Expression initializer, final tools.mdsd.jamopp.model.java.members.Field field) {
		fields.put(initializer, field);
	}

	@Override
	public void addAdditionalField(final Expression initializer, final AdditionalField field) {
		addFields.put(initializer, field);
	}

	@Override
	public void addInitializer(final Block block,
			final tools.mdsd.jamopp.model.java.statements.Block correspondingBlock) {
		initializers.put(block, correspondingBlock);
	}

	@Override
	public void addAnnotationMethod(final Expression value, final InterfaceMethod method) {
		annotationMethods.put(value, method);
	}

	@Override
	public void addSingleAnnotationParameter(final Expression value, final SingleAnnotationParameter param) {
		singleAnnotations.put(value, param);
	}

	@Override
	public void addAnnotationAttributeSetting(final Expression value, final AnnotationAttributeSetting setting) {
		annotationSetting.put(value, setting);
	}

}
