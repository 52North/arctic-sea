# Arctic Sea

<p align="center">
<img src="https://github.com/52North/arctic-sea/raw/master/overview.png" width='70%'/>
</p>

## Description

**52°North's backbone for OGC services, clients and middlewares**

*52°North's Arctic Sea is a framework for developing OGC services, clients and middleware sharing concepts for encoding, decoding of different formats and encodings, workflows and configuration*

The arctic sea is stack of projects (all named after islands that are more or less in the arctic sea) that eases development of OGC related services (like the 52°North implementation of the OGC [SOS](https://github.com/52North/SOS) and [WPS](https://github.com/52North/WPS)), clients and middlewares. It compromises the following modules: 

#### [Iceland](https://github.com/52North/iceland)

Iceland is a service framework that allows the development of OGC RPC based services. It features bindings for KVP, POX, SOAP and JSON based bindings and allows the rapid development of modular services, that are easily configured using 52°North Faroe. For request parsing and response generation 52°North Svalbard is used



#### [Svalbard](https://github.com/52North/svalbard)

Svalbard consists of various decoders and encoders for OGC models like [SensorML](http://www.opengeospatial.org/standards/sensorml), [O&M](http://www.opengeospatial.org/standards/om) or [SWE Common](http://www.opengeospatial.org/standards/swecommon), service interfaces like [SOS](http://www.opengeospatial.org/standards/sos) or [WPS](http://www.opengeospatial.org/standards/wps) and a framework for developing these, which allows the creation of decoupled and reusable encoders and decoders for various encodings (like XML, JSON or NetCDF). The object models that are used can be found in 52°North Shetland and are shared accross 52°North components.

#### [Faroe](https://github.com/52North/faroe)

Faroe is a configuration API currently featuring a JSON and a SQLite backend, it allows the injection and automatic configuration of settings of various types in classes. Including a Spring `BeanPostprocessor`.

#### [Shetland](https://github.com/52North/shetland)

Shetland compromises classes for OGC models like [SensorML](http://www.opengeospatial.org/standards/sensorml), [O&M](http://www.opengeospatial.org/standards/om) or [SWE Common](http://www.opengeospatial.org/standards/swecommon) and various service request and responses. These are shared accross different service implementations.

#### [Jan Mayen](https://github.com/52North/janmayen)

Jan Mayen contains various utility classes shared accross the arctic sea.

### Features [Marketing features]

## License
All components are licensed under the Apache License 2.0.

## Changelog
See the release pages of the various components.

## Quick Start
All projects are available on Maven Central once they are released. For the time being the Sonatype snapshot repository has to be enabled:

```xml
<repositories>
  <repository>
    <id>sonatype-nexus-snapshots</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
<dependencies>
  <dependency>
    <groupId>org.n52.iceland</groupId>
    <artifactId>iceland</artifactId>
    <version>2-SNAPSHOT</version>
  </dependency>
  <dependency>
    <groupId>org.n52.faroe</groupId>
    <artifactId>faroe</artifactId>
    <version>1-SNAPSHOT</version>
  </dependency>
  <dependency>
    <groupId>org.n52.svalbard</groupId>
    <artifactId>svalbard</artifactId>
    <version>1-SNAPSHOT</version>
  </dependency>
  <dependency>
    <groupId>org.n52.shetland</groupId>
    <artifactId>shetland</artifactId>
    <version>1-SNAPSHOT</version>
  </dependency>
  <dependency>
    <groupId>org.n52.janmayen</groupId>
    <artifactId>janmayen</artifactId>
    <version>1-SNAPSHOT</version>
  </dependency>
</dependencies>
```

For convenience the [52°North Maven parent](https://github.com/52North/maven-parents) can be used:
```xml
<parent>
    <groupId>org.n52</groupId>
    <artifactId>parent</artifactId>
    <version>6</version>
</parent>
```

For details on how to use the components, consult their respective documentation.

## Contact

If you encounter any issues with the software or if you would like to see certain functionality added, let us know:

* Christian Autermann (c.autermann@52north.org)
* Carsten Hollmann (c.hollmann@52north.org)

## Support

You can get support in the [community mailing list and forums](http://52north.org/resources/mailing-lists-and-forums/).


## Contribute
Are you are interesting in contributing to the 52°North Arctic Sea and you want to pull your changes to the 52N repository to make it available to all?

In that case we need your official permission and for this purpose we have a so called contributors license agreement (CLA) in place. With this agreement you grant us the rights to use and publish your code under an open source license.

A link to the contributors license agreement and further explanations are available [here](http://52north.org/about/licensing/cla-guidelines).
