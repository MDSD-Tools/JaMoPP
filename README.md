To build this project you need to follow these steps:
1. Download Eclipse Modeling
2. Download Java 17
3. Download Maven 3.8.7
4. In Eclipse: click File -> Import and select General -> Existing Projects into Workspace
5. Select root folder of project and import all all nested projects except "mwe2"
6. Go to "java.genmodel" and "layout.genmodel", rightclick on root element and press "generate all"
7. Open cmd in root folder and build project with "mvn clean verify"