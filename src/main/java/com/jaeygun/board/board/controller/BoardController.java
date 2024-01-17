package com.jaeygun.board.board.controller;


import com.jaeygun.board.board.dto.BoardDTO;
import com.jaeygun.board.board.dto.ReplyDTO;
import com.jaeygun.board.board.dto.ReplyLikeCheckDTO;
import com.jaeygun.board.board.service.BoardService;
import com.jaeygun.board.board.service.ReplyService;
import com.jaeygun.board.common.dto.PaginationDTO;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.util.ClientUtil;
import com.jaeygun.board.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final Logger log = LoggerFactory.getLogger(BoardController.class);

    private final BoardService boardService;

    private final ReplyService replyService;

    private final String imageUploadDir = "/Users/jaeygun/imageTest";

    /**
     * 에디터 이미지 업로드
     * @param image 파일 객체
     * @return 업로드된 파일명
     */
    @PostMapping("/image-upload")
    public String uploadEditorImage(@RequestParam final MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();                                         // 원본 파일명
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");           // 32자리 랜덤 문자열
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);  // 확장자
        String saveFilename = uuid + "." + extension;                                             // 디스크에 저장할 파일명
        String fileFullPath = Paths.get(imageUploadDir, saveFilename).toString();                      // 디스크에 저장할 파일의 전체 경로

        // uploadDir에 해당되는 디렉터리가 없으면, uploadDir에 포함되는 전체 디렉터리 생성
        File dir = new File(imageUploadDir);
        if (dir.exists() == false) {
            dir.mkdirs();
        }

        try {
            // 파일 저장 (write to disk)
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return saveFilename;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 디스크에 업로드된 파일을 byte[]로 반환
     * @param filename 디스크에 업로드된 파일명
     * @return image byte array
     */
    @GetMapping(value = "/image-print", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
    public byte[] printEditorImage(@RequestParam final String filename) {
        // 업로드된 파일의 전체 경로
        String fileFullPath = Paths.get(imageUploadDir, filename).toString();

        // 파일이 없는 경우 예외 throw
        File uploadedFile = new File(fileFullPath);
        if (uploadedFile.exists() == false) {
            throw new RuntimeException();
        }

        try {
            // 이미지 파일을 byte[]로 변환 후 반환
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            return imageBytes;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/save")
    public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response, HttpSession session, BoardDTO boardDTO) throws ServletException, IOException {

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        try {
            boardDTO = boardService.addPost(loginUser, boardDTO);
            log.info("[post add success] user : " + loginUser.getName() + ", subject : " + boardDTO.getSubject());
            resultMap.put(ClientUtil.MESSAGE, "등록되었습니다.");
            resultMap.put("redirectUrl", "/community/post/" + boardDTO.getBoardUid());
            resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        } catch (Exception e) {
            resultMap.put(ClientUtil.MESSAGE, "게시글 등록 중 오류가 발생했습니다.");
            log.error("An error occurred while registering the post.", e);
        }
        return resultMap;
    }

    @PostMapping("/{boardUid}/reply/add")
    public Map<String, Object> replySave(HttpSession session, ReplyDTO replyDTO, @PathVariable(value = "boardUid") String boardUid) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        replyDTO.setUserDTO(loginUser);

        replyDTO = replyService.addReply(replyDTO);
        if (replyDTO != null) {
            log.info("[user : " + loginUser.getName() + "] 댓글 입력 > boardUid : " + replyDTO.getBoardUid() + " > reply : " + replyDTO.getContent());
            resultMap.put("reply", replyDTO);
            resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        } else {
            resultMap.put(ClientUtil.MESSAGE, "댓글 입력이 실패했습니다.\n잠시 후 다시 시도해주세요.");
        }

        return resultMap;
    }

    @PostMapping("/{boardUid}/reply/paging")
    public Map<String, Object> replyPaging(@PathVariable(value="boardUid") long boardUid, int size, int start, HttpSession session) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
        Pageable pageable = PageRequest.of(start - 1, size, sort);
        List<ReplyDTO> replyDTOList = replyService.getRecentReplyList(boardUid, pageable);
        if (replyDTOList.size() == 0) {
            resultMap.put("replyList", "");
            return resultMap;
        }

        for (ReplyDTO replyDTO : replyDTOList) {
            int checkResult = replyService.checkReplyLikeStatus(loginUser.getUserUid(), replyDTO.getReplyUid());
            if (checkResult == 1) {
                replyDTO.setLikeCheck(true);
            }
        }
        resultMap.put("replyList", replyDTOList);

        int totalCount = replyService.getReplyTotalCount(boardUid);
        resultMap.put("totalCount", totalCount);

        // start = start == 0 ? 1 : start;
        PaginationDTO paginationDTO = new PaginationDTO(totalCount, start, 5, 5);
        resultMap.put("pagination", paginationDTO);

        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        return resultMap;
    }

    @PutMapping("/{boardUid}/reply/{replyUid}")
    public Map<String, Object> UpdateReplyLikeCount(@PathVariable(value = "boardUid") long boardUid,
                                                @PathVariable(value = "replyUid") long replyUid,
                                                HttpServletRequest request,
                                                HttpSession session) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String status = request.getParameter("status");

        // 댓글 좋아요 count update
        ReplyDTO replyDTO = replyService.updateReplyLikeCount(boardUid, replyUid, status);
        if (replyDTO == null) {
            resultMap.put(ClientUtil.MESSAGE, "오류가 발생 했습니다.\n잠시 후 다시 시도 해 주세요.");
            return resultMap;
        }
        log.info("[Owner : {}] Reply like count update > boardUid : {}, replyUid {}, status : {}", loginUser.getNickName(), boardUid, replyUid, status);

        // 댓글 좋아요 기록 update
        ReplyLikeCheckDTO replyLikeCheckDTO = new ReplyLikeCheckDTO();
        replyLikeCheckDTO.setUserUid(loginUser.getUserUid());
        replyLikeCheckDTO.setReplyUid(replyDTO.getReplyUid());
        replyService.updateReplyLikeHistory(replyLikeCheckDTO, status);

        resultMap.put("reply", replyDTO);
        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);
        return resultMap;
    }

    @PostMapping("/{boardUid}/like/{status}")
    public Map<String, Object> likePost(HttpSession session,
                                        @PathVariable(value = "boardUid") long boardUid,
                                        @PathVariable(value = "status") boolean status) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        BoardDTO boardDTO = boardService.likePost(loginUser, boardUid, status);
        resultMap.put("postLikeCount", boardDTO.getLikeCount());

        return resultMap;
    }

    @PostMapping("/{boardUid}/subscribe/{status}")
    public Map<String, Object> subscribePost(HttpSession session,
                                        @PathVariable(value = "boardUid") long boardUid,
                                        @PathVariable(value = "status") boolean status) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(JsonUtil.RESULT, JsonUtil.FAILURE);

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        boardService.subscribePost(loginUser, boardUid, status);
        resultMap.put(JsonUtil.RESULT, JsonUtil.SUCCESS);

        return resultMap;
    }
}
