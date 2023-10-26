package com.pbbs;
import java.io.File;
import java.io.IOException;
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

@WebServlet("/photo/*")
@MultipartConfig
public class PhotoServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;

	private String pathname;

	@Override
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uri = req.getRequestURI();
		String cp = req.getContextPath();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		if (info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}

		// 이미지저장경로
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "photo";

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
		}

	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PhotoDAO dao = new PhotoDAO();
		MyUtil util = new MyUtil();

		String cp = req.getContextPath();

		try {
			String page = req.getParameter("page");
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}
			int datacount = dao.dataCount();

			int size = 8;
			int total_page = util.pageCount(datacount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			int offset = (current_page - 1) * size;
			if (offset < 0)
				offset = 0;

			List<PhotoDTO> list = dao.listPhoto(offset, size);

			String listUrl = cp + "/photo/list.do";
			String articleUrl = cp + "/photo/article.do?page=" + current_page;
			String paging = util.paging(current_page, total_page, listUrl);

			req.setAttribute("list", list);
			req.setAttribute("dataCount", datacount);
			req.setAttribute("size", size);
			req.setAttribute("page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			req.setAttribute("articleUrl", articleUrl);

		} catch (Exception e) {
			// TODO: handle exception
		}

		forward(req, resp, "/WEB-INF/views/photo/list.jsp");

	}

	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/views/photo/write.jsp");

	}

	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PhotoDAO dao = new PhotoDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		if (req.getMethod().equals("GET")) {
			resp.sendRedirect(cp + "/photo/list.do");
			return;

		}

		try {
			PhotoDTO dto = new PhotoDTO();

			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			String filename;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if (map != null) {
				filename = map.get("saveFilename");

				dto.setImageFilename(filename);

			}
			dao.insertPhoto(dto);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/photo/list.do");
		return;

	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PhotoDAO dao = new PhotoDAO();
		String cp = req.getContextPath();

		String page = req.getParameter("page");

		try {
			long num = Long.parseLong(req.getParameter("num"));

			PhotoDTO dto = dao.findById(num);
			if (dto == null) {
				// 게시글이 없다면 다시 리스트로
				resp.sendRedirect(cp + "/photo/list.do?page=" + page);
				return;
			}

			// content의 엔터를 <br>로
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

			// 포워딩할 JSP에 전달할 속성
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);

			// 포워딩
			forward(req, resp, "/WEB-INF/views/photo/article.jsp");
			return;
		} catch (Exception e) {

			// TODO: handle exception
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/photo/list.do?page=" + page);
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글수정폼
		PhotoDAO dao= new PhotoDAO();
		String cp= req.getContextPath();
		
		
		String page=req.getParameter("page");
		
		
		try {
			long num=Long.parseLong(req.getParameter("num"));
			
			//테이블에서 해당 게시글 가져오기
			PhotoDTO dto=dao.findById(num);
			
			// 글없으면
			if(dto==null) {
				 resp.sendRedirect(cp+"/photo/list.do?page="+page);
				 return;
			}
			
			//JSP에 보내기
			
			req.setAttribute("dto", dto);
			req.setAttribute("postImgname", (String)dto.getImageFilename());
			req.setAttribute("page", page);
			
			req.setAttribute("mode", "update");
			forward(req, resp, "/WEB-INF/views/photo/write.jsp");
			return;
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PhotoDAO dao = new PhotoDAO();
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect("/WEB-INF/views/photo/list.jsp");
			return;
		}
		String page = req.getParameter("page");
		System.out.println(req.getParameter("selectFile"));
		try {
			PhotoDTO dto = new PhotoDTO();
			dto.setNum(Long.parseLong(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			String filename=null;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if (map != null) {
				filename = map.get("saveFilename");
				dto.setImageFilename(filename);
			}
			if(filename==null) {
				dto.setImageFilename(req.getParameter("postImgname"));
			}
			System.out.println("보내는 파일이름"+ dto.getImageFilename());
			dto.setUserId(info.getUserId());

			dao.updatePhoto(dto);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/photo/list.do?page=" + page);

	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 삭제
		System.out.println("여기는온거니");
		PhotoDAO dao = new PhotoDAO();
		String cp = req.getContextPath();

		String page = req.getParameter("page");

		try {
			

			
			dao.deletePhoto(Long.parseLong(req.getParameter("num")));

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/photo/list.do?page=" + page);
	}

}
