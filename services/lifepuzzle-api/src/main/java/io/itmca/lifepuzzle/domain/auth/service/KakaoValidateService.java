package io.itmca.lifepuzzle.domain.auth.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class KakaoValidateService {

  public String getKakaoIdByTokenValidation(String kakaoAccessToken) throws IOException {
    return this.getKakaoProfile(kakaoAccessToken);
  }

  private String getKakaoProfile(String accessToken) throws IOException {
    var url = URI.create("https://kapi.kakao.com/v2/user/me").toURL();
    var conn = (HttpURLConnection) url.openConnection();

    conn.setRequestMethod("GET");
    conn.setDoInput(true);
    conn.setRequestProperty("Authorization", "Bearer " + accessToken);
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

    int responseCode = conn.getResponseCode();

    // TODO if 처리 해놓으셨는데 비어있네요. 이 부분 확인이 필요합니다.
    if (responseCode != 200) {
      System.out.println(responseCode);
    }

    var br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    var sb = new StringBuilder();
    var line = "";

    while ((line = br.readLine()) != null) {
      sb.append(line);
    }

    var kakaoProfile = new JSONObject(sb.toString());
    var id = kakaoProfile.get("id").toString();

    return id;
  }
}
