package com.example.demo.src.payments;

import com.example.demo.config.BaseException;
import com.example.demo.src.payments.model.PostPaymentReq;
import com.example.demo.src.payments.model.PostPaymentRes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@AllArgsConstructor
@Service
public class PaymentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentDao paymentDao;

    public PostPaymentRes createPayment(int userIdx, PostPaymentReq postPaymentReq) throws BaseException {
        try {
            int paymentIdx = paymentDao.createPayment(userIdx, postPaymentReq);
            return new PostPaymentRes(paymentIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
