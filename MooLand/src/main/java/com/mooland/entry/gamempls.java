package com.mooland.entry;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mooland.bj.BjGameDto;

import jakarta.transaction.Transactional;

@Service
public class gamempls implements gameService {
	
	@Autowired(required=true)
	public gameMapper mapper;

	@Override
	public int creategame(gameDTO dto) throws Exception {
	 return mapper.createGame(dto);
		
	}

	@Override
	public void setgame(gameDTO dto) throws Exception {
		mapper.setgame(dto);
		
	}

	@Override
	public void finalgame(gameDTO dto) throws Exception {
		mapper.finalgame(dto);
		System.out.println("서비스에서" + dto.getWin());
		
		
	}
	@Override
	public void set2game(gameDTO dto) throws Exception {
		mapper.set2game(dto);
		
		
	}
	@Override
	public void savegame(Map<String, String> data2) {
		mapper.savegame(data2);
		
	}

	@Override
	public List<Integer> getgameidlist(String playerName) {
		return mapper.getgameidlist(playerName);
	}

	@Override
	public String getteamname(int gameid, String playerName) {
		// TODO Auto-generated method stub
		return mapper.getteamname(gameid , playerName);
	}

	@Override
	public String getgamewinlost(int gameid, String teamname) {
		return mapper.getgamewinlost(gameid ,teamname);
	}


	@Override
	public List<Integer> getvsidlist(String playerName, String vsName) {
		return mapper.getvsidlist(playerName, vsName);
	}

	@Override
	public List<Map<String, String>> getplayerwl(String playerName) {
		
		return mapper.getplayerwl(playerName);
	}

	@Override
	public BjGameDto getplayerwlint(String playerName) {
		
		return  mapper.getplayerwlint(playerName);
	}

	@Override
	public List<Map<String, String>> getvswl(String playerName, String vsName) {
		// TODO Auto-generated method stub
		return  mapper.getvswl(playerName, vsName);
	}

	@Override
	public BjGameDto getvswlint(String playerName, String vsName) {
		return  mapper.getvswlint(playerName, vsName);
	}

	@Override
	public List<gameDTO> getvsplayename(String playerName) {

		return mapper.getgameidvs5(playerName);
	}

	@Override
	public gameDTO getdata(String id) throws Exception {
		return mapper.getdata(id);
	}
	@Override
	public gameDTO2 teamdata(int gameid, String team) throws Exception {
		return mapper.teamdata(gameid, team);
	}




}
