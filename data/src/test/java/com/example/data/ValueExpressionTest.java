package com.example.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.expression.ValueEvaluationContext;
import org.springframework.data.expression.ValueExpressionParser;
import org.springframework.data.expression.ValueParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;

@SpringBootTest(properties = "spring.docker.compose.skip.in-tests=false")
@Import(ValueExpressionDynamicPropertyRegistrar.class)
class ValueExpressionTest {

    @Test
    void test(@Autowired Environment environment) {
        var configuration = (ValueParserConfiguration) SpelExpressionParser::new;
        var context = ValueEvaluationContext.of(environment, new StandardEvaluationContext());
        var parser = ValueExpressionParser.create(configuration);
        
        var expression = parser.parse("${message}-#{ (6 * 7) + ''}");
        
        var result = expression.evaluate(context);
        Assertions.assertEquals("ni hao-42", result);
    }

}

@TestConfiguration
class ValueExpressionDynamicPropertyRegistrar implements DynamicPropertyRegistrar {

    @Override
    public void accept(DynamicPropertyRegistry registry) {
        registry.add("message", () -> "ni hao");
    }

}