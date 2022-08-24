package com.example.demo.src.Address;

import com.example.demo.src.Address.model.GetAddressRes;
import com.example.demo.src.Address.model.PostAddressReq;
import com.example.demo.src.Address.model.PostAddressRes;
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
        String createAddressQuery = "";
        Object[] createAddressParams = new Object[]{};


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
}
