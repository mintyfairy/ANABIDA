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
				sql += " WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 " ;
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
			sb.append(" SELECT g.buyNum, g.userId, title, hitCount, joinCount, link, min_peo, NVL(imageFilename, 'no.png') imageFilename, ");
			sb.append(" TO_CHAR(g.reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append(" TO_CHAR(exp_date, 'YYYY-MM-DD') exp_date, NVL(enterCount, 0) enterCount,");
			sb.append(" m.userName ");
			sb.append(" FROM group_buying g ");
			sb.append(" JOIN member m ON g.userId = m.userId ");
			sb.append(" JOIN member m ON g.userId = m.userId ");
			sb.append(" LEFT OUTER JOIN ( ");
			sb.append("     SELECT buyNum, COUNT(*) enterCount ");
			sb.append("     FROM join_member ");
			sb.append("     GROUP BY buyNum");
			sb.append(" ) c ON g.buyNum = c.buyNum");				
			
			sb.append(" ORDER BY buyNum DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				JoinDTO dto = new JoinDTO();
				
				dto.setBuyNum(rs.getLong("buyNum"));
				//System.out.println(rs.getLong("buyNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setImageFilename(rs.getString("imageFilename"));
				dto.setTitle(rs.getString("title"));
				dto.setJoinCount(rs.getInt("joinCount"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setLink(rs.getString("link"));
				dto.setMin_peo(rs.getInt("min_peo"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setExp_date(rs.getString("exp_date"));
				dto.setEnterCount(rs.getInt("enterCount"));				
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
			sb.append(" SELECT g.buyNum, g.userId, title, hitCount, joinCount, link, min_peo,  NVL(imageFilename, 'no.png') imageFilename,");
			sb.append(" TO_CHAR(g.reg_date, 'YYYY-MM-DD') reg_date, ");
			sb.append(" TO_CHAR(exp_date, 'YYYY-MM-DD') exp_date, NVL(enterCount, 0) enterCount, ");
			sb.append(" m.userName ");
			sb.append(" FROM group_buying g ");
			sb.append(" JOIN member m ON g.userId = m.userId ");
			sb.append(" LEFT OUTER JOIN ( ");
			sb.append("     SELECT buyNum, COUNT(*) enterCount ");
			sb.append("     FROM join_member ");
			sb.append("     GROUP BY buyNum");
			sb.append(" ) c ON g.buyNum = c.buyNum");			
			if (schType.equals("all")) {
				sb.append(" WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
			} else {
				sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
			}
			sb.append(" ORDER BY buyNum DESC ");
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
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setTitle(rs.getString("title"));
				dto.setImageFilename(rs.getString("imageFilename"));
				dto.setJoinCount(rs.getInt("joinCount"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setLink(rs.getString("link"));
				dto.setMin_peo(rs.getInt("min_peo"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setExp_date(rs.getString("exp_date"));
				
				dto.setEnterCount(rs.getInt("enterCount"));
				
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
	

	// 해당게시물 보기
	public JoinDTO findById(long buyNum) {
		JoinDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT buyNum, g.userId, userName, link, title, content, g.reg_date, TO_CHAR(exp_date, 'YYYY-MM-DD') exp_date, hitCount, joinCount, min_peo,"
					+ " imageFilename "
					+ " FROM group_buying g"
					+ " JOIN member m ON g.userId = m.userId"
					+ " WHERE buyNum= ? ";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, buyNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new JoinDTO();
				
				dto.setBuyNum(rs.getLong("buyNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setContent(rs.getString("content"));
				dto.setTitle(rs.getString("title"));
				dto.setJoinCount(rs.getInt("joinCount"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setLink(rs.getString("link"));
				dto.setMin_peo(rs.getInt("min_peo"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setExp_date(rs.getString("exp_date"));
				dto.setImageFilename(rs.getString("imageFilename"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return dto;
	}
	
	// 이전글
	public JoinDTO findByPrev(long buyNum, String schType, String kwd) {
		JoinDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(kwd != null && kwd.length()!=0) {
				sb.append(" SELECT buyNum, title ");
				sb.append(" FROM group_buying g ");
				sb.append(" JOIN member m ON g.userId = m.userId ");
				if(schType.equals("all")) {
					sb.append(" AND(INSTR(title, ?) >= 1 OR INSTR(content, ?) >=1)");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append(" AND(TO_CHAR(reg_date, 'YYYYMMDD')=? ) " );
				} else {
					sb.append(" AND(INSTR(" + schType + ", ?) >=1 )");
				}
				sb.append(" ORDER BY buyNum ASC");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, buyNum);
				pstmt.setString(2, kwd);
				
				if(schType.equals("all")) {
					pstmt.setString(3, kwd);
				} 
				
			}
				else {
					sb.append(" SELECT buyNum, title ");
					sb.append(" FROM group_buying ");
					sb.append(" WHERE buyNum > ? ");
					sb.append(" ORDER BY buyNum ASC ");
					sb.append(" FETCH FIRST 1 ROWS ONLY ");
					
					pstmt = conn.prepareStatement(sb.toString());
					pstmt.setLong(1, buyNum);
				}
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					dto = new JoinDTO();
					dto.setBuyNum(rs.getLong("buyNum"));
					dto.setTitle(rs.getString("title"));
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	// 다음글
	public JoinDTO findByNext(long buyNum, String schType, String kwd) {
		JoinDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT buyNum, title ");
				sb.append(" FROM group_buying g ");
				sb.append(" JOIN member m ON g.userId = m.userId ");
				sb.append(" WHERE ( buyNum < ? ) ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
				}
				sb.append(" ORDER BY buyNum DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, buyNum);
				pstmt.setString(2, kwd);
				if (schType.equals("all")) {
					pstmt.setString(3, kwd);
				}
			} else {
				sb.append(" SELECT buyNum, title ");
				sb.append(" FROM group_buying ");
				sb.append(" WHERE buyNum < ? ");
				sb.append(" ORDER BY buyNum DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, buyNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new JoinDTO();
				
				dto.setBuyNum(rs.getLong("buyNum"));
				dto.setTitle(rs.getString("title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	// 게시물 수정
	public void updateJoin(JoinDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		
		try {
			sql = " UPDATE group_buying SET title=?, exp_date=?, min_peo=?, content=?, link=?"
					+ " WHERE buyNum=? AND userId=? ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getExp_date());
			pstmt.setInt(3, dto.getMin_peo());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getLink());
			pstmt.setLong(6, dto.getBuyNum());
			pstmt.setString(7, dto.getUserId());
			
		
			
			pstmt.executeUpdate();
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	//삭제
	public void deleteBoard(long buyNum, String userId) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (userId.equals("admin")) {
				sql = "DELETE FROM group_buying WHERE buyNum=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, buyNum);
				
				pstmt.executeUpdate();
			} else {
				sql = "DELETE FROM group_buying WHERE buyNum=? AND userId=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, buyNum);
				pstmt.setString(2, userId);
				             
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	
	// 참여자 수 증가하기
	public int enterCount(){
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT NVL(COUNT(*), 0) FROM join_member ";
			
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
	
	

	public void insertJoinMember(JoinDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			dto.setEmail(dto.getEmail1()+"@"+dto.getEmail2());
			dto.setTel(dto.getTel1()+"-"+dto.getTel2()+"-"+dto.getTel3());
			dto.setAddr(dto.getAddr1()+" " + dto.getAddr2());
			
			sql=" INSERT INTO join_member(memNum, userId, buyNum, quantity, email, tel, zip, addr, title, enterCount) "
					+ " VALUES(join_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, 0) ";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setLong(2, dto.getBuyNum());
			pstmt.setInt(3, dto.getQuantity());
			pstmt.setString(4, dto.getEmail());
			pstmt.setString(5, dto.getTel());
			pstmt.setString(6, dto.getZip());
			pstmt.setString(7, dto.getAddr());
			pstmt.setString(8, dto.getTitle());
		
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
		
	}
	
	
	
	
	
	
	
	
	
}
