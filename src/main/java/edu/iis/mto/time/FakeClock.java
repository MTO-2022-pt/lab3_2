package edu.iis.mto.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FakeClock extends Clock {

    private final Instant WHEN_STARTED = Instant.now();
    private final ZoneId DEFAULT_TZONE = ZoneId.systemDefault();
    private long count = 0;

    public Instant nextInstant(long plusSeconds) {
        count += plusSeconds;
        return WHEN_STARTED.plusSeconds(count);
    }

    @Override
    public ZoneId getZone() {
        return DEFAULT_TZONE;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return Clock.fixed(WHEN_STARTED, zone);
    }

    @Override
    public Instant instant() {
        return WHEN_STARTED.plusSeconds(count);
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.ofInstant(WHEN_STARTED.plusSeconds(count), DEFAULT_TZONE);
    }

}
