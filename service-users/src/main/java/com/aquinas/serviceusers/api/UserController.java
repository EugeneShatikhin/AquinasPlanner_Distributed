package com.aquinas.serviceusers.api;

import com.aquinas.serviceusers.api.dto.GroupDTO;
import com.aquinas.serviceusers.api.dto.UserDTO;
import com.aquinas.serviceusers.repository.model.User;
import com.aquinas.serviceusers.service.UserService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> index() {
        final List<User> users = userService.fetchAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable long id) {
        try {
            final User user = userService.fetchById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/groups")
    public ResponseEntity<List<GroupDTO>> showGroupsById(@PathVariable long id) {
        try {
            final List<GroupDTO> groups = userService.getGroupsByOwnerId(id);

            return ResponseEntity.ok(groups);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<JSONObject> create(@RequestBody UserDTO user) {
        final String username = user.getUsername();
        final String password = user.getPassword();
        final String firstname = user.getFirstname();
        final String lastname = user.getLastname();
        final String bio = user.getBio();
        try {
            final long id = userService.create(username, password, firstname, lastname, bio);
            final String location = String.format("/users/%d", id);
            return ResponseEntity.created(URI.create(location)).build();
        } catch (IllegalArgumentException e) {
            JSONObject response = new JSONObject();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody UserDTO user) {
        final String username = user.getUsername();
        final String password = user.getPassword();
        final String firstname = user.getFirstname();
        final String lastname = user.getLastname();
        final String bio = user.getBio();
        try {
            userService.update(id, username, password, firstname, lastname, bio);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
