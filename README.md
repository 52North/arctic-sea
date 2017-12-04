# Arctic Sea


## Description

### 52°North's backbone for OGC services, clients and middleware

**52°North's Arctic Sea is a framework for developing OGC services, clients and middleware sharing concepts for encoding and decoding of different formats and encodings, workflows and configuration.**

The Arctic Sea is stack of projects (all named after islands that are more or less in the arctic sea) that eases the development of OGC related services (such as the 52°North implementation of the OGC [SOS](https://github.com/52North/SOS) and [WPS](https://github.com/52North/WPS)), clients and middleware. It compromises the following modules. 
<p align="center">
<img src="https://github.com/52North/arctic-sea/raw/master/overview.png" width='60%'/>
</p>


#### [Iceland](https://github.com/52North/iceland) [![Build Status](https://travis-ci.org/52North/iceland.svg)](https://travis-ci.org/52North/iceland) [![Maven Central](https://img.shields.io/maven-central/v/org.n52.iceland/iceland.svg)](https://search.maven.org/#search|gav|1|g:org.n52.iceland%20AND%20a:iceland)

Iceland is a service framework that enables the development of OGC RPC based services. It features bindings for KVP, POX, SOAP, as well as JSON-based bindings, and facilitates the rapid development of modular services that are easily configured using Faroe. Svalbard is used for request parsing and response generation.
Download [Iceland](https://github.com/52North/iceland/releases)

#### [Svalbard](https://github.com/52North/svalbard) [![Build Status](https://travis-ci.org/52North/svalbard.svg)](https://travis-ci.org/52North/svalbard) [![Maven Central](https://img.shields.io/maven-central/v/org.n52.svalbard/svalbard.svg)](https://search.maven.org/#search|gav|1|g:org.n52.svalbard)

Svalbard consists of various decoders and encoders for OGC models like [SensorML](http://www.opengeospatial.org/standards/sensorml), [O&M](http://www.opengeospatial.org/standards/om) or [SWE Common](http://www.opengeospatial.org/standards/swecommon), service interfaces like [SOS](http://www.opengeospatial.org/standards/sos) or [WPS](http://www.opengeospatial.org/standards/wps) and a framework for developing these. This enables the creation of decoupled and reusable encoders and decoders for various encodings (like XML, JSON or NetCDF). The object models that are used can be found in 52°North Shetland and are shared accross 52°North components.
Download [Svalbard](https://github.com/52North/svalbard/releases)

#### [Faroe](https://github.com/52North/faroe) [![Build Status](https://travis-ci.org/52North/faroe.svg)](https://travis-ci.org/52North/faroe) [![Maven Central](https://img.shields.io/maven-central/v/org.n52.faroe/faroe.svg)](https://search.maven.org/#search|gav|1|g:org.n52.faroe)

Faroe is a configuration API currently featuring a JSON and a SQLite backend. It enables the injection and automatic configuration of settings of various types in classes, including a Spring `BeanPostprocessor`.
Download [Faroe](https://github.com/52North/faroe/releases)

#### [Shetland](https://github.com/52North/shetland) [![Build Status](https://travis-ci.org/52North/shetland.svg)](https://travis-ci.org/52North/shetland) [![Maven Central](https://img.shields.io/maven-central/v/org.n52.shetland/shetland.svg)](https://search.maven.org/#search|gav|1|g:org.n52.shetland)

Shetland compromises classes for OGC models like [SensorML](http://www.opengeospatial.org/standards/sensorml), [O&M](http://www.opengeospatial.org/standards/om) or [SWE Common](http://www.opengeospatial.org/standards/swecommon) and various service requests and responses. These are shared accross different service implementations.
Download [Shetland](https://github.com/52North/shetland/releases)

#### [Jan Mayen](https://github.com/52North/janmayen) [![Build Status](https://travis-ci.org/52North/janmayen.svg)](https://travis-ci.org/52North/janmayen) [![Maven Central](https://img.shields.io/maven-central/v/org.n52.janmayen/janmayen.svg)](https://search.maven.org/#search|gav|1|g:org.n52.janmayen)

Jan Mayen contains various utility classes shared accross the Arctic Sea.
Download [Jan Mayen](https://github.com/52North/janmayen/releases)

## License
All components are licensed under the [Apache License 2.0](https://spdx.org/licenses/Apache-2.0.html).

## Changelog
See the release pages of the various components.

## Quick Start
All projects are available on Maven Central.

```xml
<properties>
  <version.janmayen>1.2.0</version.janmayen>
  <version.faroe>1.3.0</version.faroe>
  <version.shetland>1.0.0</version.shetland>
  <version.svalbard>1.0.0</version.svalbard>
  <version.iceland>2.0.0</version.iceland>
</properties>
<dependencies>
  <dependency>
    <groupId>org.n52.iceland</groupId>
    <artifactId>iceland</artifactId>
    <version>${version.iceland}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.faroe</groupId>
    <artifactId>faroe</artifactId>
    <version>${version.faroe}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.faroe</groupId>
    <artifactId>faroe-annotations</artifactId>
    <version>${version.faroe}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.faroe</groupId>
    <artifactId>faroe-utils</artifactId>
    <version>${version.faroe}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.svalbard</groupId>
    <artifactId>svalbard</artifactId>
    <version>${version.svalbard}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.svalbard</groupId>
    <artifactId>svalbard-xmlstream</artifactId>
    <version>${version.svalbard}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.svalbard</groupId>
    <artifactId>svalbard-json</artifactId>
    <version>${version.svalbard}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.svalbard</groupId>
    <artifactId>svalbard-jsoncommon</artifactId>
    <version>${version.svalbard}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.svalbard</groupId>
    <artifactId>svalbard-exi</artifactId>
    <version>${version.svalbard}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.svalbard</groupId>
    <artifactId>svalbard-xmlbeans</artifactId>
    <version>${version.svalbard}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.shetland</groupId>
    <artifactId>shetland</artifactId>
    <version>${version.shetland}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.janmayen</groupId>
    <artifactId>janmayen</artifactId>
    <version>${version.janmayen}</version>
  </dependency>
</dependencies>
```

For convenience the [52°North Maven parent](https://github.com/52North/maven-parents) can be used:
```xml
<parent>
    <groupId>org.n52</groupId>
    <artifactId>parent</artifactId>
    <version>10</version>
</parent>
```

Development versions can be optained from the Sonatype OSS snapshot repository:
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
```

For details on how to use the components, consult their respective documentation.

## Contact

If you encounter any issues with the software or if you would like to see certain functionality added, let us know:

* Christian Autermann (c.autermann@52north.org)
* Carsten Hollmann (c.hollmann@52north.org)

## Support

You can get support in the [community mailing list and forums](http://52north.org/resources/mailing-lists-and-forums/).


## Contribute
Are you interested in contributing to the 52°North Arctic Sea and you would like to pull your changes to the 52N repository to have them available to all?

In that case we need your official permission and for this purpose we have a so-called contributors license agreement (CLA) in place. With this agreement you grant us the rights to use and publish your code under an open source license.

A link to the contributors license agreement and further explanations are available [here](http://52north.org/about/licensing/cla-guidelines).
