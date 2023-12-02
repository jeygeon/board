package com.jaeygun.board.board.respository;

import com.jaeygun.board.board.entity.UserPostSubscribe;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserPostSubscribeRepository extends JpaRepository<UserPostSubscribe, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UserPostSubscribe ups WHERE ups.userUid = :userUid AND ups.boardUid = :boardUid")
    void deleteByUserUidAndBoardUid(@Param("userUid") long userUid, @Param("boardUid") long boardUid);

    UserPostSubscribe findByBoardUidAndUserUid(long boardUid, long userUid);
}
