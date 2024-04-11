package com.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  //시큐리티 설정  -> 이 클래스가 spring security한테서 관리됨
//이 클래스 내부에 특정한 메소드를 만들어서 bean을 등록시켜주면 자동으로 filter에 security설정을 custom으로 가능
public class SecurityConfig {

    @Bean //어떤 곳에서든 호출하면 쓸 수 있도록!
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        //사용자 비밀번호 해시화
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        //계층적 역할 구조 정의 -> 하위 역할이 상위역할의 권한을 상속받을 수 있게
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();

        hierarchy.setHierarchy("ROLE_C > ROLE_B\n" +
                "ROLE_B > ROLE_A");

        return hierarchy;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //예외처리 해줘야된대
        //경로별 인가 작성
        //authorizeHttpRequests  : 특정한 경로에다가 요청 허용,거부 가능하고 람다식으로 작성해야함!
        //requestMatchers : 특정한 경로에 요청을 진행하고싶다 !
        // 메소드 종류
        // - permitAll : 모든 사용자에게 로그인을 하지 않아도 접근할수있도록
        // - hasRole : 특정한 규칙이 있어야 이 경로에 접근할 수 있도록
        // - authenticated : 로그인만 진행하면 접근할 수 있음
        // - denyAll : 모든 사용자의 접근을 막음
        //주의할 점 : 상단에서부터 진행되기때문에 순서가 매우 중요함!!!
//        http
//                .authorizeHttpRequests((auth) -> auth //특정한 경로에다가 요청 허용, 거부 가능하고 람다식으로 작성해야함
//                        .requestMatchers("/","/login","/join","/joinProc").permitAll()     //특정한 경로에 요청을 진행하고 싶다. 루트 경로에 대해서 특정한 작업을 진행하고싶다
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .requestMatchers("/my/**").hasAnyRole("ADMIN","USER")
//                        .anyRequest().authenticated()
//                );

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/").hasAnyRole("A")
                        .requestMatchers("/manager").hasAnyRole("B")
                        .requestMatchers("/admin").hasAnyRole("C")
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth.loginPage("/login") //admin으로 입장해도 로그인 안됐으니 로그인페이지로 리다이렉션~~
                        .loginProcessingUrl("/loginProc")            //html 로그인 폼태그에서 login한 데이터를 특정한 경로로 보냄
                        .permitAll()
                );

        http
                .httpBasic(Customizer.withDefaults());

        http
                .csrf((auth) -> auth.disable()); //security에 csrf설정이 동작되면 post요청을 보낼때 csrf토큰도 보내주어야 로그인이 진행됨
//이렇게 csrf disable속성을 막아버리면 csrf속성이 동작해서 security에서 filter를 동작시킴 -> 토큰이 있는지 검증을 진행함
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) //하나의 아이디에 대한 다중 로그인 허용 개수
                        .maxSessionsPreventsLogin(true)); // 다중 로그인 개수를 초과하였을때 처리 방법
        //true : 초과시 새로운 로그인 차단
        //false :  초과시 기존 세션 하나 삭제

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()); //세션이 고정될건지 아닌지


        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        //사용자 인증 정보에 필요한 사용자 정보 제공
        //내부에 특정한 user를 만들어두고 inMemory로 관리할 수 있도록
        UserDetails user1 = User.builder()
                .username("user1")
                .password(bCryptPasswordEncoder().encode("1234"))
                .roles("C")
                .build();
        UserDetails user2 = User.builder()
                .username("user2")
                .password(bCryptPasswordEncoder().encode("1234"))
                .roles("A")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
        //내부적으로 이 값들 저장  두개의 user를 spring security가 inMemory방식으로 쟤네를 기억하고있음
    }


}
