package jamopp.parser.jdt.injector;

import com.google.inject.AbstractModule;

import jamopp.parser.jdt.implementation.visitor.VisitorAndConverterAbstractAndEmptyModelJDTAST;
import jamopp.parser.jdt.interfaces.visitor.AbstractVisitor;

public class VisitorModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		bind(AbstractVisitor.class).to(VisitorAndConverterAbstractAndEmptyModelJDTAST.class);
	}

}
