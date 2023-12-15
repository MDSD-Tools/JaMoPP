package jamopp.parser.jdt.interfaces.helper;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.SingleAnnotationParameter;
import org.emftext.language.java.members.AdditionalField;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.InterfaceMethod;
import org.emftext.language.java.members.Method;

public interface UtilTypeInstructionSeparation {

	void addAdditionalField(Expression initializer, AdditionalField field);

	void addAnnotationAttributeSetting(Expression value, AnnotationAttributeSetting setting);

	void addAnnotationMethod(Expression value, InterfaceMethod method);

	void addConstructor(Block block, Constructor constructor);

	void addField(Expression initializer, Field field);

	void addInitializer(Block block, org.emftext.language.java.statements.Block correspondingBlock);

	void addMethod(Block block, Method method);

	void addSingleAnnotationParameter(Expression value, SingleAnnotationParameter param);

	void convertAll();

}