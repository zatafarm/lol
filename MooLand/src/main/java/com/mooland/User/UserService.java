package com.mooland.User;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
	public boolean checkid(String username) throws Exception;
	public boolean checknickname(String nickname) throws Exception;
	public boolean checkemail(String email) throws Exception;
	public void updateUserRole(String loginId , String role) throws Exception;
	public void register(UserDTO dto) throws Exception;
	public UserDTO getuserdto(String id) throws Exception;
	public List<UserDTO> getmad() throws Exception;
	public void deletebj(String bjId);
	
}
