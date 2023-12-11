package jamopp.printer.interfaces;

import java.io.BufferedWriter;
import java.io.IOException;

import org.eclipse.emf.ecore.EObject;

public interface Printer<From extends EObject> {

	public void print(From element, BufferedWriter writer) throws IOException;

}
