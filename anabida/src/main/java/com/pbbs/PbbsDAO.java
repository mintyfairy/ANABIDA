package com.pbbs;

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
				
				for (int i = 1; i < dto.getImageFiles().length; i++) {
					//1부터시작하는 이유는 이미 섬네일용으로 앞에서 0을 사용함
					pstmt.setLong(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					
					pstmt.executeUpdate();
				}
			}
			
			
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
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
			sql="update  pbbs set subject=?,content=?,IMAGEFILENAMe=? where pnum=? ";
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
				
				for (int i = 1; i < dto.getImageFiles().length; i++) {
					//1부터시작하는 이유는 이미 섬네일용으로 앞에서 0을 사용함
					pstmt.setLong(1, dto.getNum());
					pstmt.setString(2, dto.getImageFiles()[i]);
					
					pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
		}
	}
	public void deletePhoto(long	num  ) throws SQLException {
		PreparedStatement pstmt=null;
		String sql;
		try {
			sql="delete from  pbbs  where pnum=?";
			pstmt= conn.prepareStatement(sql);
			
			pstmt.setLong(1,num);
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
			DBUtil.close(pstmt);
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
	
	public long doILikeThis(long num,String id) throws SQLException {
		long b=0;
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
				b=rs.getLong("what");
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
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally {
			DBUtil.close(pstmt);
		}
	}

	public void imSoso(long num, String id)throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement pstmt =null;
		String sql;
		try {
			sql="delete from wish_lists where pnum=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
			DBUtil.close(pstmt);
			sql="update pbbs  set plike=plike-1 where pnum=?";
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setLong(1, num);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
			
		}finally {
			DBUtil.close(pstmt);
		}
	}
}
