package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.src.address.model.PostAddressReq;
import com.example.demo.src.address.model.PostAddressRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AddressService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AddressDao addressDao;
    private final AddressProvier addressProvier;

    @Autowired
    public AddressService(AddressDao addressDao, AddressProvier addressProvier) {
        this.addressDao = addressDao;
        this.addressProvier = addressProvier;
    }


    public List<PostAddressRes> createAddress(PostAddressReq postAddressReq) throws BaseException {
        try{
            if(addressProvier.checkAddress(postAddressReq) == 1){
                throw new BaseException(POST_ADDRESSES_EXISTS_ADDRESS);
            }
        } catch (Exception exception){
            throw new BaseException(POST_ADDRESSES_EXISTS_ADDRESS);
        }

        try{
            List<PostAddressRes> postAddressRes = addressDao.createAddress(postAddressReq);
            return postAddressRes;
        } catch (Exception exception){
            throw new BaseException(POST_ADDRESSES_INVALID_ADDRESS);
        }

    }
}
