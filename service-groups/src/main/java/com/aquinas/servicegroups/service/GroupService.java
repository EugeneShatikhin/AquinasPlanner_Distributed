package com.aquinas.servicegroups.service;

import com.aquinas.servicegroups.api.dto.TaskDTO;
import com.aquinas.servicegroups.api.dto.UserDTO;
import com.aquinas.servicegroups.repository.GroupRepo;
import com.aquinas.servicegroups.repository.model.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class GroupService {
    private final GroupRepo groupRepo;
    private final String userUrl = "http://localhost:8080/users/";
    private final String taskUrl = "http://localhost:8082/tasks/";
    public List<Group> fetchAll() {
        return groupRepo.findAll();
    }

    public Group fetchById(long id) throws IllegalArgumentException {
        final Optional<Group> byId = groupRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("Group with id = %d was not found", id));
        return byId.get();
    }
    public UserDTO getOwnerById(long id) {
        final Optional<Group> byId = groupRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("Group with id = %d was not found", id));
        long owner_id = byId.get().getOwner_id();
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> request = new HttpEntity<>(owner_id);
        final ResponseEntity<UserDTO> response = restTemplate.exchange(userUrl + owner_id, HttpMethod.GET, request, UserDTO.class);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new IllegalArgumentException(String.format("User with id = %d was not found", owner_id));
        }
        return response.getBody();
    }
    public List<TaskDTO> getTasksByGroupId(long id, Timestamp filter) {
        final Optional<Group> byId = groupRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("Group with id = %d was not found", id));
        final Group group = byId.get();
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<List<TaskDTO>> response = restTemplate.exchange(taskUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<TaskDTO>>() {});
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new IllegalArgumentException(String.format("Group with id = %d has no tasks", group.getId()));
        }
        List<TaskDTO> group_tasks = new ArrayList<TaskDTO>();
        for (TaskDTO task : Objects.requireNonNull(response.getBody())) {
            if (task.getGroup_id() == group.getId()) {
                if (filter != null) {
                    if (task.getDeadline().before(filter)) {
                        group_tasks.add(task);
                    }
                }
                else group_tasks.add(task);
            }
        }
        return group_tasks;
    }
    public long create(String title, String desc, long owner_id) {
        // check if user with owner_id exists
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Long> request = new HttpEntity<>(owner_id);
        try {
            final ResponseEntity<UserDTO> response = restTemplate.exchange(userUrl + owner_id, HttpMethod.GET, request, UserDTO.class);
        } catch(HttpClientErrorException e) { throw new IllegalArgumentException(String.format("User with id = %d was not found", owner_id));}
        // check if group with given title already exists
        final Optional<Group> byName = Optional.ofNullable(groupRepo.findByTitle(title));
        if (byName.isPresent()) throw new IllegalArgumentException(String.format("Group with title = %s already exists", title));
        final Group group = new Group(title, desc, owner_id);
        final Group save = groupRepo.save(group);
        return save.getId();
    }

    public void update(long id, String title, String desc)
            throws IllegalArgumentException {
        final Optional<Group> byId = groupRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("Group with id = %d was not found", id));
        final Optional<Group> byName = Optional.ofNullable(groupRepo.findByTitle(title));
        if (byName.isPresent()) throw new IllegalArgumentException(String.format("Group with title = %s already exists", title));
        final Group group = byId.get();
        if(title != null && !title.isEmpty()) group.setTitle(title);
        if(desc != null && !desc.isEmpty()) group.setDescription(desc);
        groupRepo.save(group);
    }

    public void delete(long id) {
        groupRepo.deleteById(id);
    }
}
