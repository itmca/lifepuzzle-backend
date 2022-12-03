package io.itmca.lifepuzzle.domain.user.endpoint;

import io.itmca.lifepuzzle.domain.user.endpoint.request.UserPasswordUpdateDto;
import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserWriteEndpoint {

    @PutMapping("/{id}")
    public void updateUser(@PathVariable long id, @RequestBody UserUpdateDto userUpdateDto) {}

    @PutMapping("/{id}/password")
    public void updateUserPassword(@PathVariable long id, @RequestBody UserPasswordUpdateDto userPasswordUpdateDto) {}

}
