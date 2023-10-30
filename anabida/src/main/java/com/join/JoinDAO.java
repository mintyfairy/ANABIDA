package com.join;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;
import com.util.DBUtil;

public class JoinDAO {
	private Connection conn = DBConn.getConnection();
	
	// 게시글 등록
	public void insertJoin(JoinDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
					
			
			sql=" INSERT INTO group_buying(buyNum, userId, title, link, content, reg_date, exp_date, joinCount, "
					+ " hitCount, min_peo, imageFilename)"
					+ " VALUES(group_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, ?, 0, 0, ?, ?) ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getLink());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getExp_date());
			pstmt.setInt(6, dto.getMin_peo());
			pstmt.setString(7, dto.getImageFilename());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
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
			
			pstmt.setLong(1, buyNum);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return result;
	}
	
	
	// 검색에서의 데이터 개수
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT NVL(COUNT(*), 0) "
					+ " FROM group_buying g"
					+ " JOIN member m ON g.userId = m.userId ";
			
			if(schType.equals("all")) {
				sql += " WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 " ;
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sql += " WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += "  WHERE INSTR(" + schType + ", ?) >= 1 ";
			}
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			if(schType.equals("all")) {
				pstmt.setString(2, kwd);
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	

	
	// 게시물 리스트
	public List<JoinDTO> list (int offset, int size) {
		List<JoinDTO> list = new ArrayList<JoinDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT buyNum, g.userId, title, hitCount, joinCount, link, ");
			sb.append(" TO_CHAR(g.reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append(" TO_CHAR(exp_date, 'YYYY-MM-DD') exp_date ");
			sb.append(" FROM group_buying g ");
			sb.append(" JOIN member m ON g.userId = m.userId ");
			sb.append(" ORDER BY buyNum DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				JoinDTO dto = new JoinDTO();
				
				dto.setBuyNum(rs.getLong("buyNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setTitle(rs.getString("title"));
				dto.setJoinCount(rs.getInt("joinCount"));
				dto.setLink(rs.getString("link"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setExp_date(rs.getString("exp_date"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list;
	}
	
	
	public List<JoinDTO> list(int offset, int size, String schType, String kwd) {
		List<JoinDTO> list = new ArrayList<JoinDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append(" SELECT buyNum, g.userId, title, hitCount, joinCount, link, ");
			sb.append(" TO_CHAR(reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append(" TO_CHAR(exp_date, 'YYYY-MM-DD') exp_date, ");
			sb.append(" FROM group_buying g ");
			sb.append(" JOIN member m ON g.userId = m.userId ");
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			sb.append(" ORDER BY num DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			if (schType.equals("all")) {
				pstmt.setString(1, kwd);
				pstmt.setString(2, kwd);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, size);
			} else {
				pstmt.setString(1, kwd);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);
			}

			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				JoinDTO dto = new JoinDTO();
				
				dto.setBuyNum(rs.getLong("buyNum"));
				
				list.add(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list;
	}
	
	
	/*
	
	// 참여자 수 증가하기
	public void joinCount(long buynum) throws SQLException {
		
	}
	
	// 해당게시물 보기
	public JoinDTO findById(long buynum) {
		
	}
	
	*/
	
	
	
	
	
	
	
	
	
}
