package bootiful.framework;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;

import static bootiful.framework.Messages.TWO;

@TestConfiguration
class PropertiesConfiguration {

	@Bean
	SimplePropertyRegistrar simplePropertyRegistrar() {
		return new SimplePropertyRegistrar();
	}

	static class SimplePropertyRegistrar implements DynamicPropertyRegistrar {

		@Override
		public void accept(DynamicPropertyRegistry registry) {
			registry.add("dynamic.message.two", () -> TWO);
		}

	}

}
