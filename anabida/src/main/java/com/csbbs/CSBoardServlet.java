package com.csbbs;

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

import com.member.MemberDAO;
import com.member.MemberDTO;
import com.member.SessionInfo;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@WebServlet("/csbbs/*")
@MultipartConfig
public class CSBoardServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;
	
	@Override
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		if (info == null) {
			// 로그인이 안된 경우
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator+"csbbs";
		
		// uri에 따른 작업 구분
		String uri = req.getRequestURI();
		
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
		} else if (uri.indexOf("answer.do") != -1) {
			answerForm(req, resp);
		} else if (uri.indexOf("answer_ok.do") != -1) {
			answerSubmit(req, resp);
		} else if (uri.indexOf("notice.do") !=-1) {
			noticeCheck(req,resp);
		}else if (uri.indexOf("deleteFile.do") != -1) {
			deleteFile(req, resp);
		}
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 리스트

		CSBoardDAO dao = new CSBoardDAO();
		MyUtil util = new MyUtil(); // 페이징 처리

		String cp = req.getContextPath();

		try {
			// 페이지 번호
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
			// GET 방식이면 디코딩
			if (req.getMethod().equalsIgnoreCase("GET")) {
				kwd = URLDecoder.decode(kwd, "utf-8");
			}

			// 전체 데이터 개수
			int dataCount;

			if (kwd.length() == 0) {
				// 검색이 아닌 경우
				dataCount = dao.dataCount();
			} else {
				// 검색인 경우
				dataCount = dao.dataCount(schType, kwd);
			}

			// 전체 페이지 수
			int size = 10; // 한 페이지에 출력할 몰록 개수
			int total_page = util.pageCount(dataCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 게시글 가져오기
			int offset = (current_page - 1) * size; // 3페이지 가져오려면 20개 건너 뛰기
			if (offset < 0)
				offset = 0;

			List<CSBoardDTO> list = null;

			if (kwd.length() == 0) {
				// 검색이 아닐 때
				list = dao.listBoard(offset, size);
			} else {
				// 검색일 때
				list = dao.listBoard(offset, size, schType, kwd);
			}
			String query = "";
			if (kwd.length() != 0) { // 검색 상태이면
				query = "schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			// 페이징 처리
			String listUrl = cp + "/csbbs/list.do"; // 글리스트 주소
			String articleUrl = cp + "/csbbs/article.do?page=" + current_page;
			if (query.length() != 0) {
				listUrl += "?" + query;
				articleUrl += "&" + query;
			}

			String paging = util.paging(current_page, total_page, listUrl);

			// 포워딩할 JSP에 전달한 속성(attribute)
			req.setAttribute("list", list);
			req.setAttribute("page", current_page);
			req.setAttribute("dataCount", dataCount);
			req.setAttribute("size", size);
			req.setAttribute("total_page", total_page);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("paging", paging);
			req.setAttribute("schType", schType);
			req.setAttribute("kwd", kwd);

		} catch (Exception e) {
		}
		// list.jsp 로 포워딩
		forward(req, resp, "/WEB-INF/views/csbbs/list.jsp");
	}

	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글씩 폼
		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/views/csbbs/write.jsp");

	}

	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 등록
		CSBoardDAO dao = new CSBoardDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) { // GET 방식은 막음
			resp.sendRedirect(cp + "/csbbs/list.do");
			return;
		}

		try {

			CSBoardDTO dto = new CSBoardDTO();

			dto.setUserId(info.getUserId());
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			dto.setAnswerUserId(info.getUserId());
			dto.setAnswerUserId2(info.getUserId());
			
			
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String[] saveFiles= map.get("saveFilenames");
				dto.setImgfiles(saveFiles);
			}
			
			dao.insertBoard(dto, "write");

		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/csbbs/list.do");

	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		CSBoardDAO dao = new CSBoardDAO();
		MyUtil util = new MyUtil();

		String cp = req.getContextPath();
		String page = req.getParameter("page");

		String query = "page=" + page;

		try {
			long qnum = Long.parseLong(req.getParameter("qnum"));
			String schType = req.getParameter("schType");
			String kwd = req.getParameter("kwd");

			if (schType == null) {
				schType = "all";
				kwd = "";
			}
			kwd = URLDecoder.decode(kwd, "utf-8");

			if (kwd.length() != 0) {
				query += "&schType=" + schType + "&kwd=" + URLEncoder.encode(kwd, "utf-8");
			}

			dao.updateHitcount(qnum);

			CSBoardDTO dto = dao.findById(qnum);
			
			if (dto == null) {
				// 게시글 없으면
				
				resp.sendRedirect(cp + "/csbbs/list.do?" + query);
				return;
			}
			
			// content의 엔터를 <br>로
			//dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			dto.setContent(util.htmlSymbols(dto.getContent()));
			// 이전글, 다음글
			CSBoardDTO prevDto = dao.findByPrevBoard(dto.getGroupNum(), dto.getOrderNo(), schType, kwd);
			CSBoardDTO nextDto = dao.findByNextBoard(dto.getGroupNum(), dto.getOrderNo(), schType, kwd);
		
			List<CSBoardDTO> listFile = dao.listPhotoFile(qnum);
			
			req.setAttribute("dto", dto);
			req.setAttribute("prevDto", prevDto);
			req.setAttribute("nextDto", nextDto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			req.setAttribute("query", query);

			// 포워딩
			forward(req, resp, "/WEB-INF/views/csbbs/article.jsp");
			return;

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 예외 발생시 list.do로
		resp.sendRedirect(cp + "/csbbs/list.do?" + query);

	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 수정 폼
		CSBoardDAO dao = new CSBoardDAO();
		HttpSession session = req.getSession();
		String cp = req.getContextPath();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String page = req.getParameter("page");

		try {
			long qnum = Long.parseLong(req.getParameter("qnum"));

			CSBoardDTO dto = dao.findById(qnum);
			// System.out.println(dto.getUserId());
			// System.out.println(info.getUserId());
			if (dto == null) {
				resp.sendRedirect(cp + "/csbbs/list.do?page=" + page);
				return;
			}
			// System.out.println(dto.getUserId().equals(info.getUserId()));
			if (!dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp + "/csbbs/list.do?page=" + page);
				return;
			}
			List<CSBoardDTO> listFile = dao.listPhotoFile(qnum);
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("listFile", listFile);
			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/views/csbbs/write.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/csbbs/list.do?=page" + page);

	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 수정 완료
		CSBoardDAO dao = new CSBoardDAO();
		

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/csbbs/list.do");
			return;
		}
		String page = req.getParameter("page");

		try {
			CSBoardDTO dto = new CSBoardDTO();
			
			dto.setQnum(Long.parseLong(req.getParameter("qnum")));
			dto.setUserId(info.getUserId());
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
	
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if (map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImgfiles(saveFiles);
			}
			
			dao.updateBoard(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/csbbs/list.do?page=" + page);

	}

	protected void answerForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CSBoardDAO dao = new CSBoardDAO();
		String cp = req.getContextPath();

		String page = req.getParameter("page");
		String ss = req.getParameter("answerUserId");
		String sss = req.getParameter("answerUserId2");
		try {

			long qnum = Long.parseLong(req.getParameter("qnum"));
			CSBoardDTO dto = dao.findById(qnum);
			if (dto == null) {
				resp.sendRedirect(cp + "/csbbs/list.do?page=" + page);
				return;
			}
			dto.setAnswerUserId(ss);
			dto.setAnswerUserId2(sss);
			
			String s = "[" + dto.getTitle() + "] 에 대한 답변입니다.\n";
			dto.setContent(s);
			List<CSBoardDTO> listFile = dao.listPhotoFile(qnum);
			req.setAttribute("mode", "answer");
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);

			forward(req, resp, "/WEB-INF/views/csbbs/write.jsp");
			return;
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/csbbs/list.do?=page" + page);

	}

	protected void answerSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CSBoardDAO dao = new CSBoardDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		String cp = req.getContextPath();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/csbbs/list.do");
			return;
		}

		String page = req.getParameter("page");
		int ay = Integer.parseInt(req.getParameter("answeryes"));
		
		if(ay != 1) {
			resp.sendRedirect(cp+"/cbbs/list.do");
		}
		
		try {
			CSBoardDTO dto = new CSBoardDTO();
			dto.setTitle(req.getParameter("title"));
			dto.setContent(req.getParameter("content"));
			
			dto.setAnswerUserId(req.getParameter("answerUserId"));
			dto.setAnswerUserId(req.getParameter("answerUserId2"));
			dto.setGroupNum(Long.parseLong(req.getParameter("groupNum")));
			dto.setOrderNo(Integer.parseInt(req.getParameter("orderNo")));
			dto.setDepth(Integer.parseInt(req.getParameter("depth")));
			dto.setParent(Long.parseLong(req.getParameter("parent")));
			dto.setAnsweryes(Integer.parseInt(req.getParameter("answeryes")));
			
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if (map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImgfiles(saveFiles);
			}
			
			dto.setUserId(info.getUserId());

			dao.insertBoard(dto, "answer");
			dao.answer(dto, ay);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/csbbs/list.do?page=" + page);
	}
	
	protected void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정에서 파일만 삭제
		CSBoardDAO dao = new CSBoardDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();

		String page = req.getParameter("page");

		try {
			long qnum = Long.parseLong(req.getParameter("qnum"));
			long qanum = Long.parseLong(req.getParameter("qanum"));
			
			CSBoardDTO dto = dao.findById(qnum);

			if (dto == null) {
				resp.sendRedirect(cp + "/csbbs/list.do?page=" + page);
				return;
			}

			if (!info.getUserId().equals(dto.getUserId())) {
				resp.sendRedirect(cp + "/csbbs/list.do?page=" + page);
				return;
			}
			
			CSBoardDTO vo = dao.findByFileId(qanum);
			if(vo != null) {
				FileManager.doFiledelete(pathname, vo.getFilename());
				
				dao.deletePhotoFile("one", qanum);
			}

			resp.sendRedirect(cp + "/csbbs/update.do?qnum=" + qnum + "&page=" + page);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/csbbs/list.do?page=" + page);
	}


	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 삭제
		CSBoardDAO dao = new CSBoardDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			
			
			long qnum = Long.parseLong(req.getParameter("qnum"));
			
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

			CSBoardDTO dto = dao.findById(qnum);
			
			if (dto == null) {
				resp.sendRedirect(cp + "/csbbs/list.do?" + query);
				return;
			}

			// 게시물을 올린 사용자나 admin이 아니면
			if (!dto.getUserId().equals(info.getUserId()) && !info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/csbbs/list.do?" + query);
				return;
			}
			FileManager.doFiledelete(pathname, dto.getFilename());

			
			
			
			dao.deleteBoard(qnum);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/csbbs/list.do?" + query);
	}
	
	protected void noticeCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		MemberDAO dao = new MemberDAO();
		HttpSession session = req.getSession();
		
		String cp = req.getContextPath();

		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp + "/");
			return;
		}
		
		try {
			
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			if (info == null) { // 로그아웃 된 경우
				resp.sendRedirect(cp + "/member/login.do");
				return;
			}
			MemberDTO dto = dao.findById(info.getUserId());
			if (dto == null) {
				session.invalidate();
				resp.sendRedirect(cp + "/");
				return;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
