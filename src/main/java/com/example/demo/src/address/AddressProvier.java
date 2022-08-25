package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.address.model.PostAddressReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AddressProvier {
    private final AddressDao addressDao;

    @Autowired
    public AddressProvier(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    public List<GetAddressRes> getAddress(int userIdx) throws BaseException {
        try{
            List<GetAddressRes> getAddressRes = addressDao.getAddress(userIdx);
            return getAddressRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 중복 체크
    public int checkAddress(PostAddressReq postAddressReq) throws BaseException{
        try{
            return addressDao.checkAddress(postAddressReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
