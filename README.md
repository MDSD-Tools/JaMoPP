# JaMoPP: Java Model Parser and Printer
JaMoPP is an [EMF-based](https://www.eclipse.org/modeling/emf/) tool that can parse and print Java source and byte code into EMF models and vice versa. It preserves source formatting and can be used for code analysis, refactoring, and code generation. JaMoPP is based on the [original version by DevBoost GmbH](https://github.com/DevBoost/JaMoPP) and the [Software Technology Group of the Technical University of Dresden](https://tu-dresden.de/ing/informatik/smt/st). It has been forked by the [Institute of Information Security and Dependability (KASTEL)](https://www.kastel.kit.edu/) at the [Karlsruhe Institute of Technology (KIT)](https://www.kit.edu/). This repository is not supported by or affiliated with [DevBoost GmbH](https://devboost.com/) or [TU Dresden (TUD)](https://tu-dresden.de/).

JaMoPP defines a complete metamodel and textual syntax specification for Java, covering Java versions up to 15. It also provides a parser and printer that can convert between text and models in both directions. This allows Java code to be treated like any other model. JaMoPP can also parse and print byte code (`.class` files) using the [Eclipse Java Development Tools (JDT)](https://eclipse.dev/jdt/).

For more information read the original publications: [JaMoPP publications](https://github.com/DevBoost/JaMoPP/tree/master/Doc/org.emftext.language.java.doc/publications)

## Getting Started
Currently, JaMoPP only supports a direct inclusion of its sources into Eclipse. For stand-alone usage, use the code in `jamopp.standalone.JaMoPPStandalone.java`  as a starting point.

### Prerequisites
- Eclipse Modeling Tools 2022-09
- Java 17
- Maven 3.8.7

### Installing
1. In Eclipse: click File -> Import and select General -> Existing Projects into Workspace
2. Select root folder of project and import the 'JaMoPP' project and all `/tools.mdsd.jamopp` nested projects except `/tools.mdsd.jamopp.mwe2`.
3. Generate code from `.genmodel` files. For each file right click on the root element and press Generate all. The three files are:
   - `/tools.mdsd.jamopp.model.java/metamodel/java.genmodel`   
   - `/tools.mdsd.jamopp.commons.layout/metamodel/layout.genmodel`
   - `/tools.mdsd.jamopp.commons.jdt/metamodel/jdt.genmodel`
4. To test the setup, open the console in root folder and build project with `mvn clean verify`.

## Built With
- [Eclipse Tycho](https://projects.eclipse.org/projects/technology.tycho)
- [Maven](https://maven.apache.org/)
- [Google Guice](https://github.com/google/guice) - Used for the bundles `/tools.mdsd.jamopp.printer` and `/tools.mdsd.jamopp.parser`. The modules responsible for the dependency injection are in the `injection` packages.

## Usage
In Eclipse, after installing JaMoPP, you can load Java files (`.java` and `.class` files) with any EMF-based tool similar to how you load other EMF models.

For stand-alone usage, use the code in [JaMoPPStandalone.java](https://github.com/MDSD-Tools/JaMoPP/blob/main/bundles/jamopp.standalone/src/jamopp/standalone/JaMoPPStandalone.java) as a starting point. The JaMoPPStandalone class is a standalone utility for parsing Java source code and generating XMI models. It can be used to parse any URI (absolute or relative file path/ Directory/ Archive) and output the corresponding XMI model. The class takes two input parameters:

- `INPUT`: The URI of the Java source code to parse.
- `ENABLE_OUTPUT_OF_LIBRARY_FILES`: A Boolean flag that determines whether to output XMI models for library files.
  The class generates XMI models for the Java source code and saves them to the ./standalone_output directory. The directory structure of the output files mirrors the package hierarchy of the Java source code.

## Codestyle
For styling and coding conventions the Eclipse internal formatter/compiler, the Eclipse plugins [Eclipse-PMD](https://github.com/eclipse-pmd) and [SpotBugs](https://marketplace.eclipse.org/content/spotbugs-eclipse-plugin) and [Teamscale](https://teamscale.com) are used.

- The PMD ruleset is `JaMoPP\codestyle\jamopp-ruleset.xml`.
- The Teamscale analysis profile is `JaMoPP\codestyle\Jamopp-Profile.tsanalysisprofile`.
- The Spotbugs exclude file is `JaMoPP\codestyle\myExcludeFilter.xml`.

The settings for the plugins and the internal formatter/compiler are all saved as project settings. Spotbugs and PMD must be downloaded from the Eclipse Marketplace.

## Case Studies
We tested JaMoPP with the following evaluation scenarios:
- [acmeair/acmeair](https://github.com/acmeair/acmeair/archive/refs/tags/v1.2.0.zip)
  [apache/commons-lang](https://github.com/apache/commons-lang/archive/refs/tags/rel/commons-lang-3.12.0.zip)
- [berndruecker/flowing-retail](https://github.com/berndruecker/flowing-retail/archive/refs/heads/master.zip)
- [bigbluebutton/bigbluebutton](https://github.com/bigbluebutton/bigbluebutton/archive/refs/tags/v2.4.7.zip)
- [connorimes/SPECjvm2008](https://github.com/connorimes/SPECjvm2008/archive/refs/heads/master.zip)
- [sluluyao/SPECjbb2005](https://github.com/csluluyao/SPECjbb2005/archive/refs/heads/master.zip)
- [DescartesResearch/TeaStore](https://github.com/DescartesResearch/TeaStore/archive/refs/tags/v1.4.0.zip)
- [eventuate-tram/eventuate-tram-examples-customers-and-orders-redis](https://github.com/eventuate-tram/eventuate-tram-examples-customers-and-orders-redis/archive/e4a3da5502aa11af441b70b7ab6b5f1430b17d4.zip)
- [ewolff/microservice-kafka](https://github.com/ewolff/microservice-kafka/archive/refs/heads/master.zip)
- [ewolff/microservice](https://github.com/ewolff/microservice/archive/refs/heads/master.zip)
- [h2database/h2database](https://github.com/h2database/h2database/archive/refs/tags/version-2.1.210.zip)
- [jonashackt/spring-rabbitmq-messaging-microservices](https://github.com/jonashackt/spring-rabbitmq-messaging-microservices/archive/19cadd4c1310a4651f3529626ac2acd4853a987.zip)
- [kbastani/spring-cloud-event-sourcing-example](https://github.com/kbastani/spring-cloud-event-sourcing-example/archive/refs/heads/master.zip)
- [kit-sdq/esda](https://github.com/kit-sdq/esda/archive/refs/heads/master.zip)
- [kit-sdq/TimeSheetGenerator](https://github.com/kit-sdq/TimeSheetGenerator/archive/refs/heads/main.zip)
- [meet-eat/meet-eat-data](https://github.com/meet-eat/meet-eat-data/archive/refs/heads/master.zip)
- [meet-eat/meet-eat-server](https://github.com/meet-eat/meet-eat-server/archive/refs/heads/master.zip)
- [nickboucher/trojan-source](https://github.com/nickboucher/trojan-source/archive/refs/heads/main.zip)
- [PalladioSimulator/Palladio-Addons-PlantUML](https://github.com/PalladioSimulator/Palladio-Addons-PlantUML/archive/refs/heads/main.zip)
- [PalladioSimulator/Palladio-Build-DependencyTool](https://github.com/PalladioSimulator/Palladio-Build-DependencyTool/archive/refs/heads/master.zip)
- [petros94/smart-home-websockets](https://github.com/petros94/smart-home-websockets/archive/refs/heads/master.zip)
- [sguazt/RUBiS](https://github.com/sguazt/RUBiS/archive/refs/heads/master.zip)
- [sjwoodman/clnr-demo](https://github.com/sjwoodman/clnr-demo/archive/refs/heads/master.zip)
- [spring-io/sagan](https://github.com/spring-io/sagan/archive/1995913fb2d90693c97c251fd142b429724cdf44.zip)
- [spring-petclinic/spring-petclinic-microservices](https://github.com/spring-petclinic/spring-petclinic-microservices/archive/refs/tags/v2.3.6.zip)
- [sqshq/piggymetrics](https://github.com/sqshq/piggymetrics/archive/refs/tags/spring.version.2.0.3.zip)
- [TeamatesProject/teammates](https://github.com/TeamatesProject/teammates/archive/refs/heads/master.zip)

## Contributing
We welcome contributions to JaMoPP. Please submit pull requests to the [GitHub repository](https://github.com/MDSD-Tools/JaMoPP).

## License
JaMoPP is released under the Eclipse Public License - v 1.0. For more information, please refer to the [LICENSE](https://github.com/MDSD-Tools/JaMoPP/blob/main/LICENSE) file.
