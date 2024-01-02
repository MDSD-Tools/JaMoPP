# JaMoPP
**Notice:** This repository is not endorsed by or affiliated with DevBoost GmbH or Software Technology Group, Dresden University of Technology.

JaMoPP can parse Java source code into EMF-based models and vice versa. It can be used for code analysis and refactoring.

# Getting Started
* For stand-alone usage, use the code in jamopp.standalone.JaMoPPStandalone.java (jamopp.standalone/src/jamopp/standalone/JaMoPPStandalone.java) as a starting point.

# Download/Installation
Currently, JaMoPP only supports a direct inclusion of its sources into Eclipse.
To get the project running, you need to follow these steps:
1. Download Eclipse Modeling
2. Download Java 17
3. Download Maven 3.8.7
4. In Eclipse: click File -> Import and select General -> Existing Projects into Workspace
5. Select root folder of project and import all nested projects
6. Go to "java.genmodel" and "layout.genmodel", rightclick on the root element and press "generate all"
7. To test the setup, open cmd in root folder and build project with "mvn clean verify"

# Sources on GitHub
[https://github.com/PalladioSimulator/Palladio-Supporting-EclipseJavaDevelopmentTools](https://github.com/PalladioSimulator/Palladio-Supporting-EclipseJavaDevelopmentTools)\
Original repository: [https://github.com/DevBoost/JaMoPP](https://github.com/DevBoost/JaMoPP)

# State 
- Complete support for Java up to 14 and support of Java 15 in meta-model and printer implementation.
- Removed loading of class files and old reference resolution and parser.
- For parsing and reference resolution, JDT is used.
- New printer.
- Replaced the JavaResource with a new, simple JavaResource2 implementation.
- Updated and extended all test cases.

# User Guide

# Developer Guide

# Modules
An overview of all four modules with short descriptions about their content and usage.
* bundles
    * tools.mdsd.jamopp.commons.jdt
    * tools.mdsd.jamopp.commons.layout
    * tools.mdsd.jamopp.commons.layout.edit
    * tools.mdsd.jamopp.model.java
    * tools.mdsd.jamopp.model.java.edit
    * tools.mdsd.jamopp.model.java.editor
    * tools.mdsd.jamopp.parser
    * tools.mdsd.jamopp.parser.jdt
    * tools.mdsd.jamopp.printer
    * tools.mdsd.jamopp.resource
    * tools.mdsd.jamopp.standalone
* features
    * org.emftext.language.java.feature
* releng
    * tools.mdsd.jamopp.mwe2
    * tools.mdsd.jamopp.targetplatform
    * tools.mdsd.jamopp.updatesite
* tests
    * org.emftext.language.java.test
