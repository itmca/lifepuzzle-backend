package io.itmca.lifepuzzle.domain.user.service;

import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserQueryService {

    private final UserRepository userRepository;

    @Autowired
    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserNo(long userNo) {
        return new User();
    }

    public User findByUserId(String userId) {
        return this.userRepository.findByUserId(userId);
    }

    public User findByEmail(String email) {
        return new User();
    }

    public User findByKakaoId(String kakaoId) {
        return new User();
    }

    public User findByAppleId(String appleId) {
        return new User();
    }
}
