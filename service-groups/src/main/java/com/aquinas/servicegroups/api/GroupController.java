package com.aquinas.servicegroups.api;

import com.aquinas.servicegroups.api.dto.GroupDTO;
import com.aquinas.servicegroups.api.dto.TaskDTO;
import com.aquinas.servicegroups.api.dto.UserDTO;
import com.aquinas.servicegroups.repository.model.Group;
import com.aquinas.servicegroups.service.GroupService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<Group>> index() {
        final List<Group> groups = groupService.fetchAll();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> show(@PathVariable long id) {
        try {
            final Group group = groupService.fetchById(id);
            return ResponseEntity.ok(group);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // endpoint for when deadline filter is present
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDTO>> showTasksById(@PathVariable long id, Optional<String> showingTill) {
        try {
            final List<TaskDTO> taskDTO;
            if(showingTill.isPresent()) {
                taskDTO = groupService.getTasksByGroupId(id, Timestamp.valueOf(showingTill.get()));
            }
            else {
                taskDTO = groupService.getTasksByGroupId(id, null);
            }
            return ResponseEntity.ok(taskDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // endpoint when no deadline filter is present

    @GetMapping("/{id}/owner")
    public ResponseEntity<UserDTO> showUserById(@PathVariable long id) {
        try {
            final UserDTO userDto = groupService.getOwnerById(id);

            return ResponseEntity.ok(userDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<JSONObject> create(@RequestBody GroupDTO group) {
        final String title = group.getTitle();
        final String desc = group.getDesc();
        final long owner_id = group.getOwner_id();
        try {
            final long id = groupService.create(title, desc, owner_id);
            final String location = String.format("/users/%d", id);
            return ResponseEntity.created(URI.create(location)).build();
        } catch (IllegalArgumentException e) {
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody GroupDTO group) {
        final String title = group.getTitle();
        final String desc = group.getDesc();
        try {
            groupService.update(id, title, desc);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
