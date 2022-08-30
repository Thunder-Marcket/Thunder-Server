
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
    DELETED_SALESVIEWS(false, 2023, "이미 최근 본 상품에서 삭제된 상태입니다."),


    // [POST] /addresses
    POST_ADDRESSES_EMPTY_PHONENUM(false, 2601, "배송지 입력시 전화번호가 필요합니다."),
    POST_ADDRESSES_EMPTY_USERNAME(false, 2602, "배송지 입력시 이름이 필요합니다."),
    POST_ADDRESSES_EMPTY_ADDRESS(false, 2603, "배송지 입력시 주소가 필요합니다."),
    POST_ADDRESSES_EMPTY_DETAIL_ADDRESS(false, 2604, "배송지 입력시 상세 주소가 필요합니다."),
    POST_ADDRESSES_INVAILD_PHONEBUM(false, 2605, "전화번호를 올바르게 입력해주세요."),
    POST_ADDRESSES_EXISTS_ADDRESS(false, 2606, "중복된 주소입니다."),
    POST_ADDRESSES_INVALID_ADDRESS(false, 2607, "잘못된 형식의 입력입니다."),

    // [POST] / orders
    POST_ORDERS_UNABLE_ADDRESS(false, 2621, "직거래에는 주소가 필요 없습니다."),
    POST_ORDERS_INVALID_ADDRESS(false, 2622, "사용할 수 없는 주소 입니다."),
    POST_ORDERS_INVALID_ITEM(false, 2623, "해당 상품이 존재하지 않습니다."),

    POST_FAIL_FOLLOWS( false, 2624 , "팔로우 하는데 실패했습니다." ),

    POST_FAIL_SALESVIEWS(false, 2625,"최근 본 상품을 생성하는데 실패했습니다."),

    POST_FAIL_LIKES(false, 2626 , "찜하기를 실패했습니다" ),

    // [POST] / Items
    POST_ITEMS_NEED_IMAGES(false, 2650, "상품 등록시 이미지가 필요합니다."),
    POST_ITEMS_OVER_IMAGES(false, 2651, "상품 등록시 이미지가 12개를 넘어가면 안됩니다."),
    POST_ITEMS_NEED_ITEM_NAME(false, 2652, "상품 등록시 상품 이름이 필요합니다."),
    POST_ITEMS_INVAIlD_ITEM_NAME(false, 2653, "유효하지 않은 상품 이름입니다."),
    POST_ITEMS_UNDER_ITEM_CONTENT(false, 2654, "상품 설명은 10글자를 넘어야 합니다."),
    PATCH_ITEMS_NULL_ITEM(false, 2656, "변경하고자 하는 상품이 없습니다."),


    // [POST] /comments
    POST_COMMENTS_UNABLE_WRITE(false, 2700, "후기 작성이 불가능합니다."),
    POST_COMMENTS_UNACCESS_ORDER(false,2701,"해당 주문 기록이 없습니다."),
    POST_COMMENTS_EXIST_COMMENT(false, 2702, "해당 주문에 대해 이미 후기가 작성되었습니다."),

    // [POST] /chats
    POST_CHATS_EMPTY_CHAT(false, 2750, "비어있는 채팅은 보낼 수 없습니다."),
    POST_CHATS_INVALID_ITEM(false, 2751, "해당 상품이 존재하지 않습니다."),
    POST_CHATS_INVALID_CHATROOM(false, 2752, "해당 톡방이 없습니다."),

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

    MODIFY_FAIL_PAYMENT_MONTHLYPLAN(false, 4018, "할부개월 수정 실패"),
    MODIFY_FAIL_PAYMENT(false, 4019, "결제수단 수정 실패"),

    MODIFY_FAIL_SALESVIEWS_STATUS(false, 4020, "최근 본 상품 상태 삭제로 수정 실패"),
    MODIFY_FAIL_SEARCH_STATUS( false, 4021 , "검색어 상태 삭제로 수정 실패" ),

    MODIFY_FAIL_FOLLOWS_STATUS( false, 4022 , "팔로우 상태 수정 실패" ),

    MODIFY_FAIL_SALESVIEWS_UPDATETIME(false, 4023 , "최근 본 상품 시간 수정 실패" ),

    MODIFY_FAIL_LIKE_STATUS(false, 4024 , "찜 상태 수정 실패" ),

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

