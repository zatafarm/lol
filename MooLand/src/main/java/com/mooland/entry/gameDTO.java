package com.mooland.entry;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class gameDTO {
	private int gameid;
	private int createid;
	private String gamedate;
	private int mlt;
	private String ltn;
	private String rtn;
	private String winteam;
	private int win;
	private int setwin;
	private String bjname;
	private String lane;
	private int RB;
	private int lscore;
	private int rscore;
	private int fin;

}
