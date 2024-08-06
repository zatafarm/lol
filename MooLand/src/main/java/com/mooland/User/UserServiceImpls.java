package com.mooland.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpls implements UserService {
	
	@Autowired(required=true)
	public UserMapper usermapper;
		
	@Override
	public boolean checkid(String username) throws Exception {
		String check = usermapper.idcheck(username);
		if(check == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean checknickname(String nickname) throws Exception {
		String nicknamecheck = usermapper.nicknamecheck(nickname);
		if(nicknamecheck == null) {
			return false;
		}
		return true;
	}

	@Override
	public void register(UserDTO dto) throws Exception {
		usermapper.register(dto);
		
	}

	@Override
	public UserDTO getuserdto(String id) throws Exception {
		return usermapper.getuserdto(id);
	}

	@Override
	public boolean checkemail(String email) throws Exception {
		String check = usermapper.emailcheck(email);
		if(check == null) {
			return false;
		}
		return true;
	}

	@Override
	public List<UserDTO> getmad() throws Exception {
		return usermapper.getmad();
	}

	@Override
	public void updateUserRole(String loginId, String role) throws Exception {
		usermapper.updateRole(loginId, role);

	}

	@Override
	public void deletebj(String bjId) {
		usermapper.deletebj(bjId);
		
	}


}
