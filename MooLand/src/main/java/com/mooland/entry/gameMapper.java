package com.mooland.entry;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mooland.bj.BjGameDto;





@Repository
@Mapper
public interface gameMapper {

	int createGame(gameDTO dto);

	void setgame(gameDTO dto);
	gameDTO getdata(String id);
	gameDTO2 teamdata(@Param("gameid") int gameid, @Param("team") String team);
	void finalgame(gameDTO dto);
	void set2game(gameDTO dto);
	void savegame(Map<String, String> data2);

	List<Integer> getgameidlist(String playerName);

	String getteamname(@Param("gameid") int gameid, @Param("bjname") String bjname);

	String getgamewinlost(@Param("gameid") int gameid, @Param("team") String teamname);

	List<Integer> getvsidlist(@Param("PlayerName") String playerName, @Param("VsName") String vsName);
	List<Map<String, String>> getplayerwl(@Param("playerName") String playerName);
	BjGameDto getplayerwlint(String playerName);
	List<Map<String, String>> getvswl(@Param("playerName") String playerName, @Param("VsName") String vsName);

	BjGameDto getvswlint(@Param("playerName") String playerName, @Param("VsName") String vsName);
	List<gameDTO> getgameidvs5(String playerName);
}
