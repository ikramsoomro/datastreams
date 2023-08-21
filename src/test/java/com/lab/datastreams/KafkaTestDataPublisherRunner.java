package com.lab.datastreams;

import com.lab.datastreams.util.TestDataPublisher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaTestDataPublisherRunner {

    @Autowired
    private TestDataPublisher testDataPublisher;

    @Test
    public void testPopulateKafkaTopicsPositiveData() {
        testDataPublisher.populateKafkaTopics(300, 60, 10, false); // Running for 5 minutes (300 seconds), generating valid RegistrationEvents every 10 seconds and valid SaleEvents every 60 seconds
    }

    @Test
    public void testPopulateKafkaTopicsNegativeData() {
        testDataPublisher.populateKafkaTopics(300, 65, 10, true); // Running for 5 minutes (300 seconds), generating invalid RegistrationEvents every 15 seconds and invalid SaleEvents every 75 seconds
    }
}


