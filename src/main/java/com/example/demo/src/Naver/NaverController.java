package com.example.demo.src.Naver;

import com.example.demo.config.BaseResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static java.awt.SystemColor.info;


@RestController
@RequestMapping("/naver-api")
public class NaverController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @GetMapping("")
    public BaseResponse<String> trans(@RequestParam String keyword) throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        String clientID = "jhpj902zxr";
        String clientSecret = "n3UrZoZJ26XpBk5sEyBRoFsHBIkuLv9KXzQ1js16";

        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";

        HttpHeaders headers = new HttpHeaders(); // 헤더에 key들을 담아준다.
        headers.set("X-NCP-APIGW-API-KEY-ID", clientID);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);


        HttpEntity<String> entity = new HttpEntity<>(headers);

        // URLEncoder.encode("분당구 불정로 6", "utf-8");
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiURL)
                .queryParam("query", "분당구 불정로 6");

        HttpEntity<String> result = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        logger.warn(keyword);
        logger.warn(result.toString());

        JSONObject jsonObject = new JSONObject(result);


        return new BaseResponse<>(result.getBody());
    }
}
