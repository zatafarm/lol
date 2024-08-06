package com.mooland.entry;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mooland.bj.BjGameDto;

@Service
public interface gameService {

	public int creategame(gameDTO dto) throws Exception;
	public void  setgame(gameDTO dto) throws Exception;
	public void finalgame(gameDTO dto) throws Exception;
	public gameDTO getdata(String id) throws Exception;
    public void savegame(Map<String, String> data2) throws Exception;

    public List<Integer> getgameidlist(String playerName) throws Exception;

    public String getteamname(int gameid, String playerName) throws Exception;

    public String getgamewinlost(int gameid, String teamname) throws Exception;

    public List<Integer> getvsidlist(String playerName, String vsName) throws Exception;

    public List<Map<String, String>> getplayerwl(String playerName) throws Exception;

    public BjGameDto getplayerwlint(String playerName) throws Exception;

    public List<Map<String, String>> getvswl(String playerName, String vsName) throws Exception;

    public BjGameDto getvswlint(String playerName, String vsName) throws Exception;

    public List<gameDTO> getvsplayename(String playerName) throws Exception;
    public void set2game(gameDTO dto) throws Exception;

	public gameDTO2 teamdata(int gameid, String team) throws Exception;

}
