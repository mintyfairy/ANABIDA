package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;
import com.util.DBUtil;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();
	
	public MemberDTO loginMember(String userId, String userPwd) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = " SELECT userId, userName, userPwd, reg_date, score "
					+ " FROM member"
					+ " WHERE userId = ? AND userPwd = ? AND enabled = 1";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			pstmt.setString(2, userPwd);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setScore(rs.getString("score"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}	
	public void insertMember(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "INSERT INTO member(userId, userPwd, userName, enabled, reg_date) VALUES (?, ?, ?, 1, SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserPwd());
			pstmt.setString(3, dto.getUserName());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "INSERT INTO member2(userId, birth, email, tel, zip, addr1, addr2) VALUES (?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, ?, ?)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getBirth());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getTel());
			pstmt.setString(5, dto.getZip());
			pstmt.setString(6, dto.getAddr1());
			pstmt.setString(7, dto.getAddr2());
			
			pstmt.executeUpdate();
			
			
			conn.commit();

		} catch (SQLException e) {
			DBUtil.rollback(conn);
			
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
			}
		}
		
	}
	public MemberDTO findById(String userId) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m1.userId, userName, userPwd,proFile");
			sb.append("      enabled, reg_date, score,");
			sb.append("      TRUNC(MONTHS_BETWEEN(TRUNC(SYSDATE), birth) / 12) age, ");
			sb.append("      TO_CHAR(birth, 'YYYY-MM-DD') birth, ");
			sb.append("      email, tel,");
			sb.append("      zip, addr1, addr2,proFile");
			sb.append("  FROM member m1");
			sb.append("  LEFT OUTER JOIN member2 m2 ON m1.userId=m2.userId ");
			sb.append("  WHERE m1.userId = ?");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setAge(rs.getInt("age"));
				dto.setProFile(rs.getString("proFile"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setReg_date(rs.getString("reg_date"));
				dto.setScore(rs.getString("score"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel(rs.getString("tel"));
				if(dto.getTel() != null) {
					String[] ss = dto.getTel().split("-");
					if(ss.length == 3) {
						dto.setTel1(ss[0]);
						dto.setTel2(ss[1]);
						dto.setTel3(ss[2]);
					}
				}
				dto.setEmail(rs.getString("email"));
				if(dto.getEmail() != null) {
					String[] ss = dto.getEmail().split("@");
					if(ss.length == 2) {
						dto.setEmail1(ss[0]);
						dto.setEmail2(ss[1]);
					}
				}
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
	public void deleteMember(String userId) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE member SET enabled=0 WHERE userId=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "DELETE FROM member2 WHERE userId=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	
	public void updateMember(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE member SET userPwd=?  WHERE userId=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserPwd());
			pstmt.setString(2, dto.getUserId());
			
			pstmt.executeUpdate();
			
			pstmt.close();
			pstmt = null;
			
			sql = "UPDATE member2 SET birth=TO_DATE(?,'YYYY-MM-DD'), email=?, tel=?, zip=?, addr1=?, addr2=? WHERE userId=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getBirth());
			pstmt.setString(2, dto.getEmail());
			pstmt.setString(3, dto.getTel());
			pstmt.setString(4, dto.getZip());
			pstmt.setString(5, dto.getAddr1());
			pstmt.setString(6, dto.getAddr2());
			pstmt.setString(7, dto.getUserId());
			
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	// 
	public void uploadProFile(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE member SET profile=?  WHERE userId=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getProFile());
			pstmt.setString(2, dto.getUserId());
			
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	//DeleteProFile
	public void DeleteProFile(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE member SET profile=?  WHERE userId=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getProFile());
			pstmt.setString(2, dto.getUserId());
			
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	// findByPhoto
	public MemberDTO findByPhoto(String userId) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "select profile from member where userid = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				dto.setProFile(rs.getString("proFile"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return dto;
	}
	
	// mypagewish
	public List<MPDTO> mypagewish(String userId){
				List<MPDTO> wlist = new ArrayList<MPDTO>();
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String sql;	
				
				try {
					sql="select m.userName, a.userId, a.subject, a.content,"
							+ " a.cost, a.regdate"
							+ " , a.pstate, case when"
							+ " a.pstate = 0 then '거래중' else '판매완료' end as state from pbbs a join "
							+ " wish_lists b on a.pnum=b.pnum"
							+ " join member m on a.userId=m.userId "
							+ " where b.userId=?";
					
					pstmt=conn.prepareStatement(sql);
					pstmt.setString(1, userId);
					
					rs = pstmt.executeQuery();
					
					while(rs.next()) {
						MPDTO to = new MPDTO();
						to.setWcontent(rs.getString("content"));
						to.setWcost(rs.getLong("cost"));
						to.setWreg_date(rs.getString("regdate"));
						to.setWsubject(rs.getString("subject"));
						to.setWuserId(rs.getString("userId"));
						to.setWuserName(rs.getString("userName"));
						to.setWsellstate(rs.getString("state"));
						wlist.add(to);
					}
					
				} catch (Exception e) {
				} finally {
					DBUtil.close(rs);
					DBUtil.close(pstmt);
				}
				
				
				return wlist;
			}
	
	public List<MemberDTO> meetMember(String userId){
		List<MemberDTO> meet1 = new ArrayList<MemberDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;	
		
		try {
			sql= " select ctitle,mreg_date"
					+ " from cbbs a "
					+ " join meet b on a.cnum=b.cnum "
					+ " join meetmember c on b.mnum=c.mnum"
					+ " where c.userid=? ";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			System.out.println("asdasdasdasdsa");
			
			while(rs.next()) {
				
				MemberDTO dto = new MemberDTO();
				System.out.println("asdasd"+dto.getCtitle());
				dto.setCtitle(rs.getString("ctitle"));
				dto.setMreg_date(rs.getString("mreg_date"));
				meet1.add(dto);
			}
			
		} catch (Exception e) {
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return meet1;
	}
	
	// mypagesell
	
	public List<MPDTO> mypagesell(String userId){
		List<MPDTO> slist = new ArrayList<MPDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;	
		
		
		try {
			sql=" select m.userName, a.userId, a.subject, "
					+ " a.content, a.cost, a.regdate,"
					+ " a.pstate, case when a.pstate = 0 then '거래중' else '판매완료' end as "
					+ " state from pbbs a "
					+ " join member m on a.userId=m.userId where "
					+ " a.userId=?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MPDTO vo = new MPDTO();
				vo.setScontent(rs.getString("content"));
				vo.setScost(rs.getLong("cost"));
				vo.setSreg_date(rs.getString("regdate"));
				vo.setSsubject(rs.getString("subject"));
				vo.setSuserId(rs.getString("userId"));
				vo.setSuserName(rs.getString("userName"));
				vo.setSellstate(rs.getString("state"));
				slist.add(vo);
			}
			
		} catch (Exception e) {
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return slist;
	}
	
	public List<MPDTO> mypagebuy(String userId){
        List<MPDTO> blist = new ArrayList<MPDTO>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql;


        try {
            sql="  select a.subject, a.content, a.cost, b.hdate "
                    + "from pbbs a join phistory b on a.pnum=b.hisnum "
                    + "where b.buyer=?";

            pstmt=conn.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();

            while(rs.next()) {
                MPDTO bo = new MPDTO();
                bo.setBcontent(rs.getString("content"));
                bo.setBcost(rs.getLong("cost"));
                bo.setBhdate(rs.getString("hdate"));
                bo.setBsubject(rs.getString("subject"));
                blist.add(bo);
            }

        } catch (Exception e) {
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmt);
        }


        return blist;
    }
	
	public List<MPDTO> mypageqna(String userId){
		List<MPDTO> qlist = new ArrayList<MPDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;	
		
		
		try {
			sql="select qnum, title, content, reg_date, "
					+ " answeryes, case when answeryes = 0 then '답변대기' "
					+ " else '답변완료' end as answer"
					+ " from qbbs where userId =?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MPDTO zo = new MPDTO();
				zo.setQqnum(rs.getInt("qnum"));
				zo.setQreg_date(rs.getString("reg_date"));
				zo.setQtitle(rs.getString("title"));
				zo.setQanswer(rs.getString("answer"));
				zo.setQcontent(rs.getString("content"));
				qlist.add(zo);
			}
			
		} catch (Exception e) {
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return qlist;
	}
}
