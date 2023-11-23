package jamopp.printer;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationParameterList;
import org.emftext.language.java.annotations.SingleAnnotationParameter;

public class AnnotationInstancePrinter {

	static void printAnnotationInstance(AnnotationInstance element, BufferedWriter writer) throws IOException {
		writer.append("@" + element.getNamespacesAsString());
		if (element.getParameter() != null) {
			writer.append("(");
			if (element.getParameter() instanceof SingleAnnotationParameter) {
				AnnotationValuePrinter.printAnnotationValue(((SingleAnnotationParameter) element.getParameter()).getValue(), writer);
			} else {
				AnnotationParameterList list = (AnnotationParameterList) element.getParameter();
				for (int index = 0; index < list.getSettings().size(); index++) {
					AnnotationAttributeSetting setting = list.getSettings().get(index);
					writer.append(setting.getAttribute().getName() + " = ");
					AnnotationValuePrinter.printAnnotationValue(setting.getValue(), writer);
					if (index < list.getSettings().size() - 1) {
						writer.append(", ");
					}
				}
			}
			writer.append(")");
		}
		writer.append("\n");
	}

}
