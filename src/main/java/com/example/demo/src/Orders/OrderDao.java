package com.example.demo.src.Orders;

import com.example.demo.src.Orders.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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
                "    concat(format(I.cost, 0), '원') AS itemCost,\n" +
                "    case\n" +
                "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 1\n" +
                "            then concat(FORMAT(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer), 0), '원')\n" +
                "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 0\n" +
                "            AND convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) < 3500\n" +
                "            then '무료'\n" +
                "        ELSE concat(FORMAT(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) - 3500, 0), '원')\n" +
                "    END AS safePayCost,\n" +
                "    case\n" +
                "        when I.isIncludeOrderTip = 1 then '무료'\n" +
                "        when I.isIncludeOrderTip = 0 then '배송비 별도'\n" +
                "    END AS isIncludeOrderTip,\n" +
                "       case\n" +
                "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 1\n" +
                "            then concat(FORMAT(convert(ROUND(I.cost / 100, 2) * 3.5 + I.cost - ?, unsigned integer), 0), '원')\n" +
                "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 0\n" +
                "            AND convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) < 3500\n" +
                "            then concat(FORMAT(I.cost - ?, 0), '원')\n" +
                "        ELSE concat(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) - 3500 + I.cost - ?, '원')\n" +
                "    END AS totalCost\n" +
                "\n" +
                "from Items I\n" +
                "where I.itemIdx = ?;";
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
            addressRes = getBaseAddressRes(userIdx);
            try{
                addressRes = getBaseAddressRes(userIdx);
                getAddressRes = addressRes;
            } catch (Exception exception){
                addressRes = new GetAddressRes(0, "배송지 없음", "없음", "없음", "없음");
            }
        }catch (Exception exception){
            getAddressRes = new GetAddressRes(0, "배송지 없음", "없음", "없음", "없음");

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
                    "    concat(format(I.cost, 0), '원') AS itemCost,\n" +
                    "    case\n" +
                    "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 1\n" +
                    "            then concat(FORMAT(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer), 0), '원')\n" +
                    "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 0\n" +
                    "            AND convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) < 3500\n" +
                    "            then '무료'\n" +
                    "        ELSE concat(FORMAT(convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) - 3500, 0), '원')\n" +
                    "    END AS safePayCost,\n" +
                    "    case\n" +
                    "        when I.isIncludeOrderTip = 1 then '무료'\n" +
                    "        when I.isIncludeOrderTip = 0 then '배송비 별도'\n" +
                    "    END AS isIncludeOrderTip,\n" +
                    "       case\n" +
                    "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 1\n" +
                    "            then concat(FORMAT(convert(ROUND(I.cost / 100, 2) * 3.5 + I.cost - ?, unsigned integer), 0), '원')\n" +
                    "        when exists(select orderIdx from Orders inner join Users U2 on Orders.buyUserIdx = U2.userIdx) = 0\n" +
                    "            AND convert(ROUND(I.cost / 100, 2) * 3.5, unsigned integer) < 3500\n" +
                    "            then concat(FORMAT(I.cost - ?, 0), '원')\n" +
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
                "    A.detailAddress,\n" +
                "    A.phoneNumber\n" +
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
                        rs.getString("detailAddress"),
                        rs.getString("phoneNumber")
                ), getAddressResParams);
    }

    public GetAddressRes getBaseAddressRes(int userIdx){
        String getAddressResQuery = "select\n" +
                "    A.addressIdx,\n" +
                "    A.name AS userName,\n" +
                "    A.address,\n" +
                "    A.detailAddress,\n" +
                "    A.phoneNumber\n" +
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
                        rs.getString("detailAddress"),
                        rs.getString("phoneNumber")
                ), getAddressResParams);
    }


    public PostOrderRes createOrder(PostOrderReq postOrderReq) {
        String createOrderQuery = "insert into Orders (addressIdx, itemIdx, buyUserIdx, orderRequest, isDirectDeal, paymentIdx) VALUES (?,?,?,?,?,?);";
        Object[] createOrderParams;

        if(postOrderReq.getAddressIdx() == 0){
            createOrderParams = new Object[]{
                    null,
                    postOrderReq.getItemIdx(),
                    postOrderReq.getBuyUserIdx(),
                    postOrderReq.getOrderRequest(),
                    postOrderReq.getIsDirectDeal(),
                    postOrderReq.getPaymentIdx()
            };
        }
        else{
            createOrderParams = new Object[]{
                    postOrderReq.getAddressIdx(),
                    postOrderReq.getItemIdx(),
                    postOrderReq.getBuyUserIdx(),
                    postOrderReq.getOrderRequest(),
                    postOrderReq.getIsDirectDeal(),
                    postOrderReq.getPaymentIdx()
            };
        }

        this.jdbcTemplate.update(createOrderQuery, createOrderParams);

        soldItem(postOrderReq.getItemIdx());

        String lastInsertIdQuery = "select last_insert_id()";
        return new PostOrderRes(this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class));
    }

    public int checkItem(PostOrderReq postOrderReq) {
        String checkItemQuery = "select exists(select I.itemIdx from Items I where I.itemIdx = ?);";
        int checkItemParam = postOrderReq.getItemIdx();

        return this.jdbcTemplate.queryForObject(checkItemQuery, int.class, checkItemParam);
    }

    public void soldItem(int itemIdx){
        String soldItemQuery = "update Items\n" +
                "set Items.status = case\n" +
                "    when itemCount = 1 then 'sold'\n" +
                "    ELSE Items.status\n" +
                "    END,\n" +
                "    Items.itemCount = case\n" +
                "    when itemCount = 1 then 0\n" +
                "    when itemCount = 0 then 0\n" +
                "    ELSE itemCount - 1\n" +
                "    END\n" +
                "\n" +
                "where Items.itemIdx = ?;";
        int soldItemParam = itemIdx;

        this.jdbcTemplate.update(soldItemQuery, soldItemParam);
    }


    public List<GetPurchaseOrderRes> getPurchase(int userIdx) {
        String getPurchaseQuery = "select\n" +
                "    O.orderIdx,\n" +
                "    I.itemName,\n" +
                "    concat(FORMAT(I.cost, 0), '원') AS itemCost,\n" +
                "    (select I2.imageUrl from ItemImages\n" +
                "                        inner join Images I2 on ItemImages.itemImageIdx = I2.itemImageIdx\n" +
                "                        where ItemImages.itemIdx = I.itemIdx\n" +
                "                        order by I2.createdAt desc limit 1) AS itemUrl,\n" +
                "    U.userName AS storeName,\n" +
                "    case\n" +
                "        when hour(O.updatedAt) >= 12 then DATE_FORMAT(O.updatedAt, '%y.%m.%d (오후 %l:%i)')\n" +
                "        ELSE DATE_FORMAT(O.updatedAt, '%y.%m.%d (오전 %l:%i)')\n" +
                "    END AS orderTime\n" +
                "\n" +
                "\n" +
                "from Orders O\n" +
                "inner join Items I on O.itemIdx = I.itemIdx\n" +
                "inner join Users U on I.userIdx = U.userIdx\n" +
                "where O.buyUserIdx = ?;";
        int getPurchaseParam = userIdx;

        return this.jdbcTemplate.query(getPurchaseQuery,
                (rs, rowNum) -> new GetPurchaseOrderRes(
                        rs.getInt("orderIdx"),
                        rs.getString("itemName"),
                        rs.getString("itemCost"),
                        rs.getString("itemUrl"),
                        rs.getString("storeName"),
                        rs.getString("orderTime")
                ),
                getPurchaseParam);
    }

    public List<GetSaleOrderRes> getSale(int userIdx) {
        String getSaleQuery = "select\n" +
                "    O.orderIdx,\n" +
                "    I.itemName,\n" +
                "    concat(FORMAT(I.cost, 0), '원') AS itemCost,\n" +
                "    (select I2.imageUrl from ItemImages\n" +
                "                        inner join Images I2 on ItemImages.itemImageIdx = I2.itemImageIdx\n" +
                "                        where ItemImages.itemIdx = I.itemIdx\n" +
                "                        order by I2.createdAt desc limit 1) AS itemUrl,\n" +
                "    (select U.userName from Users U where O.buyUserIdx = U.userIdx) AS buyUserName,\n" +
                "    case\n" +
                "        when hour(O.updatedAt) >= 12 then DATE_FORMAT(O.updatedAt, '%y.%m.%d (오후 %l:%i)')\n" +
                "        ELSE DATE_FORMAT(O.updatedAt, '%y.%m.%d (오전 %l:%i)')\n" +
                "    END AS orderTime\n" +
                "from Orders O\n" +
                "inner join Items I on O.itemIdx = I.itemIdx\n" +
                "where I.userIdx = ?;";
        int getSaleParam = userIdx;

        return this.jdbcTemplate.query(getSaleQuery,
                (rs, rowNum) -> new GetSaleOrderRes(
                        rs.getInt("orderIdx"),
                        rs.getString("itemName"),
                        rs.getString("itemCost"),
                        rs.getString("itemUrl"),
                        rs.getString("buyUserName"),
                        rs.getString("orderTime")
                ),
                getSaleParam);
    }
}
