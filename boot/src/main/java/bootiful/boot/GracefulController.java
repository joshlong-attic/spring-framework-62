package bootiful.boot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClient;

// macos: 		 31.6   /  0.047
// docker linux: 44.657 /  0.026
@Controller
@ResponseBody
class GracefulController {

	private final RestClient http;

	GracefulController(RestClient.Builder http) {
		this.http = http.build();
	}

	@GetMapping("/delay")
	String delay() {
		System.out.println("before request");
		var reply = this.http//
				.get()//
				.uri("https://httpbin.org/delay/5")// 
				.retrieve()//
				.body(String.class);
		System.out.println("after request");
		return reply;
	}

}
