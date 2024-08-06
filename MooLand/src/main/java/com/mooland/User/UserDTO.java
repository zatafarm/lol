package com.mooland.User;

import java.sql.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class UserDTO {
	
	private String id;
	private String pass;
	private String nickname;
	private String name;
	private String email;
	private Date cdate;
	private Date ddate;
	private int del;
	private String authNum;
	private int point;
	private String login_id;
	private String role;
}
