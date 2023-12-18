package edu.kit.ipd.are.esda.aggregate;

import java.time.LocalDateTime;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import edu.kit.ipd.are.esda.Measurement;

@Component
public class EsdaListenerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsdaListenerController.class);
    private double denominator;
    private final int messagesPerAggregat;
    private int numerator;
    private final KafkaTemplate<String, Object> template;
    private final String topicName;

    public EsdaListenerController(final KafkaTemplate<String, Object> template,
            @Value("${esda.analysis.topic-name}") final String topicName,
            @Value("${esda.messages-per-aggregat}") final int messagesPerAggregat) {
        this.template = template;
        this.topicName = topicName;
        this.messagesPerAggregat = messagesPerAggregat;
        denominator = 0.0;
        numerator = 0;
    }

    @KafkaListener(topics = "${esda.aggregate.topic-name}",
            clientIdPrefix = "${spring.kafka.consumer.client-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(final ConsumerRecord<String, Measurement> cr,
            @Payload final Measurement payload) {

        LOGGER.info("[Received] " + payload);

        denominator += payload.getValue();
        numerator += 1;

        if (numerator < messagesPerAggregat) {
            return;
        }


        final Measurement data = new Measurement(payload.getPoint(), LocalDateTime.now().toString(),
                denominator / numerator);

        denominator = 0.0;
        numerator = 0;

        template.send(topicName, data);

        LOGGER.info("[Transmitted] " + data);
    }

}
