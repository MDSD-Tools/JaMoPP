package tools.mdsd.jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;

public interface UtilTypeInstructionSeparation {

	void convertAll();

	void addMethod(Block block, tools.mdsd.jamopp.model.java.members.Method method);

	void addConstructor(Block block, tools.mdsd.jamopp.model.java.members.Constructor constructor);

	void addField(Expression initializer, tools.mdsd.jamopp.model.java.members.Field field);

	void addAdditionalField(Expression initializer, tools.mdsd.jamopp.model.java.members.AdditionalField field);

	void addInitializer(Block block, tools.mdsd.jamopp.model.java.statements.Block correspondingBlock);

	void addAnnotationMethod(Expression value, tools.mdsd.jamopp.model.java.members.InterfaceMethod method);

	void addSingleAnnotationParameter(Expression value,
			tools.mdsd.jamopp.model.java.annotations.SingleAnnotationParameter param);

	void addAnnotationAttributeSetting(Expression value,
			tools.mdsd.jamopp.model.java.annotations.AnnotationAttributeSetting setting);

}