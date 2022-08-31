package com.example.demo.src.categories;

import com.example.demo.src.categories.model.CategoryInfo;
import com.example.demo.src.categories.model.GetCategoryItemsRes;
import com.example.demo.src.categories.model.ItemInfo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class CategoryDao {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;

    public List<ItemInfo> getItemsByCategory(int userIdx, int categoryIdx) {
        String getQuery =
                "select i.itemIdx,\n" +
                "       concat(cost,'원') as cost,\n" +
                "       itemName,\n" +
                "       isSafePayment,\n" +
                "       (select Images.imageUrl from Images where Images.imageIdx = (select min(Images.imageIdx)\n" +
                "                                                                    from Images\n" +
                "                                                                        inner join ItemImages ii on Images.itemImageIdx = ii.itemImageIdx\n" +
                "                                                                        inner join Items i2 on ii.itemIdx = i2.itemIdx\n" +
                "                                                                    where i2.itemIdx = i.itemIdx)) as imageUrl,\n" +
                "       case when l.status = 'enable' then 1 else 0 end isLike,\n" +
                "       isAdItem,\n" +
                "       case when timestampdiff(second , i.updatedAt, current_timestamp) <60\n" +
                "        then concat(timestampdiff(second, i.updatedAt, current_timestamp),' 초 전')\n" +
                "        when timestampdiff(minute , i.updatedAt, current_timestamp) <60\n" +
                "        then concat(timestampdiff(minute, i.updatedAt, current_timestamp),' 분 전')\n" +
                "        when timestampdiff(hour , i.updatedAt, current_timestamp) <24\n" +
                "        then concat(timestampdiff(hour, i.updatedAt, current_timestamp),' 시간 전')\n" +
                "        else concat(datediff( current_timestamp, i.updatedAt),' 일 전')\n" +
                "    end                                      as uploadTime,\n" +
                "      i.address,\n" +
                "       likeCnt\n" +
                "from Items i\n" +
                "    join(\n" +
                "        select count(likeIdx) as likeCnt, L.itemIdx\n" +
                "        from Likes L\n" +
                "        group by L.itemIdx\n" +
                "    )L on i.itemIdx = L.itemIdx\n" +
                "    left join (\n" +
                "        select userIdx, itemIdx, status\n" +
                "        from Likes\n" +
                "        where userIdx = ?\n" +
                "    ) l on l.itemIdx = i.itemIdx\n" +
                "where i.categoryIdx = ?";
        Object[] getParams = new Object[]{userIdx, categoryIdx};
        return this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new ItemInfo(
                        rs.getInt("itemIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("cost"),
                        rs.getString("itemName"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("isLike"),
                        rs.getInt("isAdItem"),
                        rs.getString("uploadTime"),
                        rs.getString("address"),
                        rs.getInt("likeCnt")),
                getParams);
    }

    public List<ItemInfo> getItemsByTwoCategory(int userIdx, int categoryIdx, int subCategoryIdx) {
        String getQuery =
                "select i.itemIdx,\n" +
                    "       concat(cost,'원') as cost,\n" +
                    "       itemName,\n" +
                    "       isSafePayment,\n" +
                    "       (select Images.imageUrl from Images where Images.imageIdx = (select min(Images.imageIdx)\n" +
                    "                                                                    from Images\n" +
                    "                                                                        inner join ItemImages ii on Images.itemImageIdx = ii.itemImageIdx\n" +
                    "                                                                        inner join Items i2 on ii.itemIdx = i2.itemIdx\n" +
                    "                                                                    where i2.itemIdx = i.itemIdx)) as imageUrl,\n" +
                    "       case when l.status = 'enable' then 1 else 0 end isLike,\n" +
                    "       isAdItem,\n" +
                    "       case when timestampdiff(second , i.updatedAt, current_timestamp) <60\n" +
                    "        then concat(timestampdiff(second, i.updatedAt, current_timestamp),' 초 전')\n" +
                    "        when timestampdiff(minute , i.updatedAt, current_timestamp) <60\n" +
                    "        then concat(timestampdiff(minute, i.updatedAt, current_timestamp),' 분 전')\n" +
                    "        when timestampdiff(hour , i.updatedAt, current_timestamp) <24\n" +
                    "        then concat(timestampdiff(hour, i.updatedAt, current_timestamp),' 시간 전')\n" +
                    "        else concat(datediff( current_timestamp, i.updatedAt),' 일 전')\n" +
                    "    end                                      as uploadTime,\n" +
                    "      i.address,\n" +
                    "       likeCnt\n" +
                    "from Items i\n" +
                    "    join(\n" +
                    "        select count(likeIdx) as likeCnt, L.itemIdx\n" +
                    "        from Likes L\n" +
                    "        group by L.itemIdx\n" +
                    "    )L on i.itemIdx = L.itemIdx\n" +
                    "    left join (\n" +
                    "        select userIdx, itemIdx, status\n" +
                    "        from Likes\n" +
                    "        where userIdx = ?\n" +
                    "    ) l on l.itemIdx = i.itemIdx\n" +
                    "where i.categoryIdx = ? and i.subCategoryIdx = ?";
        Object[] getParams = new Object[]{userIdx, categoryIdx, subCategoryIdx};
        return this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new ItemInfo(
                        rs.getInt("itemIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("cost"),
                        rs.getString("itemName"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("isLike"),
                        rs.getInt("isAdItem"),
                        rs.getString("uploadTime"),
                        rs.getString("address"),
                        rs.getInt("likeCnt")),
                getParams);
    }

    public List<ItemInfo> getItemsByThreeCategory(int userIdx, int categoryIdx, int subCategoryIdx, int subSubcategoryIdx) {
        String getQuery =
                "select i.itemIdx,\n" +
                "       concat(cost,'원') as cost,\n" +
                "       itemName,\n" +
                "       isSafePayment,\n" +
                "       (select Images.imageUrl from Images where Images.imageIdx = (select min(Images.imageIdx)\n" +
                "                                                                    from Images\n" +
                "                                                                        inner join ItemImages ii on Images.itemImageIdx = ii.itemImageIdx\n" +
                "                                                                        inner join Items i2 on ii.itemIdx = i2.itemIdx\n" +
                "                                                                    where i2.itemIdx = i.itemIdx)) as imageUrl,\n" +
                "       case when l.status = 'enable' then 1 else 0 end isLike,\n" +
                "       isAdItem,\n" +
                "       case when timestampdiff(second , i.updatedAt, current_timestamp) <60\n" +
                "        then concat(timestampdiff(second, i.updatedAt, current_timestamp),' 초 전')\n" +
                "        when timestampdiff(minute , i.updatedAt, current_timestamp) <60\n" +
                "        then concat(timestampdiff(minute, i.updatedAt, current_timestamp),' 분 전')\n" +
                "        when timestampdiff(hour , i.updatedAt, current_timestamp) <24\n" +
                "        then concat(timestampdiff(hour, i.updatedAt, current_timestamp),' 시간 전')\n" +
                "        else concat(datediff( current_timestamp, i.updatedAt),' 일 전')\n" +
                "    end                                      as uploadTime,\n" +
                "      i.address,\n" +
                "       likeCnt\n" +
                "from Items i\n" +
                "    join(\n" +
                "        select count(likeIdx) as likeCnt, L.itemIdx\n" +
                "        from Likes L\n" +
                "        group by L.itemIdx\n" +
                "    )L on i.itemIdx = L.itemIdx\n" +
                "    left join (\n" +
                "        select userIdx, itemIdx, status\n" +
                "        from Likes\n" +
                "        where userIdx = ?\n" +
                "    ) l on l.itemIdx = i.itemIdx\n" +
                "where i.categoryIdx = ? and i.subCategoryIdx = ? and i.subSubcategoryIdx = ?;";
        Object[] getParams = new Object[]{userIdx, categoryIdx, subCategoryIdx, subSubcategoryIdx};
        return this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new ItemInfo(
                        rs.getInt("itemIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("cost"),
                        rs.getString("itemName"),
                        rs.getInt("isSafePayment"),
                        rs.getInt("isLike"),
                        rs.getInt("isAdItem"),
                        rs.getString("uploadTime"),
                        rs.getString("address"),
                        rs.getInt("likeCnt")),
                getParams);
    }

    public CategoryInfo getCategoryInfo(int categoryIdx) {
        String getQuery = "select categoryIdx, categoryName from Categorys where categoryIdx = ?;\n";
        Object[] getParams = new Object[]{categoryIdx};
        return this.jdbcTemplate.queryForObject(getQuery,
                (rs, rowNum) -> new CategoryInfo(
                        rs.getInt("categoryIdx"),
                        rs.getString("categoryName")),
                getParams);
    }

    public CategoryInfo getCategoryInfoBySub(int subCategoryIdx) {
        String getQuery = "select subCategoryIdx, categoryName from SubCategory where subCategoryIdx = ?;";
        Object[] getParams = new Object[]{subCategoryIdx};
        return this.jdbcTemplate.queryForObject(getQuery,
                (rs, rowNum) -> new CategoryInfo(
                        rs.getInt("subCategoryIdx"),
                        rs.getString("categoryName")),
                getParams);
    }

    public CategoryInfo getCategoryInfoBySubSub(int subSubcategoryIdx) {
        String getQuery = "select subSubcategoryIdx, categoryName from SubSubCategory where subSubcategoryIdx = ?;\n";
        Object[] getParams = new Object[]{subSubcategoryIdx};
        return this.jdbcTemplate.queryForObject(getQuery,
                (rs, rowNum) -> new CategoryInfo(
                        rs.getInt("subSubcategoryIdx"),
                        rs.getString("categoryName")),
                getParams);
    }

    public List<CategoryInfo> getCategoryInfoList(int categoryIdx) {
        String getQuery =
                "select subCategoryIdx, categoryName\n" +
                "from SubCategory\n" +
                "where categoryIdx = ?;";

        Object[] getParams = new Object[]{categoryIdx};
        return this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new CategoryInfo(
                        rs.getInt("subCategoryIdx"),
                        rs.getString("categoryName")),
                getParams);
    }

    public List<CategoryInfo> getCategoryInfoListBySub(int subCategoryIdx) {
        String getQuery =
                "select subSubcategoryIdx, categoryName\n" +
                "from SubSubCategory\n" +
                "where subCategoryIdx = ?;";
        Object[] getParams = new Object[]{subCategoryIdx};
        return this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new CategoryInfo(
                        rs.getInt("subSubcategoryIdx"),
                        rs.getString("categoryName")),
                getParams);
    }

    public int checkSubCategory(int categoryIdx) {
        String checkQuery = "select exists(select subCategoryIdx from SubCategory sc where categoryIdx = ?)";
        Object[] checkParams = new Object[]{categoryIdx};
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, checkParams);
    }

    public int checkSubSubCategory(int subCategoryIdx) {
        String checkQuery = "select exists(select subSubcategoryIdx from SubSubCategory where subCategoryIdx = ?)";

        Object[] checkParams = new Object[]{subCategoryIdx};
        return this.jdbcTemplate.queryForObject(checkQuery, int.class, checkParams);
    }
}
