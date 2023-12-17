package tools.mdsd.jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;

public interface UtilTypeInstructionSeparation {

	void convertAll();

	void addMethod(Block block, org.emftext.language.java.members.Method method);

	void addConstructor(Block block, org.emftext.language.java.members.Constructor constructor);

	void addField(Expression initializer, org.emftext.language.java.members.Field field);

	void addAdditionalField(Expression initializer, org.emftext.language.java.members.AdditionalField field);

	void addInitializer(Block block, org.emftext.language.java.statements.Block correspondingBlock);

	void addAnnotationMethod(Expression value, org.emftext.language.java.members.InterfaceMethod method);

	void addSingleAnnotationParameter(Expression value,
			org.emftext.language.java.annotations.SingleAnnotationParameter param);

	void addAnnotationAttributeSetting(Expression value,
			org.emftext.language.java.annotations.AnnotationAttributeSetting setting);

}