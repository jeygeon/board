package com.jaeygun.board.util;

import com.jaeygun.board.common.dto.MessageDTO;
import org.springframework.ui.Model;

public class ClientUtil {

    public static final String PAGE = "page";

    public static final String MESSAGE = "message";

    /**
     * 화면에 메시지 보여준 후 페이지 리다이렉트
     *
     * @param model
     * @param messageDTO
     * @return
     */
    public static String alertAndRedirect(Model model, MessageDTO messageDTO) {

        model.addAttribute("messageDTO", messageDTO);
        return "common/messageRedirect";
    }
}
