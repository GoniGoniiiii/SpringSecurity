package com.example.testsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(Model model) {

        String id= SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        //현재 사용자의 인증 정보 가져옴 Authentication객체로 반환
        
        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
        //현재 사용자의 권한 목록 가져옴
        Iterator<? extends GrantedAuthority> iter=authorities.iterator();
//     //권한 목록 둘러보기 위해서 Iterator반복자 생성
        GrantedAuthority auth=iter.next();
        //iter.next() 호출해서 첫 번째 권한 가져옴
        // 권한은 GrantedAuthority 객체로 사용자가 가지고 있는 권한을 보여줌
        String role=auth.getAuthority();
        //해당 권한의 이름 가지고 옴 .  여기선 역할

        model.addAttribute("id",id);
        model.addAttribute("role",role);
        return "main";
    }
}
