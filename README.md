# BAM!

**BAM!** is a Java library that enables to modeling and presenting the dynamics of a rigid body.
The graphics layer is supported by a **LWJGL** library that enables cross-platform access to OpenGL.
The physics engine is supported by a **JBox2d** library.  

## Requirements
1. JRE 8.x installed
2. Maven 3.x installed

## Building
Project building process is configured and managed by Maven 3.x.

## Sample
There is an option to build executable jar file with dependencies:
```mvn clean compile assembly:single```  
To run sample app type:
```java -jar target/bam-1.0.0-jar-with-dependencies.jar```

## References
1. LWJGL https://github.com/LWJGL/lwjgl3
2. JBox2d https://github.com/jbox2d/jbox2d
