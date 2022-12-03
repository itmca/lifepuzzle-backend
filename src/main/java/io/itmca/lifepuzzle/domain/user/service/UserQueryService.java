package io.itmca.lifepuzzle.domain.user.service;

import io.itmca.lifepuzzle.domain.user.entity.User;

public class UserQueryService {

    public User findByUserNo(long userNo){return new User();}

    public User findByUserId(String userId){return new User();}

    public User findByEmail(String email){return new User();}

    public User findByKakaoId(String kakaoId){return new User();}

    public User findByAppleId(String appleId){return new User();}
}
