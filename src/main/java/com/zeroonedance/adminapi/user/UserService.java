package com.zeroonedance.adminapi.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 加载指定用户名（唯一标识）的用户。
     *
     *
     * @param username 要加载的用户的用户名（ 唯一标识）
     * @return 如果找到用户详情，则返回用户详情，否则抛出UsernameNotFoundException异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetails = userRepository.findUserByUsername(username);
        return userDetails.orElseThrow(() -> new UsernameNotFoundException("没有此用户"));
    }



    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }


}
