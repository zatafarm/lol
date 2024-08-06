package com.mooland.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mooland.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	// 로그인 ID를 갖는 객체가 존재하는지 => 존재하면 true 리턴 (ID 중복 검사 시 필요)
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

	// 로그인 ID를 갖는 객체 반환
    Optional<Member> findByLoginId(String loginId);

}