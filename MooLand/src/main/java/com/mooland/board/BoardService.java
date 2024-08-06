package com.mooland.board;


import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface BoardService {
	public List<BoardDTO> getboardlist() throws Exception;
	public BoardDTO getboarddetail(int bno) throws Exception;
	public void insertboard(BoardDTO dto);
	public void deleteboard(int bno);
	public void updateboard(BoardDTO dto);
	
}
