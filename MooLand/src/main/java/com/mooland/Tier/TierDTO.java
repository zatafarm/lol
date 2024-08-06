package com.mooland.Tier;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class TierDTO {
	private int id;
	private String BJID;
	private String BJName;
	private String LOLNickName;
	private String LOLNickName1;
	private String LOLNickName2;
	private String LOLTier;
	private String LOLBTier;
	private String LOLMPosition;
	private String LOLSPosition;
	private String imgUrl;
	private String LOLRank;
	private boolean Live;
	
}
