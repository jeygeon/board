package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.board.dto.ReplyLikeCheckDTO;
import com.jaeygun.board.board.entity.Reply;
import com.jaeygun.board.board.entity.ReplyLikeCheck;
import com.jaeygun.board.board.respository.ReplyLikeCheckRepository;
import com.jaeygun.board.board.respository.ReplyRepository;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    private final ReplyLikeCheckRepository replyLikeCheckRepository;

    private final Logger log = LoggerFactory.getLogger(ReplyServiceImpl.class);

    @Override
    public ReplyDTO addReply(ReplyDTO replyDTO) {

        replyDTO.setCreatedTime(TimeUtil.currentTime());
        Reply reply = replyRepository.save(replyDTO.toEntity());
        return reply == null ? null : reply.toDTO();
    }

    @Override
    public List<ReplyDTO> getRecentReplyList(long boardUid, Pageable pageable) {

        List<ReplyDTO> replyDTOList = new ArrayList<>();
        List<Reply> replyList = replyRepository.findByBoardUid(boardUid, pageable);

        if (replyList.size() == 0) {
            return replyDTOList;
        }

        for (Reply reply : replyList) {
            replyDTOList.add(reply.toDTO());
        }
        return replyDTOList;
    }

    @Override
    public ReplyDTO updateReplyLikeCount(long boardUid, long replyUid, String status) {

        Reply reply = replyRepository.findByBoardUidAndReplyUid(boardUid, replyUid);

        ReplyDTO replyDTO = reply.toDTO();
        switch (status) {
            case "up":
                replyDTO.setLikeCount(replyDTO.getLikeCount() + 1);
                break;
            case "down":
                replyDTO.setLikeCount(replyDTO.getLikeCount() - 1);
                break;
        }

        reply = replyRepository.save(replyDTO.toEntity());
        return reply.toDTO();
    }

    @Override
    public void updateReplyLikeHistory(ReplyLikeCheckDTO replyLikeCheckDTO, String status) {

        switch (status) {
            case "up":
                replyLikeCheckRepository.save(replyLikeCheckDTO.toEntity());
                break;
            case "down":
                replyLikeCheckRepository.deleteByUserUidAndReplyUid(replyLikeCheckDTO.getUserUid(), replyLikeCheckDTO.getReplyUid());
                break;
        }

    }

    @Override
    public int checkReplyLikeStatus(long userUid, long replyUid) {

        ReplyLikeCheckDTO replyLikeCheckDTO = new ReplyLikeCheckDTO();
        ReplyLikeCheck replyLikeCheck = replyLikeCheckRepository.findByUserUidAndReplyUid(userUid, replyUid);
        return replyLikeCheck != null ? 1 : 0;
    }
}
