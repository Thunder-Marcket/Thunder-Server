package com.example.demo.src.address;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.address.model.PostAddressReq;
import com.example.demo.src.address.model.PostAddressRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/addresses")
public class AddressController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AddressProvier addressProvier;
    private final AddressService addressService;
    private final JwtService jwtService;

    @Autowired
    public AddressController(AddressProvier addressProvier, AddressService addressService, JwtService jwtService) {
        this.addressProvier = addressProvier;
        this.addressService = addressService;
        this.jwtService = jwtService;
    }


    /**
     * 내 배송지 조회 API
     * [GET] /addresses
     * @return BaseResponse<List<GetAddressRes>>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetAddressRes>> getAddress(){
        try{
            int userIdx = jwtService.getUserIdx();

            List<GetAddressRes> getAddressRes = addressProvier.getAddress(userIdx);
            return new BaseResponse<>(getAddressRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 배송지 추가 API
     * [POST] /addresses
     * @return BaseResponse<List<PostAddressRes>>
     */
    @ResponseBody
    @PostMapping("/create")
    public BaseResponse<List<PostAddressRes>> createAddress(@RequestBody PostAddressReq postAddressReq){
        // 전화번호가 없으면 에러
        if(postAddressReq.getPhoneNumber() == null){
            return new BaseResponse<>(POST_ADDRESSES_EMPTY_PHONENUM);
        }
        // 이름이 없으면 에러
        if(postAddressReq.getUserName() == null){
            return new BaseResponse<>(POST_ADDRESSES_EMPTY_USERNAME);
        }
        // 주소 정보가 없으면 에러
        if(postAddressReq.getAddress() == null){
            return new BaseResponse<>(POST_ADDRESSES_EMPTY_ADDRESS);
        }
        // 상세 주소가 없으면 에러
        if(postAddressReq.getDetailAddress() == null){
            return new BaseResponse<>(POST_ADDRESSES_EMPTY_DETAIL_ADDRESS);
        }

        if(postAddressReq.getPhoneNumber().length() != 11){
            return new BaseResponse<>(POST_ADDRESSES_INVAILD_PHONEBUM);
        }
        if(!postAddressReq.getPhoneNumber().matches("^([0-9]*)$")){
            return new BaseResponse<>(POST_ADDRESSES_INVAILD_PHONEBUM);
        }

        try{
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdxByJwt != postAddressReq.getUserIdx()){
                throw new BaseException(INVALID_JWT);
            }

            List<PostAddressRes> postAddressRes = addressService.createAddress(postAddressReq);
            return new BaseResponse<>(postAddressRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
