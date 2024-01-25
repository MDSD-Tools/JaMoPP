package tools.mdsd.jamopp.parser.implementation.jamopp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import tools.mdsd.jamopp.parser.interfaces.jamopp.JamoppClasspathEntriesSearcher;

public class JamoppClasspathEntriesSearcherImpl implements JamoppClasspathEntriesSearcher {

	private final Logger logger;

	@Inject
	public JamoppClasspathEntriesSearcherImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String[] getClasspathEntries(final Path dir) {
		String[] entries;
		try (Stream<Path> paths = Files.walk(dir)) {
			entries = paths.filter(Files::isRegularFile).filter(this::isJar).map(Path::toAbsolutePath)
					.map(Path::normalize).map(Path::toString).toArray(i -> new String[i]);
		} catch (final IOException e) {
			logger.error(dir, e);
			entries = new String[0];
		}
		return entries;
	}

	private boolean isJar(final Path path) {
		final Path fileName = path.getFileName();
		Objects.requireNonNull(fileName);
		return fileName.toString().toLowerCase(Locale.US).endsWith("jar");
	}

}
