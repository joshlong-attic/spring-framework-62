package bootiful.framework;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Fallback;
import org.springframework.context.annotation.Primary;

@Configuration
class FallbackDemoConfiguration {

	@Bean
	@Fallback
	DefaultNoOpMessageProvider defaultNoOpFoo() {
		return new DefaultNoOpMessageProvider();
	}

	@Bean
	SimpleMessageProvider foo() {
		return new SimpleMessageProvider();
	}

	@Bean
	@Primary
	SophisticatedMessageProvider sophisticatedFoo() {
		return new SophisticatedMessageProvider();
	}

	@Bean
	ApplicationRunner fallbackDemoConfigurationRunner(MessageProvider messageProvider) {
		return args -> System.out.println(messageProvider.message());
	}

}

class DefaultNoOpMessageProvider implements MessageProvider {

	@Override
	public String message() {
		return "default noop implementation";
	}

}

class SimpleMessageProvider implements MessageProvider {

	@Override
	public String message() {
		return "simple implementation";
	}

}

class SophisticatedMessageProvider implements MessageProvider {

	@Override
	public String message() {
		return "\uD83E\uDD14 + \uD83C\uDFA9";
	}

}
