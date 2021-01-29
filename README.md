# Arctic Sea [![Build Status](https://travis-ci.org/52North/arctic-sea.svg)](https://travis-ci.org/52North/arctic-sea) [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/arctic-sea.svg)](https://search.maven.org/search?q=g:org.n52.arctic-sea) [![Join the chat at https://gitter.im/52North/arctic-sea](https://badges.gitter.im/52North/arctic-sea.svg)](https://gitter.im/52North/arctic-sea) [![Total alerts](https://img.shields.io/lgtm/alerts/g/52North/arctic-sea.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/52North/arctic-sea/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/52North/arctic-sea.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/52North/arctic-sea/context:java)

## Description

### 52°North's backbone for OGC services, clients and middleware

**A framework for developing OGC services, clients and middleware sharing concepts for encoding and decoding of different formats and encodings, workflows and configuration.**

Arctic Sea is a stack of projects that eases the development of OGC related services such as the 52°North implementation of the OGC [SOS](https://github.com/52North/SOS) and [WPS](https://github.com/52North/javaPS), as well as clients and middleware. It compromises the following modules.

![Overview](https://github.com/52North/arctic-sea/raw/master/etc/overview.jpg)

#### Iceland [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/iceland.svg)](https://search.maven.org/search?q=g:org.n52.arctic-sea%20and%20a:iceland*%20and%20p:jar)

Iceland is a service framework that enables the development of OGC RPC based services. It features bindings for KVP, POX, SOAP, as well as JSON-based bindings. Iceland facilitates the rapid development of modular services that use Faroe for easy configuration and Svalbard for request parsing and response generation.

#### Svalbard [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/svalbard.svg)](https://search.maven.org/search?q=g:org.n52.arctic-sea%20and%20a:svalbard*%20and%20p:jar)

Svalbard consists of various decoders and encoders for OGC models (e.g. [SensorML](http://www.opengeospatial.org/standards/sensorml), [O&M](http://www.opengeospatial.org/standards/om) and [SWE Common](http://www.opengeospatial.org/standards/swecommon), service interfaces (like [SOS](http://www.opengeospatial.org/standards/sos) and [WPS](http://www.opengeospatial.org/standards/wps)) and a framework for developing these. This enables the creation of decoupled and reusable encoders and decoders for various encodings (e.g. XML, JSON or NetCDF). The object models used are found in 52°North Shetland and are shared across 52°North components.

#### Faroe [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/faroe.svg)](https://search.maven.org/search?q=g:org.n52.arctic-sea%20and%20a:faroe*%20and%20p:jar)

Faroe is a configuration API currently featuring a JSON and a SQLite backend. It enables the injection and automatic configuration of settings of various types in classes, including a Spring `BeanPostprocessor`.

#### Shetland [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/shetland.svg)](https://search.maven.org/search?q=g:org.n52.arctic-sea%20and%20a:shetland*%20and%20p:jar)

Shetland consists of classes for OGC models like [SensorML](http://www.opengeospatial.org/standards/sensorml), [O&M](http://www.opengeospatial.org/standards/om) and [SWE Common](http://www.opengeospatial.org/standards/swecommon) and various service requests and responses. These are shared across different service implementations.

#### Jan Mayen [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/janmayen.svg)](https://search.maven.org/search?q=g:org.n52.arctic-sea%20and%20a:janmayen*%20and%20p:jar)

Jan Mayen contains various utility classes shared across Arctic Sea.

### Key Technologies

- OGC Web Services
- Web Processing Service (WPS)
- Sensor Observation Service (SOS)
- SWE Common
- SensorML
- Observation and Measurement (O&M)
- Spring
- Java
- XML

### Benefits

- The middleware component provides a robust layer to easily create web services compliant to OGC standards.
- The configuration API enables harmonized management of service properties.
- Centralized XML encoding and decoding reduces increases stability.

## License

All components are licensed under the [Apache License 2.0](https://spdx.org/licenses/Apache-2.0.html).

## Changelog

... can be found on the [release page](https://github.com/52North/arctic-sea/releases).

## Quick Start

All projects are available on Maven Central.

```xml
<properties>
  <version.arctic-sea>8.0.3</version.arctic-sea>
</properties>
<dependencies>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>faroe</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>faroe-annotations</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>faroe-json</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>faroe-utils</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>iceland</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>iceland-statistics</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>iceland-statistics-generator</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>iceland-statistics-geolocation</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>iceland-statistics-kibana</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>janmayen</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>shetland</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>shetland-rdf</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard-exi</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard-json</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard-json-common</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard-odata</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard-xmlbeans</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard-xmlstream</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId></artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
</dependencies>
```

For convenience the [52°North Maven parent](https://github.com/52North/maven-parents) can be used:

```xml
<parent>
    <groupId>org.n52</groupId>
    <artifactId>parent</artifactId>
    <version>16</version>
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
<properties>
  <version.arctic-sea>7.6.0-SNAPSHOT</version.arctic-sea>
</properties>
```

For details on how to use the components, consult their respective documentation.

## Contact

If you encounter any issues with the software or if you would like to see certain functionality added, let us know:

- Christian Autermann (c.autermann@52north.org)
- Carsten Hollmann (c.hollmann@52north.org)

## Support

You can get support in the [community mailing list and forums](https://52north.org/discuss/#mailing-lists).

## Contribute

Are you interested in contributing to the 52°North Arctic Sea and you would like to pull your changes to the 52N repository to have them available to all?

In that case we need your official permission and for this purpose we have a so-called contributors license agreement (CLA) in place. With this agreement you grant us the rights to use and publish your code under an open source license.

A link to the contributors license agreement and further explanations are available [here](https://52north.org/software/licensing/guidelines/).

## References

The development of the 52°North Faroe implementation was supported by several organizations and projects. Among other we would like to thank the following organizations and project

| Project/Logo    | Description    |
| :-------------: | :------------- |
| [![Cos4Cloud](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/nexos-logo.png)](https://cos4cloud-eosc.eu/) | The development of this version of 52°North Arctic Sea was supported by the [Horizon 2020](https://ec.europa.eu/programmes/horizon2020/) research project [Cos4Cloud](https://cos4cloud-eosc.eu/) (co-funded by the European Commission under the grant agreement n°863463) |
| [![BMBF](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/bmbf-geotechnologien-logo.png)](https://www.bmbf.de/)[![TaMIS](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/TaMIS_Logo_small.png)](http://tamis.kn.e-technik.tu-dortmund.de/) | The development of this version 52°North Arctic Sea was supported by the [German Federal Ministry of Education and Research](https://www.bmbf.de/) research project [TaMIS](https://colabis.de/) (co-funded by the German Federal Ministry of Education and Research, programme Geotechnologien, under grant agreement no. 03G0854) |
| [![JERICO-S3](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/nexos-logo.png)](https://www.jerico-ri.eu) | The development of this version of 52°North Arctic Sea was supported by the [Horizon 2020](https://ec.europa.eu/programmes/horizon2020/) research project [JERICO-S3](https://www.jerico-ri.eu) (co-funded by the European Commission under the grant agreement n°871153) |
| [![NeXOS](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/nexos-logo.png)](http://www.nexosproject.eu/) | The development of this version of 52°North Arctic Sea was supported by the [European FP7](https://ec.europa.eu/research/fp7/index_en.cfm) research project [NeXOS](http://www.nexosproject.eu/) (co-funded by the European Commission under the grant agreement n°614102) |
| [![BMBF](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/bmbf-geotechnologien-logo.png)](https://www.bmbf.de/)[![COLABIS](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/colabis-logo.png)](https://colabis.de/) | The development of this version 52°North Arctic Sea was supported by the [German Federal Ministry of Education and Research](https://www.bmbf.de/) research project [COLABIS](https://colabis.de/) (co-funded by the German Federal Ministry of Education and Research, programme Geotechnologien, under grant agreement no. 03G0852A) |
| [![SeaDataCloud](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/seadatacloud-logo.png)](https://www.seadatanet.org/About-us/SeaDataCloud/) | The development of this version of 52°North Arctic Sea was supported by the [Horizon 2020](https://ec.europa.eu/programmes/horizon2020/) research project [SeaDataCloud](https://www.seadatanet.org/About-us/SeaDataCloud/) (co-funded by the European Commission under the grant agreement n°730960) |
| [![ODIP II](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/odip-ii-logo.png)](http://www.odip.org/) | The development of this version of 52°North Arctic Sea was supported by the [Horizon 2020](https://ec.europa.eu/programmes/horizon2020/) research project [ODIP II](http://www.odip.org/) (co-funded by the European Commission under the grant agreement n°654310) |
| [![ConnectinGEO](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/connectingeo-logo.png)](http://www.connectingeo.net/) | The development of this version of 52°North Arctic Sea was supported by the [Horizon 2020](https://ec.europa.eu/programmes/horizon2020/) research project [ConnectinGEO](http://www.connectingeo.net/) (co-funded by the European Commission under the grant agreement n°641538) |
| [![GeoViQua](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/geoviqua-logo.png)](https://cordis.europa.eu/project/id/265178) | The development of this version of 52°North Arctic Sea was supported by the [European FP7](https://ec.europa.eu/research/fp7/index_en.cfm) research project [GeoViQua](https://cordis.europa.eu/project/id/265178) (co-funded by the European Commission under the grant agreement n°265178) |
| [![BMVI](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/bmvi-logo.png)](https://www.bmvi.de/)[![mFund](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/mfund-logo.jpg)](https://www.bmvi.de/DE/Themen/Digitales/mFund/Ueberblick/ueberblick.html)[![WaCoDis](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/wacodis-logo.png)](http://wacodis.fbg-hsbo.de/) | The development of this version of 52°North Arctic Sea was supported by the [German Federal Ministry of of Transport and Digital Infrastructure](https://www.bmvi.de/) research project [WaCoDis](http://wacodis.fbg-hsbo.de/) (co-funded by the German Federal Ministry of Transport and Digital Infrastructure, programme mFund) |
| [![BMBF](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/bmbf-logo.png)](https://www.bmbf.de/)[![fona](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/fona-logo.png)](https://www.fona.de/)[![MuDak-WRM](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/mudak-wrm-logo.png)](https://colabis.de/) | The development of this version of 52°North Arctic Sea was supported by the [German Federal Ministry of Education and Research](https://www.bmbf.de/) research project [MuDak-WRM](http://www.mudak-wrm.kit.edu/) (co-funded by the German Federal Ministry of Education and Research, programme [fona](https://www.fona.de/)) |
| [![BRIDGES](https://raw.githubusercontent.com/52North/arctic-sea/master/etc/images/bridges-logo.jpg)](http://www.bridges-h2020.eu/)| The development of this version of the 52°North Arctic Sea was supported by the [Horizon 2020](https://ec.europa.eu/programmes/horizon2020/) research project [BRIDGES](http://www.bridges-h2020.eu/) (co-funded by the European Commission under the grant agreement n°635359)
