
package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    POST_USERS_EMPTY_PHONENUM(false, 2018 , "전화번호를 입력해주세요" ),
    POST_USERS_INVALID_PHONENUM( false, 2019 , "전화번호 형식을 확인해주세요." ),
    POST_USERS_EXISTS_PHONENUM( false, 2020 , "중복된 전화번호입니다."),
    POST_USERS_EXISTS_NAME( false, 2021 , "이미 존재하는 이름입니다." ),

    DELETED_USER( false, 2022 , "탈퇴한 유저입니다." ),


    // [POST] /addresses
    POST_ADDRESSES_EMPTY_PHONENUM(false, 2601, "배송지 입력시 전화번호가 필요합니다."),
    POST_ADDRESSES_EMPTY_USERNAME(false, 2602, "배송지 입력시 이름이 필요합니다."),
    POST_ADDRESSES_EMPTY_ADDRESS(false, 2603, "배송지 입력시 주소가 필요합니다."),
    POST_ADDRESSES_EMPTY_DETAIL_ADDRESS(false, 2604, "배송지 입력시 상세 주소가 필요합니다."),
    POST_ADDRESSES_INVAILD_PHONEBUM(false, 2605, "전화번호를 올바르게 입력해주세요."),
    POST_ADDRESSES_EXISTS_ADDRESS(false, 2606, "중복된 주소입니다."),
    POST_ADDRESSES_INVALID_ADDRESS(false, 2607, "잘못된 형식의 입력입니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    NOT_EXIST_USER(false, 3015 , "존재하지 않는 사용자입니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    MODIFY_FAIL_USER( false, 4015 , "유저 정보 수정 실패" ),
    MODIFY_FAIL_USER_STATUS(false, 4016 , "유저 상태 탈퇴로 수정 실패" ),
    MODIFY_FAIL_USER_STATUS_TO_ENABLE(false, 4017, "유저 상태 enable로 수정 실패" ),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

