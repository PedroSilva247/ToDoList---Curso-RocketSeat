package br.com.pedrosilva.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.pedrosilva.todolist.model.User;
import br.com.pedrosilva.todolist.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/create")
    public ResponseEntity create(@RequestBody User user) {

        if(this.userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome de usuário já cadastrado!");
        } else if (this.userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já cadastrado!");
        } else if (this.userRepository.findByCpf(user.getCpf()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cpf já cadastrado!");
        }

        var passwordHashred = BCrypt.withDefaults()
                .hashToString(12,user.getPassword().toCharArray());
        user.setPassword(passwordHashred);
        var userCrated = this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(userCrated);
    }
}
