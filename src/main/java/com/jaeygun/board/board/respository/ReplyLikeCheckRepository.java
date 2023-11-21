package com.jaeygun.board.board.respository;

import com.jaeygun.board.board.entity.ReplyLikeCheck;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ReplyLikeCheckRepository extends JpaRepository<ReplyLikeCheck, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM ReplyLikeCheck rlc WHERE rlc.userUid = :userUid AND rlc.replyUid = :replyUid")
    void deleteByUserUidAndReplyUid(@Param("userUid") long userUid, @Param("replyUid") long replyUid);

    ReplyLikeCheck findByUserUidAndReplyUid(long userUid, long replyUid);
}
