package com.join;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			sql=" INSERT INTO group_buying(buyNum, userId, title, link, content, reg_date, exp_date, joinCount, "
					+ " hitCount, min_peo, imageFilename)"
					+ " VALUES(group_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?, ?) ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getLink());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getExp_date());
			pstmt.setInt(6, dto.getJoinCount());
			pstmt.setInt(7, dto.getHitCount());
			pstmt.setInt(8, dto.getMin_peo());
			pstmt.setString(9, dto.getImageFilename());
			
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	

	public void updateHitCount(long buyNum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql; 
		
		try {
			sql = " UPDATE group_buying SET hitCount=hitCount+1 WHERE buyNum = ?";
			pstmt = conn.prepareStatement(sql);
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	
	
	// 데이터 개수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql; 
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM group_buying ";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return result;
	}
	/*
	// 검색에서의 데이터 개수
	public int datCount(String schType, String kwd) {
		
	}
	
	
	// 게시물 리스트
	public List<JoinDTO> list = new ArrayList<JoinDTO>(int offset, int size) {
		
	}
	
	public List<JoinDTO> list = new ArrayList<JoinDTO>(int offset, int size, String schType, String kwd) {
		
	}
	
	
	// 참여자 수 증가하기
	public void joinCount(long buynum) throws SQLException {
		
	}
	
	// 해당게시물 보기
	public JoinDTO findById(long buynum) {
		
	}
	
	*/
	
	
	
	
	
	
	
	
	
}
