package com.example.demo.src.Address;

import com.example.demo.src.Address.model.GetAddressRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AddressDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetAddressRes> getAddress(int userIdx) {
        String getAddressQuery = "";
        int getAddressParams = userIdx;

        return this.jdbcTemplate.query(getAddressQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getString("userName"),
                        rs.getString("address"),
                        rs.getString("detailAddress"),
                        rs.getString("phoneNumber"),
                        rs.getInt("isBaseAddress")
                ), getAddressParams);
    }
}
