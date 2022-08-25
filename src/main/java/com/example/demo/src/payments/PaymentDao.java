package com.example.demo.src.payments;

import com.example.demo.src.payments.model.PostPaymentReq;
import com.example.demo.src.payments.model.PostPaymentRes;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class PaymentDao {
    private JdbcTemplate jdbcTemplate;

    public int createPayment(int userIdx, PostPaymentReq postPaymentReq) {
        String createAccountQuery = "insert into Accounts(accountName, monthlyPlan) values (?, ?);";
        Object[] createAccountParams = new Object[]{postPaymentReq.getAccountName(), postPaymentReq.getMonthlyPlan()};
        this.jdbcTemplate.update(createAccountQuery,createAccountParams);

        String lastInsertAccountIdQuery = "select last_insert_id()";
        int accountIdx = this.jdbcTemplate.queryForObject(lastInsertAccountIdQuery, int.class);

        String createPaymentQuery = "insert into Payments(userIdx, accountIdx) values (?, ?);";
        Object[] createPaymentParams = new Object[]{userIdx, accountIdx};

        this.jdbcTemplate.update(createPaymentQuery, createPaymentParams);

        String lastInsertPaymentIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertPaymentIdQuery, int.class);
    }
}
