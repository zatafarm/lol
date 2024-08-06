package com.mooland.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mooland.entity.Member;
import com.mooland.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	 private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    

    public boolean checkLoginIdDuplicate(String loginId){
        return memberRepository.existsByLoginId(loginId);
    }
    public boolean checkEmailDuplicate(String email){
        return memberRepository.existsByEmail(email);
    }
    
    public boolean checknicknameDuplicate(String nickname){
        return memberRepository.existsByNickname(nickname);
    }



    public void join(JoinRequest joinRequest) {
        // 중복 검사 후 회원가입 처리
        if (checkLoginIdDuplicate(joinRequest.getId())) {
            throw new RuntimeException("로그인 ID가 이미 존재합니다.");
        }
        if (checkEmailDuplicate(joinRequest.getEmail())) {
            throw new RuntimeException("이메일이 이미 존재합니다.");
        }
        if (checknicknameDuplicate(joinRequest.getNickname())) {
            throw new RuntimeException("닉네임이 이미 존재합니다.");
        }

        memberRepository.save(joinRequest.toEntity());
    }
    public Member login(LoginRequest loginRequest) {
    	 Optional<Member> findMember = memberRepository.findByLoginId(loginRequest.getLoginId());

        if(findMember.isEmpty()){
            return null;
        }
        
        Member member = findMember.get();
        if (!member.getPassword().equals(loginRequest.getPassword())) {
            return null;
        }

        return member;
    }

    public Member getLoginMemberById(Long memberId) {
        if (memberId == null )    	return null;
        Optional<Member> optionalmember = memberRepository.findById(memberId);
        if(optionalmember.isEmpty()) return null;
        return optionalmember.get();
      
      
    }
    
    public Member getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<Member> optionalUser = memberRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
    
    // BCryptPasswordEncoder 를 통해서 비밀번호 암호화 작업 추가한 회원가입 로직
    public void securityJoin(JoinRequest joinRequest){
        if(memberRepository.existsByLoginId(joinRequest.getId())){
            return;
        }
        String lawpass = joinRequest.getPass();
        String encoderpass = passwordEncoder.encode(lawpass);
        joinRequest.setPass(encoderpass);

        memberRepository.save(joinRequest.toEntity());
    }
   
    public boolean changePasswordByLoginId(String loginId, String currentPassword, String newPassword) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId); // loginId로 회원 조회

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            // 현재 비밀번호 검증
            if (passwordEncoder.matches(currentPassword, member.getPassword())) {
                // 새 비밀번호 암호화
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                member.setPassword(encodedNewPassword); // 비밀번호 설정
                memberRepository.save(member);
                return true; // 비밀번호 변경 성공
            } else {
                return false; // 현재 비밀번호가 일치하지 않음
            }
        }
        return false; // 회원을 찾을 수 없음
    }
    

}