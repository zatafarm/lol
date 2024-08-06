package com.mooland.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mooland.entity.Member;
import com.mooland.repository.MemberRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
    private MemberRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		Member member = userRepository.findByLoginId(loginId) .orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));
		System.out.println("ROLE_" + member.getRole().name());
        return new User(member.getLoginId(), member.getPassword(), 
        	     AuthorityUtils.createAuthorityList("ROLE_" + member.getRole().name())); 
	}

}