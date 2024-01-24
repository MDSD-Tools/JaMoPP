package tools.mdsd.jamopp.resource;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

import tools.mdsd.jamopp.model.java.JavaClasspath;
import tools.mdsd.jamopp.model.java.containers.JavaRoot;
import tools.mdsd.jamopp.parser.jdt.JaMoPPJDTParser;
import tools.mdsd.jamopp.parser.jdt.JaMoPPParserAPI;
import tools.mdsd.jamopp.printer.JaMoPPPrinter;

public class JavaResource2 extends ResourceImpl {

	public JavaResource2(final URI uri) {
		super(uri);
	}

	@Override
	protected void doLoad(final InputStream input, final Map<?, ?> options) {
		final Resource resource = JavaClasspath.get().getResource(getURI());
		if (resource != null) {
			getContents().addAll(resource.getContents());
			return;
		}
		final JaMoPPParserAPI api = new JaMoPPJDTParser();
		api.setResourceSet(getResourceSet());
		getContents().add(api.parse(getURI().toString(), input));
	}

	@Override
	protected void doSave(final OutputStream output, final Map<?, ?> options) {
		getContents().forEach(object -> {
			if (object instanceof JavaRoot) {
				JaMoPPPrinter.print((JavaRoot) object, output);
			}
		});
	}
}
