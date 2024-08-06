package com.mooland.controller;

import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mooland.entity.Member;
import com.mooland.entity.MemberRole;
import com.mooland.service.JoinRequest;
import com.mooland.service.MemberService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
public class Mcontrollerlog {
	
	
	@Controller
	@RequiredArgsConstructor
	public class SecurityLoginController {
	    private final MemberService memberService;
	    @GetMapping(value = {"/home"})
	    public String home(Model model, Authentication auth,HttpSession session) {
	    
	        if(auth != null) {
	            Member loginUser = memberService.getLoginUserByLoginId(auth.getName());
	            if (loginUser != null) {
	
	            	session.setAttribute("nickname", loginUser.getNickname());
	            	session.setAttribute("loginid", loginUser.getLoginId());
	            	MemberRole role = loginUser.getRole(); // MemberRole 객체 가져오기
	            	 if (role == MemberRole.ADMIN) {
	                     session.setAttribute("admin", "admin"); // admin 세션 추가
	                 }
	            	String id = String.valueOf(loginUser.getId());
	            	session.setAttribute("id", id);
	            	session.setMaxInactiveInterval(10800);
	            }
	        }

	        return "index";
	    }

	    @GetMapping("/register")
	    public String joinPage(Model model) {
	        model.addAttribute("joinRequest", new JoinRequest());
	        return "view/register";
	    }	   
	    @GetMapping(value = {"''","/"})
	    public String homejoin() {
	        return "redirect:/home";
	    }
	    @PostMapping("/register")
	    public String join(@Valid @ModelAttribute JoinRequest joinRequest,
	                       BindingResult bindingResult, Model model) {
	        memberService.securityJoin(joinRequest);
	        return "redirect:/home";
	    }

	    @GetMapping("/loginError")
	    public String loginErro(Model model,RedirectAttributes redirectAttributes) throws Exception{
	    	  redirectAttributes.addFlashAttribute("msg", "아이디 또는 비밀번호를 다시 확인하세요");
	        return "redirect:/home";
	    }
	    @PostMapping("/change-password")
	    public String changePassword(
	    		  HttpSession session,
	            @RequestParam("currentPassword") String currentPassword,
	            @RequestParam("newPassword") String newPassword,  RedirectAttributes redirectAttributes) {
	    	  String regex = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d!@#$%^&*()]{8,16}$";
	    	    Pattern pattern = Pattern.compile(regex);

	    	    // 비밀번호 유효성 검사
	    	    if (!pattern.matcher(newPassword).matches()) {
	    	    	   redirectAttributes.addFlashAttribute("error2", true);
	    	        return "redirect:/mypage"; // 유효성 검사 실패 시 비밀번호 변경 페이지로 리디렉션
	    	    }
	    	 String loginId = (String) session.getAttribute("loginid");
	    	 System.out.println(loginId);
	        boolean isChanged = memberService.changePasswordByLoginId(loginId, currentPassword, newPassword); // 비밀번호 변경 시도
	        System.out.println(isChanged);
	        if (isChanged) {
	            redirectAttributes.addFlashAttribute("success", true);
		            return "redirect:/mypage"; 
	        } else {
	        	   redirectAttributes.addFlashAttribute("error", true);
	            return "redirect:/mypage"; // 실패 시 비밀번호 변경 페이지로 리디렉션
	        }
	}
	}
}
