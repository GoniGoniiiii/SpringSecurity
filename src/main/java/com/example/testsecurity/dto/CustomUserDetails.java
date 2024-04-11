package com.example.testsecurity.dto;

import com.example.testsecurity.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    //UserDetails인터페이스를 구현한 클래스
    //UserEntity를 기반으로  사용자의 상세 정보 제공
    private UserEntity userEntity;

    //UserEntity객체를 필드로 가짐 -> UserEntity 정보에 접근 가능
    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //사용자의 권한에 대해 return해주는 메소드
        //UserEntity에서 가져온 역할 정보를 GranteAuthority 타입으로 반환
        //GrantAuthority : 스프링 시큐리티에서 권한을 나타내는 인터페이스
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() { //사용자 비밀번호 반환
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() { //사용자 이름 반환
        return userEntity.getUsername();
    }

    //밑에 4개 true값으로 바꿔줘야 만료되지않고 사용 가능하대
    // 모두 true 반환 -> 계정 만료X, 잠금 X , 자격증명 만료 X , 계정 활성화 O
    @Override
    public boolean isAccountNonExpired() { //사용자의 아이디가 만료되었는지
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //사용자의 아이디가 잠겼는지
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {//사용자의 비밀번호가 만료되었는지 
        return true;
    }

    @Override
    public boolean isEnabled() { //계정  활성화 여부
        return true;
    }


}
