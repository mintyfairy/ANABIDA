package com.join;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.csbbs.CSBoardDAO;
import com.member.SessionInfo;
import com.util.MyServlet;

@WebServlet("/join/*")
public class JoinServlet extends MyServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();
		
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
			
			// userId는 세션에 저장된 정보이므로
			dto.setUserId(info.getUserId());
			
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			dto.setMin_peo(Integer.parseInt(req.getParameter("min_peo")));
			
			dao.insertJoin(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/join/list.do");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	
}
