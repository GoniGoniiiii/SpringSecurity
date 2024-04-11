package com.example.testsecurity.service;

import com.example.testsecurity.dto.CustomUserDetails;
import com.example.testsecurity.entity.UserEntity;
import com.example.testsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
        //UserDetailservice 인터페이스 구현 => 스프링 시큐리티가 사용자 정보를 로드하는데 사용
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //주어진 사용자 이름에 해당하는 사용자 정보를 db에서 찾아와서 UserDetails타입으로 반환
        //사용자 이름을 입력받아 해당하는 사용자의 상세 정보 반환
        UserEntity userData =userRepository.findByUsername(username);
        if(userData != null){
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
