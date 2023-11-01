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
 	position: relative;
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
.list-title{
text-align : center;
height: 50px ;
}
.more{
width: 150px;
height: 73px ;
 float: right;
}
.more:hover{
cursor: pointer;
}
@media (min-width: 1000px) {
	.container {
	    max-width: 980px;
	}
}

</style>

</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>
	
<main>
	<div class="container body-container">
		<img src="${pageContext.request.contextPath}/resource/images/precent.png" class="list-title">
	    <ul class="list-content" style="width:	1000px">
			<c:forEach var="dto" items="${list_date}">
				<li class="card"
					onclick="location.href='${articleUrl}&num=${dto.num}';"><img
					src="${pageContext.request.contextPath}/uploads/pbbs/${dto.imageFilename}">
					<p class="card-title"> ${dto.subject }</p>
					<p class="card-title"> ${dto.cost } 원</p>
					<c:if test="${dto.pstate==1}">
							<div style="position: absolute; top: 5px; ;opacity: 0.5; "><img src="${pageContext.request.contextPath}/resource/images/saled.png" style="width: 95%;	"/></div>
					</c:if>
				</li>

			</c:forEach>
		</ul>
		<img class="more" src="${pageContext.request.contextPath}/resource/images/plus.png" onclick="location.href='${pageContext.request.contextPath}/pbbs/list.do?order=like&page=1';">;
		<div class="page-navigation">${dataCount==0?"등록된 게시물이 없습니다.":paging}
		</div>
	</div>
	<div class="container body-container">
		<img src="${pageContext.request.contextPath}/resource/images/pmostview.png"class="list-title">
		<br>
	    <ul class="list-content" style="width:	1000px">
			<c:forEach var="dto" items="${list_view}">
				<li class="card"
					onclick="location.href='${articleUrl}&num=${dto.num}';"><img
					src="${pageContext.request.contextPath}/uploads/pbbs/${dto.imageFilename}">
					<p class="card-title"> ${dto.subject }</p>
					<p class="card-title"> ${dto.cost } 원</p>
					<c:if test="${dto.pstate==1}">
									<div style="position: absolute; top: 5px; ;opacity: 0.5; "><img src="${pageContext.request.contextPath}/resource/images/saled.png" style="width: 95%;	"/></div>
							</c:if>
				</li>

			</c:forEach>
		</ul>
		<img class="more" src="${pageContext.request.contextPath}/resource/images/plus.png" onclick="location.href='${pageContext.request.contextPath}/pbbs/list.do?order=like&page=1';">;
		<div class="page-navigation">${dataCount==0?"등록된 게시물이 없습니다.":paging}
		</div>
	</div>
		
	<div class="container body-container">
			<img src="${pageContext.request.contextPath}/resource/images/ppop.png"class="list-title">
	    <ul class="list-content" style="width:	1000px">
			<c:forEach var="dto" items="${list_pop}">
				<li class="card"
					onclick="location.href='${articleUrl}&num=${dto.num}';"><img
					src="${pageContext.request.contextPath}/uploads/pbbs/${dto.imageFilename}">
					<p class="card-title"> ${dto.subject }</p>
					<p class="card-title"> ${dto.cost } 원</p>
					<c:if test="${dto.pstate==1}">
									<div style="position: absolute; top: 5px; ;opacity: 0.5; "><img src="${pageContext.request.contextPath}/resource/images/saled.png" style="width: 95%;	"/></div>
							</c:if>
				</li>

			</c:forEach>
		</ul>
		<img class="more"  src="${pageContext.request.contextPath}/resource/images/plus.png" onclick="location.href='${pageContext.request.contextPath}/pbbs/list.do?order=like&page=1';">;
		<div class="page-navigation">${dataCount==0?"등록된 게시물이 없습니다.":paging}
		</div>
	</div>
</main>

<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/></body>
</html>