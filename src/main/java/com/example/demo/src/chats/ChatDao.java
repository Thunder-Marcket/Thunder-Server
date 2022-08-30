package com.example.demo.src.chats;

import com.example.demo.src.chats.model.GetChatRoomListRes;
import com.example.demo.src.chats.model.GetChatRoomRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChatDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetChatRoomListRes> getChatRoomList(int userIdx) {
        String getChatRoomListQuery = "select\n" +
                "    (select U.profileImgUrl from Items I\n" +
                "            inner join Users U on I.userIdx = U.userIdx\n" +
                "            where I.itemIdx = CR.itemIdx) AS storeImageUrl,\n" +
                "    (select U.userName from Items I\n" +
                "                      inner join Users U on I.userIdx = U.userIdx\n" +
                "                      where I.itemIdx = CR.itemIdx) AS storeName,\n" +
                "    (select I.itemIdx from Items I where I.itemIdx = CR.itemIdx) AS itemIdx,\n" +
                "    CR.chatRoomIdx,\n" +
                "    (select I.userIdx from Items I where I.itemIdx = CR.itemIdx) AS storeUserIdx,\n" +
                "    (select C.message from Chats C where C.chatRoomIdx = CR.chatRoomIdx order by C.createdAt desc limit 1) AS lastChat,\n" +
                "    (select\n" +
                "        case\n" +
                "            when datediff(now(), C.createdAt) < 1 then concat(abs(hour(now()) - hour(C.createdAt)), '시간 전')\n" +
                "            ELSE date_format(now(), '%c월 %d일')\n" +
                "        END\n" +
                "     from Chats C where C.chatRoomIdx = CR.chatRoomIdx) AS period\n" +
                "\n" +
                "\n" +
                "from ChatRooms CR\n" +
                "\n" +
                "where CR.buyUserIdx = ? or ? in (select I.userIdx from Items I where I.itemIdx = CR.itemIdx);";
        Object[] getChatRoomListParam = new Object[]{userIdx, userIdx};

        return this.jdbcTemplate.query(getChatRoomListQuery,
                (rs, rowNum) -> new GetChatRoomListRes(
                        rs.getString("storeImageUrl"),
                        rs.getString("storeName"),
                        rs.getString("period"),
                        rs.getString("lastChat"),
                        rs.getInt("itemIdx"),
                        rs.getInt("chatRoomIdx"),
                        rs.getInt("storeUserIdx")
                ), getChatRoomListParam);

    }

    public List<GetChatRoomRes> getChatRoom(int chatRoomIdx) {
        String getChatRoomQuery = "select\n" +
                "    C.userIdx,\n" +
                "    C.message\n" +
                "from ChatRooms CR\n" +
                "inner join Chats C on CR.chatRoomIdx = C.chatRoomIdx\n" +
                "where CR.chatRoomIdx = ?\n" +
                "order by C.createdAt asc;";
        int getChatRoomParam = chatRoomIdx;

        return this.jdbcTemplate.query(getChatRoomQuery,
                (rs, rowNum) -> new GetChatRoomRes(
                        rs.getInt("userIdx"),
                        rs.getString("message")
                ), getChatRoomParam);
    }

    public int checkChatRoom(int userIdx, int chatRoomIdx) {
        String checkChatRoomQuery = "select\n" +
                "    case\n" +
                "        when exists(select chatRoomIdx from ChatRooms where buyUserIdx = ? and chatRoomIdx = ?) = 1 then 1\n" +
                "        when exists(select chatRoomIdx from ChatRooms\n" +
                "                                    inner join Items I on ChatRooms.itemIdx = I.itemIdx\n" +
                "                                    where I.userIdx = ? and chatRoomIdx = ?) = 1 then 1\n" +
                "        ELSE 0\n" +
                "    END";
        Object[] checkChatRoomParams = new Object[]{userIdx, chatRoomIdx, userIdx, chatRoomIdx};

        return this.jdbcTemplate.queryForObject(checkChatRoomQuery, int.class, checkChatRoomParams);
    }
}
