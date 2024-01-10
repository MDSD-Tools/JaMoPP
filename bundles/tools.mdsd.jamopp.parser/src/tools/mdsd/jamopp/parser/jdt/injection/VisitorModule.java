package tools.mdsd.jamopp.parser.jdt.injection;

import com.google.inject.AbstractModule;

import tools.mdsd.jamopp.parser.jdt.implementation.visitor.VisitorAndConverterAbstractAndEmptyModelJDTAST;
import tools.mdsd.jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

public class VisitorModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(AbstractVisitor.class).to(VisitorAndConverterAbstractAndEmptyModelJDTAST.class);
	}

}
