package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.annotations.AnnotationInstance;
import org.emftext.language.java.annotations.AnnotationParameterList;
import org.emftext.language.java.annotations.AnnotationValue;
import org.emftext.language.java.annotations.SingleAnnotationParameter;

import com.google.inject.Inject;
import com.google.inject.Provider;

import jamopp.printer.interfaces.Printer;

public class AnnotationInstancePrinterImpl implements Printer<AnnotationInstance> {

	private final Provider<Printer<AnnotationValue>> annotationValuePrinter;

	@Inject
	public AnnotationInstancePrinterImpl(Provider<Printer<AnnotationValue>> annotationValuePrinter) {
		this.annotationValuePrinter = annotationValuePrinter;
	}

	@Override
	public void print(AnnotationInstance element, BufferedWriter writer) throws IOException {
		writer.append("@" + element.getNamespacesAsString());
		if (element.getParameter() != null) {
			writer.append("(");
			if (element.getParameter() instanceof SingleAnnotationParameter) {
				this.annotationValuePrinter.get().print(((SingleAnnotationParameter) element.getParameter()).getValue(),
						writer);
			} else {
				var list = (AnnotationParameterList) element.getParameter();
				for (var index = 0; index < list.getSettings().size(); index++) {
					var setting = list.getSettings().get(index);
					writer.append(setting.getAttribute().getName() + " = ");
					this.annotationValuePrinter.get().print(setting.getValue(), writer);
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
