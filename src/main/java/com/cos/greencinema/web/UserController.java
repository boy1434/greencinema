package com.cos.greencinema.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.greencinema.domain.user.User;
import com.cos.greencinema.domain.user.UserRepository;
import com.cos.greencinema.util.MyAlgorithm;
import com.cos.greencinema.util.SHA;
import com.cos.greencinema.util.Script;
import com.cos.greencinema.web.dto.JoinReqDto;
import com.cos.greencinema.web.dto.LoginReqDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	
	// 생성자 주입 (DI)
	private final UserRepository userRepository;
	private final HttpSession session;
	
	//상진 영역
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
	}
	
	@GetMapping("/user/{id}")
	public String userInfo(@PathVariable int id) {
		return "user/updateForm";
	}
	
	//민수 영역
	
	//민환 영역
	
	//민홍 영역
	
	// 1. 회원 가입
	@PostMapping("/join")
	public @ResponseBody String Join(@Valid JoinReqDto dto, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			for(FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			model.addAttribute("errorMap", errorMap);
			
			return Script.back(errorMap.toString());
			
		}
		
		System.out.println(dto.getUsername());
		String encpassword = SHA.encrypt(dto.getPassword(), MyAlgorithm.SHA256);
		dto.setPassword(encpassword);
		
		userRepository.save(dto.toEntity());
		return Script.href("test/user/loginForm");
	}
	
	// 2. 로그인
	@PostMapping("/login")
	public  String Login(LoginReqDto dto) {		
		// 일반 회원 로그인
		User principal = userRepository.mLogin(dto.getUsername(), SHA.encrypt(dto.getPassword(), MyAlgorithm.SHA256));
		if ( principal.getAdminNum() == 0 ) {
			System.out.println(principal.getAdminNum());
			session.setAttribute("principal", principal); // [상진] - 세션 추가
			return "redirect:/";
		} else {
			return "/admin/movieManage";
		}
	}
	
	@GetMapping("/joinForm")
	public String userjoinForm() {
		return "user/joinForm";
	}
	@GetMapping("/loginForm")
	public String userloinForm() {
		return "user/loginForm";
	}
}
