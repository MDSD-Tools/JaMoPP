package edu.kit.ipd.are.esda.analysis;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import edu.kit.ipd.are.esda.Measurement;

@Component
public class EsdaListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsdaListener.class);

    @KafkaListener(topics = "${esda.analysis.topic-name}",
            clientIdPrefix = "${spring.kafka.consumer.client-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(final ConsumerRecord<String, Measurement> cr,
            @Payload final Measurement payload) {

        // TODO The analysis
        LOGGER.info("[Received] " + payload);
    }

}
