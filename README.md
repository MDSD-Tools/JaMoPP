# JaMoPP

**Notice:** This repository is not endorsed by or affiliated with DevBoost GmbH or Software Technology Group, Dresden University of Technology.

JaMoPP can parse Java source code into EMF-based models and vice versa. It can be used for code analysis and refactoring. For more information read the original publication: [JaMoPP publications](https://github.com/DevBoost/JaMoPP/tree/master/Doc/org.emftext.language.java.doc/publications)

## State

- Complete support for Java up to 14 and support of Java 15 in meta-model and printer implementation.
- Removed loading of class files and old reference resolution and parser.
- For parsing and reference resolution, JDT is used.
- New printer.
- Replaced the JavaResource with a new, simple JavaResource2 implementation.
- Updated and extended all test cases.

## Getting Started

Currently, JaMoPP only supports a direct inclusion of its sources into Eclipse. For stand-alone usage, use the code in `jamopp.standalone.JaMoPPStandalone.java`  as a starting point.

### Prerequisites

- Eclipse Modeling

- Java 17

- Maven 3.8.7

### Installing

1. In Eclipse: click File -> Import and select General -> Existing Projects into Workspace

2. Select root folder of project and import all `/tools.mdsd.jamopp` nested projects except `/tools.mdsd.jamopp.mwe2`.

3. Generate code from `.genmodel` files. For each file right click on the root element and press Generate all. The three files are:
   
   - `/tools.mdsd.jamopp.model.java/metamodel/java.genmodel`
   
   - `/tools.mdsd.jamopp.commons.layout/metamodel/layout.genmodel`
   
   - `/tools.mdsd.jamopp.commons.jdt/metamodel/jdt.genmodel`

4. To test the setup, open the console in root folder and build project with `mvn clean verify`.

## Built With

- [Eclipse Tycho](https://projects.eclipse.org/projects/technology.tycho)

- [Maven](https://maven.apache.org/)

- [Google Guice](https://github.com/google/guice) - Used for the bundles `/tools.mdsd.jamopp.printer` and `/tools.mdsd.jamopp.parser`. The modules responsible for the dependency injection are in the `injection` packages.

## Codestyle

For styling the Eclipse plugin [Eclipse-PMD](https://github.com/eclipse-pmd) and [Teamscale](https://teamscale.com) is used.

The PMD ruleset is `JaMoPP\codestyle\jamopp-ruleset.xml`.

The Teamscale analysis profile is `JaMoPP\codestyle\Jamopp-Profile.tsanalysisprofile`.

## Links

- Related: [GitHub - PalladioSimulator/Palladio-Supporting-EclipseJavaDevelopmentTools](https://github.com/PalladioSimulator/Palladio-Supporting-EclipseJavaDevelopmentTools)

- Original repository: [GitHub - DevBoost/JaMoPP](https://github.com/DevBoost/JaMoPP)

## Licensing

The code in this project is licensed under the [Eclipse Public License - Version 1.0](https://www.eclipse.org/legal/epl-v10.html).
