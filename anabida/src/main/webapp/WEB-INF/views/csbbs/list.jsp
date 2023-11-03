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
	max-width: 700px;
}

.table-list thead>tr:first-child {
	background:  #e6f7ff;
}

.table-list th, .table-list td {
	text-align: center;
}

.table-list .left {
	text-align: left;
	padding-left: 5px;
}

.table-list .num {
	width: 60px;
	color: #787878;
}

.table-list .subject {
	color: #787878;
}

.table-list .name {
	width: 100px;
	color: #787878;
}

.table-list .date {
	width: 100px;
	color: #787878;
}

.table-list .hit {
	width: 70px;
	color: #787878;
}

.hover { cursor: pointer; background: #787878; }
.paginate span {
	border: 1px solid #0088cc;
	color: #3399ff;
	font-weight: 600;
	padding: 3px 7px;
	margin-left: 3px;
	vertical-align: middle;
}

a { color: #222222; text-decoration: none; cursor: pointer; }
a:active, a:hover { color: #668cff; text-decoration: underline; }

</style>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
$(function(){
	$("table.tc tr:gt(0)").hover( // 마우스 hover 처리
		function() {$(this).addClass("hover");},
		function() {$(this).removeClass("hover");}
	);
	
});

function searchList() {
	const f = document.searchForm;
	f.submit();
	}
</script>
</head>
<body>

	<header>
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<main>
		<div class="container body-container">
			<div class="body-title">
				<h2>
					<i class="fa-regular fa-square"></i> 문의 게시판
				</h2>
			</div>

			<div class="body-main mx-auto">
				<table class="table">
					<tr>
						<td width="50%">${dataCount}개  ${page}/${total_page}페이지</td>
						<td align="right">&nbsp;</td>
					</tr>
				</table>

				<table class="table table-border table-list tc">
					<thead>
						<tr>
							<th class="qnum">번호</th>
							<th class="title">제목</th>
							<th class="name">작성자</th>
							<th class="date">작성일</th>
							<th class="hit">조회수</th>
							<th class="answer">답변 여부</th>
						</tr>
					</thead>

					<tbody>
							<c:forEach var="dto" items="${list}" varStatus="status">
								<tr>
									<td>${dataCount-(page-1)*size-status.index}</td>
									<td class="left">

									
									<c:if test="${sessionScope.member.userId==dto.userId || dto.userId == 'admin' || sessionScope.member.userId=='admin' || sessionScope.member.userId==dto.answerUserId2}">
										<c:forEach var="n" begin="1" end="${dto.depth }">&nbsp;&nbsp;</c:forEach>
										<c:if test="${dto.depth!=0 }">└&nbsp;</c:if>
										<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin' || sessionScope.member.userId==dto.answerUserId2 }"><a href="${articleUrl}&qnum=${dto.qnum}">${dto.title}</a></c:if>
									</c:if>
									<c:if test="${sessionScope.member.userId != dto.answerUserId2 && sessionScope.member.userId != dto.userId && sessionScope.member.userId != 'admin'}">
										확인 권한이 없습니다.
									</c:if>
								</td>
								<c:if test="${dto.userId=='admin'}">
								<td style="color: #0099e6;">${dto.userName}</td>
								</c:if>
								<c:if test="${dto.userId==sessionScope.member.userId && dto.userId != 'admin'}">
								<td style="font-weight: bold;">${dto.userName}</td>
								</c:if>
								<c:if test="${dto.userId!='admin' && dto.userId!=sessionScope.member.userId}">
								<td>${dto.userName}</td>
								</c:if>
								<td>${dto.reg_date}</td>
								<td>${dto.hitCount}</td>
								<c:if test="${dto.userId!='admin' && dto.answeryes==1}">
								<td style="font-weight: bold;">${dto.answeryes==0 ? "답변대기":"답변완료"}</td>	
								</c:if>
								<c:if test="${dto.userId!='admin' && dto.answeryes==0}">
								<td style="color: black;">${dto.answeryes==0 ? "답변대기":"답변완료"}</td>	
								</c:if>
								<c:if test="${dto.userId == 'admin' }">
								<td style="font-weight: bold;">└admin</td>
								</c:if>
								</tr>
							</c:forEach>

					<tbody>
				</table>

				<div class="page-navigation" style="color: #0099e6">
				${dataCount==0 ? "등록된 게시글이 없습니다." : paging }
				</div>

				<table class="table">
					<tr>
						<td width="100">
							<button type="button" class="btn"
								onclick="location.href='${pageContext.request.contextPath}/csbbs/list.do';"
								title="새로고침">
								<i class="fa-solid fa-arrow-rotate-right"></i>
							</button>
						</td>
						<td align="center">
							<form name="searchForm" action="${pageContext.request.contextPath}/csbbs/list.do" method="post">
								<select name="schType" class="form-select">
								<option value="userName" ${schType=="userName"?"selected":"" }>유저 아이디</option>
								</select> <input type="text" name="kwd" value="${kwd}" class="form-control">
								<button type="button" class="btn" onclick="searchList();">게시물 검색</button>
							</form>
						</td>
						<td align="right" width="100">
							<button type="button" class="btn"
								onclick="location.href='${pageContext.request.contextPath}/csbbs/write.do';">글올리기</button>
						</td>
					</tr>
				</table>

			</div>
		</div>
	</main>

	<footer>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>

	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
</html>