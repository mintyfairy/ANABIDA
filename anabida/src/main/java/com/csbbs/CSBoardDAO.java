package com.csbbs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;
import com.util.DBUtil;

public class CSBoardDAO {
	private Connection conn = DBConn.getConnection();

	public void insertBoard(CSBoardDTO dto, String mode) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		long seq;

		try {
			sql = "SELECT qbbs_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			seq = 0;
			if (rs.next()) {
				seq = rs.getLong(1);
			}
			dto.setQnum(seq);
			String n = null;
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;

			if (mode.equals("write")) {
				// 글쓰기일 때
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
				dto.setAnswerUserId2(n);
			} else if (mode.equals("answer")) {
				// 답변일때
				updateOrderNo(dto.getGroupNum(), dto.getOrderNo());
				dto.setDepth(dto.getDepth() + 1);
				dto.setOrderNo(dto.getOrderNo() + 1);
				dto.setAnswerUserId2(dto.getAnswerUserId());

			}

			sql = " INSERT INTO qbbs( qnum, userId, title, content, "
					+ " groupNum, depth, orderNo, parent, answeruserId, answeruserId2,   "
					+ " reg_date, hitCount,answeryes ) " + " VALUES (?, ?, ?, ?,?,?,?,?,?,?, SYSDATE, 0,?)  ";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, dto.getQnum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getTitle());
			pstmt.setString(4, dto.getContent());
			pstmt.setLong(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNo());
			pstmt.setLong(8, dto.getParent());
			pstmt.setString(9, dto.getAnswerUserId());
			pstmt.setString(10, dto.getAnswerUserId2());
			pstmt.setInt(11, dto.getAnsweryes());

			pstmt.executeQuery();
			pstmt.close();
			pstmt = null;
			
			if (dto.getImgfiles() != null) {

				sql = "INSERT INTO qattachment (qanum, qnum, filename)" + " VALUES(qattachment_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);

				for (int i = 0; i < dto.getImgfiles().length; i++) {
					pstmt.setLong(1, dto.getQnum());
					pstmt.setString(2, dto.getImgfiles()[i]);

					pstmt.executeUpdate();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	// 답변일 경우 orderNo 변경
	public void updateOrderNo(long groupNum, int orderNo) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE qbbs SET orderNo = orderNo+1 WHERE groupNum = ? AND orderNo > ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, groupNum);
			pstmt.setInt(2, orderNo);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}

	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM qbbs";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			if (rs.next()) {
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

	// 검색에서의 데이터 개수
	public int dataCount(String schType, String kwd) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NVL(COUNT(*), 0) " + " FROM qbbs a " + " JOIN member m ON a.userId = m.userId ";
			if (schType.equals("all")) {
				sql += "  WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ";
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

	// 게시글 리스트
	public List<CSBoardDTO> listBoard(int offset, int size) {
		List<CSBoardDTO> list = new ArrayList<CSBoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT b.qnum, b.userId, m.userName, b.parent, b.answerUserId, b.answerUserId2, ");
			sb.append("       b.title, b.groupNum, b.orderNo, b.depth, b.hitCount, b.answeryes, ");
			sb.append("       TO_CHAR(b.reg_date, 'YYYY-MM-DD') reg_date ");
			sb.append(" FROM qbbs b ");
			sb.append(" JOIN member m ON b.userId = m.userId ");
			sb.append(" ORDER BY groupNum ASC, depth ASC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				CSBoardDTO dto = new CSBoardDTO();

				dto.setParent(rs.getLong("parent"));
				dto.setQnum(rs.getLong("qnum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setTitle(rs.getString("title"));
				dto.setGroupNum(rs.getLong("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setAnswerUserId(rs.getString("answerUserId"));
				dto.setAnswerUserId2(rs.getString("answerUserId2"));
				dto.setAnsweryes(rs.getInt("answeryes"));
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

	// 검색에서 게시글 리스트
	public List<CSBoardDTO> listBoard(int offset, int size, String schType, String kwd) {
		List<CSBoardDTO> list = new ArrayList<CSBoardDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			sb.append(" SELECT a.qnum, a.title, a.hitCount, b.userId, a.parent, a.answerUserId, a.answerUserId2, ");
			sb.append(
					" groupNum, orderNo, depth, a.answeryes, TO_CHAR(a.reg_date, 'YYYY-MM-DD') reg_date, b.userName  ");
			sb.append(" FROM qbbs a JOIN member b ON a.userId = b.userId ");
			sb.append(" WHERE  a.answerUserId=?");
			sb.append(" ORDER BY groupNum desc, depth ASC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());

			pstmt.setString(1, kwd);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				CSBoardDTO dto = new CSBoardDTO();

				dto.setQnum(rs.getLong("qnum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setParent(rs.getLong("parent"));
				dto.setTitle(rs.getString("title"));
				dto.setGroupNum(rs.getLong("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setAnswerUserId(rs.getString("answerUserId"));
				dto.setAnswerUserId2(rs.getString("answerUserId2"));
				dto.setAnsweryes(rs.getInt("answeryes"));

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

	// 조회수 증가
	public void updateHitcount(long qnum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = " UPDATE qbbs set hitCount = hitCount +1 where qnum = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, qnum); // 시퀀스는 long으로 처리 (자리가 크기 때문)

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

	}

	// 번호로 찾기
	public CSBoardDTO findById(long qnum) {
		CSBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT a.qnum, a.title, a.content, b.userId, " + " a.hitCount, a.reg_date, c.qanum, c.filename, "
					+ " a.groupNum, a.depth, a.answeryes, a.orderNo, a.parent, a.answerUserId, a.answerUserId2, b.userName from qbbs a join member b on "
					+ " a.userId=b.userId join qattachment c on a.qnum=c.qnum where a.qnum = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, qnum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new CSBoardDTO();

				dto.setQnum(rs.getLong("qnum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getLong("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setParent(rs.getLong("parent"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setAnswerUserId(rs.getString("answerUserId"));
				dto.setAnswerUserId2(rs.getString("answerUserId2"));
				dto.setQanum(rs.getLong("qanum"));
				dto.setFilename(rs.getString("filename"));
				dto.setAnsweryes(rs.getInt("answeryes"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return dto;
	}

	// 이전글
	public CSBoardDTO findByPrevBoard(long groupNum, int orderNo, String schType, String kwd) {
		CSBoardDTO dto = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT b.qnum, b.title");
				sb.append(" FROM qbbs b ");
				sb.append(" JOIN member m ON b.userId = m.userId ");
				sb.append(" WHERE ( (groupNum = ? AND orderNo < ?) OR (groupNum > ?) ) ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
				}
				sb.append(" ORDER BY groupNum ASC, orderNo DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setLong(3, groupNum);
				pstmt.setString(4, kwd);
				if (schType.equals("all")) {
					pstmt.setString(5, kwd);
				}
			} else {
				sb.append(" SELECT qnum, title");
				sb.append(" FROM qbbs ");
				sb.append(" WHERE (groupNum = ? AND orderNo < ?) OR (groupNum > ?) ");
				sb.append(" ORDER BY groupNum ASC, orderNo DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setLong(3, groupNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new CSBoardDTO();

				dto.setQnum(rs.getLong("qnum"));
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

	// 다음글
	public CSBoardDTO findByNextBoard(long groupNum, int orderNo, String schType, String kwd) {
		CSBoardDTO dto = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();

		try {
			if (kwd != null && kwd.length() != 0) {
				sb.append(" SELECT b.qnum, b.title");
				sb.append(" FROM qbbs b ");
				sb.append(" JOIN member m ON b.userId = m.userId ");
				sb.append(" WHERE ( (groupNum = ? AND orderNo < ?) OR (groupNum > ?) ) ");
				if (schType.equals("all")) {
					sb.append("   AND ( INSTR(title, ?) >= 1 OR INSTR(content, ?) >= 1 ) ");
				} else if (schType.equals("reg_date")) {
					kwd = kwd.replaceAll("(\\-|\\/|\\.)", "");
					sb.append("   AND ( TO_CHAR(reg_date, 'YYYYMMDD') = ? ) ");
				} else {
					sb.append("   AND ( INSTR(" + schType + ", ?) >= 1 ) ");
				}
				sb.append(" ORDER BY groupNum ASC, orderNo DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setLong(3, groupNum);
				pstmt.setString(4, kwd);
				if (schType.equals("all")) {
					pstmt.setString(5, kwd);
				}
			} else {
				sb.append(" SELECT qnum, title ");
				sb.append(" FROM qbbs ");
				sb.append(" WHERE (groupNum = ? AND orderNo < ?) OR (groupNum > ?) ");
				sb.append(" ORDER BY groupNum ASC, orderNo DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");

				pstmt = conn.prepareStatement(sb.toString());

				pstmt.setLong(1, groupNum);
				pstmt.setInt(2, orderNo);
				pstmt.setLong(3, groupNum);
			}

			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new CSBoardDTO();
				dto.setQnum(rs.getLong("qnum"));
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

	public void updateBoard(CSBoardDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE qbbs set title=?, content=?" + " where qnum=? AND userId=?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setLong(3, dto.getQnum());
			pstmt.setString(4, dto.getUserId());
			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;

			if (dto.getImgfiles() != null) {
				sql ="INSERT INTO qattachment(qanum, qnum, filename) VALUES "
				+ " (qattachment_seq.NEXTVAL, ?, ?)";

			pstmt = conn.prepareStatement(sql);
			
			for(int i = 0; i<dto.getImgfiles().length; i++) {
			pstmt.setLong(1, dto.getQnum());
			pstmt.setString(2, dto.getImgfiles()[i]);
			
			pstmt.executeUpdate();
				
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	public void deleteBoard(long qnum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "DELETE FROM qbbs WHERE qnum IN " + " (SELECT qnum FROM qbbs " + " START WITH qnum = ? "
					+ " CONNECT BY PRIOR qnum = parent)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, qnum);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}

	public void answer(CSBoardDTO dto, int answeryes) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "UPDATE qbbs set answeryes=? where groupNum=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, answeryes);
			pstmt.setLong(2, dto.getGroupNum());

			pstmt.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	public List<CSBoardDTO> listPhotoFile(long qnum) {
		List<CSBoardDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT qanum, qnum, filename FROM qattachment WHERE qnum = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, qnum);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CSBoardDTO dto = new CSBoardDTO();

				dto.setQanum(rs.getLong("qanum"));
				dto.setQnum(rs.getLong("qnum"));
				dto.setFilename(rs.getString("filename"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

		return list;
	}
	
	public CSBoardDTO findByFileId(long qanum) {
		CSBoardDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT qanum, qnum, filename FROM qattachment WHERE qanum = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, qanum);
			
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new CSBoardDTO();
				
				dto.setQanum(rs.getLong("qanum"));
				dto.setQnum(rs.getLong("qnum"));
				dto.setFilename(rs.getString("filename"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}
	
	public void deletePhotoFile(String mode, long qnum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (mode.equals("all")) {
				sql = "DELETE FROM qattachment WHERE qnum = ?";
			} else {
				sql = "DELETE FROM qattachment WHERE qanum = ?";
			}
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, qnum);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
	}
	
	

	/*
	 * 
	 * // 검색에서 데이터 개수 구하기 public int dataCount(String schType, String kwd) { int
	 * result = 0; PreparedStatement pstmt = null; ResultSet rs = null; String sql;
	 * 
	 * try { sql = "SELECT COUNT(*) FROM qbbs a join member b on a.userId=b.userId";
	 * if (schType.equals("all")) { // 제목 또는 내용 sql +=
	 * " WHERE INSTR(title, ?) >= 1 OR INSTR(content, ?) >=1"; } else if
	 * (schType.equals("reg_date")) { // 날짜 kwd = kwd.replaceAll("(\\-|\\/|\\.)",
	 * ""); sql += " WHERE TO_CHAR(a.reg_date, 'YYYYMMDD') = ?"; } else
	 * if(schType.equals("userName")){ // 이름, 제목, 내용 sql += " WHERE INSTR(" +
	 * schType + ", ?) >=1"; } else if (schType.equals("title")) { sql +=
	 * " WHERE INSTR(" + schType + ", ?) >=1"; } else if (schType.equals("content"))
	 * { sql += " WHERE INSTR(" + schType + ", ?) >=1"; } pstmt =
	 * conn.prepareStatement(sql);
	 * 
	 * pstmt.setString(1, kwd); if (schType.equals("all")) { pstmt.setString(2,
	 * kwd); }
	 * 
	 * rs = pstmt.executeQuery(); if (rs.next()) { result = rs.getInt(1); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } finally { DBUtil.close(rs);
	 * DBUtil.close(pstmt); }
	 * 
	 * return result; }
	 * 
	 */

}
