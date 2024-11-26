package bootiful.framework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.convention.TestBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@SuppressWarnings("unused")
class GreetingsControllerTest {

	private static final String TEST_MESSAGE = "this is a first message";

	private final MockMvcTester mockMvc;

	@TestBean
	private MessageProvider messageProvider;

	static MessageProvider messageProvider() {
		return () -> TEST_MESSAGE;
	}

	GreetingsControllerTest(@Autowired WebApplicationContext wac) {
		this.mockMvc = MockMvcTester.from(wac);
	}

	@Test
	void message() throws Exception {
		var mvcTestResult = this.mockMvc
				.get()
				.uri("/hello")
				.accept(MediaType.APPLICATION_JSON)
				.exchange();
		Assertions.assertThat(mvcTestResult.getResponse().getContentAsString())
				.contains(TEST_MESSAGE);
	}

}
