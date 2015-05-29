# Binding

This section describes the already by 52°North Icleand supported binding and how to configure your service to use these bindings. It also contains the documentation how to implement and configure a new [Binding](../src/main/java/org/n52/iceland/binding/Binding.java).

But first, what is a binding:

"*[A binding] describe[s] how SOS [...] clients and servers can communicate with each other.*"
([OGC#12-006 Sec. 13](https://portal.opengeospatial.org/files/?artifact_id=47599))

## Supported Bindings

52°North Icleand already supports some bindings by default.

### KVP

[KvpBinding](../src/main/java/org/n52/iceland/binding/kvp/KvpBinding.java)

### POX

[PoxBinding](../src/main/java/org/n52/iceland/binding/pox/PoxBinding.java)

### SOAP

[SoapBinding](../src/main/java/org/n52/iceland/binding/soap/SoapBinding.java)

### JSON

[JSONBinding](../src/main/java/org/n52/iceland/binding/json/JSONBinding.java)


## Add a new Binding

### Binding



#### Implementation

[Binding](../src/main/java/org/n52/iceland/binding/Binding.java)
[SimpleBinding](../src/main/java/org/n52/iceland/binding/SimpleBinding.java)
[SimpleBinding](../src/main/java/org/n52/iceland/binding/AbstractXmlBinding.java)

#### Configuration

### ResponseWriter

#### Implementation

[ResponseWriter](../src/main/java/org/n52/iceland/coding/encode/ResponseWriter.java)

#### Configuration

Back to [Index](Index.md)