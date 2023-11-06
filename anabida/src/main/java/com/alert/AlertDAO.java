package com.alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;
import com.util.DBUtil;

public class AlertDAO {
	private Connection conn = DBConn.getConnection();

	public void insertArlert(AlertDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {

			sql = "INSERT INTO alert(alertNum,title,content,reg_date,hitcount, userId)"
					+ " VALUES(alert_seq.NEXTVAL,?,?,SYSDATE,0,?)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getUserId());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	public int dataCount(String schType, String kwd) {

		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) "
					+ " FROM alert b "
					+ " JOIN member m ON b.userId = m.userId ";
			if (schType.equals("all")) {
				sql += "  WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			} else if (schType.equals("reg_date")) {
				kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ? ";
			} else {
				sql += "  WHERE INSTR(" + schType + ", ?) >= 1 ";
			}

			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, kwd);
			if (schType.equals("all")) {
				pstmt.setString(2, kwd);
			}

			rs = pstmt.executeQuery();
			
			if (rs.next()) {
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

	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM alert";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			
			if (rs.next()) {
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

	public List<AlertDTO> listAlert(int offset, int size) {
			List<AlertDTO> list = new ArrayList<AlertDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT alertNum, title,a.userId, userName, content, a.reg_date,hitcount ");
				sb.append(" FROM alert a ");
				sb.append(" JOIN member m ON a.userId = m.userId ");
				sb.append(" ORDER BY alertNum DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, offset);
				pstmt.setInt(2, size);

				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					AlertDTO dto = new AlertDTO();
					
					dto.setAlertNum(rs.getLong("alertNum"));
					dto.setTitle(rs.getString("title"));
					dto.setUserId(rs.getString("UserId"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("Reg_date"));
					dto.setUserName(rs.getString("userName"));
					dto.setHitcount(rs.getLong("hitCount"));
					
					list.add(dto);
				}
			} catch (Exception e) {
				e.printStackTrace();

			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			return list;
			
		}



// 여기짜자
public List<AlertDTO> listAlert(int offset, int size, String schType, String kwd) {
			List<AlertDTO> list = new ArrayList<AlertDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT alertNum, title,a.userId,content,reg_date,hitcount");
				sb.append(" FROM alert a ");
				sb.append(" JOIN member m ON a.userId = m.userId ");
				
				if(schType.equals("all")) {
					sb.append(" WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append(" WHERE TO_CHAR(reg_date, 'YYYYMMDD') = ?");
				} else {
					sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
				}
				sb.append(" ORDER BY alertNum DESC ");  //이게 맞나??/???
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
				
				while (rs.next()) {
					AlertDTO dto = new AlertDTO();
					
					dto.setAlertNum(rs.getLong("alertNum"));
					dto.setTitle(rs.getString("title"));
					dto.setUserId(rs.getString("UserId"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("Reg_date"));
					
					list.add(dto);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}

			return list;
		}

//조회수
public void updateHitCount(long alertNum) throws SQLException {
	PreparedStatement pstmt = null;
	String sql;

	try {
		sql = "UPDATE alert SET hitCount=hitCount+1 WHERE alertNum=?";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setLong(1, alertNum);
		
		pstmt.executeUpdate();
	} catch (SQLException e) {
		e.printStackTrace();
		throw e;
	} finally {
		DBUtil.close(pstmt);
	}

}

//업데이트
public void updateAlert(AlertDTO dto) throws SQLException {
	PreparedStatement pstmt = null;
	String sql;

	try {
		sql = "UPDATE alert SET title=?, content=? WHERE alertNum=? AND userId=?";
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, dto.getTitle());
		pstmt.setString(2, dto.getContent());
		pstmt.setLong(3, dto.getAlertNum());
		pstmt.setString(4, dto.getUserId());
		
		pstmt.executeUpdate();

	} catch (SQLException e) {
		e.printStackTrace();
		throw e;
	} finally {
		DBUtil.close(pstmt);
	}

}

//이전글합시다
public AlertDTO findByPrev(long alertNum) {
	AlertDTO dto = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	StringBuilder sb = new StringBuilder();

	try {
			sb.append(" SELECT alertNum, title ");
			sb.append(" FROM alert ");
			sb.append(" WHERE alertNum > ? ");
			sb.append(" ORDER BY alertNum ASC ");
			sb.append(" FETCH FIRST 1 ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, alertNum);
	
	

		rs = pstmt.executeQuery();

		if (rs.next()) {
			dto = new AlertDTO();
			
			dto.setAlertNum(rs.getLong("alertNum"));
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


//다음글
public AlertDTO findByNext(long alertNum) {
	AlertDTO dto = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	StringBuilder sb = new StringBuilder();

	try {	
						
			sb.append(" SELECT alertNum, title ");
			sb.append(" FROM alert ");
			sb.append(" WHERE alertNum < ?  ");
			sb.append(" ORDER BY alertNum DESC ");
			sb.append(" FETCH FIRST 1 ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, alertNum);


		rs = pstmt.executeQuery();

		if (rs.next()) {
			dto = new AlertDTO();
			
			dto.setAlertNum(rs.getLong("alertNum"));
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



//해당 게시물 보기
	public AlertDTO findById(long alertNum) {
		AlertDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT  alertNum, title,a.userId, content, a.reg_date, hitCount, userName FROM alert a JOIN member m ON a.userId = m.userId WHERE alertNum = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, alertNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new AlertDTO();
				
				dto.setAlertNum(rs.getLong("alertNum"));
				dto.setTitle(rs.getString("title"));
				dto.setUserId(rs.getString("userId"));
				dto.setContent(rs.getString("content"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setHitcount(rs.getLong("hitCount"));
				dto.setUserName(rs.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	public void deleteAlert(long alertNum) throws SQLException{
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = " DELETE FROM alert WHERE alertNum= ? ";
					
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, alertNum);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		
	}

			
}

