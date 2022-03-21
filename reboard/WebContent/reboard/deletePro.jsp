<%@page import="reboard.BoardDBBean"%>
<%@page import="reboard.BoardDataBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("utf-8");
%>

<jsp:useBean id="board" class="reboard.BoardDataBean"/>
<jsp:setProperty property="*" name="board"/>

<%
	String nowpage = request.getParameter("page");

	BoardDBBean dao = BoardDBBean.getInstance();
	BoardDataBean old = dao.getContent(board.getNum());
	
	//	비번 비교
	if(old.getPasswd().equals(board.getPasswd())){	// 비번 일치시
		int result = dao.delete(board);	//delete SQL문 실행
		if(result == 1){
%>
			<script>
				alert("글삭제 성공");
				location.href="list.jsp?page=<%=nowpage%>";
			</script>

<% 		}
	}else{	// 비번 불일치시 %>
		<script>
			alert("비번이 일치하지 않습니다.");
			history.go(-1);
		</script>
<% } %>