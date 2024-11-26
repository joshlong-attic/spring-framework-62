package bootiful.framework;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


@Component
class Initializer {

    private final TodoRepository repository;

    Initializer(TodoRepository repository) {
        this.repository = repository;
    }

    @EventListener({ApplicationReadyEvent.class, TodosResetEvent.class})
    void reset() {
        this.repository.deleteAll();
        Stream.of(
                        "write a new blog",
                        "record a video on HTMX",
                        "record a new podcast episode"
                ) 
                .forEach(t -> this.repository.save(new Todo(null, t)));
    }
}

class TodosResetEvent extends ApplicationEvent {
    TodosResetEvent() {
        super(Instant.now());
    }
}

@Controller
@RequestMapping("/todos")
class TodosController {

    private final TodoRepository repository;

    private final ApplicationEventPublisher publisher;

    TodosController(TodoRepository repository,
                    ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @PostMapping("/reset")
    String reset(Model model) {
        this.publisher.publishEvent(new TodosResetEvent());
        model.addAttribute("todos", this.repository.findAll());
        return "todos :: todos";
    }

    @PostMapping
    Collection<ModelAndView> add(@RequestParam("new-todo") String newTodo, Model model) {
        this.repository.save(new Todo(null, newTodo));
        model.addAttribute("todos", this.repository.findAll());
        return List.of(new ModelAndView("todos :: todos"),
                new ModelAndView("todos :: todos-form"));
    }

    @ResponseBody
    @DeleteMapping(path = "/{todoId}", produces = MediaType.TEXT_HTML_VALUE)
    String delete(@PathVariable Integer todoId) {
        this.repository.findById(todoId).ifPresent(this.repository::delete);
        return "";
    }

    @GetMapping
    String todos(Model model) {
        model.addAttribute("todos", this.repository.findAll());
        return "todos";
    }
}


interface TodoRepository extends CrudRepository<Todo, Integer> {
}

record Todo(@Id Integer id, String title) {
}