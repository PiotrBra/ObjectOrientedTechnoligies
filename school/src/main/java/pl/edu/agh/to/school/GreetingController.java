package pl.edu.agh.to.school;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping
    public String greeting() {
        return "Technologie obiektowe";
    }
}
