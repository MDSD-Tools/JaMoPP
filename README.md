To build this project you need to follow these steps:
1. Download Eclipse Modeling
2. Download Java 17
3. Download Maven 3.8.7
4. In Eclipse: click File -> Import and select General -> Existing Projects into Workspace
5. Select root folder of project and import all all nested projects except "mwe2"
6. Go to "java.genmodel" and "layout.genmodel", rightclick on root element and press "generate all"
7. Open cmd in root folder and build project with "mvn clean verify"


# OLD

**Notice:** This repository is not endorsed by or affiliated with DevBoost GmbH or Software Technology Group, Dresden University of Technology.

JaMoPP can parse Java source code into EMF-based models and vice versa. It can be used for code analysis and refactoring.

# Getting Started

* For stand-alone usage, use the code in jamopp.standalone.JaMoPPStandalone.java (jamopp.standalone/src/jamopp/standalone/JaMoPPStandalone.java) as a starting point.

# Download/Installation

Currently, JaMoPP only supports a direct inclusion of its sources into Eclipse.

1. Use Eclipse Modeling Tools 2020-06 (no other versions were tested) with Java 11.
2. Clone this repository.
3. Import all existing projects in this repository.
4.
a. In "org.emftext.commons.jdt/metamodel/", right-click on "jdt.genmodel" and choose "Open With - Genmodel Editor".
b. In the opened window, use the icon in the upper right corner to choose "Generate Model". This automatically generates the model code.
5. Repeat step 4 for "org.emftext.commons.layout/metamodel/layout.genmodel", but choose "Generate Model + Edit" instead of "Generate Model".
6. Repeat step 5 for "org.emftext.language.java/metamodel/java.genmodel", but choose "Generate Model + Edit" and "Generate Editor".
9. Now, JaMoPP should be usable.

# Sources on GitHub

[https://github.com/PalladioSimulator/Palladio-Supporting-EclipseJavaDevelopmentTools](https://github.com/PalladioSimulator/Palladio-Supporting-EclipseJavaDevelopmentTools)

Original repository: [https://github.com/DevBoost/JaMoPP](https://github.com/DevBoost/JaMoPP)


# State

- Complete support for Java up to 14 and support of Java 15 in meta-model and printer implementation.
- Removed loading of class files and old reference resolution and parser.
- For parsing and reference resolution, JDT is used.
- New printer.
- Replaced the JavaResource with a new, simple JavaResource2 implementation.
- Updated and extended all test cases.
