package com.example.demo.src.Orders;

import com.example.demo.src.Orders.model.GetAddressRes;
import com.example.demo.src.Orders.model.GetDirectOrderRes;
import com.example.demo.src.Orders.model.GetIndirectOrderRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class OrderDao {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetDirectOrderRes getDirectOrderRes(int userIdx, int itemIdx, int usePoint) {
        String getDirectOrderResQuery = "select\n" +
                "    I.itemName,\n" +
                "    (select Images.imageUrl from Images\n" +
                "                 where Images.imageIdx = (select min(Images.imageIdx) from Images\n" +
                "                                                                      inner join ItemImages II on Images.itemImageIdx = II.itemImageIdx\n" +
                "                                                                      inner join Items I2 on II.itemIdx = I2.itemIdx\n" +
                "                                                                      where I2.itemIdx = I.itemIdx)) AS itemImageUrl,\n" +
                "    (select U.point from Users U where U.userIdx = ?) AS point,\n" +
                "    I.cost AS itemCost,\n" +
                "    case\n" +
                "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 1\n" +
                "            then concat(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer), '원')\n" +
                "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 0\n" +
                "            AND convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) < 3500\n" +
                "            then '무료'\n" +
                "        ELSE concat(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) - 3500, '원')\n" +
                "    END AS safePayCost,\n" +
                "    case\n" +
                "        when I.isIncludeOrderTip = 1 then '무료'\n" +
                "        when I.isIncludeOrderTip = 0 then '배송비 별도'\n" +
                "    END AS isIncludeOrderTip,\n" +
                "       case\n" +
                "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 1\n" +
                "            then concat(convert(ROUND(I.cost / 100, 2) * 3.5 + I.cost - ?, unsigned integer), '원')\n" +
                "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 0\n" +
                "            AND convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) < 3500\n" +
                "            then concat(I.cost - ?, '원')\n" +
                "        ELSE concat(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) - 3500 + I.cost - ?, '원')\n" +
                "    END AS totalCost\n" +
                "\n" +
                "from Items I\n" +
                "where I.itemIdx = ?";
        Object[] getDirectOrderResParams = new Object[]{userIdx, usePoint, usePoint, usePoint, itemIdx};

        return this.jdbcTemplate.queryForObject(getDirectOrderResQuery,
                (rs, rowNum) -> new GetDirectOrderRes(
                        rs.getString("itemName"),
                        rs.getString("itemImageUrl"),
                        rs.getInt("point"),
                        rs.getString("itemCost"),
                        rs.getString("safePayCost"),
                        rs.getString("isIncludeOrderTip"),
                        rs.getString("totalCost")

                ), getDirectOrderResParams);
    }

    public GetIndirectOrderRes getIndirectOrderRes(int userIdx, int itemIdx, int usePoint) {
        GetAddressRes getAddressRes = null;
        GetAddressRes addressRes = null;
        try{
            getAddressRes = getNewAddressRes(userIdx);

            try{
                addressRes = getBaseAddressRes(userIdx);
                getAddressRes = addressRes;
            } catch (Exception exception){
                addressRes = null;
            }
        }catch (Exception exception){
            getAddressRes = new GetAddressRes(0, "배송지 없음", "없음", "없음");

        } finally {
            if(addressRes != null){
                getAddressRes = addressRes;
            }

            String getIndirectOrderQuery = "select\n" +
                    "    I.itemName,\n" +
                    "    (select Images.imageUrl from Images\n" +
                    "                 where Images.imageIdx = (select min(Images.imageIdx) from Images\n" +
                    "                                                                      inner join ItemImages II on Images.itemImageIdx = II.itemImageIdx\n" +
                    "                                                                      inner join Items I2 on II.itemIdx = I2.itemIdx\n" +
                    "                                                                      where I2.itemIdx = I.itemIdx)) AS itemImageUrl,\n" +
                    "    (select U.point from Users U where U.userIdx = ?) AS point,\n" +
                    "    I.cost AS itemCost,\n" +
                    "    case\n" +
                    "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 1\n" +
                    "            then concat(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer), '원')\n" +
                    "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 0\n" +
                    "            AND convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) < 3500\n" +
                    "            then '무료'\n" +
                    "        ELSE concat(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) - 3500, '원')\n" +
                    "    END AS safePayCost,\n" +
                    "    case\n" +
                    "        when I.isIncludeOrderTip = 1 then '무료'\n" +
                    "        when I.isIncludeOrderTip = 0 then '배송비 별도'\n" +
                    "    END AS isIncludeOrderTip,\n" +
                    "       case\n" +
                    "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 1\n" +
                    "            then concat(convert(ROUND(I.cost / 100, 2) * 3.5 + I.cost - ?, unsigned integer), '원')\n" +
                    "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 0\n" +
                    "            AND convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) < 3500\n" +
                    "            then concat(I.cost - ?, '원')\n" +
                    "        ELSE concat(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) - 3500 + I.cost - ?, '원')\n" +
                    "    END AS totalCost\n" +
                    "\n" +
                    "from Items I\n" +
                    "where I.itemIdx = ?;";
            Object[] getIndirectOrderParams = new Object[]{userIdx, usePoint, usePoint, usePoint, itemIdx};

            GetAddressRes address = getAddressRes;
            return this.jdbcTemplate.queryForObject(getIndirectOrderQuery,
                    (rs, rowNum) -> new GetIndirectOrderRes(
                            address,
                            rs.getString("itemName"),
                            rs.getString("itemImageUrl"),
                            rs.getInt("point"),
                            rs.getString("itemCost"),
                            rs.getString("safePayCost"),
                            rs.getString("isIncludeOrderTip"),
                            rs.getString("totalCost")
                    ), getIndirectOrderParams);
        }
    }

    public GetAddressRes getNewAddressRes(int userIdx){
        String getAddressResQuery = "select\n" +
                "    A.addressIdx,\n" +
                "    A.name AS userName,\n" +
                "    A.address,\n" +
                "    A.detailAddress\n" +
                "\n" +
                "from Addresss A\n" +
                "inner join Users U on A.userIdx = U.userIdx\n" +
                "where U.userIdx = ?\n" +
                "order by A.updatedAt desc limit 1;";
        int getAddressResParams = userIdx;

        return jdbcTemplate.queryForObject(getAddressResQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getInt("addressIdx"),
                        rs.getString("userName"),
                        rs.getString("address"),
                        rs.getString("detailAddress")
                ), getAddressResParams);
    }

    public GetAddressRes getBaseAddressRes(int userIdx){
        String getAddressResQuery = "select\n" +
                "    A.addressIdx,\n" +
                "    A.name,\n" +
                "    A.address,\n" +
                "    A.detailAddress\n" +
                "\n" +
                "from Addresss A\n" +
                "inner join Users U on A.userIdx = U.userIdx\n" +
                "where U.userIdx = ?\n" +
                "AND A.isBaseAddress limit 1;";
        int getAddressResParams = userIdx;

        return jdbcTemplate.queryForObject(getAddressResQuery,
                (rs, rowNum) -> new GetAddressRes(
                        rs.getInt("addressIdx"),
                        rs.getString("userName"),
                        rs.getString("address"),
                        rs.getString("detailAddress")
                ), getAddressResParams);
    }


}
