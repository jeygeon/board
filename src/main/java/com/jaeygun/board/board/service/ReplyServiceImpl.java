package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.board.entity.Reply;
import com.jaeygun.board.board.respository.ReplyRepository;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;

    @Override
    public ReplyDTO addReply(ReplyDTO replyDTO) {

        replyDTO.setCreatedTime(TimeUtil.currentTime());
        Reply reply = replyRepository.save(replyDTO.toEntity());
        return reply == null ? null : reply.toDTO();
    }

    @Override
    public List<ReplyDTO> getRecentReplyList(long boardUid, Pageable pageable) {

        List<ReplyDTO> replyDTOList = new ArrayList<>();
        List<Reply> replyList = replyRepository.findAll(pageable).getContent();

        if (replyList.size() == 0) {
            return null;
        }

        for (Reply reply : replyList) {
            replyDTOList.add(reply.toDTO());
        }
        return replyDTOList;
    }

    @Override
    public int getReplyTotalCount(long boardUid) {

        return replyRepository.countByBoardUid(boardUid);
    }
}
