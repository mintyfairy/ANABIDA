<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
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
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<main>
		<div class="container body-container">
			<div class="body-title">
				<h3>
					<i class="far fa-image	"></i>
					<a href="${pageContext.request.contextPath}/pbbs/list.do?page=1">전체</a> |
					<a href="${pageContext.request.contextPath}/pbbs/list.do?cat=1&page=1">명품</a> |
					<a href="${pageContext.request.contextPath}/pbbs/list.do?cat=2&page=1">의류</a> |
					<a href="${pageContext.request.contextPath}/pbbs/list.do?cat=3&page=1">전자기기</a> |
					<a href="${pageContext.request.contextPath}/pbbs/list.do?cat=4&page=1">리빙/생활</a> |
					<a href="${pageContext.request.contextPath}/pbbs/list.do?cat=5&page=1">도서/음반/문구</a> |
					<a href="${pageContext.request.contextPath}/pbbs/list.do?cat=6&page=1">반려동물</a> |
					<a href="${pageContext.request.contextPath}/pbbs/list.do?cat=7&page=1">쿠폰</a> |
					<a href="${pageContext.request.contextPath}/pbbs/list.do?cat=8&page=1">스포츠</a> |
				</h3>
			</div>

			<div class="body-main mx-auto">
				<div class="list-header">
					<span class="list-header-left">${dataCount}개(${page}/${total_page} 페이지)</span> 
					<span class="list-header-right">
						<button type="button" class="btn"
							onclick="location.href='${pageContext.request.contextPath}/pbbs/write.do';">등록</button>
					</span>
				</div>
				<ul class="list-content">
					<c:forEach var="dto" items="${list}">
						<li class="card"
							onclick="location.href='${articleUrl}&num=${dto.num}';"><img
							src="${pageContext.request.contextPath}/uploads/pbbs/${dto.imageFilename}">
							<p class="card-title"> ${dto.subject }</p>
							<p class="card-title"> ${dto.cost } 원</p>
						</li>

					</c:forEach>
				</ul>
				<div class="page-navigation">${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</div>

			</div>
		</div>
	</main>

	<footer>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>

	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>