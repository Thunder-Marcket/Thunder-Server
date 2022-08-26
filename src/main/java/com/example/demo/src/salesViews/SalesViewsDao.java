package com.example.demo.src.salesViews;

import com.example.demo.src.salesViews.model.GetSalesViewsRes;
import com.example.demo.src.salesViews.model.PatchSalesViewsRes;
import com.example.demo.src.salesViews.model.SalesViews;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Repository
public class SalesViewsDao {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 최근 본 상품 조회
    public List<GetSalesViewsRes> getSalesViews(int userIdx) {
        String getSalesViewsQuery =
                "select i.itemIdx, itemName, concat(cost, '원') as cost, isSafePayment,\n" +
                "       case when timestampdiff(second , i.updatedAt, current_timestamp) <60\n" +
                "        then concat(timestampdiff(second, i.updatedAt, current_timestamp),' 초 전')\n" +
                "        when timestampdiff(minute , i.updatedAt, current_timestamp) <60\n" +
                "        then concat(timestampdiff(minute, i.updatedAt, current_timestamp),' 분 전')\n" +
                "        when timestampdiff(hour , i.updatedAt, current_timestamp) <24\n" +
                "        then concat(timestampdiff(hour, i.updatedAt, current_timestamp),' 시간 전')\n" +
                "        else concat(datediff( current_timestamp, i.updatedAt),' 일 전')\n" +
                "    end                                      as uploadTime,\n" +
                "       (select Images.imageUrl from Images where Images.imageIdx = (select min(Images.imageIdx)\n" +
                "                                                                    from Images\n" +
                "                                                                        inner join ItemImages ii on Images.itemImageIdx = ii.itemImageIdx\n" +
                "                                                                        inner join Items i2 on ii.itemIdx = i2.itemIdx\n" +
                "                                                                    where i2.itemIdx = i.itemIdx)) as imageUrl\n" +
                "from (\n" +
                "     select userIdx, itemIdx, updatedAt\n" +
                "    from SaleViews\n" +
                "    where userIdx = ?\n" +
                ") sv\n" +
                "join (\n" +
                "    select itemIdx, itemName, cost, updatedAt, isSafePayment\n" +
                "    from Items\n" +
                ") i on i.itemIdx = sv.itemIdx\n" +
                "order by sv.updatedAt desc;";
        int getSalesViewsParams = userIdx;
        return this.jdbcTemplate.query(getSalesViewsQuery,
                (rs, rowNum) -> new GetSalesViewsRes(
                        rs.getInt("itemIdx"),
                        rs.getString("itemName"),
                        rs.getString("cost"),
                        rs.getInt("isSafePayment"),
                        rs.getString("uploadTime"),
                        rs.getString("imageUrl")),
                getSalesViewsParams);
    }

    public int modifySalesViewsStatus(SalesViews salesViews) {
        String modifyStatusQuery = "update SaleViews set status = 'disable' where viewItemIdx = ?";
        int modifyStatusParams = salesViews.getViewItemIdx();

        return this.jdbcTemplate.update(modifyStatusQuery, modifyStatusParams);

    }
}
