package com.example.demo.src.Address;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Address.model.GetAddressRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


}
