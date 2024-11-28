package bootiful.modulith.producer;

import bootiful.modulith.CrossModuleEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
class MessagePublishingApplicationRunner {

	private final ApplicationEventPublisher publisher;

	MessagePublishingApplicationRunner(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	@Scheduled(initialDelay = 1, timeUnit = TimeUnit.SECONDS)
	public void run() {
		this.publisher.publishEvent(new CrossModuleEvent(Instant.now()));
	}

}