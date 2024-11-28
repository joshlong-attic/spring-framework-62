package bootiful.modulith.consumer;

import bootiful.modulith.CrossModuleEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.modulith.events.ApplicationModuleListener;

@Configuration
class ConsumerConfiguration {

	@ApplicationModuleListener
	void consume(CrossModuleEvent crossModuleEvent) {
		System.out.println("got the event " + crossModuleEvent);
	}

}
