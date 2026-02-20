package vincenzocalvaruso.GestioneEventi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzocalvaruso.GestioneEventi.entity.User;
import vincenzocalvaruso.GestioneEventi.exceptions.ValidationException;
import vincenzocalvaruso.GestioneEventi.payloads.LoginDTO;
import vincenzocalvaruso.GestioneEventi.payloads.LoginRespondeDTO;
import vincenzocalvaruso.GestioneEventi.payloads.RegisterDTO;
import vincenzocalvaruso.GestioneEventi.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Validated RegisterDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            validationResult.getFieldErrors().forEach(err ->
                    System.out.println("Campo: " + err.getField() + " - Errore: " + err.getDefaultMessage())
            );
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return userService.register(body);
        }
    }

    @PostMapping("/login")
    public LoginRespondeDTO login(@RequestBody LoginDTO body) {
        return new LoginRespondeDTO(this.userService.checkCredenzialAndReturnToken(body));
    }
}
