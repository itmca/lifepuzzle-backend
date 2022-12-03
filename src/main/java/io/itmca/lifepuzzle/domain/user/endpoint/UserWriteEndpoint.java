package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.endpoint.request.UserPasswordUpdateRequest;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserWriteEndpoint {

    @PutMapping("/{id}")
    public void updateUser(@PathVariable long id, @RequestBody UserUpdateRequest userUpdateDto) {
    }

    @PutMapping("/{id}/password")
    public void updateUserPassword(@PathVariable long id, @RequestBody UserPasswordUpdateRequest userPasswordUpdateDto) {
    }

}
