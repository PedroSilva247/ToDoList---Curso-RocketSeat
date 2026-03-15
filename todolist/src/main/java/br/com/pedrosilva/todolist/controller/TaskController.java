package br.com.pedrosilva.todolist.controller;

import br.com.pedrosilva.todolist.model.Task;
import br.com.pedrosilva.todolist.repository.TaskRepository;
import br.com.pedrosilva.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Task task, HttpServletRequest request) {
        UUID idUser = (UUID) request.getAttribute("idUser");
        task.setIdUser(idUser);

        LocalDateTime currentDate = LocalDateTime.now();
        if(task.getStartAt().isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser menor que a data término!");
        }
        if(currentDate.isAfter(task.getStartAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser maior que a data atual!");
        }
        if(currentDate.isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de término deve ser maior que a data atual!");
        }
        // System.out.println("Chegou no controller");

        Task taskCrated = this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.taskRepository.save(taskCrated));
    }

    @GetMapping("/")
    public List<Task> list(HttpServletRequest request) {
        return this.taskRepository.findByIdUser((UUID) request.getAttribute("idUser"));
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody Task task, HttpServletRequest request, @PathVariable UUID id) {
        var originalTask = this.taskRepository.findById(id).orElse(null);

        if(originalTask == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        if(!originalTask.getIdUser().equals((UUID) request.getAttribute("idUser"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autorizado");
        }


        Utils.copyNonNullPropertys(task, originalTask);

        return ResponseEntity.ok().body(this.taskRepository.save(originalTask));
    }
}
