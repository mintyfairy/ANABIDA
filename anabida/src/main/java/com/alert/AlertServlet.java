package com.alert;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/alert/*")
public class AlertServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;


	@Override
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();
		String cp = req.getContextPath();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (uri.indexOf("list.do") == -1 && info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		// 저장할 경로
		String root = session.getServletContext().getRealPath("/");
		String pathname = root + "uploads" + File.separator + "alert";

		if (uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if (uri.indexOf("write.do") != -1) {
			writeForm(req, resp);
		} else if (uri.indexOf("write_ok.do") != -1) {
			writeSubmit(req, resp);
		} else if (uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if (uri.indexOf("download.do") != -1) {
			download(req, resp);
		} 
	}

	

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		AlertDAO dao = new AlertDAO();
		MyUtil util = new MyUtil();

		String cp = req.getContextPath();

		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}

			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			if (req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}

			int dataCount;
			if (kwd.length() == 0) {
				dataCount = dao.dataCount();
			} else {
				dataCount = dao.dataCount(schType, kwd);
			}

			// 사이즈는 안넣었고 -> 걍 넣음
			int size = 10;
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 게시뮬만 일단 가져옴
			int offset = (current_page - 1)*size;
			if (offset < 0) offset = 0;

			List<AlertDTO> list = null;
			if (kwd.length() == 0) {
				list = dao.listAlert(offset, size);
			} else {
				list = dao.listAlert(offset, size, schType, kwd);  
			}

			String query = "";
			if (kwd.length() != 0) {
				query = "schType" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			// 페이징처리
			String listUrl = cp + "/alert/list.do";
			String articleUrl = cp + "/alert/article.do?page=" + current_page;
			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}

			String paging = util.paging(current_page, total_page, listUrl);

			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("size", size);
			req.setAttribute("atrticleUrl", articleUrl);
			req.setAttribute("paging", paging);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		forward(req, resp, "/WEB-INF/views/alert/list.jsp");
	}

	

	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//여기짜고
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();

		if (!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/alert/list.do");
			return;
		}
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/views/alert/write.jsp");

	}

	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//여기짜기
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();

		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/alert/list.do");
			return;
		}

		if (!info.getUserId().equals("admin")) { // admin만 글 저장 가능
			resp.sendRedirect(cp + "/alert/list.do");
			return;
		}
		AlertDAO dao = new AlertDAO();

		try {
			AlertDTO dto = new AlertDTO();

			dto.setUserId(info.getUserId());
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));

			dao.insertArlert(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/alert/list.do");
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AlertDAO dao = new AlertDAO();
		MyUtil util = new MyUtil();
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		String query = "page=" + page;

		try {
			long alertNum = Long.parseLong(req.getParameter("alertNum"));
			
	
			// 조회수 증가 잇어야함
			dao.updateHitCount(alertNum);

			// 게시물 가져오기 넣어햐함
			AlertDTO dto = dao.findById(alertNum);
			if (dto == null) {
				resp.sendRedirect(cp + "/alert/list.do?" + query);
				return;
			}
			dto.setContent(util.htmlSymbols(dto.getContent()));

			// 이전글 다음글  alertDAO에 안만들어서 넘김
			AlertDTO prevDto = dao.findByPrev(dto.getAlertNum());
			AlertDTO nextDto = dao.findByNext(dto.getAlertNum());
			

			// JSP로 전달할 속성
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("prevDto", prevDto);
			req.setAttribute("nextDto", nextDto);

			// 포워딩
			forward(req, resp, "/WEB-INF/views/alert/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/alert/list.do?" + query);
	}
		
		

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AlertDAO dao = new AlertDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		
		String page = req.getParameter("page");

		try {
			long alertNum = Long.parseLong(req.getParameter("alertNum"));
			AlertDTO dto = dao.findById(alertNum);

			if (dto == null) {
				resp.sendRedirect(cp + "/alert/list.do?page=" + page);
				return;
			}

			
			if (! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp + "/alert/list.do?page=" + page);
				return;
			}

			req.setAttribute("dto", dto); 
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/views/alert/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/alert/list.do?page=" + page);
	}


	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AlertDAO dao = new AlertDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/alert/list.do");
			return;
		}
		
		String page = req.getParameter("page");
		
		try {
			AlertDTO dto = new AlertDTO();
			
			dto.setAlertNum(Long.parseLong(req.getParameter("alertNum")));
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			dto.setUserId(info.getUserId());
			dao.updateAlert(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/alert/list.do?page=" + page);
	}


	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		AlertDAO dao = new AlertDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			long alertNum = Long.parseLong(req.getParameter("alertNum"));
			
			AlertDTO dto = dao.findById(alertNum);
			
			if(dto == null) {
				resp.sendRedirect(cp + "/alert/list.do?" + query);
				return;
			}
			
	
			if(! dto.getUserId().equals(info.getUserId()) && ! info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/alert/list.do?" + query);
				return;
			}

			dao.deleteAlert(alertNum);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/alert/list.do?" + query);
	}
		



	protected void download(HttpServletRequest req, HttpServletResponse resp) {

	}

}
