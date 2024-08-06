package com.mooland.service;

import java.time.LocalDate;

import com.mooland.entity.Member;
import com.mooland.entity.MemberRole;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "ID를 입력하세요.")
    private String id;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String pass;
    private String pass2;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;
    
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;
    
    @NotBlank(message = "닉네임을 입력하세요.")
    private String nickname;

    public Member toEntity(){
        return Member.builder()
                .loginId(this.id)
                .password(this.pass)
                .name(this.name)
                .nickname(this.nickname)
                .email(this.email)
                .cdate(LocalDate.now())
                .role(MemberRole.USER)
                .build();
    }
}