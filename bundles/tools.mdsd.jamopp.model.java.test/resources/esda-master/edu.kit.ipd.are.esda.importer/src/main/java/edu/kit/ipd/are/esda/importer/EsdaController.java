package edu.kit.ipd.are.esda.importer;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.kit.ipd.are.esda.Measurement;

@RestController
public class EsdaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsdaController.class);
    private final KafkaTemplate<String, Object> template;
    private final String topicName;

    public EsdaController(final KafkaTemplate<String, Object> template,
            @Value("${esda.aggregate.topic-name}") final String topicName) {
        this.template = template;
        this.topicName = topicName;
    }

    @RequestMapping("/add/{point}/{measurement}")
    public String addMeasurement(@PathVariable final String point,
            @PathVariable final double measurement) {

        final Measurement data =
                new Measurement(point, LocalDateTime.now().toString(), measurement);

        template.send(topicName, data);

        LOGGER.info("[Transmitted] " + data);
        return data.toString();
    }

}
