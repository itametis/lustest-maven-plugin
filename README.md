# What is lustest-maven-plugin ?
Lustest is a Maven plugin which watches the changes made on the sources to recompile only the modified files then it executes the unit tests. So that you can stay keep focus on the code and use Java like if it was interpreted language.

This bring on Maven a basic feature of Gradle which is truly missing. Have fun !

# How does it works ?
Just add the lustest-maven-plugin in your pom like this :
```xml
<plugin>
    <groupId>com.itametis.maven.plugins</groupId>
    <artifactId>lustest-maven-plugin</artifactId>
    <version>1.1.0</version>
</plugin>
```

Then run the command line :
```bash
mvn lustest:run
```

And that's it ! Lustest will start listening recursively the **./src** folder. After each save, the Lustest daemon will compile only the changed files (either in sources or tests) at a lightening fast speed and execute tests just after.

# How to specify multiple folders to watch ?
By setting this declaration in you pom.xml :
```xml
<plugin>
    <groupId>com.itametis.maven.plugins</groupId>
    <artifactId>lustest-maven-plugin</artifactId>
    <version>1.1.0</version>
    <configuration>
        <foldersToWatch>
            <param>src/main/java/</param>
            <param>src/test/java/</param>
        </foldersToWatch>
    </configuration>
</plugin>
```

# How to specify sources encoding ?
With this configuration attribute (by default Lustest expects UTF-8) :
```xml
<plugin>
    <groupId>com.itametis.maven.plugins</groupId>
    <artifactId>lustest-maven-plugin</artifactId>
    <version>1.1.0</version>
    <configuration>
        <sourceEncoding>UTF-8</sourceEncoding>
    </configuration>
</plugin>
```

# How to specify the compiler version to use ?
With this configuration attribute (by default Lustest will use maven-compiler-plugin v3.6.1) :
```xml
<plugin>
    <groupId>com.itametis.maven.plugins</groupId>
    <artifactId>lustest-maven-plugin</artifactId>
    <version>1.1.0</version>
    <configuration>
        <mavenCompilerVersion>3.5.0</mavenCompilerVersion>
    </configuration>
</plugin>
```

# How to specify the surefire version to use ?
With this configuration attribute (by default Lustest will use maven-surefire-plugin v2.20) :
```xml
<plugin>
    <groupId>com.itametis.maven.plugins</groupId>
    <artifactId>lustest-maven-plugin</artifactId>
    <version>1.1.0</version>
    <configuration>
        <mavenSurefireVersion>2.20</mavenSurefireVersion>
    </configuration>
</plugin>
```
