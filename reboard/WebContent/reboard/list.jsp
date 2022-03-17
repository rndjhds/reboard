<%@page import="reboard.BoardDataBean"%>
<%@page import="java.util.List"%>
<%@page import="reboard.BoardDBBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
	// 1. 한 화면에 출력할 데이터 갯수
	int page_size = 10;

	String pageNum = request.getParameter("page");
	if(pageNum == null){
		pageNum = "1";	// 1page : 최근글이 보이는 page
	}
	
	// 2. 현재 페이지 번호 
	int currentPage = Integer.parseInt(pageNum);
	
	// startRow : 각 page에 추출할 데이터의 시작번호
	// endRow : 각 page에 추출할 데이터의 끝번호
	// 1page : startRow=1, endRow=10
	// 2page : startRow=11, endRow=20
	// 3page : startRow=21, endRow=30
	
	int startRow = (currentPage - 1) * page_size + 1;
	int endRow = currentPage * page_size;
	
	// 3. 총 데이터 갯수
	int count = 0;
	
	BoardDBBean dao = BoardDBBean.getInstance();
	count = dao.getContent();
	System.out.println("count:"+count);
	
	List<BoardDataBean> list = null;
	if(count > 0){
//		list = dao.getList(startRow, endRow);	// 데이터 10개 추출
	}
	System.out.println("list:"+list);

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>