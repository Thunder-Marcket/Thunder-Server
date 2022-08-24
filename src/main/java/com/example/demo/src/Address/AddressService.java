package com.example.demo.src.Address;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Address.model.PostAddressReq;
import com.example.demo.src.Address.model.PostAddressRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AddressService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AddressDao addressDao;

    @Autowired
    public AddressService(AddressDao addressDao) {
        this.addressDao = addressDao;
    }


    public List<PostAddressRes> createAddress(PostAddressReq postAddressReq) throws BaseException {
        try{
            List<PostAddressRes> postAddressRes = addressDao.createAddress(postAddressReq);
            return postAddressRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
