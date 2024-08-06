package com.mooland.User;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;



@Repository
@Mapper
public interface UserMapper {
	String idcheck(String username) throws Exception;
	String nicknamecheck(String nickname) throws Exception;
	void register(UserDTO dto) throws Exception;
	UserDTO getuserdto(String id) throws Exception;
	String emailcheck(String email) throws Exception; 
	List<UserDTO> getmad() throws Exception;
	void updateRole(@Param("loginId") String loginId,@Param("role")  String role);
	void deletebj(String bjId);
}
