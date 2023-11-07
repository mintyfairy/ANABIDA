package com.pbbs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;
import com.util.DBUtil;

public class PbbsDAO {
	private Connection conn=DBConn.getConnection();
	
	public void insertPhoto(PbbsDTO dto) throws SQLException {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		long seq;
		
		try {
			conn.setAutoCommit(false);
			/*sql="insert into pbbs(pnum,userid,subject,content,IMAGEFILENAME,cost,catnum) values(pbbs_seq.nextval,?,?,?,?,?,?)";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getImageFilename());
			pstmt.setLong(5, dto.getCost());
			pstmt.setLong(6, dto.getCatNum());
			
			pstmt.executeUpdate();*///단일파일업로드흔적
			sql = "SELECT pbbs_seq.NEXTVAL FROM dual";
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
			//시퀀스를 한번 불러서 다같은값으로 확실히넣으려는 것

			sql = "insert into pbbs(pnum,userid,subject,content,IMAGEFILENAME,cost,catnum) values(?,?,?,?,?,?,?)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, dto.getNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setString(5, dto.getImageFiles()[0]);//첫번째를 섬네일로 고르는 모습
			pstmt.setLong(6, dto.getCost());
			pstmt.setLong(7, dto.getCatNum());
			
			pstmt.executeUpdate();

			pstmt.close();
			pstmt = null;
			//여기까지 게시글 테이블에 넣음
			
			if (dto.getImageFiles() != null) {
				sql = "INSERT INTO pPic(picNum,num,imageFilename) VALUES "
						+ " (pPic_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for (int i = 0; i < dto.getImageFiles().length; i++) {
					//1부터시작하는 이유는 이미 섬네일용으로 앞에서 0을 사용함--취소
					//섬네일용을 한번더 저장하는 방식으로
					pstmt.setLong(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					
					pstmt.executeUpdate();
				}
			}
			
			
			
			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
			}
		}
	}

	public int dataCount() throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		ResultSet rs=null;
		
		try {
			sql="select count(*) from pbbs";
			pstmt=conn.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	public int dataCount(long num) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		ResultSet rs=null;
		
		try {
			sql="select count(*) from pbbs where CATNUM=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return result;
	}
	public List<PbbsDTO> listPhoto(int offset, int size) throws SQLException {
		List<PbbsDTO> list=new ArrayList<PbbsDTO>();
		PreparedStatement pstmt=null;
		String sql;
		ResultSet rs=null;
		
		try {
			sql="select pnum,userid,subject,imagefilename,cost,to_char(regdate,'YYYY-MM-DD') regdate,pstate from pbbs order by pnum desc offset ? rows fetch first ? rows only";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				PbbsDTO dto= new PbbsDTO();
				
				dto.setNum(rs.getLong("pnum"));
				dto.setUserId(rs.getString("userid"));
				dto.setSubject(rs.getString("subject"));
				dto.setImageFilename(rs.getString("imagefilename"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setCost(rs.getLong("cost"));
				dto.setPstate(rs.getLong("pstate"));
				
				list.add(dto);
			}
			
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return list;
	}
	
	public List<PbbsDTO> mostViewListPhoto(int offset, int size) throws SQLException {
		List<PbbsDTO> list=new ArrayList<PbbsDTO>();
		PreparedStatement pstmt=null;
		String sql;
		ResultSet rs=null;
		
		try {
			sql="select pnum,userid,subject,imagefilename,cost,to_char(regdate,'YYYY-MM-DD') regdate,pstate, HITCOUNT from pbbs  order by HITCOUNT  desc offset ? rows fetch first ? rows only";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				PbbsDTO dto= new PbbsDTO();
				
				dto.setNum(rs.getLong("pnum"));
				dto.setUserId(rs.getString("userid"));
				dto.setSubject(rs.getString("subject"));
				dto.setImageFilename(rs.getString("imagefilename"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setCost(rs.getLong("cost"));
				dto.setPstate(rs.getLong("pstate"));
				dto.setHitCount(rs.getLong("HITCOUNT"));
				
				list.add(dto);
			}
			
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return list;
	}
	
	public List<PbbsDTO> popularListPhoto(int offset, int size) throws SQLException {
		List<PbbsDTO> list=new ArrayList<PbbsDTO>();
		PreparedStatement pstmt=null;
		String sql;
		ResultSet rs=null;
		
		try {
			sql="select pnum,userid,subject,imagefilename,cost,to_char(regdate,'YYYY-MM-DD') regdate,pstate, plike from pbbs order by plike desc offset ? rows fetch first ? rows only";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setInt(1, offset);
			pstmt.setInt(2, size);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				PbbsDTO dto= new PbbsDTO();
				
				dto.setNum(rs.getLong("pnum"));
				dto.setUserId(rs.getString("userid"));
				dto.setSubject(rs.getString("subject"));
				dto.setImageFilename(rs.getString("imagefilename"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setCost(rs.getLong("cost"));
				dto.setPstate(rs.getLong("pstate"));
				
				list.add(dto);
			}
			
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return list;
	}
	
	public List<PbbsDTO> listPhoto(int offset, int size,long cat) throws SQLException {
		List<PbbsDTO> list=new ArrayList<PbbsDTO>();
		PreparedStatement pstmt=null;
		String sql;
		ResultSet rs=null;
		
		try {
			sql="select pnum,userid,subject,imagefilename,cost,to_char(regdate,'YYYY-MM-DD') regdate,pstate from pbbs WHERE catnum=? order by pnum desc offset ? rows fetch first ? rows only";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, cat);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				PbbsDTO dto= new PbbsDTO();
				
				dto.setNum(rs.getLong("pnum"));
				dto.setUserId(rs.getString("userid"));
				dto.setSubject(rs.getString("subject"));
				dto.setImageFilename(rs.getString("imagefilename"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setCost(rs.getLong("cost"));
				dto.setPstate(rs.getLong("pstate"));
				
				
				list.add(dto);
			}
			
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return list;
	}
	
	public List<PbbsDTO> mostViewListPhoto(int offset, int size,long cat) throws SQLException {
		List<PbbsDTO> list=new ArrayList<PbbsDTO>();
		PreparedStatement pstmt=null;
		String sql;
		ResultSet rs=null;
		
		try {
			sql="select pnum,userid,subject,imagefilename,cost,to_char(regdate,'YYYY-MM-DD') regdate,pstate, HITCOUNT from pbbs WHERE catnum=?  order by HITCOUNT  desc offset ? rows fetch first ? rows only";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, cat);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				PbbsDTO dto= new PbbsDTO();
				
				dto.setNum(rs.getLong("pnum"));
				dto.setUserId(rs.getString("userid"));
				dto.setSubject(rs.getString("subject"));
				dto.setImageFilename(rs.getString("imagefilename"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setCost(rs.getLong("cost"));
				dto.setPstate(rs.getLong("pstate"));
				dto.setHitCount(rs.getLong("HITCOUNT"));
				
				list.add(dto);
			}
			
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return list;
	}
	
	public List<PbbsDTO> popularListPhoto(int offset, int size,long cat) throws SQLException {
		List<PbbsDTO> list=new ArrayList<PbbsDTO>();
		PreparedStatement pstmt=null;
		String sql;
		ResultSet rs=null;
		
		try {
			sql="select pnum,userid,subject,imagefilename,cost,to_char(regdate,'YYYY-MM-DD') regdate,pstate, plike from pbbs WHERE catnum=? order by plike desc offset ? rows fetch first ? rows only";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, cat);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, size);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				PbbsDTO dto= new PbbsDTO();
				
				dto.setNum(rs.getLong("pnum"));
				dto.setUserId(rs.getString("userid"));
				dto.setSubject(rs.getString("subject"));
				dto.setImageFilename(rs.getString("imagefilename"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setCost(rs.getLong("cost"));
				dto.setPstate(rs.getLong("pstate"));
				
				list.add(dto);
			}
			
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return list;
	}
		

	public PbbsDTO findById(long num) throws SQLException {
		PbbsDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet	rs =null;
		String sql;
		
		try {
			sql="select pnum,pbbs.userid id,username,subject,content,cost,TO_CHAR(regdate, 'YYYY-MM-DD') regdate,IMAGEFILENAME,pstate,PLIKE,CATNUM,HITCOUNT from pbbs join member 	on pbbs.userid=member.userid  where pnum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto= new PbbsDTO();
				
				dto.setNum(rs.getLong("pnum"));
				dto.setUserId(rs.getString("id"));
				dto.setUserName(rs.getString("username"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setRegdate(rs.getString("regdate"));
				dto.setImageFilename(rs.getString("IMAGEFILENAME"));
				dto.setCost(rs.getLong("cost")); ;
				dto.setPstate(rs.getLong("pstate")); ;
				dto.setPlike(rs.getLong("PLIKE")); ;
				dto.setCatNum(rs.getLong("CATNUM")); ;
				dto.setHitCount(rs.getLong("HITCOUNT")); ;
				
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		
		return dto;
		
	}
	
	public List<PbbsDTO> listPhotoFile(long num) {
		List<PbbsDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT picNum, num, imageFilename FROM pPic WHERE num = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				PbbsDTO dto = new PbbsDTO();

				dto.setFileNum(rs.getLong("picNum"));
				dto.setNum(rs.getLong("num"));
				dto.setImageFilename(rs.getString("imageFilename"));
				
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

		return list;
	}

	
	public void updatePhoto(PbbsDTO dto ) throws SQLException {
		PreparedStatement pstmt=null;
		String sql;
		try {
			conn.setAutoCommit(false);
			sql="update  pbbs set subject=?,content=?,IMAGEFILENAME=? where pnum=? ";
			pstmt= conn.prepareStatement(sql);
			
			pstmt.setString(1,	 dto.getSubject());
			pstmt.setString(2,	 dto.getContent());
			pstmt.setString(3,	 dto.getImageFiles()[0]);			
			pstmt.setLong(4,	 dto.getNum());
			//pstmt.setString(5,	 dto.getUserId());
			
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;

			
			if (dto.getImageFiles() != null) {
				sql = "INSERT INTO pPic(picNum,num,imageFilename) VALUES "
						+ " (pPic_seq.NEXTVAL, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				
				for (int i = 0; i < dto.getImageFiles().length; i++) {
					//1부터시작하는 이유는 이미 섬네일용으로 앞에서 0을 사용함
					pstmt.setLong(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					
					pstmt.executeUpdate();
				}
			}
			conn.commit();
		} catch (SQLException e) {
			// TODO: handle exception
			DBUtil.rollback(conn);
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
			}
		}
	}
	public void deletePhoto(long	num  ) throws SQLException {
		CallableStatement cstmt=null;
		try {
			cstmt = conn.prepareCall("{call anabada.pbbs_del(?)}");
			cstmt.setLong(1,num);
			
			cstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(cstmt);
		}
	}
	public String getCat(long num) throws SQLException {
		String cat="";
		PreparedStatement pstmt=null;
		String sql;
		ResultSet rs=null;
		
		try {
			sql="select CATNAME from category where CATNUM=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setLong(1, num);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				cat = rs.getString(1);
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return cat;
	}

	public void deletePhotoFile(String mode, long num) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			if (mode.equals("all")) {
				sql = "DELETE FROM pPic WHERE num = ?";
			} else {
				sql = "DELETE FROM pPic WHERE picNum = ?";
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

	public PbbsDTO findByFileId(long picNum) {
		PbbsDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT picNum, num, imageFilename FROM ppic WHERE picNum = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, picNum);
			
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new PbbsDTO();

				dto.setFileNum(rs.getLong("picNum"));
				dto.setNum(rs.getLong("num"));
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
	
	
	public void updateHitCount(long pnum) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {

			sql = "UPDATE pbbs SET hitCount=hitCount+1 WHERE pnum=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, pnum);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}
	
	public String doILikeThis(long num,String id) throws SQLException {
		String b="0";
		PreparedStatement pstmt =null;
		String sql;
		ResultSet rs = null;
		try {
			sql="select count(*) what from wish_lists w join pbbs p on w.pnum=p.pnum where w.userid=? and p.pnum=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setLong(2, num);
			
			rs=pstmt.executeQuery();//해당안되면 영향받은값이 0이 나올것이라 기대됨
			//b=pstmt.executeUpdate();//해당안되면 영향받은값이 0이 나올것이라 기대됨
			//0이라는 숫자를반환하는게 무조건 1로나와서 실패함
			if(rs.next()) {
				b=rs.getString("what");
			}
			
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		
		return b;
	
	}

	public void iLikeIt(long num, String id) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstmt =null;
		String sql;
		try {
			conn.setAutoCommit(false);
			sql="insert into wish_lists values(?,?)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setLong(2, num);
			
			pstmt.executeUpdate();
			DBUtil.close(pstmt);
			sql="update pbbs  set plike=plike+1 where pnum=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
			}
		}
	}

	public void imSoso(long num, String id)throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstmt =null;
		String sql;
		try {
			conn.setAutoCommit(false);
			sql="delete from wish_lists where pnum=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
			DBUtil.close(pstmt);
			sql="update pbbs  set plike=plike-1 where pnum=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
			}
		}
	}
	public void iChooseYou(long num,String buyerId,long replynum) throws SQLException {
		PreparedStatement pstmt =null;
		String sql;
		try {
			conn.setAutoCommit(false);
			sql="update pbbs  set pstate=1 where pnum=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
			DBUtil.close(pstmt);
			
			sql="update preply  set choosed=1 where replynum=? and answer=0";//대댓값안들어가게
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
			DBUtil.close(pstmt);
			
			
			sql="insert into phistory (hisnum,buyer,hdate) values (?,?,sysdate)";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			pstmt.setString(2, buyerId);
			
			pstmt.executeUpdate();
			
		
			conn.commit();
		} catch (SQLException e) {
			DBUtil.rollback(conn);
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
			}
		}
	}
	
	// 게시물의 댓글 및 답글 추가
		public void insertReply(PReplyDTO dto) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			try {
				sql = "INSERT INTO pReply(replyNum, pnum, userId, replycontent, answer, reply_reg_date) "
						+ " VALUES (PREPLY_SEQ.NEXTVAL, ?, ?, ?, ?, SYSDATE)";
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
						+ " FROM pReply "
						+ " WHERE pnum = ? AND answer = 0";
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
		public List<PReplyDTO> listReply(long num, int offset, int size) {
			List<PReplyDTO> list = new ArrayList<>();
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {
				sb.append(" SELECT r.replyNum, r.userId, userName, pnum, replycontent, reply_reg_date, ");
				sb.append("     NVL(answerCount, 0) answerCount ");
				sb.append(" FROM pReply r ");
				sb.append(" JOIN member m ON r.userId = m.userId ");
				sb.append(" LEFT OUTER  JOIN (");
				sb.append("	    SELECT answer, COUNT(*) answerCount ");
				sb.append("     FROM pReply ");
				sb.append("     WHERE answer != 0 ");
				sb.append("     GROUP BY answer ");
				sb.append(" ) a ON r.replyNum = a.answer ");
				sb.append(" WHERE pnum = ? AND r.answer=0 ");
				sb.append(" ORDER BY r.replyNum DESC ");
				sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				
				pstmt.setLong(1, num);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, size);

				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					PReplyDTO dto = new PReplyDTO();
					
					dto.setReplyNum(rs.getLong("replyNum"));
					dto.setNum(rs.getLong("pnum"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setContent(rs.getString("replycontent"));
					dto.setReg_date(rs.getString("reply_reg_date"));
					dto.setAnswerCount(rs.getInt("answerCount"));
					
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

		public PReplyDTO findByReplyId(long replyNum) {
			PReplyDTO dto = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT replyNum, pnum, r.userId, userName, replycontent, r.reply_reg_date "
						+ " FROM pReply r  "
						+ " JOIN member m ON r.userId = m.userId  "
						+ " WHERE replyNum = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, replyNum);

				rs=pstmt.executeQuery();
				
				if(rs.next()) {
					dto=new PReplyDTO();
					
					dto.setReplyNum(rs.getLong("replyNum"));
					dto.setNum(rs.getLong("pnum"));
					dto.setUserId(rs.getString("userId"));
					dto.setUserName(rs.getString("userName"));
					dto.setContent(rs.getString("replycontent"));
					dto.setReg_date(rs.getString("reply_reg_date"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return dto;
		}
		public String findReplyId(long replyNum) {
			String b = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql;
			
			try {
				sql = "SELECT r.USERID "
						+ " FROM pReply r  "
						+ " JOIN member m ON r.userId = m.userId  "
						+ " WHERE replyNum = ? ";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setLong(1, replyNum);
				
				rs=pstmt.executeQuery();
				
				if(rs.next()) {
					
					b=rs.getString("userId");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			return b;
		}
	
	
		// 게시물의 댓글 삭제
		public void deleteReply(long replyNum, String userId) throws SQLException {
			PreparedStatement pstmt = null;
			String sql;
			
			if(! userId.equals("admin")) {
				PReplyDTO dto = findByReplyId(replyNum);
				if(dto == null || (! userId.equals(dto.getUserId()))) {
					return;
				}
			}
			
			try {
				sql = "DELETE FROM pReply "
						+ " WHERE replyNum IN  "
						+ " (SELECT replyNum FROM pReply START WITH replyNum = ?"
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
	public List<PReplyDTO> listReplyAnswer(long answer) {
		List<PReplyDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb=new StringBuilder();
		
		try {
			sb.append(" SELECT replyNum, pnum, r.userId, userName, replycontent, reply_reg_date, answer ");
			sb.append(" FROM pReply r ");
			sb.append(" JOIN member m ON r.userId = m.userId ");
			sb.append(" WHERE answer = ? ");
			sb.append(" ORDER BY replyNum  ");
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setLong(1, answer);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				PReplyDTO dto=new PReplyDTO();
				
				dto.setReplyNum(rs.getLong("replyNum"));
				dto.setNum(rs.getLong("pnum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setContent(rs.getString("replycontent"));
				dto.setReg_date(rs.getString("reply_reg_date"));
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
		public int dataCountReplyAnswer(long answer) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) "
					+ " FROM pReply WHERE answer = ?";
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

		public String writerId(long num) throws SQLException {
			
			PreparedStatement pstmt=null;
			ResultSet	rs =null;
			String sql;
			String id="";
			
			try {
				sql="select pbbs.userid id from pbbs join member 	on pbbs.userid=member.userid  where pnum=?";
				pstmt=conn.prepareStatement(sql);
				pstmt.setLong(1, num);
				
				rs=pstmt.executeQuery();
				
				if(rs.next()) {
					id=rs.getString("id");
					
				}
				
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
				throw e;
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			
			return id;
			
		}
		public String saleChk(long num) throws SQLException {
			
			PreparedStatement pstmt=null;
			ResultSet	rs =null;
			String sql;
			String id="";
			
			try {
				sql="select pstate from pbbs join member 	on pbbs.userid=member.userid  where pnum=?";
				pstmt=conn.prepareStatement(sql);
				pstmt.setLong(1, num);
				
				rs=pstmt.executeQuery();
				
				if(rs.next()) {
					id=rs.getString("pstate");
					
				}
				
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
				throw e;
			}finally {
				DBUtil.close(rs);
				DBUtil.close(pstmt);
			}
			
			
			return id;
			
		}
	
	
	
	
	
	
	
	
	
	
	
		public String chkReply(long num,String id) throws SQLException {
			String b=null;
			PreparedStatement pstmt =null;
			String sql;
			ResultSet rs = null;
			try {
				sql="select pstate what from wish_lists w join pbbs p on w.pnum=p.pnum where w.userid=? and p.pnum=?";
				pstmt=conn.prepareStatement(sql);
				
				pstmt.setString(1, id);
				pstmt.setLong(2, num);
				
				rs=pstmt.executeQuery();//해당안되면 영향받은값이 0이 나올것이라 기대됨
				//b=pstmt.executeUpdate();//해당안되면 영향받은값이 0이 나올것이라 기대됨
				//0이라는 숫자를반환하는게 무조건 1로나와서 실패함
				if(rs.next()) {
					b=rs.getString("what");
				}
				
				
			} catch (SQLException e) {
				// TODO: handle exception
				e.printStackTrace();
				throw e;
				
			}finally {
				DBUtil.close(pstmt);
				DBUtil.close(rs);
			}
			
			return b;
			
		}
	
}
