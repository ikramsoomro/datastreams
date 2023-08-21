# datastreams

To build: 
gradle cleam build

To run the spring boot app:
java DataStreamsApplication

To run KafkaTestDataPublisherRunner to publish data to the topics. 
java KafkaTestDataPublisherRunner positive

- positive : to publilsh valid test data
- negative : to publilsh invalid test data
- all: both positive and negative
