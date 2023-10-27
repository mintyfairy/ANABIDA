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
		String sql;
		try {
			sql="insert into pbbs(pnum,userid,subject,content,IMAGEFILENAME,cost,catnum) values(pbbs_seq.nextval,?,?,?,?,?,?)";
			
			pstmt=conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getImageFilename());
			pstmt.setLong(5, dto.getCost());
			pstmt.setLong(6, dto.getCatNum());
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}finally {
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
	public void updatePhoto(PbbsDTO dto ) throws SQLException {
		PreparedStatement pstmt=null;
		String sql;
		try {
			sql="update  pbbs set subject=?,content=?,IMAGEFILENAMe=? where pnum=? and userid=?";
			pstmt= conn.prepareStatement(sql);
			
			pstmt.setString(1,	 dto.getSubject());
			pstmt.setString(2,	 dto.getContent());
			pstmt.setString(3,	 dto.getImageFilename());
			pstmt.setLong(4,	 dto.getNum());
			pstmt.setString(5,	 dto.getUserId());
			
			pstmt.executeUpdate();
			
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
			System.out.println("오ㅘㅅ니");
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
}
