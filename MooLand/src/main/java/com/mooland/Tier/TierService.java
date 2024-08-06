package com.mooland.Tier;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface TierService {
	public List<TierDTO> getlist(String Position) throws Exception;
	public String getone(String player) throws Exception;
	public int getonec(String player) throws Exception;
	public TierDTO getdto(String value) throws Exception;
	public List<TierDTO> getslist(String position);

}
