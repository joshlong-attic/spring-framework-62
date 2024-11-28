package com.example.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.http.config.EnableControlBusController;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableControlBusController
class ControlBusConfiguration {

	@Bean
	IntegrationFlow controlBusFlow(MessageChannel controlBusFlowMessageChannel) {
		return IntegrationFlow
				.from(controlBusFlowMessageChannel)
				.controlBusOnRegistry()
				.get();
	}

	@Bean
	DirectChannelSpec controlBusFlowMessageChannel() {
		return MessageChannels.direct();
	}

	@Bean
	MyOperationsManagedResource myOperationsManagedResource() {
		return new MyOperationsManagedResource();
	}
	
	@ManagedResource
	public static class MyOperationsManagedResource {

		static final AtomicInteger COUNTER = new AtomicInteger(0);

		@ManagedOperation(description = "Update the magic number")
		public void updateMagicNumber(int magicNumber) {
			System.out.println("doSomething with magic number " + magicNumber);
			COUNTER.incrementAndGet();
		}

	}

}
 