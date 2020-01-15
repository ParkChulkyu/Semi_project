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
<script type="text/javascript">
function allChk(bool){
	var chk = $("input[name='chk']");
	for(var i = 0; i < chk.length; i++){
		chk[i].checked = bool;
	}
}
</script>
</head>
<body>

	<h1>일정 목록</h1>
	
	<form action="calcontroller.do" method="post">
		<input type="hidden" name="command" value="muldel">
		
		<!-- util 클래스를 씀 -->
		<jsp:useBean id="util" class="com.cal.dao.Util"></jsp:useBean>
		
		<table border="1">
			<col width="50px">
			<col width="50px">
			<col width="300px">
			<col width="200px">
			<col width="100px">
			
			<tr>
				<th><input type="checkbox" name="all" onclick="allChk(this.checked);"></th>
				<th>번호</th>
				<th>제목</th>
				<th>일정</th>
				<th>작성일</th>
			</tr>
			
			<c:choose>
				<c:when test='${empty list}'>
					<tr>
						<td colspan="5">======일정이 없습니다.=======</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items='${list }' var="dto">
						<tr>
							<td><input type="checkbox" name="chk" value="${dto.seq }"></td>
							<td>${dto.seq }</td>
							<td><a href="calcontroller.do?command=detail&seq=${dto.seq }">${dto.title }</a></td>
							<td>
								<jsp:setProperty property="todates" name="util" value="${dto.mdate }"/>
								<jsp:getProperty property="todates" name="util"/>
							</td>
							<td><fmt:formatDate value="${dto.regdate }" pattern="yyyy:MM:dd"/></td>
							<!-- fmt time -->
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			<tr>
				<td colspan="5">
					<input type="submit" value="선택삭제" onclick="allChk(bool);">
					<input type="button" value="돌아가기" onclick="location.href='calcontroller.do?command=calendar'">
				</td>
			</tr>
		</table>
	</form>

</body>
</html>