package com.lab.datastreams.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class KafkaTestDataPublisherRunner {

    @Autowired
    private TestDataPublisher testDataPublisher;

    public static void main(String[] args) {
        // Start the Spring Boot application
        ConfigurableApplicationContext context = SpringApplication.run(KafkaTestDataPublisherRunner.class, args);

        // Retrieve the TestDataPublisher bean
        TestDataPublisher testDataPublisher = context.getBean(TestDataPublisher.class);

        // Check arguments for data type: "positive" or "negative"
        if (args.length > 0) {
            String dataType = args[0].toLowerCase();

            switch (dataType) {
                case "positive":
                    testDataPublisher.populateKafkaTopics(300, 2, 2, false);  // Modify duration and interval as needed
                    break;
                case "negative":
                    testDataPublisher.populateKafkaTopics(300,2, 2, true);  // Modify duration and interval as needed
                    break;
                default:
                    testDataPublisher.populateKafkaTopics(300, 2, 2, false);  // Modify duration and interval as needed
                    testDataPublisher.populateKafkaTopics(300,2, 2, true);  // Modify duration and interval as needed
                    break;
            }
        } else {
            System.out.println("No arguments provided. Please provide 'positive' or 'negative' as an argument.");
        }

        // Close the Spring context
        context.close();
    }
}

