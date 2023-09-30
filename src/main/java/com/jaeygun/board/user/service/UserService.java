package com.jaeygun.board.user.service;

import com.jaeygun.board.user.dto.UserDTO;

public interface UserService {

    /**
     * 사용자 추가
     *
     * @param userDTO
     * @return
     */
    UserDTO addUser(UserDTO userDTO);

    /**
     * 사용자 확인 (by Id)
     *
     * @param userDTO
     * @return
     */
    UserDTO getUserById(UserDTO userDTO);

    /**
     * 사용자 확인 (by Id and Password)
     *
     * @param userDTO
     * @return
     */
    UserDTO getUserByIdAndPassword(UserDTO userDTO);
}
