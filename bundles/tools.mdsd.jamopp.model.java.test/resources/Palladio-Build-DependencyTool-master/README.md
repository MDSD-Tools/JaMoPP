# Palladio-Build-DependencyTool
Tool to analyze the dependencies of [Palladio projects](https://www.palladio-simulator.com/) for the [build process](https://build.palladio-simulator.com/).

## Building
Note: The `P2RepositoryReader` requires the tool `unpack200` to be available. You may need to switch to Java 11, this happens automatically if you use asdf or [rtx](https://github.com/jdxcode/rtx).

By means of the instruction `mvn clean package`, the tool can be packed into an [uber jar](https://maven.apache.org/plugins/maven-shade-plugin/), including its dependencies. This jar can be found in at `./target/deploy/dependencytool.jar` after successful compilation.

## Usage

### CLI Options
* Required options: `-o`, `-at`, `-us`
* Usage: `java -jar dependencytool.jar <args>`
    * `-h`, `--help`, Print this message.

    * `-at`, `--oauth <arg>`, Valid authentication token for GitHub API.
    * `-us`, `--update-site <arg>`, The update site to use
    * `-o`, `--output <arg>`, Decide what to output. One of REPOSITORIES, TOPOLOGY, DEPENDENCIES, NEO4J.

    * `-j`, `--json`, Format the output as json.

    * `-ii`, `--include-imports`, Consider feature.xml includes while calculating dependencies.
    * `-ia`, `--include-archived`, Include archived repositories into the dependency calculation.
    * `-inus`, `--include-no-updatesite`, Include repositories even if an update site could not be found.
    * `-ri`, `--repository-ignore <arg>`, Specify one or more repositories which should be ignored when calculating dependencies. Split by one comma.
    * `-rif`, `--repository-ignore-file <arg>`, Path to file with repositories to ignore. Each repository name must be in a new line.
    * `-ur`, `--use-release`, Use release update site instead of nightly.
    * `-rrf`, `--require-repo-file <arg>`, Filter repositories that do not have the file specified by `<arg>`.

### Sample Interaction
The `<access-token>` parameter must be replaced by a [personal access token](https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token), since this tool loads the required data via the [GitHub API](https://docs.github.com/en/rest).

```bash
git clone git@github.com:PalladioSimulator/Palladio-Build-DependencyTool.git
cd ./Palladio-Build-DependencyTool/
mvn clean package
java -jar target/deploy/dependencytool.jar -at <your-token> -ri Palladio-Build-UpdateSite -ii -us "https://updatesite.palladio-simulator.com/" -o topology -j PalladioSimulator
```

### Neo4j
By means of the `-o NEO4J` flag, the detected dependencies are written into a [Neo4j database](https://neo4j.com/). The root directory of this database is relative to the archive in the `./neo4j` folder. To avoid inconsistencies and unexpected side effects, it is recommended to delete this directory before each tool execution. [Docker](https://neo4j.com/developer/docker/) can be used to mount this directory into a running Neo4j database instance. This running instance can be retrieved via [`localhost:7474`](http://localhost:7474/) and can be accessed with the [native user and default password](https://neo4j.com/docs/operations-manual/current/configuration/set-initial-password/).

```bash
git clone git@github.com:PalladioSimulator/Palladio-Build-DependencyTool.git
cd ./Palladio-Build-DependencyTool/
mvn clean package
java -jar target/deploy/dependencytool.jar -at <your-token> -ri Palladio-Build-UpdateSite -ii -us "https://updatesite.palladio-simulator.com/" -o neo4j PalladioSimulator
# Use the same version as defined in the pom.xml
docker run --rm -p7474:7474 -p7687:7687 -v $PWD/neo4j/data:/data -v $PWD/neo4j/logs:/logs neo4j:4.4.19
# Open the browser at localhost:7474 and login using user and passwort `neo4j`
```
