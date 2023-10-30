package com.jaeygun.board.board.service;

import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.board.entity.Reply;
import com.jaeygun.board.board.respository.ReplyRepository;
import com.jaeygun.board.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
