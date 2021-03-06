package com.shuai.hehe.server.mapper;

import com.shuai.hehe.server.entity.Feed;
import com.shuai.hehe.server.entity.PicInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 *
 */
@Mapper
public interface FeedMapper {
    @SelectProvider(type = SqlBuilder.class, method = "buildSelectVideoSql")
    @Results({
            @Result(id=true,property="mId",column="id"),
            @Result(property="mType",column="type"),
            @Result(property="mTitle",column="title"),
            @Result(property="mContent",column="content"),
            @Result(property="mFrom",column="from"),
            @Result(property="mShowTime",column="show_time",typeHandler = TimeHandler.class)
    })
    List<Feed> getVideoList(
            @Param("id") String id,
            @Param("count") int count);

    @SelectProvider(type = SqlBuilder.class, method = "buildSelectAlbumSql")
    @Results({
            @Result(id=true,property="mId",column="id"),
            @Result(property="mType",column="type"),
            @Result(property="mTitle",column="title"),
            @Result(property="mContent",column="content"),
            @Result(property="mFrom",column="from"),
            @Result(property="mShowTime",column="show_time",typeHandler = TimeHandler.class)
    })
    List<Feed> getAlbumList(
            @Param("id") String id,
            @Param("count") int count);

    @Select("SELECT id,big_url,description FROM pic WHERE feed_id=#{feedId}")
    @Results({
            @Result(id=true,property="mId",column="id"),
            @Result(property="mBigUrl",column="big_url"),
            @Result(property="mDescription",column="description")
    })
    List<PicInfo> getAlbumPics(@Param("feedId") int feedId);

    @Select("SELECT * FROM hot_album WHERE id=#{feedId}")
    @Results({
            @Result(id=true,property="mId",column="id"),
            @Result(property="mType",column="type"),
            @Result(property="mTitle",column="title"),
            @Result(property="mContent",column="content"),
            @Result(property="mFrom",column="from"),
            @Result(property="mShowTime",column="show_time",typeHandler = TimeHandler.class)
    })
    Feed getAlbumFeed(@Param("feedId")int feedId);

    @Select("SELECT * FROM hot_video WHERE id=#{feedId}")
    @Results({
            @Result(id=true,property="mId",column="id"),
            @Result(property="mType",column="type"),
            @Result(property="mTitle",column="title"),
            @Result(property="mContent",column="content"),
            @Result(property="mFrom",column="from"),
            @Result(property="mShowTime",column="show_time",typeHandler = TimeHandler.class)
    })
    Feed getVideoFeed(@Param("feedId")int feedId);

    class SqlBuilder {
        public String buildSelectVideoSql(@Param("id") String id, @Param("count") int count) {
            String sql = "SELECT id,type,title,content,`from`,show_time FROM hot_video WHERE state!=0";

            if (count > 0) {
                sql += " AND show_time>'" + id+"'";
            } else {
                sql += " AND show_time<'" + id+"'";
            }

            sql = sql + " ORDER BY show_time " + (count < 0 ? " DESC " : " ASC ") + " LIMIT " + Math.abs(count);
            return sql;
        }

        public String buildSelectAlbumSql(@Param("id")String id, @Param("count")int count) {
            String sql = "SELECT id,type,title,content,`from`,show_time FROM hot_album WHERE state!=0";

            if (count > 0) {
                sql += " AND show_time>'" + id+"'";
            } else {
                sql += " AND show_time<'" + id+"'";
            }

            sql = sql + " ORDER BY show_time " + (count < 0 ? " DESC " : " ASC ") + " LIMIT " + Math.abs(count);
            return sql;
        }

    }
}
