# RequestResponseModifier

This section describes how to implement and configure a new [RequestResponseModifier](../src/main/java/org/n52/iceland/convert/RequestResponseModifier.java).

The *RequestResponseModifier* allows to modify requests and responses. Use cases would be to add prefixes to identifier in the response and remove the prefixes from the identifier in the request or for coordinate transformation.

## Implementation

### RequestResponseModifier


### RequestResponseModifierKeyType

The [RequestResponseModifierKeyType](../src/main/java/org/n52/iceland/convert/RequestResponseModifierKeyType.java) class defines for which *service*, *version*, *AbstractServiceRequest* and *AbstractServiceResponse* this *RequestResponseModifier* provides modification operations.

### RequestResponseModifierFacilitator

The [RequestResponseModifierFacilitator](../src/main/java/org/n52/iceland/convert/RequestResponseModifierFacilitator.java) class defines the types of modification the *RequestResponseModifier* performs. This helps to reduce the processing, for example an identifier adding/removing should be done after/before another *RequestResponseModifier* performs an observation merging/splitting.

Currently, the *RequestResponseModifierFacilitator* supports *isMerger*, *isSplitter* and *isAdderRemover* but this can be extended with further functionality.

## Configuration


Back to [Index](Index.md)