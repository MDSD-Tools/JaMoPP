package tools.mdsd.jamopp.resource;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;

import tools.mdsd.jamopp.printer.JaMoPPPrinter;
import tools.mdsd.jamopp.parser.api.JaMoPPParserAPI;
import tools.mdsd.jamopp.parser.jdt.JaMoPPJDTParser;

public class JavaResource2 extends ResourceImpl {

	public JavaResource2(URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(InputStream input, Map<?, ?> options) {
		Resource r = JavaClasspath.get().getResource(getURI());
		if (r != null) {
			this.getContents().addAll(r.getContents());
			return;
		}
		JaMoPPParserAPI api = new JaMoPPJDTParser();
		api.setResourceSet(this.getResourceSet());
		this.getContents().add(api.parse(this.getURI().toString(), input));
	}

	@Override
	protected void doSave(OutputStream output, Map<?, ?> options) {
		this.getContents().forEach(object -> {
			if (object instanceof JavaRoot) {
				JaMoPPPrinter.print((JavaRoot) object, output);
			}
		});
	}
}
