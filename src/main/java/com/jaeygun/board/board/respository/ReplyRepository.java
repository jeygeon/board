package com.jaeygun.board.board.respository;

import com.jaeygun.board.board.entity.Reply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    int countByBoardUid(long boardUid);

    List<Reply> findByBoardUid(long boardUid, Pageable pageable);

    Reply findByBoardUidAndReplyUid(long boardUid, long replyUid);
}
