package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationAttributeSetting;
import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationParameterList;
import org.emftext.language.java.annotations.SingleAnnotationParameter;

import com.google.inject.Inject;

import jamopp.printer.interfaces.Printer;

class AnnotationInstancePrinter implements Printer<AnnotationInstance> {

	private final AnnotationValuePrinter AnnotationValuePrinter;

	@Inject
	public AnnotationInstancePrinter(jamopp.printer.implementation.AnnotationValuePrinter annotationValuePrinter) {
		super();
		AnnotationValuePrinter = annotationValuePrinter;
	}

	public void print(AnnotationInstance element, BufferedWriter writer) throws IOException {
		writer.append("@" + element.getNamespacesAsString());
		if (element.getParameter() != null) {
			writer.append("(");
			if (element.getParameter() instanceof SingleAnnotationParameter) {
				AnnotationValuePrinter.print(((SingleAnnotationParameter) element.getParameter()).getValue(), writer);
			} else {
				AnnotationParameterList list = (AnnotationParameterList) element.getParameter();
				for (int index = 0; index < list.getSettings().size(); index++) {
					AnnotationAttributeSetting setting = list.getSettings().get(index);
					writer.append(setting.getAttribute().getName() + " = ");
					AnnotationValuePrinter.print(setting.getValue(), writer);
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
