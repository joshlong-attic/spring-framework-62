package com.example.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.docker.compose.skip.in-tests=false")
class CustomerRepositoryTest {

    @Test
    void all(@Autowired CustomerRepository repository) {
        var all = repository.all();
        assertEquals(3, all.size());
    }

    @Test
    void byLanguage(@Autowired CustomerRepository repository) {
        var bySystemLanguage = repository.findCustomersBySystemLanguage();
        assertEquals(1, bySystemLanguage.size());
    }

    @Test
    void bySystemArch(@Autowired CustomerRepository repository) {
        var bySystemArch = repository.findCustomersHavingSameOperatingSystemAsUs();
        assertEquals(1, bySystemArch.size());
    }

}
 