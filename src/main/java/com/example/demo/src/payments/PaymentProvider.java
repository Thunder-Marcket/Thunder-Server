package com.example.demo.src.payments;

import com.example.demo.config.BaseException;
import com.example.demo.src.payments.model.GetPaymentRes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@AllArgsConstructor
@Service
public class PaymentProvider {

    private final PaymentDao paymentDao;

    public GetPaymentRes getPayment(int userIdx, int paymentIdx) throws BaseException {
        try {
            GetPaymentRes getPaymentRes = paymentDao.getPayment(paymentIdx);
            return getPaymentRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
