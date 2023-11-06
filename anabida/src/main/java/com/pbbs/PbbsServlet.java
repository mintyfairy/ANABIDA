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

import org.json.JSONObject;

import com.member.SessionInfo;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@WebServlet("/pbbs/*")
@MultipartConfig
public class PbbsServlet extends MyUploadServlet {
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
		pathname = root + "uploads" + File.separator + "pbbs";

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
		} else if (uri.indexOf("deleteFile") != -1) {
			deleteFile(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if (uri.indexOf("like.do") != -1) {
			like(req, resp);
		} else if (uri.indexOf("choose.do") != -1) {
			choose(req, resp);
		} else if (uri.indexOf("insertReply.do") != -1) {
			insertReply(req, resp);
		} else if (uri.indexOf("listReply.do") != -1) {
			listReply(req, resp);
		} else if (uri.indexOf("deleteReply.do") != -1) {
			deleteReply(req, resp);
		} else if (uri.indexOf("insertReplyAnswer.do") != -1) {
			insertReplyAnswer(req, resp);
		} else if (uri.indexOf("listReplyAnswer.do") != -1) {
			listReplyAnswer(req, resp);
		} else if (uri.indexOf("deleteReplyAnswer.do") != -1) {
			deleteReplyAnswer(req, resp);
		} else if (uri.indexOf("countReplyAnswer.do") != -1) {
			countReplyAnswer(req, resp);
		}
		

	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PbbsDAO dao = new PbbsDAO();
		MyUtil util = new MyUtil();

		String cp = req.getContextPath();

		try {
			String page = req.getParameter("page");
			String cat =req.getParameter("cat");
			String category="";
			if(cat!=null) {
				category="&"+"cat="+cat+"&";
			}
			String ord =req.getParameter("order");
			String order="";
			if(ord!=null) {
				order="&"+"order="+ord+"&";
			}
			if(ord==null) {
				ord="";
			}
			
			
			List<PbbsDTO> list;
			
			int current_page = 1;
			if (page != null) {
				current_page = Integer.parseInt(page);
			}
			int datacount=0;
			if(cat==null) {datacount = dao.dataCount();
			}else {datacount = dao.dataCount(Long.parseLong(cat));
			}
			
			int size = 12;
			int total_page = util.pageCount(datacount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}
			
			int offset = (current_page - 1) * size;
			if (offset < 0)offset = 0;
			if(ord.equals("view")) {
				if(cat==null) 
				{
					list = dao.mostViewListPhoto(offset, size);
				}else {
					list = dao.mostViewListPhoto(offset, size,Long.parseLong(cat));
				}
			}else if (ord.equals("like")) {
				if(cat==null) 
				{
					list = dao.popularListPhoto(offset, size);
				}else {
					list = dao.popularListPhoto(offset, size,Long.parseLong(cat));
				}
			}else {
				if(cat==null) 
				{
					list = dao.listPhoto(offset, size);
				}else {
					list = dao.listPhoto(offset, size,Long.parseLong(cat));
				}
			}
			
			String articleUrl = cp + "/pbbs/article.do?"+category+order  +"page=" + current_page;
			String listUrl = cp + "/pbbs/list.do?"+category+order;
			String paging = util.paging(current_page, total_page, listUrl);

			req.setAttribute("list", list);
			req.setAttribute("dataCount", datacount);
			req.setAttribute("size", size);
			req.setAttribute("page", current_page);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("category", category);
			req.setAttribute("order", order);


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		forward(req, resp, "/WEB-INF/views/pbbs/list.jsp");

	}

	protected void writeForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("mode", "write");
		forward(req, resp, "/WEB-INF/views/pbbs/write.jsp");

	}

	protected void writeSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PbbsDAO dao = new PbbsDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();
		if (req.getMethod().equals("GET")) {
			resp.sendRedirect(cp + "/pbbs/list.do");
			return;

		}

		try {
			PbbsDTO dto = new PbbsDTO();

			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			if(req.getParameter("catNum")!=null) {
				dto.setCatNum(Long.parseLong(req.getParameter("catNum")));
			};
			dto.setCost(Long.parseLong(req.getParameter("cost")));
			/*String filename;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if (map != null) {
				filename = map.get("saveFilename");

				dto.setImageFilename(filename);
			}*///단일파일업로드의 흔적 
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if (map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}//이름들이 배열로저장됏다
			
			dao.insertPhoto(dto);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/pbbs/list.do");


	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PbbsDAO dao = new PbbsDAO();
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");


		String page = req.getParameter("page");
		String cat =req.getParameter("cat");
		String category="";
		if(cat!=null) {
			category="&"+"cat="+cat+"&";
		}
		String ord =req.getParameter("order");
		String order="";
		if(ord!=null) {
			order="&"+"order="+ord+"&";
		}
		if(ord==null) {
			ord="";
		}
		try {
			
			long num = Long.parseLong(req.getParameter("num"));
		//	long num=	62;
			// 조회수 증가
			dao.updateHitCount(num);
			
			PbbsDTO dto = dao.findById(num);
			if (dto == null) {
				// 게시글이 없다면 다시 리스트로
				resp.sendRedirect(cp + "/pbbs/list.do?"+category+order  +"page=" + page);
				return;
			}		


			// content의 엔터를 <br>로 
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));

			//유저가 이게시글을 찜했는지 검사
			String plike=dao.doILikeThis(num,info.getUserId());
			
			//제안을한번만 하게 제한
			String chkReply="0";
			String qq=dao.chkReply(num,info.getUserId());
			if(qq!=null) {
				chkReply=qq;
			}
			dto.setChkReply(chkReply);
			
			//사진
			List<PbbsDTO> listFile = dao.listPhotoFile(num);
			long listSize=listFile.size()+1;
			long listNum=1;
			//숫자인 카케고리구분을 스트링으로
			dto.setCatString(dao.getCat(dto.getCatNum()));
			// 포워딩할 JSP에 전달할 속성
			req.setAttribute("listFile", listFile);
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("category", category);
			req.setAttribute("order", order);
			req.setAttribute("listSize", listSize);
			req.setAttribute("listNum", listNum);
			req.setAttribute("plike", plike);
			req.setAttribute("pstate", dto.getPstate());
			

			// 포워딩
			forward(req, resp, "/WEB-INF/views/pbbs/article.jsp");
			return;
		} catch (Exception e) {

			// TODO: handle exception
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/pbbs/list.do?"+category+order +"page=" + page);
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글수정폼
		PbbsDAO dao= new PbbsDAO();
		String cp= req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		
		String page=req.getParameter("page");
		String cat =req.getParameter("cat");
		String category="";
		if(cat!=null) {
			category="&"+"cat="+cat+"&";
		}
		String ord =req.getParameter("order");
		String order="";
		if(ord!=null) {
			order="&"+"order="+ord+"&";
		}
		if(ord==null) {
			ord="";
		}
		
		try {
			long num=Long.parseLong(req.getParameter("num"));
			
			//테이블에서 해당 게시글 가져오기
			PbbsDTO dto=dao.findById(num);
			// 글없으면
			if(dto==null) {
				 resp.sendRedirect(cp+"/pbbs/list.do?"+category+order  +"page=" + page);
				 return;
			}
			dto.setCatString(dao.getCat(dto.getCatNum()));
			if (!dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp + "/pbbs/list.do?page=" + page);
				return;
			}
			//JSP에 보내기
			List<PbbsDTO> listFile = dao.listPhotoFile(num);
			PbbsDTO thumnail=dao.findById(num);
			listFile.add(0, thumnail);
			req.setAttribute("dto", dto);
			req.setAttribute("listFile",listFile);
			req.setAttribute("page", page);
			
			req.setAttribute("mode", "update");
			forward(req, resp, "/WEB-INF/views/pbbs/write.jsp");
			return;
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PbbsDAO dao = new PbbsDAO();
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		if (req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect("/WEB-INF/views/pbbs/list.jsp");
			return;
		}
		String page = req.getParameter("page");
		String cat =req.getParameter("cat");
		String category="";
		if(cat!=null) {
			category="&"+"cat="+cat+"&";
		}
		String ord =req.getParameter("order");
		String order="";
		if(ord!=null) {
			order="&"+"order="+ord+"&";
		}
		if(ord==null) {
			ord="";
		}
		try {
			PbbsDTO dto = new PbbsDTO();
			dto.setNum(Long.parseLong(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if (map != null) {
				String[] saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			dto.setUserId(info.getUserId());

			dao.updatePhoto(dto);
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/pbbs/list.do?"+category+order +"page="+ page);

	}
	protected void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정에서 파일만 삭제
		PbbsDAO dao = new PbbsDAO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();

		String page = req.getParameter("page");
		String cat =req.getParameter("cat");
		String category="";
		if(cat!=null) {
			category="&"+"cat="+cat+"&";
		}
		String ord =req.getParameter("order");
		String order="";
		if(ord!=null) {
			order="&"+"order="+ord+"&";
		}
		if(ord==null) {
			ord="";
		}
		try {
			long num = Long.parseLong(req.getParameter("num"));
			long fileNum = Long.parseLong(req.getParameter("fileNum"));
			
			PbbsDTO dto = dao.findById(num);

			if (dto == null) {
				resp.sendRedirect(cp + "/pbbs/list.do?page=" + page);
				return;
			}

			if (!info.getUserId().equals(dto.getUserId())) {
				resp.sendRedirect(cp + "/pbbs/list.do?page=" + page);
				return;
			}
			
			PbbsDTO vo = dao.findByFileId(fileNum);
			if(vo != null) {
				FileManager.doFiledelete(pathname, vo.getImageFilename());
				
				dao.deletePhotoFile("one", fileNum);
			}
			
			resp.sendRedirect(cp + "/pbbs/update.do?"+category +order +"num=" + num + "&page=" + page);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp + "/pbbs/list.do?"+category+order  +"page=" + page);
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 삭제
		PbbsDAO dao = new PbbsDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String cp = req.getContextPath();

		String page = req.getParameter("page");
		String cat =req.getParameter("cat");
		String category="";
		if(cat!=null) {
			category="&"+"cat="+cat+"&";
		}
		String ord =req.getParameter("order");
		String order="";
		if(ord!=null) {
			order="&"+"order="+ord+"&";
		}
		if(ord==null) {
			ord="";
		}
		try {
			long num=Long.parseLong(req.getParameter("num"));
			PbbsDTO dto = dao.findById(num);
			if (dto == null) {
				resp.sendRedirect(cp + "/pbbs/list.do?"+category +order +"page=" + page);
				return;
			}
			// 게시물을 올린 사용자가 아니면
			if (!dto.getUserId().equals(info.getUserId())&&!info.getUserId().equals("admin")) {
				resp.sendRedirect(cp + "/pbbs/list.do?"+category +order +"page=" + page);
				return;
			}

			// 이미지 파일 지우기
			List<PbbsDTO> listFile = dao.listPhotoFile(num);
			for (PbbsDTO vo : listFile) {
				FileManager.doFiledelete(pathname, vo.getImageFilename());
			
			}
			dao.deletePhotoFile("all", num);

			
			dao.deletePhoto(num);
			resp.sendRedirect(cp + "/pbbs/list.do?"+category+order +"page="+ page);
			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	protected void like(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 찜하기
		PbbsDAO dao = new PbbsDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		/*String cp = req.getContextPath();
		String page = req.getParameter("page");
		String cat =req.getParameter("cat");
		String category="";
		if(cat!=null) {
			category="&"+"cat="+cat+"&";
		}
		String ord =req.getParameter("order");
		String order="";
		if(ord!=null) {
			order="&"+"order="+ord+"&";
		}
		if(ord==null) {
			ord="";
		}*/
		try {
			long num=Long.parseLong(req.getParameter("num"));
			String id=info.getUserId();
			String chk=dao.doILikeThis(num, id);
			String plike="";
			// 찜테이블 업데이트하고 게시들의 찜된횟수 업데이트
			if(chk.equals("0")) {
				dao.iLikeIt( num,id);
				plike="1";
			}else {
				dao.imSoso( num,id);
				plike="0";
			}
			
			// JSON 으로 응답
			JSONObject job = new JSONObject();
			job.put("plike", plike);
			respJson(resp, job.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
		// 리플 리스트 - AJAX:TEXT
	protected void listReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PbbsDAO dao = new PbbsDAO();
		MyUtil util = new MyUtil();

		try {
			long num = Long.parseLong(req.getParameter("num"));
			String pageNo = req.getParameter("pageNo");
			int current_page = 1;
			if (pageNo != null) {
				current_page = Integer.parseInt(pageNo);
			}

			int size = 5;
			int total_page = 0;
			int replyCount = 0;

			replyCount = dao.dataCountReply(num);
			total_page = util.pageCount(replyCount, size);
			if (current_page > total_page) {
				current_page = total_page;
			}

			// 리스트에 출력할 데이터
			int offset = (current_page - 1) * size;
			if(offset < 0) offset = 0;
			
			List<PReplyDTO> listReply = dao.listReply(num, offset, size);

			// 엔터를 <br>
			for (PReplyDTO dto : listReply) {
				dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
				dto.setWriterId(dao.writerId(num));
				dto.setSalechk(dao.saleChk(num));
			}

			// 페이징 처리 : AJAX 용 - listPage : 자바스크립트 함수명
			String paging = util.pagingMethod(current_page, total_page, "listPage");

			req.setAttribute("listReply", listReply);
			req.setAttribute("pageNo", current_page);
			req.setAttribute("replyCount", replyCount);
			req.setAttribute("total_page", total_page);
			req.setAttribute("paging", paging);
			
			viewPage(req, resp, "pbbs/listReply.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendError(400);
		
	}
	
	
	protected void choose(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PbbsDAO dao = new PbbsDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String cat =req.getParameter("cat");
		String category="";
		if(cat!=null) {
			category="&"+"cat="+cat+"&";
		}
		String ord =req.getParameter("order");
		String order="";
		if(ord!=null) {
			order="&"+"order="+ord+"&";
		}
		if(ord==null) {
			ord="";
		}
		try {
			long num=Long.parseLong(req.getParameter("num"));
			PbbsDTO dto = dao.findById(num);
			if (dto == null) {
				resp.sendRedirect(cp + "/pbbs/list.do?"+category+order  +"page=" + page);
				return;
			}
			String buyer=req.getParameter("buyer");
			long replynum=Long.parseLong(req.getParameter("replynum"));
			dao.iChooseYou(num, buyer,replynum);
			long pstate=1;
			
			JSONObject job = new JSONObject();
			job.put("pstate", pstate);
			respJson(resp, job.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	
	
	//////////
	
	
	// 리플 또는 답글 저장 - AJAX:JSON
		protected void insertReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			PbbsDAO dao = new PbbsDAO();

			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			
			String state = "false";
			try {
				PReplyDTO dto = new PReplyDTO();

				long num = Long.parseLong(req.getParameter("num"));
				dto.setNum(num);
				dto.setUserId(info.getUserId());
				dto.setContent(req.getParameter("content"));
				String answer = req.getParameter("answer");
				if (answer != null) {
					dto.setAnswer(Long.parseLong(answer));
				}
				
				dao.insertReply(dto);
				
				state = "true";
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONObject job = new JSONObject();
			job.put("state", state);

			// JSON 으로 응답
			respJson(resp, job.toString());
		}

		// 리플 또는 답글 삭제 - AJAX:JSON
		protected void deleteReply(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			PbbsDAO dao = new PbbsDAO();

			HttpSession session = req.getSession();
			SessionInfo info = (SessionInfo) session.getAttribute("member");
			String state = "false";

			try {
				long replyNum = Long.parseLong(req.getParameter("replyNum"));

				dao.deleteReply(replyNum, info.getUserId());
				
				state = "true";
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONObject job = new JSONObject();
			job.put("state", state);

			// JSON 으로 응답
			respJson(resp, job.toString());
		}
		protected void insertReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			insertReply(req, resp);
		}

		// 리플의 답글 리스트 - AJAX:TEXT
		protected void listReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			PbbsDAO dao = new PbbsDAO();

			try {
				long answer = Long.parseLong(req.getParameter("answer"));

				List<PReplyDTO> listReplyAnswer = dao.listReplyAnswer(answer);

				// 엔터를 <br>(스타일 => style="white-space:pre;")
				for (PReplyDTO dto : listReplyAnswer) {
					dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
				}

				req.setAttribute("listReplyAnswer", listReplyAnswer);

				viewPage(req, resp, "pbbs/listReplyAnswer.jsp");
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resp.sendError(400);
		}

		// 리플 답글 삭제 - AJAX:JSON
		protected void deleteReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			deleteReply(req, resp);
		}

		// 리플의 답글 개수 - AJAX:JSON
		protected void countReplyAnswer(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			PbbsDAO dao = new PbbsDAO();
			int count = 0;

			try {
				long answer = Long.parseLong(req.getParameter("answer"));
				count = dao.dataCountReplyAnswer(answer);
			} catch (Exception e) {
				e.printStackTrace();
			}

			JSONObject job = new JSONObject();
			job.put("count", count);

			// JSON 으로 응답 
			respJson(resp, job.toString());
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
