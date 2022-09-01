package com.example.demo.src.users;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.users.model.KakaoLogInReq;
import com.example.demo.src.users.model.KakaoLogInRes;
import com.example.demo.utils.JwtService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.NOT_FOUND_USER_BY_USERNAME;


@AllArgsConstructor
@Service
public class KakaoLogInService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserDao userDao;
    @Autowired
    private final JwtService jwtService;

    public HashMap<String, Object>  getKakaoUserInfo(KakaoLogInReq kakaoLogInReq) throws IOException {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String token = kakaoLogInReq.getAccessToken();
        HashMap<String, Object> userInfo = new HashMap<>();

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            logger.debug("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            logger.debug("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            Long kakao_id = element.getAsJsonObject().get("id").getAsLong();
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String profile_image = properties.getAsJsonObject().get("profile_image").getAsString();

            logger.debug("kakao_id : "+kakao_id);
            logger.debug("nickname : " + nickname);
            logger.debug("profile_image : " + profile_image);

            userInfo.put("kakao_id", kakao_id);
            userInfo.put("nickname",nickname);
            userInfo.put("profile_image", profile_image);

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public KakaoLogInRes saveOrUpdateKakaoUser(HashMap<String, Object> kakaoUserInfo) throws BaseException {
        int isUser = userProvider.checkUserByKakaoId(kakaoUserInfo.get("kakao_id"));
        logger.debug("isUser = {}", isUser);
        int userIdx = 0;
        String jwt = null;
        if (isUser == 1) {
            userIdx = userProvider.findUserIdByKakaoId(kakaoUserInfo.get("kakao_id"));
            int result = userDao.updateUserInfo(userIdx, kakaoUserInfo.get("nickname"), kakaoUserInfo.get("profile_image"));
            if (result == 0) {
                throw new BaseException(DATABASE_ERROR);
            }
            jwt = jwtService.createJwt(userIdx);

        } else {
            throw new BaseException(NOT_FOUND_USER_BY_USERNAME);
        }
        return new KakaoLogInRes(userIdx, jwt);
    }
}
