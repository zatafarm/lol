package com.mooland.board;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceimpls implements BoardService {
	
	@Autowired(required=true)
	public BoardMapper mapper;

	@Override
	public List<BoardDTO> getboardlist() throws Exception {
		return mapper.getboardlist();
	}

	@Override
	public BoardDTO getboarddetail(int bno) throws Exception {
		return mapper.getboarddetail(bno);
	}

	@Override
	public void insertboard(BoardDTO dto) {
		mapper.insertboard(dto);
		
	}

	@Override
	public void deleteboard(int bno) {
		mapper.deleteboard(bno);
		
	}

	@Override
	public void updateboard(BoardDTO dto) {
		mapper.updateboard(dto);
		
	}

}
