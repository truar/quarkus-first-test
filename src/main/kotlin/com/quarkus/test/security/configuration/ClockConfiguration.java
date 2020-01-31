package com.quarkus.test.security.configuration;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import java.time.Clock;

@Dependent
public class ClockConfiguration {

    @Produces
    public Clock tracer() {
        return Clock.systemUTC();
    }

}
