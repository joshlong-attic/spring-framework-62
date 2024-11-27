package com.example.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

@Controller
@ResponseBody
class SecuredController {

	@GetMapping("/admin")
	Map<String, String> admin(Principal principal) {
		return Map.of("admin", principal.getName());
	}

	@GetMapping("/")
	Map<String, String> hello(Principal principal) {
		return Map.of("user", principal.getName());
	}

}
