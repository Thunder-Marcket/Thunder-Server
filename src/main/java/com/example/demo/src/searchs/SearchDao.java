package com.example.demo.src.searchs;

import com.example.demo.src.searchs.model.GetSearchesRes;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class SearchDao {

    private JdbcTemplate jdbcTemplate;

    public List<GetSearchesRes> getSearches(int userIdx) {
        String getSearchesQuery =
                "select u.userIdx, searchText\n" +
                        "from Users u\n" +
                        "join Searchs s on u.userIdx = s.userIdx\n" +
                        "where u.userIdx = ?\n" +
                        "order by s.updatedAt desc;";
        int getSearchesParams = userIdx;
        return this.jdbcTemplate.query(getSearchesQuery,
                (rs, rowNum) -> new GetSearchesRes(
                        rs.getInt("userIdx"),
                        rs.getString("searchText"),
                        getBrandNames()),
                getSearchesParams);
    }

    private List<String> getBrandNames() {
        String getBrandNames =
                "select brandName\n" +
                "from Brands\n" +
                "limit 5;";
        return this.jdbcTemplate.query(getBrandNames,
                (rs, rowNum) -> new String(
                        rs.getString("brandName")));
    }
}
