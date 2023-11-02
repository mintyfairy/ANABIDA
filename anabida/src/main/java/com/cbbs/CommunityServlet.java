package com.cbbs;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.member.SessionInfo;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/cbbs/*")
public class CommunityServlet extends MyUploadServlet{

	
	private static final long serialVersionUID = 1L;

	private String pathname;
	
	@Override
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 세션에 저장된 로그인 정보
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		// 로그인이 안된경우
		if (info == null) { // 로그인되지 않은 경우
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		
		// 이미지를 저장할 경로(pathname)
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "cbbs";
		
		// uri에 따른 작업 구분
		if(uri.indexOf("clist.do")!=-1) {
			list(req, resp);
		}else if(uri.indexOf("cwrite.do")!=-1) {
			writeForm(req, resp);
		}else if(uri.indexOf("cwrite_ok.do")!=-1) {
			writeSubmit(req, resp);
		}else if(uri.indexOf("carticle.do")!=-1) {
			article(req, resp);
		}else if(uri.indexOf("cupdate.do")!=-1) {
			updateForm(req, resp);
		}else if(uri.indexOf("cupdate_ok.do")!=-1) {
			updateSubmit(req, resp);
		}else if(uri.indexOf("cdelete.do")!=-1) {
			delete(req, resp);
		}else if(uri.indexOf("participate.do")!=-1) {
			participate(req, resp);
		}else if(uri.indexOf("insertBoardLike.do")!=-1) {
			insertBoardLike(req, resp);
		}else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} 
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CommunityDAO dao = new CommunityDAO();
		MyUtil util = new MyUtil();
		// 커뮤니티 게시글 리스트
		String cp = req.getContextPath();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			// 검색
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			
			// GET 방식인 경우 디코딩
			if (req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}
			
			// 전체 데이터 개수
			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(schType, kwd);
			}
			
			// 전체 페이지 수
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}
			
			// 게시물 가져오기
			int offset = (current_page - 1) * size;
			if (offset < 0) offset = 0;
			
			List<CommunityDTO> list = null;
			
			if (kwd.length() == 0) {
				list = dao.listCommunity(offset, size);
			} else {
				list = dao.listCommunity(offset, size, schType, kwd);
			}
			
			String query = "";
			if (kwd.length() != 0) {
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			
			// 페이징 처리
			String listUrl = cp + "/cbbs/clist.do";
			String articleUrl = cp + "/cbbs/carticle.do?page=" + current_page;
			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩할 JSP에 전달할 속성
			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("size", size);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("paging", paging);
			req.setAttribute("schType", schType);
			req.setAttribute("kwd", kwd);
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		forward(req, resp, "/WEB-INF/views/cbbs/list.jsp");

		
	}
	
	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 쓰기 폼
		req.setAttribute("mode", "cwrite");
		String ccategory = req.getParameter("ccategory");
		req.setAttribute("ccategory", ccategory);
		String cp = req.getContextPath();
		
		if(ccategory.equals("2") || ccategory.equals("3")|| ccategory.equals("4")|| ccategory.equals("5") ) {
			req.setAttribute("ccategory", ccategory);
			forward(req, resp, "/WEB-INF/views/cbbs/write.jsp");
		} else if(ccategory.equals("1")) {
			req.setAttribute("ccategory", ccategory);
			req.setAttribute("toge", "toge");
			forward(req, resp, "/WEB-INF/views/cbbs/write.jsp");
		} else {
			resp.sendRedirect(cp+"/cbbs/clist.do");
		}
		
	}
	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 등록
		CommunityDAO dao = new CommunityDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/cbbs/list.do");
			return;
		}
		
		try {
			CommunityDTO dto = new CommunityDTO();
			dto.setUserId(info.getUserId());
			dto.setCcontent(req.getParameter("ccontent"));
			dto.setCtitle(req.getParameter("ctitle"));
			dto.setCcategory(req.getParameter("ccategory"));
			
			if(dto.getCcategory().equals("1")) {
				dto.setMreg_date(req.getParameter("mreg_date"));
				dto.setZip(req.getParameter("zip"));
				dto.setAddr1(req.getParameter("addr1"));
				dto.setAddr2(req.getParameter("addr2"));
				dto.setCmember(Integer.parseInt( req.getParameter("mcount")));
			}
			
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if (map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setPicFileNames(saveFiles);
			}
			
			if(dto.getCcategory().equals("1")) {
				dao.insertMeet(dto);
			}else{
				dao.insertCbbs(dto);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/cbbs/clist.do");
	}
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 보기
		CommunityDAO dao = new CommunityDAO();
		MyUtil util = new MyUtil();
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query ="page=" + page;
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}
			
			// 조회수 증가
			dao.updateHitCount(num);
			
			// 게시물 가져오기
			CommunityDTO dto = dao.findById(num);
			if (dto == null) { // 게시물이 없으면 다시 리스트로
				resp.sendRedirect(cp + "/cbbs/clist.do?" + query);
				return;
			}
			dto.setCcontent(util.htmlSymbols(dto.getCcontent()));
			
			
			// 모임테이블 가져오기
			CommunityDTO meet = dao.findMeet(num);
			
			/*
			 * if (meet == null) { // 게시물이 없으면 다시 리스트로 resp.sendRedirect(cp +
			 * "/cbbs/clist.do?" + query); return; 
			 * }
			 */
			
			if(meet != null) {
				int meetmember = dao.findMeetmember(meet.getMnum());
				List<CommunityDTO> username = dao.findMeetUsername(meet.getMnum());
				req.setAttribute("meetmember", meetmember);
				req.setAttribute("username", username);
			}
			
			List<CommunityDTO> listFile = dao.listPhotoFile(num);
			
			int cdataCount = dao.cdataCount(dto.getNum());
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			boolean isUserLike = dao.isUserBoardLike(num, info.getUserId());
			
			req.setAttribute("meet", meet);	
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("listFile", listFile);
			req.setAttribute("cdataCount", cdataCount);
			req.setAttribute("isUserLike", isUserLike);
			
			forward(req, resp, "/WEB-INF/views/cbbs/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 수정
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 완료
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 삭제
		CommunityDAO dao = new CommunityDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page=" + page;
		
		try {
			long num = Long.parseLong(req.getParameter("num"));
			
			CommunityDTO dto = dao.findById(num);
			if (dto == null) {
				resp.sendRedirect(cp + "/cbbs/clist.do?" + query);
				return;
			}
			
			// 게시물을 올린 사용자가 아니면
			if (!dto.getUserId().equals(info.getUserId())) {
				viewPage(req, resp, "redirect:/cbbs/clist.do?page=" + page);
				return;
			}
			List<CommunityDTO> listFile = dao.listPhotoFile(num);
			for (CommunityDTO vo : listFile) {
				FileManager.doFiledelete(pathname, vo.getPicFileName());
			}
			dao.deletePhotoFile("all", num);
			
			dao.deleteCbbs(num, info.getUserId());
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/cbbs/clist.do?" + query);
	}
	
	protected void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 댓글리스트
	}
	
	
	protected void participate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 참여하기 버튼
		CommunityDAO dao = new CommunityDAO();
				

		String cp = req.getContextPath();
			if (req.getMethod().equalsIgnoreCase("GET")) {
					resp.sendRedirect(cp + "/cbbs/list.do");
					return;
		}
		
		try {
			CommunityDTO dto = new CommunityDTO();
			dto.setUserId(req.getParameter("userId"));
			dto.setMnum(Integer.parseInt(req.getParameter("mnum")));
			
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			
			long num = dto.getNum();
			String page = req.getParameter("page");		
			
			
			int result = dao.selectmember(dto.getMnum(),dto.getUserId());
			System.out.println("result : "+ result);
			
			if(result == 1 ) {
				resp.sendRedirect(cp + "/cbbs/carticle.do?num="+num+"&page="+page);
				return;
			}else {
				dao.insertmeetmember(dto);
				resp.sendRedirect(cp + "/cbbs/carticle.do?num="+num+"&page="+page);
				return;
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 게시물 공감저장 -AJAX
		protected void insertBoardLike(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
			CommunityDAO dao = new CommunityDAO();
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			String state = "false";
			int likeCount = 0;
			try {
				long num = Long.parseLong(req.getParameter("num"));
				String isNoLike = req.getParameter("isNoLike");
				if(isNoLike.equals("true")) {
					dao.insertBoardLike(num, info.getUserId()); // 공감
				} else {
					dao.deleteBoardLike(num, info.getUserId()); // 공감 취소
				}
				
				likeCount = dao.countBoardLike(num);

				state = "true";
			} catch (SQLException e) {
				state = "liked";
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			JSONObject job = new JSONObject();
			job.put("state", state);
			job.put("likeCount", likeCount);

			respJson(resp, job.toString());
			
		}
	
	
	
	
}
