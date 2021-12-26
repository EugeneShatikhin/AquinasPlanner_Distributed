package com.aquinas.servicetasks.repository.api;

import com.aquinas.servicetasks.repository.api.dto.TaskDTO;
import com.aquinas.servicetasks.repository.model.Task;
import com.aquinas.servicetasks.repository.model.TaskType;
import com.aquinas.servicetasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> index() {
        final List<Task> tasks = taskService.fetchAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> show(@PathVariable long id) {
        try {
            final Task task = taskService.fetchById(id);
            return ResponseEntity.ok(task);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    /*
    @GetMapping("/{id}/owner")
    public ResponseEntity<UserDTO> showUserById(@PathVariable long id) {
        try {
            final UserDTO userDto = groupService.getOwnerById(id);

            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    } */
    @PostMapping
    public ResponseEntity<JSONObject> create(@RequestBody TaskDTO task) {
        final String title = task.getTitle();
        final String description = task.getDescription();
        final Timestamp deadline = task.getDeadline();
        final long group_id = task.getGroup_id();
        final TaskType status = task.getStatus();
        try {
            final long id = taskService.create(title, description, deadline, group_id, status);
            final String location = String.format("/groups/%d", id);
            return ResponseEntity.created(URI.create(location)).build();
        } catch (IllegalArgumentException e) {
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody TaskDTO task) {
        final String title = task.getTitle();
        final String description = task.getDescription();
        final Timestamp deadline = task.getDeadline();
        final TaskType status = task.getStatus();
        try {
            taskService.update(id, title, description, deadline, status);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
