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
		ResultSet rs = null;
		String sql;
		long seq;

		try {

			sql = "INSERT INTO alert(alertnum,title,content,reg_date,hitcount)"
					+ "VALUES(alert_seq.nextVal,?,?,SYSDATE,0)";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	public int dataCount(String schType, String kwd) {

		return 0;
	}

	public int dataCount() {
		return 0;
	}

	public List<AlertDTO> listAlert(int offset, int size) {
			List<AlertDTO> list = new ArrayList<AlertDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT alertNum, title,a.userId,content,TO_CHAR(reg_date,'YYYY-MM-DD')reg_date,hitcount");
				sb.append(" FROM alert a ");
				sb.append(" JOIN member m ON a.userId = m.userId ");
				sb.append(" ORDER BY groupNum DESC, orderNo ASC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, offset);
				pstmt.setInt(2, size);

				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					AlertDTO dto = new AlertDTO();
					
					dto.setAlertnum(rs.getLong("alertNum"));
					dto.setTitle(rs.getString("title"));
					dto.setUserId(rs.getString("UserId"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("Reg_date"));
					
					list.add(dto);
				}
			} catch (Exception e) {
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
				sb.append(" SELECT alertNum, title,a.userId,content,TO_CHAR(reg_date,'YYYY-MM-DD')reg_date,hitcount");
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
				sb.append(" ORDER BY alertNum DESC ");
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
					
					dto.setAlertnum(rs.getLong("alertNum"));
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

			
}

