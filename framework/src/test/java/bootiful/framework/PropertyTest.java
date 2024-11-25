package bootiful.framework;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static bootiful.framework.Messages.ONE;
import static bootiful.framework.Messages.TWO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(PropertiesConfiguration.class)
class PropertyTest {

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("dynamic.message.one", () -> ONE);
	}

	@Test
	void properties(@Autowired ApplicationContext ac) {
		var environment = ac.getEnvironment();
		assertEquals(ONE, environment.getProperty("dynamic.message.one"));
		assertEquals(TWO, environment.getProperty("dynamic.message.two"));
	}

}
