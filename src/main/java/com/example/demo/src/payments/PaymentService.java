package com.example.demo.src.payments;

import com.example.demo.config.BaseException;
import com.example.demo.src.payments.model.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@AllArgsConstructor
@Service
public class PaymentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentDao paymentDao;
    private final PaymentProvider paymentProvider;

    public PostPaymentRes createPayment(int userIdx, PostPaymentReq postPaymentReq) throws BaseException {
        try {
            int paymentIdx = paymentDao.createPayment(userIdx, postPaymentReq);
            return new PostPaymentRes(paymentIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPaymentRes modifyPaymentMonthlyPlan(int userIdx, int paymentIdx, PatchPaymentMonthlyPlanReq patchPaymentMonthlyPlanReq) throws BaseException {
        try {
            int result = paymentDao.modifyPaymentMonthlyPlan(paymentIdx, patchPaymentMonthlyPlanReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_PAYMENT_MONTHLYPLAN);
            }
            GetPaymentRes getPaymentRes = paymentProvider.getPayment(userIdx, paymentIdx);
            return getPaymentRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPaymentRes modifyPayment(int userIdx, int paymentIdx, PatchPaymentReq patchPaymentReq) throws BaseException {
        try {
            int result = paymentDao.modifyPayment(paymentIdx, patchPaymentReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_PAYMENT);
            }
            GetPaymentRes getPaymentRes = paymentProvider.getPayment(userIdx, paymentIdx);
            return getPaymentRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
