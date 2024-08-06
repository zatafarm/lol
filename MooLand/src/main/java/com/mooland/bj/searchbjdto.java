package com.mooland.bj;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class searchbjdto {
	private int gameid;
	private String gamedate;
	private String ltn;
	private String rtn;
	private int lscore;
	private int rscore;
	private int win;
	private String wins;
	private String lp1;
	private String lp2;
	private String lp3;
	private String lp4;
	private String lp5;
	private String rp1;
	private String rp2;
	private String rp3;
	private String rp4;
	private String rp5;
	private String lp1bjid;
	private String lp2bjid;
	private String lp3bjid;
	private String lp4bjid;
	private String lp5bjid;
	private String rp1bjid;
	private String rp2bjid;
	private String rp3bjid;
	private String rp4bjid;
	private String rp5bjid;
	private String TimeAgo;
	  
    public String getImgUrl(String bjid) {
        if (bjid == null || bjid.length() < 	2) {
            return "";
        }
        String prefix = bjid.substring(0, 2);
        return "http://stimg.afreecatv.com/LOGO/" + prefix + "/" + bjid + "/" + bjid + ".jpg";
    }



}
