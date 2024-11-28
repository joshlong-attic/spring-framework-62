package bootiful.modulith;

import org.springframework.modulith.events.Externalized;

import java.time.Instant;

@Externalized("events")
public record CrossModuleEvent(Instant instant) {
}
