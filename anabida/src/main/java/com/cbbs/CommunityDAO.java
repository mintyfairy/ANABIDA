package com.cbbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.DBConn;
import com.util.DBUtil;

public class CommunityDAO {
	private Connection conn = DBConn.getConnection();
	
	public void insertCbbs(CommunityDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		long seq;
		
		try {
			sql = "SELECT cbbs_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			seq = 0;
			
			if (rs.next()) {
				seq = rs.getLong(1);
			}
			
			dto.setNum(seq);
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			sql = "INSERT INTO cbbs ( cnum, userId, ctitle, ccontent, creg_date,ccategory,chitCount ) "
					+ " VALUES (?, ?, ?, ?, SYSDATE,?,0)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getCtitle());
			pstmt.setString(4, dto.getCcontent());
			pstmt.setString(5, dto.getCcategory());
			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;
			
			if (dto.getPicFileNames() != null) {
				sql = "INSERT INTO cpics(picnum, cnum, picFileName) VALUES "
						+ " (cpics_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for (int i = 0; i < dto.getPicFileNames().length; i++) {
					pstmt.setLong(1, dto.getNum());
					pstmt.setString(2, dto.getPicFileNames()[i]);
					
					pstmt.executeUpdate();
				}
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	
	}
	
	public void insertMeet(CommunityDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		long seq;
		
		try {
			
			conn.setAutoCommit(false);
			sql = "SELECT cbbs_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			seq = 0;
			
			if (rs.next()) {
				seq = rs.getLong(1);
			}
			
			dto.setNum(seq);
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			sql = "INSERT INTO cbbs ( cnum, userId, ctitle, ccontent, creg_date,ccategory,chitCount ) "
					+ " VALUES (?, ?, ?, ?, SYSDATE,?,0)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getCtitle());
			pstmt.setString(4, dto.getCcontent());
			pstmt.setString(5, dto.getCcategory());
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			if (dto.getPicFileNames() != null) {
				sql = "INSERT INTO cpics(picnum, cnum, picFileName) VALUES "
						+ " (cpics_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for (int i = 0; i < dto.getPicFileNames().length; i++) {
					pstmt.setLong(1, dto.getNum());
					pstmt.setString(2, dto.getPicFileNames()[i]);
					
					pstmt.executeUpdate();
				}
			}
			
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO meet ( mnum,cnum, mcount, mreg_date, zip, addr1,addr2) "
					+ " VALUES (meet_seq.NEXTVAL,?,?,?,?,?,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, dto.getNum());
			pstmt.setLong(2, dto.getCmember());
			pstmt.setString(3, dto.getMreg_date());
			pstmt.setString(4, dto.getZip());
			pstmt.setString(5, dto.getAddr1());
			pstmt.setString(6, dto.getAddr2());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			
			sql = "INSERT INTO meetmember (mnum,userId) values "
					+ " (meet_seq.CURRVAL,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			
			pstmt.executeUpdate();
			
			conn.commit();
			
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
			}
		}
		
	}
	
	
	// 전체 게시글 수
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM cbbs";
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
	
	// 검색에서의 데이터 개수
		public int dataCount(String schType, String kwd) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT NVL(COUNT(*), 0) "
						+ " FROM cbbs b "
						+ " JOIN member m ON b.userId = m.userId ";
				if (schType.equals("all")) {
					sql += "  WHERE INSTR(ctitle, ?) >= 1 OR INSTR(ccontent, ?) >= 1 ";
				} else if (schType.equals("creg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sql += "  WHERE TO_CHAR(creg_date, 'YYYYMMDD') = ? ";
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
		
		// 게시물 리스트
		public List<CommunityDTO> listCommunity(int offset, int size) {
			List<CommunityDTO> list = new ArrayList<CommunityDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				sb.append(" SELECT p.cnum, p.userId, userName, ctitle, picfilename,TO_CHAR(creg_date, 'YYYY-MM-DD') creg_date,");
				sb.append(" ccategory, chitCount FROM cbbs p ");
				sb.append(" JOIN member m ON p.userId = m.userId ");
				sb.append(" LEFT OUTER JOIN ( ");
				sb.append(" SELECT picnum, cnum, picfilename FROM ( ");
				sb.append(" SELECT picnum, cnum, picfilename, ");
				sb.append(" ROW_NUMBER() OVER(PARTITION BY cnum ORDER BY picnum ASC) rank ");
				sb.append(" FROM cpics");
				sb.append(" ) WHERE rank = 1 ");
				sb.append("  ) i ON p.cnum = i.cnum ");
				sb.append(" ORDER BY cnum DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			

				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setInt(1, offset);
				pstmt.setInt(2, size);

				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					CommunityDTO dto = new CommunityDTO();
					
					dto.setNum(rs.getLong("cnum"));
					dto.setCcategory(rs.getString("ccategory"));
					dto.setUserName(rs.getString("userName"));
					dto.setCtitle(rs.getString("ctitle"));
					dto.setChitCount(rs.getInt("chitCount"));
					dto.setCreg_date(rs.getString("creg_date"));
					dto.setPicFileName(rs.getString("picfilename"));
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
		
		public List<CommunityDTO> listCommunity(int offset, int size, String schType, String kwd) {
			List<CommunityDTO> list = new ArrayList<CommunityDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();

			try {
				sb.append(" SELECT cnum, userName, ctitle, chitCount, ");
				sb.append("      TO_CHAR(creg_date, 'YYYY-MM-DD') creg_date ");
				sb.append(" FROM cbbs b ");
				sb.append(" JOIN member m ON b.userId = m.userId ");
				if (schType.equals("all")) {
					sb.append(" WHERE INSTR(ctitle, ?) >= 1 OR INSTR(ccontent, ?) >= 1 ");
				} else if (schType.equals("creg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append(" WHERE TO_CHAR(creg_date, 'YYYYMMDD') = ?");
				} else {
					sb.append(" WHERE INSTR(" + schType + ", ?) >= 1 ");
				}
				sb.append(" ORDER BY cnum DESC ");
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
					CommunityDTO dto = new CommunityDTO();

					dto.setNum(rs.getLong("cnum"));;

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
		
		// 조회수 증가하기
		public void updateHitCount(long num) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				sql = "UPDATE cbbs SET chitCount=chitCount+1 WHERE cnum=?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}
		
		// 해당 게시물 보기
		public CommunityDTO findById(long num) {
			CommunityDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = " SELECT b.cnum, ctitle, b.ccategory, m.userId, userName, ccontent,  "
						+ " creg_date, chitCount , NVL(likeCount, 0) likeCount  "
						+ " FROM cbbs b  "
						+ " JOIN member m ON b.userId=m.userId "
						+ "  LEFT OUTER JOIN ("
						+ "  SELECT num, COUNT(*) likeCount FROM cbbsLike"
						+ "   GROUP BY num"
						+ "  ) bc ON b.cnum = bc.num"
						+ "  WHERE b.cnum = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);

				rs = pstmt.executeQuery();	

				if (rs.next()) {
					dto = new CommunityDTO();
					
					dto.setNum(rs.getLong("cnum"));
					dto.setCcategory(rs.getString("ccategory"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setCtitle(rs.getString("ctitle"));
					dto.setCcontent(rs.getString("ccontent"));
					dto.setChitCount(rs.getInt("chitCount"));
					dto.setCreg_date(rs.getString("creg_date"));
					dto.setLikeCount(rs.getInt("likeCount"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}

			return dto;
		}
		
		// 이미지 부르기
		public List<CommunityDTO> listPhotoFile(long num){
			List<CommunityDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT picnum, cnum, picFileName FROM cpics WHERE cnum = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					CommunityDTO dto = new CommunityDTO();

					dto.setPicnum(rs.getLong("picnum"));
					dto.setNum(rs.getLong("cnum"));
					dto.setPicFileName(rs.getString("picFileName"));
					
					list.add(dto);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(pstmt);
			}
			
			return list;
		}
		
		// 해당 모임 테이블 가져오기  findMeet
		public CommunityDTO findMeet(long num) {
			CommunityDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "select mnum,mcount,mreg_date,zip,addr1,addr2 "
						+ " from meet where cnum = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);

				rs = pstmt.executeQuery();
				
				if (rs.next()) {
					dto = new CommunityDTO();
					
					dto.setMnum(rs.getLong("mnum"));
					dto.setCmember(rs.getInt("mcount"));
					dto.setMreg_date(rs.getString("mreg_date"));
					dto.setZip(rs.getString("zip"));
					dto.setAddr1(rs.getString("addr1"));
					dto.setAddr2(rs.getString("addr2"));
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			return dto;
		}
		
		// 해당 게시글에 모임 인원 수 및 아이디
		public int findMeetmember(long meetnum) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "select NVL(COUNT(*), 0) from meetmember"
						+ " where mnum = ? ";
				pstmt = conn.prepareStatement(sql);

				pstmt.setLong(1, meetnum);

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
		
		
		// cdataCount
		public int cdataCount(long num) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;

			try {
				sql = "SELECT NVL(COUNT(*), 0) FROM cpics where cnum = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, num);
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
		
		
		// 해당 모임에 참여 인원수 
		
		public List<CommunityDTO> findMeetUsername(long num) {
			List<CommunityDTO> list = new ArrayList<CommunityDTO>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = " select mnum,m.userid,username from meetmember mt "
						+ " join member m on m.userid = mt.userid where mnum = ? ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, num);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					CommunityDTO dto = new CommunityDTO();
					
					dto.setUserName(rs.getString("userName"));
					list.add(dto);
				}
			}  catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			
			return list;
		}
		
		// 참여인원수
		public void insertmeetmember(CommunityDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "insert into meetmember(mnum,userid) values "
						+ " (?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, dto.getMnum());
				pstmt.setString(2, dto.getUserId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}
		
		// deletemeetmember
		public void deletemeetmember(CommunityDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "delete from meetmember where mnum=? and userId= ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, dto.getMnum());
				pstmt.setString(2, dto.getUserId());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}
		
		// 이미 참여 했나 안했나 selectmember
		public int selectmember(long mnum,String userId) {
			int result =0 ;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "select userId from meetmember where mnum = ? and userId = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, mnum);
				pstmt.setString(2,userId);
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = 1;
				}else {
					result = 0;
				}
			}catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			return result;
		}
		
		
		// 게시물의 공감 추가
		public void insertBoardLike(long num, String userId) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "INSERT INTO cbbsLike(num, userId) VALUES (?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				pstmt.setString(2, userId);
				
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
			
		}
		
		// 게시글 공감 삭제
		public void deleteBoardLike(long num, String userId) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "DELETE FROM cbbsLike WHERE num = ? AND userId = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				pstmt.setString(2, userId);
				
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
			
		}
		
		// 게시물의 공감 개수
		public int countBoardLike(long num) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT NVL(COUNT(*), 0) FROM cbbsLike WHERE num=?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
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
		
		// 로그인 유저의 게시글 공감 유무
		public boolean isUserBoardLike(long num, String userId) {
			boolean result = false;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT num, userId "
						+ " FROM cbbsLike "
						+ " WHERE num = ? AND userId = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				pstmt.setString(2, userId);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return result;
		}
		
		// 삭제 deleteCbbs
		public void deleteCbbs(long num, String userId) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			try {
				if(userId.equals("admin")) {
					sql = "DELETE FROM cbbs WHERE cnum = ?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					
					pstmt.executeUpdate();
				}else {
					sql = "DELETE FROM cbbs WHERE cnum = ? AND userId = ?";
					
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setLong(1, num);
					pstmt.setString(2, userId);
					
					pstmt.executeUpdate();
				}
			}  catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}  finally {
				DBUtil.close(pstmt);
			}
		
		}
		
		// 사진파일 삭제 deletePhotoFile
		public void deletePhotoFile(String mode, long num) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;

			try {
				if (mode.equals("all")) {
					sql = "DELETE FROM cpics WHERE cnum = ?";
				} else {
					sql = "DELETE FROM cpics WHERE picnum = ?";
				}
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
		}

		
		// 게시물의 댓글 및 답글 추가
		public void insertReply(CreplyDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "INSERT INTO cbbsReply(replyNum, cnum, userId, content, answer, reg_date) "
						+ " VALUES (cbbsReply_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, dto.getNum());
				pstmt.setString(2, dto.getUserId());
				pstmt.setString(3, dto.getContent());
				pstmt.setLong(4, dto.getAnswer());
				
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}
			
		}
		
		// 게시물의 댓글 개수
		public int dataCountReply(long num) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT NVL(COUNT(*), 0) "
						+ " FROM cbbsReply "
						+ " WHERE cnum = ? AND answer = 0";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, num);
				
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
		
		// 게시물 댓글 리스트
		public List<CreplyDTO> listReply(long num, int offset, int size) {
			List<CreplyDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT r.replyNum, r.userId, userName, cnum, content, r.reg_date, ");
				sb.append("     NVL(answerCount, 0) answerCount, ");
				sb.append("     NVL(likeCount, 0) likeCount, ");
				sb.append("     NVL(disLikeCount, 0) disLikeCount ");
				sb.append(" FROM cbbsReply r ");
				sb.append(" JOIN member m ON r.userId = m.userId ");
				sb.append(" LEFT OUTER  JOIN (");
				sb.append("	    SELECT answer, COUNT(*) answerCount ");
				sb.append("     FROM cbbsReply ");
				sb.append("     WHERE answer != 0 ");
				sb.append("     GROUP BY answer ");
				sb.append(" ) a ON r.replyNum = a.answer ");
				sb.append(" LEFT OUTER  JOIN ( ");
				sb.append("	    SELECT replyNum, ");
				sb.append("         COUNT(DECODE(replyLike, 1, 1)) likeCount, ");
				sb.append("         COUNT(DECODE(replyLike, 0, 1)) disLikeCount ");
				sb.append("     FROM cbbsReplyLike ");
				sb.append("     GROUP BY replyNum ");
				sb.append(" ) b ON r.replyNum = b.replyNum  ");
				sb.append(" WHERE cnum = ? AND r.answer=0 ");
				sb.append(" ORDER BY r.replyNum DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);

				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					CreplyDTO dto = new CreplyDTO();
					
					dto.setReplyNum(rs.getLong("replyNum"));
					dto.setNum(rs.getLong("cnum"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
					dto.setAnswerCount(rs.getInt("answerCount"));
					dto.setLikeCount(rs.getInt("likeCount"));
					dto.setDisLikeCount(rs.getInt("disLikeCount"));
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
		
		///////////////////
		
		public CreplyDTO findByReplyId(long replyNum) {
			CreplyDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT replyNum, cnum, r.userId, userName, content, r.reg_date "
						+ " FROM cbbsReply r  "
						+ " JOIN member m ON r.userId = m.userId  "
						+ " WHERE replyNum = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, replyNum);

				rs=pstmt.executeQuery();
				
				if(rs.next()) {
					dto=new CreplyDTO();
					
					dto.setReplyNum(rs.getLong("replyNum"));
					dto.setNum(rs.getLong("cnum"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return dto;
		}
		
		// 게시물의 댓글 삭제
		public void deleteReply(long replyNum, String userId) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			if(! userId.equals("admin")) {
				CreplyDTO dto = findByReplyId(replyNum);
				if(dto == null || (! userId.equals(dto.getUserId()))) {
					return;
				}
			}
			
			try {
				sql = "DELETE FROM cbbsReply "
						+ " WHERE replyNum IN  "
						+ " (SELECT replyNum FROM cbbsReply START WITH replyNum = ?"
						+ "     CONNECT BY PRIOR replyNum = answer)";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, replyNum);
				
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}		
			
		}

		// 댓글의 답글 리스트
		public List<CreplyDTO> listReplyAnswer(long answer) {
			List<CreplyDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb=new StringBuilder();
			
			try {
				sb.append(" SELECT replyNum, cnum, r.userId, userName, content, r.reg_date, answer ");
				sb.append(" FROM cbbsReply r ");
				sb.append(" JOIN member m ON r.userId = m.userId ");
				sb.append(" WHERE answer = ? ");
				sb.append(" ORDER BY replyNum DESC ");
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, answer);

				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					CreplyDTO dto=new CreplyDTO();
					
					dto.setReplyNum(rs.getLong("replyNum"));
					dto.setNum(rs.getLong("cnum"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setContent(rs.getString("content"));
					dto.setReg_date(rs.getString("reg_date"));
					dto.setAnswer(rs.getLong("answer"));
					
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
		
		// 댓글의 답글 개수
		public int dataCountReplyAnswer(long answer) {
			int result = 0;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT NVL(COUNT(*), 0) "
						+ " FROM cbbsReply WHERE answer = ?";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, answer);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					result=rs.getInt(1);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return result;
		}
		
		// 댓글의 좋아요 / 싫어요 추가
		public void insertReplyLike(CreplyDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "INSERT INTO cbbsReplyLike(replyNum, userId, replyLike) VALUES (?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, dto.getReplyNum());
				pstmt.setString(2, dto.getUserId());
				pstmt.setInt(3, dto.getReplyLike());
				
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				DBUtil.close(pstmt);
			}		

		}
		
		// 댓글의 좋아요 / 싫어요 개수
		public Map<String, Integer> countReplyLike(long replyNum) {
			Map<String, Integer> map = new HashMap<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = " SELECT COUNT(DECODE(replyLike, 1, 1)) likeCount,  "
					+ "     COUNT(DECODE(replyLike, 0, 1)) disLikeCount  "
					+ " FROM cbbsReplyLike WHERE replyNum = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, replyNum);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					map.put("likeCount", rs.getInt("likeCount"));
					map.put("disLikeCount", rs.getInt("disLikeCount"));
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return map;
		}	
		
}
