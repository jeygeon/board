package com.jaeygun.board.board.respository;

import com.jaeygun.board.board.entity.UserPostLike;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM UserPostLike upl WHERE upl.userUid = :userUid AND upl.boardUid = :boardUid")
    void deleteByUserUidAndBoardUid(@Param("userUid") long userUid, @Param("boardUid") long boardUid);

}
