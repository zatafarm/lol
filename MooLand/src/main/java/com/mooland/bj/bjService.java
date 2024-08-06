package com.mooland.bj;


import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface bjService {
	public List<String> getbjsearchlist(String search) throws Exception;
	public BJDTO getbjdto(String search) throws Exception;
	public BJDTO getbjdtoid(String search) throws Exception;
	public BJDTO getbjdtolol(String search) throws Exception;
	public void intsertbj(BJDTO dto) throws Exception;
	public List<BJDTO> getbjdtoallimg();
	public PagingResponse<searchbjdto> getdata(String search,SearchDto params) throws Exception;
	public PagingResponse<BJDTO> bjalllist(SearchDto params) throws Exception;
}
