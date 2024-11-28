package bootiful.modulith.consumer;

import bootiful.modulith.CrossModuleEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.GenericHandler;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.dsl.Files;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
class IntegrationConsumerConfiguration {

    @Bean
    IntegrationFlow integrationFlow(
            @Value("file:${user.home}/Desktop/outbound") File destination,
            MessageChannel events) {
        var outboundAdapter = Files.outboundAdapter(destination).autoCreateDirectory(true);
        return IntegrationFlow
                .from(events)
                .handle((GenericHandler<CrossModuleEvent>) (payload, headers) ->
                        payload.instant().toString())
                .handle(outboundAdapter)
                .get();
    }

}
