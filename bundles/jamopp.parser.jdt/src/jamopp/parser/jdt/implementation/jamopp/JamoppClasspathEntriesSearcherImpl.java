package jamopp.parser.jdt.implementation.jamopp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import jamopp.parser.jdt.interfaces.jamopp.JamoppClasspathEntriesSearcher;

public class JamoppClasspathEntriesSearcherImpl implements JamoppClasspathEntriesSearcher {

	private final Logger logger;

	@Inject
	public
	JamoppClasspathEntriesSearcherImpl(Logger logger) {
		this.logger = logger;
	}

	@Override
	public String[] getClasspathEntries(Path dir) {
		try (Stream<Path> paths = Files.walk(dir)) {
			return paths
					.filter(path -> Files.isRegularFile(path)
							&& path.getFileName().toString().toLowerCase().endsWith("jar"))
					.map(Path::toAbsolutePath).map(Path::normalize).map(Path::toString).toArray(i -> new String[i]);
		} catch (final IOException e) {
			logger.error(dir, e);
			return new String[0];
		}
	}

}
