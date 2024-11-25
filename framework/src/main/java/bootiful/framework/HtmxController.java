package bootiful.framework;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
class HtmxController {

    private final List<Customer> customers = List.of(
            new Customer(1, "josh"),
            new Customer(2, "gino"));

    @GetMapping("/customers")
    String renderCustomers(Model model) {
        model.addAttribute("customers", customers);
        return "customers";
    }

}

record Customer(int id, String name) {
}