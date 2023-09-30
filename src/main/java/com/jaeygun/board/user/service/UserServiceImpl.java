package com.jaeygun.board.user.service;

import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.user.entity.User;
import com.jaeygun.board.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserDTO addUser(UserDTO userDTO) {

        User user = userRepository.save(userDTO.toEntity());
        if (user == null) {
            return null;
        }
        return user.toDTO();
    }

    @Override
    public UserDTO getUserById(UserDTO userDTO) {

        User user = userRepository.findUserById(userDTO.toEntity().getId());
        if (user == null) {
            return null;
        }
        return user.toDTO();
    }

    @Override
    public UserDTO getUserByIdAndPassword(UserDTO userDTO) {

        User user = userDTO.toEntity();
        user = userRepository.findUserByIdAndPassword(user.getId(), user.getPassword());
        if (user == null) {
            return null;
        }
        return user.toDTO();
    }
}
