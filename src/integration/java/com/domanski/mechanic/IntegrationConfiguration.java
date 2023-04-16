package com.domanski.mechanic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@Configuration
@Profile("integration")
public class IntegrationConfiguration {

    @Bean
    @Primary
    Clock clock() {
        return Clock.fixed(Instant.parse("2022-05-05T11:55:00.00Z"), ZoneOffset.UTC);
    }
}
