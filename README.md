# Json Schema Generator

You may need this to share your schema with downstream or other systems because there are plugin to generate the classes from schema but not vice versa.
This plugin works with your existing java codes and provide the JSON schema from POJO directly. 

Add below snippet in your project to generate JSON schema
```xml

<build>
    <plugins>
        <plugin>
            <groupId>com.ash.jsonschema</groupId>
            <artifactId>jsonschema-maven-plugin</artifactId>
            <version>0.1-SNAPSHOT</version>
            <executions>
                <execution>
                    <id>generateOCSchema</id>
                    <phase>process-classes</phase>
                    <goals>
                        <goal>generateSchema</goal> <!-- Goal name to generate the schema-->
                    </goals>
                    <configuration>
                        <!-- List Type [MANDATORY]:Generate schema for the package \ classes whose path are in below pattern (ant-ish). -->
                        <includes>
                            <!-- Atleast one include is mandatory -->
                            <include>com/ash/*</include>
                        </includes>
                        <!-- List Type [OPTIONAL]: Exclude schema for the package \ classes whose path are in below pattern (ant-ish) -->
                        <excludes>
                            <exclude>com/ash/aws/*</exclude>
                        </excludes>
                        <!-- Class Type [OPTIONAL]: Provide your object mapper implementation. Default: com.ash.jsonschema.service.CustomMapper -->
                        <!--<objectMapperFactoryClassName>com.ash.OCJsonSchemaObjectMapperFactory</objectMapperFactoryClassName>-->
 
                        <!-- Path Type [OPTIONAL]: Provide your schema directory. Default: ${basedir}/schema -->
                        <outputDirectory>${basedir}/myfileSchema</outputDirectory>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>

```

### How to execute
Once you will run your application's maven compile target then, it will generate the .json file in our output directory.


#### Useful commands -

###### Describe Help

```
mvn help:describe -DgroupId=com.ash.jsonschema -DartifactId=jsonschema-maven-plugin
```



###### Describe Goal Help
```
mvn help:describe -DgroupId=com.ash.jsonschema -DartifactId=jsonschema-maven-plugin -Dgoal=generateSchema -Ddetail
```
