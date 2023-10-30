<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
.body-main {
	max-width: 800px;
}

.list-header {
	padding-top: 25px;
	padding-bottom: 10px;
	display: flex;
	justify-content: space-between;
}

.list-header .list-header-left {
	padding-left: 5px;
	display: flex;
	align-items: center;
}

.list-header .list-header-right {
	padding-right: 5px;
	display: flex;
	align-items: center;
}

.list-content {
	list-style: none;
	width: 100%;
	margin-bottom: 5px;
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
	gap: 10px;
	justify-content: center;
}

.card {
	border: 1px solid #ced4da;
	cursor: pointer;
	padding: 5px 5px 0;
}

.card img {
	display: block;
	width: 100%;
	height: 200px;
	border-radius: 5px;
}

.card img:hover {
	scale: 100.7%;
}

.card-title {
	font-size: 14px;
	font-weight: 500;
	padding: 10px 2px;
	width: 175px;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

</style>

</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
	
<main>
		<div style="font-size:20px; font-weight: blod; margin: 10px; text-align: center"> 최근 매물</div>
	<div class="container body-container">
	    <ul class="list-content" style="width:	1000px">
			<c:forEach var="dto" items="${list_date}">
				<li class="card"
					onclick="location.href='${articleUrl}&num=${dto.num}';"><img
					src="${pageContext.request.contextPath}/uploads/pbbs/${dto.imageFilename}">
					<p class="card-title"> ${dto.subject }</p>
					<p class="card-title"> ${dto.cost } 원</p>
				</li>

			</c:forEach>
		</ul>
		<a style="font-size:20px; font-weight: blod; margin: 10px; " onclick="location.href='${pageContext.request.contextPath}/pbbs/list.do?page=1';"> 더보기</a>
		<div class="page-navigation">${dataCount==0?"등록된 게시물이 없습니다.":paging}
		</div>
	</div>
		<div style="font-size:20px; font-weight: blod; margin: 10px ; text-align: center"> 많이 본 매물</div>
	<div class="container body-container">
		<br>
	    <ul class="list-content" style="width:	1000px">
			<c:forEach var="dto" items="${list_view}">
				<li class="card"
					onclick="location.href='${articleUrl}&num=${dto.num}';"><img
					src="${pageContext.request.contextPath}/uploads/pbbs/${dto.imageFilename}">
					<p class="card-title"> ${dto.subject }</p>
					<p class="card-title"> ${dto.cost } 원</p>
				</li>

			</c:forEach>
		</ul>
		<a style="font-size:20px; font-weight: blod; margin: 10px; " onclick="location.href='${pageContext.request.contextPath}/pbbs/list.do?order=view&page=1';"> 더보기</a>
		<div class="page-navigation">${dataCount==0?"등록된 게시물이 없습니다.":paging}
		</div>
	</div>
		<div style="font-size:20px; font-weight: blod; margin: 10px; text-align: center"> 인기 매물</div>
	<div class="container body-container">
	    <ul class="list-content" style="width:	1000px">
			<c:forEach var="dto" items="${list_pop}">
				<li class="card"
					onclick="location.href='${articleUrl}&num=${dto.num}';"><img
					src="${pageContext.request.contextPath}/uploads/pbbs/${dto.imageFilename}">
					<p class="card-title"> ${dto.subject }</p>
					<p class="card-title"> ${dto.cost } 원</p>
				</li>

			</c:forEach>
		</ul>
		<a style="font-size:20px; font-weight: blod; margin: 10px; " onclick="location.href='${pageContext.request.contextPath}/pbbs/list.do?order=like&page=1';"> 더보기</a>
		<div class="page-navigation">${dataCount==0?"등록된 게시물이 없습니다.":paging}
		</div>
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/></body>
</html>