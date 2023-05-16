package io.itmca.lifepuzzle.domain.auth.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class KakaoValidateService {

  public String getKakaoIdByTokenValidation(String kakaoAccessToken) throws IOException {
    return this.getKakaoProfile(kakaoAccessToken);
  }

  private String getKakaoProfile(String accessToken) throws IOException {
    var url = new URL("https://kapi.kakao.com/v2/user/me");
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

    // TODO type 명시를 일괄적으로 하는 것이 좋을 것 같아요. 저희는 var로 통일하기로 했으니 var 쓰는 것이 어떠할까요?
    //  아니면 이렇게 쓰신 이유가 있을까요?
    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String line = "";

    while ((line = br.readLine()) != null) {
      sb.append(line);
    }

    var kakaoProfile = new JSONObject(sb.toString());
    var id = kakaoProfile.get("id").toString();

    return id;
  }
}
