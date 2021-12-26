package com.aquinas.serviceusers.service;

import com.aquinas.serviceusers.api.dto.GroupDTO;
import com.aquinas.serviceusers.repository.UserRepo;
import com.aquinas.serviceusers.repository.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public final class UserService {
    private final UserRepo userRepo;
    private final String groupUrl = "http://service-groups:8081/groups/";
    public List<User> fetchAll() {
        return userRepo.findAll();
    }

    public User fetchById(long id) throws IllegalArgumentException {
        final Optional<User> byId = userRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("User with id = %d was not found", id));
        return byId.get();
    }
    public List<GroupDTO> getGroupsByOwnerId(long id) {
        final Optional<User> byId = userRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("User with id = %d was not found", id));
        final User user = byId.get();
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<List<GroupDTO>> response = restTemplate.exchange(groupUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<GroupDTO>>() {});
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new IllegalArgumentException(String.format("User with id = %d has no groups", user.getId()));
        }
        List<GroupDTO> user_groups = new ArrayList<GroupDTO>();
        for (GroupDTO group : Objects.requireNonNull(response.getBody())) {
            if (group.getOwner_id() == user.getId()) {
                user_groups.add(group);
            }
        }
        return user_groups;
    }
    public long create(String username, String password, String firstname, String lastname, String bio) {
        final Optional<User> byName = Optional.ofNullable(userRepo.findByUsername(username));
        if (byName.isPresent()) throw new IllegalArgumentException(String.format("User with username = %s already exists", username));
        final User user = new User(username, password, firstname, lastname, bio);
        final User save = userRepo.save(user);
        return save.getId();
    }

    public void update(long id, String username, String password, String firstname, String lastname, String bio)
    throws IllegalArgumentException {
        final Optional<User> byId = userRepo.findById(id);
        if (byId.isEmpty()) throw new IllegalArgumentException(String.format("User with id = %d was not found", id));
        final Optional<User> byName = Optional.ofNullable(userRepo.findByUsername(username));
        if (byName.isPresent()) throw new IllegalArgumentException(String.format("User with username = %s already exists", username));
        final User user = byId.get();
        if(username != null && !username.isEmpty()) user.setUsername(username);
        if(password != null && !password.isEmpty()) user.setPassword(password);
        if(firstname != null && !firstname.isEmpty()) user.setFirstname(firstname);
        if(lastname != null && !lastname.isEmpty()) user.setLastname(lastname);
        if(bio != null && !bio.isEmpty()) user.setBio(bio);
        userRepo.save(user);
    }

    public void delete(long id) {
        userRepo.deleteById(id);
    }
}
