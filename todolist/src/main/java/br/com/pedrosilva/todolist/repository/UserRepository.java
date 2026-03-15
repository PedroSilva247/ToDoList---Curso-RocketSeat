package br.com.pedrosilva.todolist.repository;

import br.com.pedrosilva.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByCpf(String cpf);

}
