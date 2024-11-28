package bootiful.ai;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiConfigurationTest {

	@Test
	void functionCallbacks(@Autowired ChatClient cc) {
		var weatherInSf = cc
				.prompt("give me the weather for the city of san francisco") 
				.call() 
				.content();
		Assertions.assertNotNull(weatherInSf);
		Assertions.assertTrue(weatherInSf.contains("68"));
	}

}