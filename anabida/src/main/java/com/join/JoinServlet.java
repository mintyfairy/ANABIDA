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
			if(page!=null) {
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
	
	
}
