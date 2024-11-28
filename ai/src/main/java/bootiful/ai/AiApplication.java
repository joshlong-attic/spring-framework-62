package bootiful.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class AiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }

}

@Configuration
class AiConfiguration {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder, FunctionCallback weatherFunctionCallback) {
        return builder
//                .defaultFunctions("requestWeatherResponseFunction")
                .defaultFunctions(weatherFunctionCallback)
                .build();
    }

    /*
    @Bean
    @Description("returns the weather for a given city")
    Function<WeatherRequest, WeatherResponse> requestWeatherResponseFunction() {
        return wr -> new WeatherResponse(wr.city(),
                wr.city().equalsIgnoreCase("san francisco") ? 68.0f : 72.0f);
    }
    */
    
    private WeatherResponse weatherFor(WeatherRequest wr) {
        return new WeatherResponse(wr.city(),
                wr.city().equalsIgnoreCase("san francisco") ? 68.0f : 72.0f);
    }

    @Bean
    FunctionCallback weatherFunctionCallback() {
        return FunctionCallback
                .builder()
                .description("returns the weather for a given city")
                .function("getCurrentWeatherForACity", this::weatherFor)
                .inputType(WeatherRequest.class)
                .build();
    }

}

record WeatherRequest(String city) {
}

record WeatherResponse(String city, float temperature) {
}
