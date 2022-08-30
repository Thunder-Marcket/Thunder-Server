package com.example.demo.src.searches;

import com.example.demo.src.searches.model.GetBrands;
import com.example.demo.src.searches.model.GetSearch;
import com.example.demo.src.searches.model.GetSearchUserRes;
import com.example.demo.src.searches.model.PatchSearchesReq;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class SearchDao {

    private JdbcTemplate jdbcTemplate;

    public List<GetSearch> getSearches(int userIdx) {
        String getSearchesQuery =
                "select u.userIdx, searchIdx, searchText\n" +
                "from Users u\n" +
                "join Searchs s on u.userIdx = s.userIdx\n" +
                "where u.userIdx = ? and s.status = 'enable'\n" +
                "order by s.updatedAt desc;";

        int getSearchesParams = userIdx;
        return this.jdbcTemplate.query(getSearchesQuery,
                (rs, rowNum) -> new GetSearch(
                        rs.getInt("userIdx"),
                        rs.getInt("searchIdx"),
                        rs.getString("searchText")),
                getSearchesParams);
    }

    public List<GetBrands> getBrands(int userIdx) {
        String getBrandNamesQuery =
                "select brandName, brandSubName, imageUrl\n" +
                "       , concat((select ifnull(brandItemCnt,0)),'개') as brandItemCnt\n" +
                "       , case when bf.status = 'enable' then 1 else 0 end as isFollowing\n" +
                "from Brands b\n" +
                "    left join(\n" +
                "        select count(itemIdx) as brandItemCnt, tagName\n" +
                "        from Tags\n" +
                "        group by tagName\n" +
                "    ) t on t.tagName = b.brandName\n" +
                "    left join(\n" +
                "        select brand_followIdx, followingBrandIdx, followerUserIdx, status\n" +
                "        from Brand_Follows\n" +
                "        where followerUserIdx = ? and status = 'enable'\n" +
                "    ) bf on bf.followingBrandIdx = b.brandIdx\n" +
                "limit 5";
        int getBrandNamesParams = userIdx;
        return this.jdbcTemplate.query(getBrandNamesQuery,
                (rs, rowNum) -> new GetBrands(
                        rs.getString("brandName"),
                        rs.getString("brandSubName"),
                        rs.getString("imageUrl"),
                        rs.getString("brandItemCnt"),
                        rs.getInt("isFollowing")),
                getBrandNamesParams);
    }

    public int modifySearchStatus(int userIdx, PatchSearchesReq patchSearchesReq) {
        String modifyStatusQuery = "update Searchs set status = 'disable' where userIdx = ? and searchIdx = ? ";
        Object[] modifyStatusParams = new Object[]{userIdx, patchSearchesReq.getSearchIdx()};
        return this.jdbcTemplate.update(modifyStatusQuery, modifyStatusParams);
    }

    public int modifyAllSearchStatus(int userIdx) {
        String modifyStatusQuery = "update Searchs set status = 'disable' where userIdx = ? ";
        int modifyStatusParams = userIdx;
        return this.jdbcTemplate.update(modifyStatusQuery, modifyStatusParams);
    }

    public List<GetSearchUserRes> getSearchUsers( String search) {
        String getQuery =
                "select u.userIdx, profileImgUrl, userName, followerCnt, itemCnt\n" +
                "from Users u\n" +
                "    join (\n" +
                "        select count(followerUserIdx) as followerCnt, followingUserIdx\n" +
                "        from Follows\n" +
                "        group by followingUserIdx\n" +
                "    ) F on u.userIdx = F.followingUserIdx\n" +
                "    join (\n" +
                "        select count(itemIdx) as itemCnt, userIdx\n" +
                "        from Items i\n" +
                "        group by i.userIdx\n" +
                "    ) I on u.userIdx = I.userIdx\n" +
                "where userName like ? ";
        String getParams = "%"+search+"%";
        return this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new GetSearchUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("profileImgUrl"),
                        rs.getString("userName"),
                        rs.getInt("followerCnt"),
                        rs.getInt("itemCnt")),
                getParams);
    }
}
