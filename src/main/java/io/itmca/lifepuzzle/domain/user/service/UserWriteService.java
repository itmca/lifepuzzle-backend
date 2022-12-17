package io.itmca.lifepuzzle.domain.user.service;

import io.itmca.lifepuzzle.domain.user.endpoint.request.UserUpdateRequest;
import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserWriteService {

    private final UserRepository userRepository;

    public User save(User user, UserUpdateRequest userUpdateRequest) {
        return userRepository.save(user);
    }

    @Transactional
    public void updateUserPassword(User user, String password) {
        user.changePassword(password);
    }

    public void deleteByUserNo(Long userNo) {
        userRepository.deleteById(userNo);
    }

    @Transactional
    public void changeRecentHeroNo(User user, Long heroNo) {
        user.changeRecentHeroNo(heroNo);
    }
}