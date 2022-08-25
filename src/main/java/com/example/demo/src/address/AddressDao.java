package com.example.demo.src.address;

import com.example.demo.src.address.model.GetAddressRes;
import com.example.demo.src.address.model.PostAddressReq;
import com.example.demo.src.address.model.PostAddressRes;
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
        String getAddressQuery = "select\n" +
                "    A.name AS userName,\n" +
                "    A.address,\n" +
                "    A.detailAddress,\n" +
                "    A.phoneNumber,\n" +
                "    A.isBaseAddress\n" +
                "from Addresss A\n" +
                "inner join Users U on A.userIdx = U.userIdx\n" +
                "\n" +
                "where A.userIdx = ?";
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

    public List<PostAddressRes> createAddress(PostAddressReq postAddressReq) {
        String createAddressQuery = "insert into Addresss (`name` ,address, detailAddress, phoneNumber, isBaseAddress, userIdx) VALUES (?,?,?,?,?,?);";
        Object[] createAddressParams = new Object[]{
                postAddressReq.getUserName(),
                postAddressReq.getAddress(),
                postAddressReq.getDetailAddress(),
                postAddressReq.getPhoneNumber(),
                postAddressReq.getIsBaseAddress(),
                postAddressReq.getUserIdx()
        };


        this.jdbcTemplate.update(createAddressQuery, createAddressParams);

        String getAddressQuery = "select\n" +
                "    A.name AS userName,\n" +
                "    A.address,\n" +
                "    A.detailAddress,\n" +
                "    A.phoneNumber,\n" +
                "    A.isBaseAddress\n" +
                "from Addresss A\n" +
                "inner join Users U on A.userIdx = U.userIdx\n" +
                "\n" +
                "where A.userIdx = ?";
        int getAddressParams = postAddressReq.getUserIdx();

        return this.jdbcTemplate.query(getAddressQuery,
                (rs, rowNum) -> new PostAddressRes(
                        rs.getString("userName"),
                        rs.getString("address"),
                        rs.getString("detailAddress"),
                        rs.getString("phoneNumber"),
                        rs.getInt("isBaseAddress")
                ), getAddressParams);
    }

    public int checkAddress(PostAddressReq postAddressReq) {
        String checkAddressQuery = "select exists(select addressIdx from Addresss\n" +
                "                                where address = ?\n" +
                "                                AND detailAddress = ?\n" +
                "                                AND `name` = ?\n" +
                "                                AND phoneNumber = ?\n" +
                "                                AND userIdx = ?)";
        Object[] checkAddressParams = new Object[]{
                postAddressReq.getAddress(),
                postAddressReq.getDetailAddress(),
                postAddressReq.getUserName(),
                postAddressReq.getPhoneNumber(),
                postAddressReq.getUserIdx()
        };

        return this.jdbcTemplate.queryForObject(checkAddressQuery,
                int.class,
                checkAddressParams);
    }
}
