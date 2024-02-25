package com.example.kafkastreams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaStreamsInfrastructureCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CustomInfrastructureCustomizer implements KafkaStreamsInfrastructureCustomizer {

    private static final Serde<Long> KEY_SERDE = Serdes.Long();
    private static final Serde<String> VALUE_SERDE = Serdes.String();

    @Override
    public void configureBuilder(StreamsBuilder builder) {
        builder
                .stream("stream-topic-sample", Consumed.with(KEY_SERDE, VALUE_SERDE))
                .filter((key, value) -> value!= null && value.length() > 1)
                .groupBy((key, value) -> value.length())
                .reduce((acc, value) -> value, Materialized.<Integer, String, KeyValueStore<Bytes, byte[]>>as("usecase1-gc-basic-reduce").withValueSerde(VALUE_SERDE));
    }
}