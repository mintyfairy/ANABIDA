package com.cbbs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;

@WebServlet("/cbbs/*")
public class CommunityServlet extends MyServlet{

	
	private static final long serialVersionUID = 1L;

	@Override
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// 세션에 저장된 로그인 정보
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		// 로그인이 안된경우
		if (info == null) {
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		
		// uri에 따른 작업 구분
		String uri = req.getRequestURI();
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
		}
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 리스트
	}
	
	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 쓰기 폼
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/views/cbbs/write.jsp");
		
	}
	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 등록
		CommunityDAO dao = new CommunityDAO();
		HttpSession session = req.getSession();
		
		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/");
			return;
		}
		
		try {
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			if(info == null) {
				resp.sendRedirect(cp+"/member/login.do");
			}
			
			CommunityDTO dto = new CommunityDTO();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 보기
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 수정
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 완료
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 게시글 삭제
	}
	protected void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 커뮤니티 댓글리스트
	}
	
	
	
	
}
