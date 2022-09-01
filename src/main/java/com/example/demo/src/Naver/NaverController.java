package com.example.demo.src.Naver;

import com.example.demo.config.BaseResponse;

import com.example.demo.src.Naver.model.GetLocationRes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Color.getColor;
import static java.awt.SystemColor.info;


@RestController
@RequestMapping("/naver-api")
public class NaverController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetLocationRes> trans(@RequestParam String keyword) throws IOException, ParseException {

        RestTemplate restTemplate = new RestTemplate();

        String clientID = "jhpj902zxr";
        String clientSecret = "n3UrZoZJ26XpBk5sEyBRoFsHBIkuLv9KXzQ1js16";

        String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";

        HttpHeaders headers = new HttpHeaders(); // 헤더에 key들을 담아준다.
        headers.set("X-NCP-APIGW-API-KEY-ID", clientID);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);
        headers.set("Accept", "application/json");
        //headers.set("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // URLEncoder.encode("분당구 불정로 6", "utf-8");
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiURL)
                .queryParam("query", keyword)
                .encode(Charset.forName("UTF-8"))
                .encode();

        URI uri = uriBuilder.build().toUri();

        HttpEntity<Object> result = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                Object.class
        );



        //logger.warn(keyword);
        //logger.warn(result.getHeaders().toString());
        //logger.warn(result.getBody().toString());



        String temp = result.getBody().toString();

        int startX = temp.indexOf("x=");
        int startY = temp.indexOf(", y=");
        int endY = temp.indexOf(", dis");
        logger.warn(temp.indexOf("x=") + "x위치");
        logger.warn(temp.indexOf(", y=") + "y위치");
        logger.warn(temp.indexOf(", dis") + "y 끝");

        logger.warn(temp.substring(startX + 2, startY));
        logger.warn(temp.substring(startY + 4, endY));


        int startRoadAddr = temp.indexOf("roadAddress=");
        int startJibunAddr = temp.indexOf(", jibunAddress=");
        int endJibunAddr = temp.indexOf(", englishAddress=");

        logger.warn(temp.substring(startRoadAddr + 12, startJibunAddr));
        logger.warn(temp.substring(startJibunAddr + 15, endJibunAddr));

        GetLocationRes getLocationRes = new GetLocationRes(
                temp.substring(startRoadAddr + 12, startJibunAddr),
                temp.substring(startJibunAddr + 15, endJibunAddr),
                temp.substring(startX + 2, startY),
                temp.substring(startY + 4, endY)
        );


       // String naverRes = result.getBody().toString();


        //JSONObject jsonObject = new JSONObject(naverRes);
        //JSONParse에 json데이터를 넣어 파싱한 다음 JSONObject로 변환한다.
        //JSONObject jsonObj = (JSONObject) jsonParse.parse(String.valueOf(result.getBody()));
//
//        //JSONObject에서 PersonsArray를 get하여 JSONArray에 저장한다.
//        JSONArray addressArray = (JSONArray) jsonObj.get("addresses");
//
//        JSONObject addressObj = (JSONObject)addressArray.get(0);
//        logger.warn(addressObj.getString("x"));

        return new BaseResponse<>(getLocationRes);
    }
}
