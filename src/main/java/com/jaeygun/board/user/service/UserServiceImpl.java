package com.jaeygun.board.user.service;

import com.jaeygun.board.user.dto.NaverUserDTO;
import com.jaeygun.board.user.dto.NaverUserKeyDTO;
import com.jaeygun.board.user.dto.UserDTO;
import com.jaeygun.board.user.entity.NaverUserKey;
import com.jaeygun.board.user.entity.User;
import com.jaeygun.board.user.repository.NaverUserKeyRepository;
import com.jaeygun.board.user.repository.UserRepository;
import com.jaeygun.board.util.TimeUtil;
import com.jaeygun.board.util.variable.RegType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final NaverUserKeyRepository naverUserKeyRepository;

    @Override
    public UserDTO addUser(UserDTO userDTO) {

        userDTO.setRegDate(TimeUtil.currentTime());
        userDTO.setRegType(RegType.COMMON);
        User user = userRepository.save(userDTO.toEntity());
        if (user == null) {
            return null;
        }
        return user.toDTO();
    }

    @Override
    public UserDTO addNaverUser(NaverUserDTO naverUserDTO) {

        UserDTO userDTO = new UserDTO();
        userDTO.setName(naverUserDTO.getName());
        userDTO.setNickName(naverUserDTO.getNickName());
        userDTO.setEmailId(naverUserDTO.getEmail());
        userDTO.setPhoneNumber(naverUserDTO.getMobile());
        userDTO.setBirthday(naverUserDTO.getBirthyear() + "." + naverUserDTO.getBirthday().replace("-", "."));
        userDTO.setRegType(RegType.NAVER);
        userDTO.setRegDate(TimeUtil.currentTime());
        userDTO.setLoginTime(TimeUtil.currentTime());
        User user = userRepository.save(userDTO.toEntity());

        NaverUserKeyDTO userKeyDTO = new NaverUserKeyDTO();
        userKeyDTO.setUid(user.toDTO().getUid());
        userKeyDTO.setNKey(naverUserDTO.getId());
        naverUserKeyRepository.save(userKeyDTO.toEntity());

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

    @Override
    public List<UserDTO> getUserByNameAndPhoneNumber(UserDTO userDTO) {

        User user = userDTO.toEntity();
        List<UserDTO> existUserList = new ArrayList<UserDTO>();

        List<User> userList = userRepository.findUserByNameAndPhoneNumber(user.getName(), user.getPhoneNumber());
        for (User existUser : userList) {
            existUserList.add(existUser.toDTO());
        }

        return existUserList;
    }

    @Override
    public UserDTO getNaverUser(NaverUserDTO naverUserDTO) {

        String key = naverUserDTO.getId();
        NaverUserKey existKey = naverUserKeyRepository.findUserKeyBynKey(key);

        if (existKey != null) {
            User user = userRepository.findUserByUid(existKey.getUid());
            return user.toDTO();
        }

        return null;
    }
}
