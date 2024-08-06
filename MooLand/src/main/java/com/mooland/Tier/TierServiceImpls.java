package com.mooland.Tier;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TierServiceImpls implements TierService {
	
	@Autowired(required=true)
	public TierMapper mapper;

	@Override
	public List<TierDTO> getlist(String Position) throws Exception {
		if(Position.equals("all")) {
			return mapper.getalltierlist();
		}
		return mapper.gettierlist(Position);
	}

	@Override
	public String getone(String player) throws Exception {
		return mapper.getone(player);
	}

	@Override
	public int getonec(String player) throws Exception {
		// TODO Auto-generated method stub
		return mapper.getonec(player);
	}

	@Override
	public TierDTO getdto(String valu) throws Exception {
		
		return mapper.getdto(valu);
	}

	@Override
	public List<TierDTO> getslist(String position) {
		return mapper.gettierslist(position);
	}



}
