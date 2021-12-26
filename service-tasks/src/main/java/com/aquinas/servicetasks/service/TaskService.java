package com.aquinas.servicetasks.service;

import com.aquinas.servicetasks.repository.TaskRepo;
import com.aquinas.servicetasks.repository.api.dto.GroupDTO;
import com.aquinas.servicetasks.repository.model.Task;
import com.aquinas.servicetasks.repository.model.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class TaskService {
    private final TaskRepo taskRepo;
    private final String groupUrl = "http://service-groups:8081/groups/";
    public List<Task> fetchAll() {
        return taskRepo.findAll();
    }

    public Task fetchById(long id) throws IllegalArgumentException {
        final Optional<Task> byId = taskRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("Task with id = %d was not found", id));
        return byId.get();
    }

    public long create(String title, String description, Timestamp deadline, long group_id, TaskType status) {
        // check if given group exists
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> request = new HttpEntity<>(group_id);
        try {
            final ResponseEntity<GroupDTO> response = restTemplate.exchange(groupUrl + group_id, HttpMethod.GET, request, GroupDTO.class);
        } catch(HttpClientErrorException e) { throw new IllegalArgumentException(String.format("Group with id = %d was not found", group_id));}
        // check if task with given name already exists
        final Optional<Task> byName = Optional.ofNullable(taskRepo.findByTitle(title));
        if (byName.isPresent()) throw new IllegalArgumentException(String.format("Task with title = %s already exists", title));
        final Task task = new Task(title, description, Timestamp.from(Instant.now()), deadline, group_id, status);
        final Task save = taskRepo.save(task);
        return save.getId();
    }

    public void update(long id, String title, String description, Timestamp deadline, TaskType status)
            throws IllegalArgumentException {
        final Optional<Task> byId = taskRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("Task with id = %d was not found", id));
        final Optional<Task> byName = Optional.ofNullable(taskRepo.findByTitle(title));
        if (byName.isPresent()) throw new IllegalArgumentException(String.format("Task with title = %s already exists", title));
        final Task task = byId.get();
        if(title != null && !title.isEmpty()) task.setTitle(title);
        if(description != null && !description.isEmpty()) task.setDescription(description);
        if(deadline != null && deadline.after(task.getCreated())) task.setDeadline(deadline);
        if(status != null) task.setStatus(status);
        taskRepo.save(task);
    }

    public void delete(long id) {
        taskRepo.deleteById(id);
    }
}
