package com.mooland.board;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;



@Repository
@Mapper
public interface BoardMapper {
	public List<BoardDTO> getboardlist() throws Exception;
	public BoardDTO getboarddetail(int bno) throws Exception;
	public void insertboard(BoardDTO dto);
	public void deleteboard(int bno);
	public void updateboard(BoardDTO dto);
}
