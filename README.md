# Arctic Sea [![Build Status](https://travis-ci.org/52North/arctic-sea.svg)](https://travis-ci.org/52North/arctic-sea) [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/arctic-sea.svg)](https://search.maven.org/#search|gav|1|g:org.n52.arctic-sea) [![Join the chat at https://gitter.im/52North/arctic-sea](https://badges.gitter.im/52North/arctic-sea.svg)](https://gitter.im/52North/arctic-sea)


## Description

### 52°North's backbone for OGC services, clients and middleware

**52°North's Arctic Sea is a framework for developing OGC services, clients and middleware sharing concepts for encoding and decoding of different formats and encodings, workflows and configuration.**

The Arctic Sea is stack of projects (all named after islands that are more or less in the arctic sea) that eases the development of OGC related services (such as the 52°North implementation of the OGC [SOS](https://github.com/52North/SOS) and [WPS](https://github.com/52North/WPS)), clients and middleware. It compromises the following modules.
<p align="center">
<img src="https://github.com/52North/arctic-sea/raw/master/etc/overview.png" width='60%'/>
</p>


#### Iceland [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/iceland.svg)](https://search.maven.org/#search|gav|1|g:org.n52.arctic-sea%20AND%20a:iceland)

Iceland is a service framework that enables the development of OGC RPC based services. It features bindings for KVP, POX, SOAP, as well as JSON-based bindings, and facilitates the rapid development of modular services that are easily configured using Faroe. Svalbard is used for request parsing and response generation.

#### Svalbard [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/svalbard.svg)](https://search.maven.org/#search|gav|1|g:org.n52.arctic-sea%20AND%20a:svalbard)

Svalbard consists of various decoders and encoders for OGC models like [SensorML](http://www.opengeospatial.org/standards/sensorml), [O&M](http://www.opengeospatial.org/standards/om) or [SWE Common](http://www.opengeospatial.org/standards/swecommon), service interfaces like [SOS](http://www.opengeospatial.org/standards/sos) or [WPS](http://www.opengeospatial.org/standards/wps) and a framework for developing these. This enables the creation of decoupled and reusable encoders and decoders for various encodings (like XML, JSON or NetCDF). The object models that are used can be found in 52°North Shetland and are shared accross 52°North components.

#### Faroe [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/faroe.svg)](https://search.maven.org/#search|gav|1|g:org.n52.arctic-sea%20AND%20a:faroe)

Faroe is a configuration API currently featuring a JSON and a SQLite backend. It enables the injection and automatic configuration of settings of various types in classes, including a Spring `BeanPostprocessor`.

#### Shetland [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/shetland.svg)](https://search.maven.org/#search|gav|1|g:org.n52.arctic-sea%20AND%20a:shetland)

Shetland compromises classes for OGC models like [SensorML](http://www.opengeospatial.org/standards/sensorml), [O&M](http://www.opengeospatial.org/standards/om) or [SWE Common](http://www.opengeospatial.org/standards/swecommon) and various service requests and responses. These are shared accross different service implementations.

#### Jan Mayen [![Maven Central](https://img.shields.io/maven-central/v/org.n52.arctic-sea/janmayen.svg)](https://search.maven.org/#search|gav|1|g:org.n52.arctic-sea%20AND%20a:janmayen)

Jan Mayen contains various utility classes shared accross the Arctic Sea.

## License
All components are licensed under the [Apache License 2.0](https://spdx.org/licenses/Apache-2.0.html).

## Changelog
... can be found on the [release page](https://github.com/52North/arctic-sea/releases).

## Quick Start
All projects are available on Maven Central.

```xml
<properties>
  <version.arctic-sea>5.0.1</version.arctic-sea>
</properties>
<dependencies>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>iceland</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
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
    <artifactId>faroe-utils</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>faroe-json</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard-xmlstream</artifactId>
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
    <artifactId>svalbard-exi</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard-xmlbeans</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>svalbard-odata</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>shetland</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
  <dependency>
    <groupId>org.n52.arctic-sea</groupId>
    <artifactId>janmayen</artifactId>
    <version>${version.arctic-sea}</version>
  </dependency>
</dependencies>
```

For convenience the [52°North Maven parent](https://github.com/52North/maven-parents) can be used:
```xml
<parent>
    <groupId>org.n52</groupId>
    <artifactId>parent</artifactId>
    <version>12</version>
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
  <version.arctic-sea>5.1.0-SNAPSHOT</version.arctic-sea>
</properties>
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


## Credits

The development of the 52°North Faroe implementation was supported by several organizations and projects. Among other we would like to thank the following organizations and project

| Project/Logo | Description |
| :-------------: | :------------- |
| <a target="_blank" href="http://www.nexosproject.eu/"><img alt="NeXOS - Next generation, Cost-effective, Compact, Multifunctional Web Enabled Ocean Sensor Systems Empowering Marine, Maritime and Fisheries Management" align="middle" width="172" src="https://raw.githubusercontent.com/52North/sos/develop/spring/views/src/main/webapp/static/images/funding/logo_nexos.png" /></a> | The development of this version of the 52&deg;North Faroe was supported by the <a target="_blank" href="http://cordis.europa.eu/fp7/home_en.html">European FP7</a> research project <a target="_blank" href="http://www.nexosproject.eu/">NeXOS</a> (co-funded by the European Commission under the grant agreement n&deg;614102) |
| <a target="_blank" href="https://bmbf.de/"><img alt="BMBF" align="middle"  src="https://raw.githubusercontent.com/52North/sos/develop/spring/views/src/main/webapp/static/images/funding/bmbf_logo_en.png"/></a><a target="_blank" href="https://colabis.de/"><img alt="COLABIS - Collaborative Early Warning Information Systems for Urban Infrastructures" align="middle"  src="https://raw.githubusercontent.com/52North/sos/develop/spring/views/src/main/webapp/static/images/funding/colabis.png"/></a> | The development of this version 52&deg;North Faroe was supported by the <a target="_blank" href="https://www.bmbf.de"> German Federal Ministry of Education and Research</a> research project <a target="_blank" href="https://colabis.de/">COLABIS</a> (co-funded by the German Federal Ministry of Education and Research, programme Geotechnologien, under grant agreement no. 03G0852A) |
| <a target="_blank" href="https://www.seadatanet.org/About-us/SeaDataCloud/"><img alt="SeaDataCloud" align="middle" width="156" src="https://raw.githubusercontent.com/52North/sos/develop/spring/views/src/main/webapp/static/images/funding/LOGO_SDC_Layer_opengraphimage.png"/></a> | The development of this version of the 52&deg;North SOS was supported by the <a target="_blank" href="https://ec.europa.eu/programmes/horizon2020/">Horizon 2020</a> research project <a target="_blank" href="https://www.seadatanet.org/About-us/SeaDataCloud/">SeaDataCloud</a> (co-funded by the European Commission under the grant agreement n&deg;730960) |
| <a target="_blank" href="http://www.odip.org"><img alt="ODIP II - Ocean Data Interoperability Platform" align="middle" width="100" src="https://raw.githubusercontent.com/52North/sos/develop/spring/views/src/main/webapp/static/images/funding/odip-logo.png"/></a> | The development of this version of the 52&deg;North Faroe was supported by the <a target="_blank" href="https://ec.europa.eu/programmes/horizon2020/">Horizon 2020</a> research project <a target="_blank" href="http://www.odip.org/">ODIP II</a> (co-funded by the European Commission under the grant agreement n&deg;654310) |
| <a target="_blank" href="http://www.connectingeo.net/"><img alt="ConnectinGEO - Coordinating an Observation Network of Networks EnCompassing saTellite and IN-situ to fill the Gaps in European Observations" align="middle" width="100" src="https://raw.githubusercontent.com/52North/sos/develop/spring/views/src/main/webapp/static/images/funding/ConnectinGEO_logo.png"/></a> | The development of this version of the 52&deg;North Faroe was supported by the <a target="_blank" href="https://ec.europa.eu/programmes/horizon2020/">Horizon 2020</a> research project <a target="_blank" href="http://www.connectingeo.net/">ConnectinGEO</a> (co-funded by the European Commission under the grant agreement n&deg;641538) |
| <a target="_blank" href="http://www.geoviqua.org/"><img alt="GeoViQua - QUAlity aware VIsualization for the Global Earth Observation System of Systems" align="middle" width="172" src="https://raw.githubusercontent.com/52North/sos/develop/spring/views/src/main/webapp/static/images/funding/logo_geoviqua.png"/></a> | The development of this version 52&deg;North Faroe was supported by the <a target="_blank" href="http://cordis.europa.eu/fp7/home_en.html">European FP7</a> research project <a href="http://www.geoviqua.org/" title="GeoViQua">GeoViQua</a> (co-funded by the European Commission under the grant agreement n&deg;265178) |
