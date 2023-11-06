package com.join;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.member.MemberDAO;
import com.member.MemberDTO;
import com.member.SessionInfo;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@WebServlet("/join/*")
@MultipartConfig
public class JoinServlet extends MyUploadServlet{
	private static final long serialVersionUID = 1L;
	private String pathname;
	
	@Override
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		// 세션에 저장된 로그인 정보
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		// 로그인이 안된경우
		if (info == null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		} 
	
		
		// 이미지 저장 경로
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "join";
		
		
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if(uri.indexOf("write.do") != -1) {
			// 글쓰기
			writeForm(req, resp);
		} else if(uri.indexOf("write_ok.do") != -1) {
			// 글 등록
			writeSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			// 글보기
			article(req, resp);
		} else if(uri.indexOf("update.do")!= -1) {
			// 글 수정
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!= -1) {
			// 글 수정 완료
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do")!= -1) {
			// 글 삭제
			delete(req, resp);
		} else if(uri.indexOf("join.do")!=-1) {
			joinForm(req, resp);
		} else if(uri.indexOf("join_ok.do")!=-1) {
			JoinSubmit(req, resp);
		}
	}
	
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기	
		JoinDAO dao = new JoinDAO();
		MyUtil util = new MyUtil();
		
		String cp = req.getContextPath();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			
			if(page!=null ) {
				current_page = Integer.parseInt(page);
			}
			
			// 검색
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType==null) {
				schType = "all";
				kwd ="";
			}
			
			// GET 방식인 경우 디코딩
			if(req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}
			
			// 전체 데이터 개수
			int dataCount;
			if(kwd.length()==0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(schType, kwd);
			}
			
			// 전체 페이지 수 
			int size = 5;
			int total_page = util.pageCount(dataCount, size);
			if(current_page>total_page){
				current_page = total_page;
			}
			
			// 게시물 가져오기
			int offset = (current_page -1) * size;
			if(offset<0) offset = 0;
			
			List<JoinDTO> list = null;
			if(kwd.length()==0) {
				list = dao.list(offset, size);
			} else {
				list = dao.list(offset, size, schType, kwd);
			}
			
			String query = "";
			if(kwd.length()!=0) {
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}
			
			// 페이징 처리
			String listUrl = cp + "/join/list.do";
			String articleUrl = cp + "/join/article.do?page=" + current_page;
			
			if(query.length()!=0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}
			
			String paging = util.paging(current_page, total_page, listUrl);
			
			// 포워딩할 jsp에 전달할 속성
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
		
		forward(req, resp, "/WEB-INF/views/join/list.jsp");
	}
	
	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "write");		
		forward(req, resp, "/WEB-INF/views/join/write.jsp");
	}
	
	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JoinDAO dao = new JoinDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/join/list.do");
			return;
		}
		
		try {
			JoinDTO dto = new JoinDTO();
			
			//userId는 세션에 저장된 정보이므로
			dto.setUserId(info.getUserId());
			// dto.setUserId(req.getParameter("userId"));
			
			dto.setTitle(req.getParameter("title"));
			dto.setLink(req.getParameter("link"));
			dto.setContent(req.getParameter("content"));
			dto.setReg_date(req.getParameter("reg_date"));
			dto.setExp_date(req.getParameter("exp_date"));
			dto.setMin_peo(Integer.parseInt(req.getParameter("min_peo")));
			
			String filename;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map!=null) {
				filename = map.get("saveFilename");
				dto.setImageFilename(filename);
			}
			
			dao.insertJoin(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/join/list.do");

	}
	
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JoinDAO dao = new JoinDAO();
		MyUtil util = new MyUtil();
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			long buyNum = Long.parseLong(req.getParameter("buyNum"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType==null) {
				schType = "all";
				kwd ="";
			} 
			kwd = URLDecoder.decode(kwd, "utf-8");
			
			if(kwd.length()!=0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "UTF-8");
			}
			
			// 조회수 증가
			dao.updateHitCount(buyNum);
			
			// 게시물 가져오기
			JoinDTO dto = dao.findById(buyNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/join/list.do?"+query);
				return;
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));
			
			// 이전글 다음글
			JoinDTO prevDto = dao.findByPrev(dto.getBuyNum(), schType, kwd);
			JoinDTO nextDto = dao.findByNext(dto.getBuyNum(), schType, kwd);
			
			// JSP로 전달할 속성
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("prevDto", prevDto);
			req.setAttribute("nextDto", nextDto);
			
			forward(req, resp, "/WEB-INF/views/join/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		resp.sendRedirect(cp + "/join/list.do?" + query);
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JoinDAO dao = new JoinDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		
		
		try {
			long buyNum = Long.parseLong(req.getParameter("buyNum"));
			JoinDTO dto = dao.findById(buyNum);
			
			if(dto==null) {
				resp.sendRedirect(cp + "/join/list.do?page="+page);
				return;
			}
			
			if(!dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/join/list.do?page="+page);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			
			
			forward(req, resp, "/WEB-INF/views/join/write.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/join/list.do?page="+page);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JoinDAO dao = new JoinDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/join/list.do");
			return;
		}
		
		String page = req.getParameter("page");
		try {
			JoinDTO dto = new JoinDTO();
			
			dto.setTitle(req.getParameter("title"));
			dto.setLink(req.getParameter("link"));
			dto.setContent(req.getParameter("content"));
			dto.setReg_date(req.getParameter("reg_date"));
			dto.setExp_date(req.getParameter("exp_date"));
			dto.setMin_peo(Integer.parseInt(req.getParameter("min_peo")));
			dto.setBuyNum(Long.parseLong(req.getParameter("buyNum")));
			
			dto.setUserId(info.getUserId());
			
			
			dao.updateJoin(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/join/list.do?page=" + page);
	}
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JoinDAO dao = new JoinDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			long buyNum = Long.parseLong(req.getParameter("buyNum"));
			
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
			
			dao.deleteBoard(buyNum, info.getUserId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp + "/join/list.do?"+query);
				
	}
	
	protected void joinForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			JoinDAO dao = new JoinDAO();
			MemberDAO mdao = new MemberDAO(); // 
			
			long buyNum = Long.parseLong(req.getParameter("buyNum"));
			JoinDTO dto = dao.findById(buyNum);
			
			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			MemberDTO mdto = mdao.findById(info.getUserId()); 
			
			String page = req.getParameter("page");
		
			req.setAttribute("mdto", mdto);
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		req.setAttribute("mode", "enter");
		forward(req, resp, "/WEB-INF/views/join/joinwrite.jsp");
	}
	
	
	protected void JoinSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JoinDAO dao = new JoinDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		// 참여자 수 증가
		dao.enterCount();
		
		/*
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/join/list.do");
			return;
		}
		*/
		
		// req.setAttribute("mode", "enter");
		
		try {
			JoinDTO dto = new JoinDTO();
			
			dto.setUserId(info.getUserId());
			dto.setTitle(req.getParameter("title"));
			dto.setUserName(req.getParameter("userName"));
			dto.setQuantity(Integer.parseInt(req.getParameter("quantity")));
			dto.setSelectEmail(req.getParameter("selectEmail"));
			dto.setEmail1(req.getParameter("email1"));
			dto.setEmail2(req.getParameter("email2"));
			dto.setTel1(req.getParameter("tel1"));
			dto.setTel2(req.getParameter("tel2"));
			dto.setTel3(req.getParameter("tel3"));
			
			
			dto.setZip(req.getParameter("zip"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			
			dto.setBuyNum(Long.parseLong(req.getParameter("buyNum")));
			
			dao.insertJoinMember(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		resp.sendRedirect(cp+"/join/list.do");
	}
	
}
