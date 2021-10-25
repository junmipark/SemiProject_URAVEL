package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.biz.HistoryBizImpl;
import com.dto.HistoryDto;

public class history_Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		String command = request.getParameter("command");
		System.out.println("[command : "+command+"]");
		
		HistoryBizImpl biz = new HistoryBizImpl();
		
		if(command.equals("history-search")) {
			System.out.println("keyword : "+request.getParameter("keyword"));
			dispatch("searchmain.jsp",request,response);
		}
		
		else if(command.equals("list")) {
			List<HistoryDto> list = biz.selectAll();
			
			System.out.println("list : "+request.getParameter("list"));
			request.setAttribute("list", list);
			
			dispatch("history/history_list.jsp",request,response);
			
		}
		else if(command.equals("desc")) {
			int historyno = Integer.parseInt(request.getParameter("historyno"));
			
			HistoryDto dto = biz.selectOne(historyno);
			
			request.setAttribute("dto", dto);
			
			System.out.println("desc : "+request.getParameter("desc"));
			dispatch("history_info.jsp",request,response);
			
		}
		
		else if(command.equals("writeform")) {
			response.sendRedirect("history_insertinfo.jsp");
		}
		else if(command.equals("write")) {
			
			int historyno = Integer.parseInt(request.getParameter("historyno").trim());
			int travelno = Integer.parseInt(request.getParameter("travelno"));
			String travelname = request.getParameter("travelname");
			String url_pic1 = request.getParameter("url_pic1");
			String url_pic2 = request.getParameter("url_pic2");
			String description = request.getParameter("description");
			String source = request.getParameter("source");
			
			HistoryDto dto = new HistoryDto(historyno,travelno,travelname,url_pic1,url_pic2,description,source);
			
			boolean res = biz.insert(dto);
			
			if(res) {
				jsResponse("글 작성 성공","history_Controller?command=list",response);
			} else {
				jsResponse("글 작성 실패","history_Controller?command=writeform",response);
			}
			
			System.out.println("write : "+request.getParameter("write"));
			
		}
		
		else if(command.equals("updateform")) {
			int historyno = Integer.parseInt(request.getParameter("historyno"));
			HistoryDto dto = biz.selectOne(historyno);
			
			request.setAttribute("dto", dto);
			dispatch("history_updateinfo.jsp",request,response);
		}
		
		else if(command.equals("update")) {
			String travelname = request.getParameter("history-form-title");
			String url_pic1 = request.getParameter("history-form-url1");
			String url_pic2 = request.getParameter("history-form-url2");
			String description = request.getParameter("history-form-content");
			String source = request.getParameter("history-form-source");
			int historyno = Integer.parseInt(request.getParameter("historyno").trim());
			
			HistoryDto dto = new HistoryDto();
			dto.setTravelname(travelname);
			dto.setUrl_pic1(url_pic1);
			dto.setUrl_pic2(url_pic2);
			dto.setDescription(description);
			dto.setSource(source);
			dto.setHistoryno(historyno);
			
			boolean res = biz.update(dto);
			
			if(res) {
				jsResponse("글 수정 성공","history_Controller?command=desc&historyno="+historyno,response);
			} else {
				jsResponse("글 수정 실패","history_Controller?command=update&historyno="+historyno,response);
			}
			System.out.println("update : "+request.getParameter("update"));
			
		}
	
	}
	
	private void jsResponse(String msg, String url, HttpServletResponse response) throws IOException {
		String s = "<script type='text/javascript'>"+
					"alert('"+msg+"');"+
					"location.href='"+url+"';" +
				   "</script>";
		PrintWriter out = response.getWriter();
		out.print(s);
	}
	
	private void dispatch(String url, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
