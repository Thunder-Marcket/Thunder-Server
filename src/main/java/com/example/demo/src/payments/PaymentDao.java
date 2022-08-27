package com.example.demo.src.payments;

import com.example.demo.src.payments.model.*;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class PaymentDao {
    private JdbcTemplate jdbcTemplate;

    public int createPayment(int userIdx, PostPaymentReq postPaymentReq) {
        String createAccountQuery = "insert into Accounts(accountName) values (?);";
        Object[] createAccountParams = new Object[]{postPaymentReq.getAccountName()};
        this.jdbcTemplate.update(createAccountQuery,createAccountParams);

        String lastInsertAccountIdQuery = "select last_insert_id()";
        int accountIdx = this.jdbcTemplate.queryForObject(lastInsertAccountIdQuery, int.class);

        String createPaymentQuery = "insert into Payments(userIdx, accountIdx) values (?, ?)";
        Object[] createPaymentParams = new Object[]{userIdx, accountIdx};

        this.jdbcTemplate.update(createPaymentQuery, createPaymentParams);

        String lastInsertPaymentIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertPaymentIdQuery, int.class);
    }

    public GetPaymentRes getPayment(int paymentIdx) {
        String getPaymentQuery =
                "select paymentIdx, i.accountIdx, accountName, monthlyPlan\n" +
                "from Payments i\n" +
                "    join Accounts A on i.accountIdx = A.accountIdx\n" +
                "where i.paymentIdx = ?\n";
        int getPaymentParams = paymentIdx;
        return this.jdbcTemplate.queryForObject(getPaymentQuery,
                (rs, rowNum) -> new GetPaymentRes(
                        rs.getInt("paymentIdx"),
                        rs.getInt("accountIdx"),
                        rs.getString("accountName"),
                        rs.getInt("monthlyPlan")),
                getPaymentParams);
    }

    public int modifyPaymentMonthlyPlan(int paymentIdx, PatchPaymentMonthlyPlanReq patchPaymentMonthlyPlanReq) {
        int accountIdx = getAccountIdx(paymentIdx);

        String modifyPaymentQuery = "update Accounts set monthlyPlan = ? where accountIdx = ?";
        Object[] modifyPaymentParams = new Object[]{patchPaymentMonthlyPlanReq.getMonthlyPlan(), accountIdx};

        return this.jdbcTemplate.update(modifyPaymentQuery, modifyPaymentParams);
    }

    public int modifyPayment(int paymentIdx, PatchPaymentReq patchPaymentReq) {
        int accountIdx = getAccountIdx(paymentIdx);
        String modifyPaymentQuery = "update Accounts set accountName = ?, monthlyPlan = 1 where accountIdx = ?";
        Object[] modifyPaymentParams = new Object[]{patchPaymentReq.getAccountName(), accountIdx};

        return this.jdbcTemplate.update(modifyPaymentQuery, modifyPaymentParams);
    }

    private int getAccountIdx(int paymentIdx) {
        String getAccountQuery = "select accountIdx from Payments where paymentIdx = ?";
        int getAccountParams = paymentIdx;
        return this.jdbcTemplate.queryForObject(getAccountQuery, int.class, getAccountParams);
    }
}
