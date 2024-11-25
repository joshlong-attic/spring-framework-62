package bootiful.framework;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
class GreetingsController {

    private final MessageProvider messageProvider;

    GreetingsController(MessageProvider messageProvider) {
        this.messageProvider = messageProvider;
    }

    @GetMapping("/hello")
    String hello() {
        return this.messageProvider.message();
    }
}
