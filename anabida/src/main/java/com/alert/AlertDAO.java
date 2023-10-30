package com.alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.util.DBConn;
import com.util.DBUtil;

public class AlertDAO {
		private Connection conn = DBConn.getConnection();
		
		public void insertArlert(AlertDTO dto)throws SQLException{
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			long seq;
			
			try {
				sql = "SELECT alert_seq.NEXTVAL FORM dual";
				pstmt = conn.prepareStatement(sql);
				
				rs = pstmt.executeQuery();
				
				seq = 0;
				if(rs.next()) {
					seq = rs.getLong(1);
				}
				dto.setAlertnum(seq);
				
				rs.close();
				pstmt.close();
				rs = null;
				pstmt = null;
				
				sql = "INSERT INTO alert(alertnum,title,content,reg_date,hitcount)"
						+ "VALUES(?,?,?,SYSDATE,0)";
				
				 pstmt = conn.prepareStatement(sql);
				 
				 pstmt.setLong(1, dto.getAlertnum());
				 pstmt.setString(2, dto.getTitle());
				 pstmt.setString(3, dto.getContent());
				 
				 pstmt.executeUpdate();
				 
				 pstmt.close();
				 pstmt = null;
				 
				 if(dto.getSaveFiles()!=null) {
					 sql = "INSERT INTO noticeFile(fileNum,num,saveFilename, originalFilename) VALUES (noticeFile_seq.NEXTVAL, ?, ?, ?)";
					 pstmt = conn.prepareStatement(sql);
					 
					 for(int i=0; i<dto.getSaveFiles().length;i++) {
						 pstmt.setLong(1,dto.getAlertnum());
						 pstmt.setString(2,dto.getSaveFiles()[i]);
						 pstmt.setString(3,dto.getOriginalFiles()[i]);
						 
						 pstmt.executeUpdate();
					 }
				 }
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}finally {
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
			
			return null;
		}

		public List<AlertDTO> listAlert(int offset, int size, String schType, String kwd) {
		
			return null;
		}

		
		


		
		
		
		
		
		
		

}
