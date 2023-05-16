package io.itmca.lifepuzzle.domain.user.service;

import io.itmca.lifepuzzle.domain.user.entity.User;
import io.itmca.lifepuzzle.domain.user.repository.UserRepository;
import io.itmca.lifepuzzle.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    public User findByUserNo(long userNo) {
        return userRepository.findById(userNo)
                .orElseThrow(() -> new UserNotFoundException(userNo));
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    public User findByKakaoId(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

    public User findByAppleId(String appleId) {
        return userRepository.findByAppleId(appleId)
                .orElseThrow(() -> new IllegalArgumentException("no such data"));
    }

}
