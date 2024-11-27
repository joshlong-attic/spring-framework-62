package com.example.security;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.core.io.ClassPathResource;

import java.util.Set;

class UiResourcesRuntimeHintsRegistrar implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		for (var r : Set.of("org/springframework/security/spring-security-webauthn.js",
				"org/springframework/security/default-ui.css"))
			hints.resources().registerResource(new ClassPathResource(r));
	}

}
