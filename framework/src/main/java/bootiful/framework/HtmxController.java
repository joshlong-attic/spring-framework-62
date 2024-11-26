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
import org.springframework.web.servlet.view.FragmentsRendering;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

/*
    @PostMapping
    FragmentsRendering addFragmentsRendering(@RequestParam("new-todo") String newTodo) {
        this.repository.save(new Todo(null, newTodo));
        var model = Map.of("todos", (Object) this.repository.findAll());
        return FragmentsRendering
                .with("todos :: todos")
                .fragment("todos :: todos-form", model)
                .headers(httpHeaders -> httpHeaders.setAccessControlMaxAge(100))
                .build();
    }
*/

    @PostMapping
    Collection<ModelAndView> addModelAndView(@RequestParam("new-todo") String newTodo) {
        this.repository.save(new Todo(null, newTodo));
        var todos = new ModelAndView("todos :: todos");
        var model = Map.of("todos", this.repository.findAll());
        var todosForm = new ModelAndView("todos :: todos-form", model);
        return List.of(todos, todosForm);
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