package jamopp.printer.implementation;

import java.io.BufferedWriter;
import java.io.IOException;

import org.emftext.language.java.expressions.ShiftExpression;

import jamopp.printer.interfaces.Printer;

interface IShiftExpressionPrinter extends Printer<ShiftExpression>{

	void print(ShiftExpression element, BufferedWriter writer) throws IOException;

}