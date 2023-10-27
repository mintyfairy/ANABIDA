package com.join;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.util.DBConn;
import com.util.DBUtil;

public class JoinDAO {
	private Connection conn = DBConn.getConnection();
	
	// 게시글 등록
	public void insertJoin(JoinDTO dto) throws SQLException {
		// 세션에 저장된 로그인 정보
		
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql=" INSERT INTO group_buying(buyNum, userId, title, content, reg_date, joinCount, hitCount, min_peo, link)"
					+ " VALUES(group_seq.NEXTVAL, ?, ?, ?, SYSDATE, 0, 0, ?, ?) ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setInt(4, dto.getMin_peo());
			pstmt.setString(5, dto.getLink());
			
			pstmt.executeUpdate();
			
			DBUtil.close(pstmt);
			
			sql=" INSERT INTO group_att(fileNum, attName)"
					+ " VALUES(gr_att.NEXTVAL, ?) ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getAttName());
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	/*
	
	// 데이터 개수
	public int dataCount() {
		
	}
	
	// 검색에서의 데이터 개수
	public int datCount(String schType, String kwd) {
		
	}
	
	
	// 게시물 리스트
	public List<JoinDTO> list = new ArrayList<JoinDTO>(int offset, int size) {
		
	}
	
	public List<JoinDTO> list = new ArrayList<JoinDTO>(int offset, int size, String schType, String kwd) {
		
	}
	
	
	
	
	
	
	
	// 조회수 증가하기 
	public void updateHitCount(long buynum) throws SQLException {
		
	}
	
	// 참여자 수 증가하기
	public void joinCount(long buynum) throws SQLException {
		
	}
	
	// 해당게시물 보기
	public JoinDTO findById(long buynum) {
		
	}
	
	*/
	
	
}
