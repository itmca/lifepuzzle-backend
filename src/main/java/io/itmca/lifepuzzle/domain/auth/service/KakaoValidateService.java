package io.itmca.lifepuzzle.domain.auth.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        if (responseCode != 200) {
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = "";

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        var kakaoProfile = new JSONObject(sb.toString());
        var id = kakaoProfile.getString("id");

        return id;
    }
}
