package com.mooland.bj;


import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class bjServiceImpls implements bjService {
	
	@Autowired(required=true)
	public BjMapper mapper;

	@Override
	public List<String> getbjsearchlist(String search) throws Exception {
		
		return mapper.getsearch(search);
	}

	@Override
	public BJDTO getbjdto(String search) throws Exception {
		
		return  mapper.getbjdto(search);
	}

	@Override
	public PagingResponse<searchbjdto> getdata(String search,SearchDto params) throws Exception {
	     int count = mapper.getdataint(search, params);
	        if (count < 1) {
	            return new PagingResponse<>(Collections.emptyList(), null);
	        }

	        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params에 계산된 페이지 정보 저장
	        Pagination pagination = new Pagination(count, params);
	        params.setPagination(pagination);

	        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
	        List<searchbjdto> list = mapper.getdata(search , params);
	        return new PagingResponse<>(list, pagination);
	}

	@Override
	public void intsertbj(BJDTO dto) throws Exception {
		// TODO Auto-generated method stub
		mapper.insertBJ(dto);
	}

	@Override
	public BJDTO getbjdtoid(String search) throws Exception {
		// TODO Auto-generated method stub
		return mapper.getbjdtoid(search);
	}

	@Override
	public BJDTO getbjdtolol(String search) throws Exception {
		// TODO Auto-generated method stub
		return mapper.getbjdtolol(search);
	}

	@Override
	public PagingResponse<BJDTO> bjalllist(SearchDto params) throws Exception {
	     int count = mapper.getbjlistint(params);
	        if (count < 1) {
	            return new PagingResponse<>(Collections.emptyList(), null);
	        }

	        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params에 계산된 페이지 정보 저장
	        Pagination pagination = new Pagination(count, params);
	        params.setPagination(pagination);

	        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
	        List<BJDTO> list = mapper.bjlist(params);
	        return new PagingResponse<>(list, pagination);
	}

	@Override
	public List<BJDTO> getbjdtoallimg() {
		// TODO Auto-generated method stub
		return mapper.getbjdtoallimg();
	}


}
