package com.main;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pbbs.PbbsDAO;
import com.pbbs.PbbsDTO;
import com.util.MyServlet;

@WebServlet("/main.do")
public class MainServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		if(uri.indexOf("main.do") != -1) {
			main_list(req, resp);	
		}
	}
	protected void main_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		PbbsDAO dao = new PbbsDAO();
		String cp = req.getContextPath();

		try {
			int size = 10;
			int offset = 0;
			
			List<PbbsDTO> list_date,list_view,list_pop;
			list_date = dao.listPhoto(offset, size);
			list_view = dao.mostViewListPhoto(offset, size);
			list_pop = dao.popularListPhoto(offset, size);
			String articleUrl = cp + "/pbbs/article.do?page=1";
			System.out.println("cp: "+cp);
			
			req.setAttribute("articleUrl", articleUrl);
			req.setAttribute("list_date", list_date);
			req.setAttribute("list_view", list_view);
			req.setAttribute("list_pop", list_pop);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		forward(req, resp, "/WEB-INF/views/main/main.jsp");

	}
}
