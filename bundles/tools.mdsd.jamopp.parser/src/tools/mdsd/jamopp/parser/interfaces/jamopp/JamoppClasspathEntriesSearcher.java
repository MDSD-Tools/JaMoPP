package tools.mdsd.jamopp.parser.interfaces.jamopp;

import java.nio.file.Path;

public interface JamoppClasspathEntriesSearcher {

	String[] getClasspathEntries(Path dir);

}