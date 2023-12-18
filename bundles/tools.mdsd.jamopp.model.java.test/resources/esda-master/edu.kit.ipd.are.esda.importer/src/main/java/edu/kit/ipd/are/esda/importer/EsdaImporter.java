package edu.kit.ipd.are.esda.importer;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@SpringBootApplication
public class EsdaImporter {

    public static void main(final String[] args) {
        SpringApplication.run(EsdaImporter.class, args);
    }

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${esda.aggregate.topic-name}")
    private String topicId;

    @Bean
    public NewTopic aggregateTopic() {
        return new NewTopic(topicId, 1, (short) 1);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<String, Object>(producerFactory());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        final Map<String, Object> properties =
                new HashMap<String, Object>(kafkaProperties.buildProducerProperties());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return properties;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<String, Object>(producerConfigs());
    }

}
