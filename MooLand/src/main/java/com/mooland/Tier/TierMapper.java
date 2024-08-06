package com.mooland.Tier;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;



@Repository
@Mapper
public interface TierMapper {

	List<TierDTO> gettierlist(String position);

	List<TierDTO> getalltierlist();

	String getone(String player);

	int getonec(String player);

	TierDTO getdto(String valu);

	List<TierDTO> gettierslist(String position);

}
