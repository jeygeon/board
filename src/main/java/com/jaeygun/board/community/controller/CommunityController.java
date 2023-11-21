package com.jaeygun.board.community.controller;

import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.board.service.BoardService;
import com.jaeygun.board.board.service.ReplyService;
import com.jaeygun.board.common.dto.PaginationDTO;
import com.jaeygun.board.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final Logger log = LoggerFactory.getLogger(CommunityController.class);

    private final BoardService boardService;

    private final ReplyService replyService;

    @GetMapping("/main")
    public String index(HttpSession session, Model model) {

        UserDTO user = (UserDTO) session.getAttribute("loginUser");
        model.addAttribute("user", user);

        List<BoardDTO> boardList = boardService.getRecentBoardList(4);
        model.addAttribute("boardList", boardList);
        return "community/main";
    }

    @GetMapping("/write")
    public String write() {

        return "community/write";
    }

    @GetMapping("/post/{boardUid}")
    public String post(HttpSession session, Model model, @PathVariable long boardUid) {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        BoardDTO boardDTO = boardService.findPostByBoardUid(boardUid);
        model.addAttribute("board", boardDTO);

        int totalCount = replyService.getReplyTotalCount(boardUid);
        model.addAttribute("totalCount", totalCount);

        PaginationDTO paginationDTO = new PaginationDTO(totalCount, 1, 5, 5);
        model.addAttribute("pagination", paginationDTO);

        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        Pageable pageable = PageRequest.of(0, 5, sort);
        List<ReplyDTO> replyDTOList = replyService.getRecentReplyList(boardUid, pageable);
        for (ReplyDTO replyDTO : replyDTOList) {
            int checkResult = replyService.checkReplyLikeStatus(loginUser.getUserUid(), replyDTO.getReplyUid());
            if (checkResult == 1) {
                replyDTO.setLikeCheck(true);
            }
        }
        model.addAttribute("replyList", replyDTOList);

        return "community/post";
    }
}
