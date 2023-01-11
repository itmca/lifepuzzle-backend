package io.itmca.lifepuzzle.domain.register.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NicknameProvideService {

    public String getRandomNickname(String id) {
        var randomNicknames = new String[]{"노른자색", "청포도색", "나팔꽃색", "꽃분홍", "감자색", "낙엽색", "진초록", "녹차색"};
        var hash = this.getHashNumber(id);
        return randomNicknames[(int) hash % randomNicknames.length];
    }

    private long getHashNumber(String id) {
        var hash = 0;
        for (int i = 0; i < id.length(); i++) {
            var ch = id.codePointAt(i);
            hash += ch;
        }
        return hash + Instant.now().toEpochMilli();
    }
}
