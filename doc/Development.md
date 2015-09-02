# Development

## Running webapp with jetty:run

To run the web application while you're developing it you can use either your IDE or Maven with the ``jetty-maven-plugin``.

``mvn jetty:run``

To debug the project from your IDE (e.g. Netbeans), configure a Maven goal for ``jetty:run`` with the configuration option ``jpda.listen=maven``. You can also put the jetty plugin configuration into a profile and activate it in the Maven goal.

An example configuration of the plugin might look as follows:

```xml
<plugins>
    <plugin>
        <groupId>org.eclipse.jetty</groupId>
        <artifactId>jetty-maven-plugin</artifactId>
        <version>9.3.2.v20150730</version>
        <configuration>
            <webApp>
                <contextPath>/${context.path}</contextPath>
            </webApp>
            <scanIntervalSeconds>1</scanIntervalSeconds>
            <!--<dumpOnStart>true</dumpOnStart>-->
            <extraclasspath>target/classes</extraclasspath>
            <contextXml>${basedir}/src/main/webapp/WEB-INF/jetty-web.xml</contextXml>
        </configuration>
    </plugin>
</plugins>
```

This configuration already includes an important configuration parameter to speed up the deployment. Since Servlet 3.x Jetty 9 is scanning all classes for annotations, which takes a while. You can disable this feature by pointing jetty to only scan a non-existing jar file. Put the following XML snippet in a file ``src/main/webapp/WEB-INF/jetty-web.xml``:

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "http://www.eclipse.org/jetty/configure_9_0.dtd">
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
    <Call name="setAttribute">
        <!-- disable jar scanning to speed up deployment -->
        <Arg>org.eclipse.jetty.server.webapp.WebInfIncludeJarPattern</Arg>
        <Arg>nothing.jar</Arg>
    </Call>
</Configure>
```

Background information see [here](https://code.google.com/p/run-jetty-run/issues/detail?id=140) and [here](http://stackoverflow.com/a/30687370/261210).

Back to [Index](Index.md)