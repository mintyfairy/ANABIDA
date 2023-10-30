package com.alert;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

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

	private String pathname;

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
		pathname = root + "uploads" + File.separator + "alert";

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
		} else if (uri.indexOf("deleteFile.do") != -1) {
			deleteFile(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if (uri.indexOf("download.do") != -1) {
			download(req, resp);
		} else if (uri.indexOf("deleteList.do") != -1) {
			deleteList(req, resp);
		}
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		 /*
		  AlertDAO dao = new AlertDAO();
		  
		 
		MyUtil util = new MyUtil();
		
		String cp = req.getContextPath();
		
		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if(page != null) {
				current_page = Integer.parseInt(page);
			}
			
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");
			if(schType == null) {
				schType = "all";
				kwd = "";
			}
			if(req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd,"utf-8");
			}
			////----------------------------------------
			String pageSize = req.getParameter("size");
			int size = pageSize == null?10:Integer.parseInt(pageSize);
			
			int dataCount, total_page;
			
			if(kwd.length()!=0) {
				dataCount = dao.dataCount(schType, kwd);
			}else {
				dataCount = dao.dataCount( );
			}
			total_page = util.pageCount(dataCount, size);
			
			if(current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<AlertDTO> list;
			if (kwd.length() != 0) {
				list = dao.listAlert(offset, size,schType,kwd);
			} else {
				list = dao.listAlert(offset,size);
			}
			//-----------------공지
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
			
		}*/
		forward(req,resp,"/WEB-INF/views/alert/list.jsp");
		

	}
	

	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//여기짜고
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		String size = req.getParameter("size");
		
		if(!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/alert/list.do?size=" + size);
			return;
		}
		req.setAttribute("mode", "write");
		req.setAttribute("size", size);
		forward(req,resp,"/WEB-INF/views/alert/write.jsp");
		
	}

	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {
//여기짜기
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+ "/alert/list.do");
			return;
		}
		
		if(!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp + "/alert/list.do");
			return;
		}
		AlertDAO dao = new AlertDAO();
		
		String size = req.getParameter("size");
		try {
			AlertDTO dto = new AlertDTO();
			
			dto.setUserId(info.getUserId());
			if(req.getParameter("alert")!=null) {
				dto.setAlert(Integer.parseInt(req.getParameter("alert")));
			}
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] saveFiles = map.get("saveFilenames");
				String[] originalFiles = map.get("originalFilenames");
				dto.setSaveFiles(saveFiles);
				dto.OriginalFiles(originalFiles);
					
			}
			dao.insertArlert(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/alert/list.do?size=" + size);
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {

	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {

	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	protected void deleteFile(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException  {

	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) {

	}

	protected void download(HttpServletRequest req, HttpServletResponse resp) {

	}

	protected void deleteList(HttpServletRequest req, HttpServletResponse resp) {

	}

}
