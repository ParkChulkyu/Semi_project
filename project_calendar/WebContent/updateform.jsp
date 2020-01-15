<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<% request.setCharacterEncoding("UTF-8"); %>
<% response.setContentType("text/html; charset=UTF-8"); %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<h1>수정하기</h1>

	<form action="calcontroller.do" method="post">
		<input type="hidden" name="seq" value="${dto.seq }">
		<input type="hidden" name="command" value="updateres">
		<table border="1">
			<tr>
				<th>번호</th>
				<td>${dto.seq }</td>
			</tr>
			<tr>
				<th>ID</th>
				<td>${dto.id }</td>
			</tr>
			<tr>
				<th>제목</th>
				<td>
					<input type="text" name="title" value="${dto.title }">
				</td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea cols="20" rows="20" name="content">${dto.content }</textarea></td>
			</tr>
			<tr>
				<th>등록날짜</th>
				<td><fmt:formatDate value="${dto.regdate }" pattern="yyyy:MM:dd"/></td>
			</tr>
			<tr>
				<td colspan="4">
					<input type="submit" value="수정하기">
					<input type="button" value="취소하기" onclick="location.href='calcontroller.do?command=callist'">
				</td>
			</tr>
		</table>
	
	</form>

</body>
</html>