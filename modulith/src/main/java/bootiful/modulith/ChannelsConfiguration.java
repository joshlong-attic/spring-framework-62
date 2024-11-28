package bootiful.modulith;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.DirectChannelSpec;
import org.springframework.integration.dsl.MessageChannels;

@Configuration
class ChannelsConfiguration {

	@Bean
	DirectChannelSpec events() {
		return MessageChannels.direct();
	}

}
