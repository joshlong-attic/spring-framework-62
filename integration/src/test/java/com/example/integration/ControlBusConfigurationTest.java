package com.example.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ControlBusConfigurationTest {

	@Test
	void controlBusConfigurationRunner(@Autowired MessageChannel controlBusFlowMessageChannel) {
		var msg = MessageBuilder
				.withPayload("myOperationsManagedResource.updateMagicNumber")
				.setHeaderIfAbsent(IntegrationMessageHeaderAccessor.CONTROL_BUS_ARGUMENTS, List.of(42))
				.build();
		assertEquals(ControlBusConfiguration.MyOperationsManagedResource.COUNTER.get(), 0,
				"there should be zero invocations of the control bus thus far");
		controlBusFlowMessageChannel.send(msg);
		assertEquals(ControlBusConfiguration.MyOperationsManagedResource.COUNTER.get(), 1,
				"there should be one invocation of the control bus thus far");
	}

}