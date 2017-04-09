package de.weltraumschaf.maconha.frontend.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import de.weltraumschaf.maconha.model.User;
import de.weltraumschaf.maconha.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public final class HelloWorldRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldRestController.class);

    /**
     * Service which will do all data retrieval/manipulation work
     */
    @Autowired
    private UserService userService;

    /**
     * Retrieve All Users.
     *
     * @return
     */
    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        final List<User> users = userService.findAllUsers();

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Retrieve Single User.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        LOGGER.debug("Fetching User with {}.", id);
        User user = userService.findById(id);

        if (user == null) {
            LOGGER.debug("User with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Create a User.
     *
     * @param user
     * @param ucBuilder
     * @return
     */
    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        LOGGER.debug("Creating User {}.", user.getUsername());

        if (userService.isUserExist(user)) {
            LOGGER.debug("A User with name {} already exist.", user.getUsername());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        userService.saveUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Update a User.
     *
     * @param id
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        LOGGER.debug("Updating User {}.", id);
        User currentUser = userService.findById(id);

        if (currentUser == null) {
            LOGGER.debug("User with id {} not found.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        currentUser.setUsername(user.getUsername());
        currentUser.setPassword(user.getPassword());

        userService.updateUser(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    /**
     * Delete a User.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        LOGGER.debug("Fetching & Deleting User with id {}.", id);
        User user = userService.findById(id);

        if (user == null) {
            LOGGER.debug("Unable to delete. User with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Delete All Users.
     *
     * @return
     */
    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteAllUsers() {
        LOGGER.debug("Deleting All Users");

        userService.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
