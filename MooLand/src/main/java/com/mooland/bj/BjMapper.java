package com.mooland.bj;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;



@Repository
@Mapper
public interface BjMapper {
	List<String> getsearch(String search);
	List<BJDTO> bjlist(@Param("page") SearchDto params);
	List<BJDTO> getbjdtoallimg();
	int getbjlistint(@Param("page") SearchDto params);
	void UpdateBJtier(BJDTO dto);
	void insertBJ(BJDTO dto) throws Exception;
	void UpdateBJtierandposition(BJDTO dto);
	String BjName(int i);
	String LOLNickName(int i);
	String LOLpuuid(int i);
	void Updatelolnickname(BJDTO dto);
	void UpdateBJpuuid(BJDTO dto);
	void UpdateNtier(BJDTO dto);
	int bjint();
	BJDTO getbjdto(String search);
	BJDTO getbjdtoid(String search);	
	BJDTO getbjdtolol(String search);
	List<searchbjdto> getdata(@Param("search") String search, @Param("page") SearchDto params);
    int getdataint(@Param("search") String search, @Param("page") SearchDto params);
}
