package com.example.demo.src.Address;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Address.model.GetAddressRes;
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
}
