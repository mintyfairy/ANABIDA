<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
* { padding: 0; margin: 0; }
*, *::after, *::before { box-sizing: border-box; }

body {
	font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
	font-size: 14px;
	color: #222;
}

a { color: #222; text-decoration: none; cursor: pointer; }
a:active, a:hover { color: #f28011; text-decoration: underline; }

/* form-control */
.btn {
	color: #333;
	border: 1px solid #999;
	background-color: #fff;
	padding: 5px 10px;
	border-radius: 4px;
	font-weight: 500;
	cursor:pointer;
	font-size: 14px;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}
.btn:active, .btn:focus, .btn:hover {
	background-color: #f8f9fa;
	color:#333;
}
.btn[disabled], fieldset[disabled] .btn {
	pointer-events: none;
	cursor: default;
	opacity: .65;
}

.form-control {
	border: 1px solid #999; border-radius: 4px; background-color: #fff;
	padding: 5px 5px; 
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}
.form-control[readonly] { background-color:#f8f9fa; }

textarea.form-control { height: 170px; resize : none; }

.form-select {
	border: 1px solid #999; border-radius: 4px; background-color: #fff;
	padding: 4px 5px; 
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}
.form-select[readonly] { background-color:#f8f9fa; }

textarea:focus, input:focus { outline: none; }
input[type=checkbox], input[type=radio] { vertical-align: middle; }

/* table */
.table { width: 100%; border-spacing: 0; border-collapse: collapse; }
.table th, .table td { padding-top: 10px; padding-bottom: 10px; }

.table-border thead > tr { border-top: 2px solid #212529; border-bottom: 1px solid #ced4da; }
.table-border tbody > tr { border-bottom: 1px solid #ced4da; }
.table-border tfoot > tr { border-bottom: 1px solid #ced4da; }
.td-border td { border: 1px solid #ced4da; }

/* board */
.board { margin: 30px auto; width: 900px; }

.title { width:100%; font-size: 16px; font-weight: bold; padding: 13px 0; }

.table-article { margin-top: 20px; }
.table-article tr > td { padding-left: 5px; padding-right: 5px; }

.imgg { margin:30px 150px;}

.table tr td {

overflow:hidden;
white-space: initial;
text-overflow: ellipsis;
webkit-box-orient: vertical;
word-break: break-all;


}
</style>

<script type="text/javascript">
<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
	function deleteBoard() {
		if(confirm("게시글을 삭제 하시겠습니까? ")){
		let query = "buyNum=${dto.buyNum}&${query}";
		let url = "${pageContext.request.contextPath}/join/delete.do?"+query;
		location.href = url;
		}
	}
</c:if>


</script>


</head>
<body>
<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>

<div class="board">
	<div class="title">
	    <h3><span>|</span>공동구매 확인</h3>
	</div>
	
	<table class="table table-border table-article">
		<thead>
			<tr>
				<td colspan="2" align="center">
					${dto.title}
				</td>
			</tr>
		</thead>
		
		<tbody>
			<tr>
				<td width="50%">
					이름 : ${dto.userName}
				</td>
				<td align="right">
					${dto.reg_date} | 조회 ${dto.hitCount} 
				</td>
			</tr>
			
			<tr>
				<td width="50%">
					모집종료기한 : ${dto.exp_date}
				</td>
				<td align="right">
					참여 인원수 : (${dto.enterCount}/${dto.min_peo}) 
				</td>
			</tr>
			
			<tr>
				<td >
					제품 상세페이지 : <a href="${dto.link}" target="_blank"> ${dto.link}</a>
				</td>
			</tr>
			
			<tr style="border-bottom: none;">
				<td colspan="2" valign="top">
					<img src="<c:url value='/uploads/join/${dto.imageFilename}'/>" width="400px" height="400px" class="imgg">
				</td>
			</tr>
			
			<tr>
				<td colspan="2" valign="top" height="200">
					${dto.content}
				</td>
			</tr>
			
			<tr>
				<td colspan="2">
					이전글 : 
					<c:if test="${not empty prevDto}">
						<a href="${pageContext.request.contextPath}/join/article.do?${query}&buyNum=${prevDto.buyNum}">
						${prevDto.title}
						</a>
					</c:if>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					다음글 : 
					<c:if test="${not empty nextDto}">
					<a href="${pageContext.request.contextPath}/join/article.do?${query}&buyNum=${nextDto.buyNum}">
					${nextDto.title}
					</a>
					</c:if>
				</td>
			</tr>
		<tbody>
	</table>
	
	<table class="table">
		<tr>
			<td width="50%">
				<c:choose>
					<c:when test="${sessionScope.member.userId==dto.userId}">
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/update.do?buyNum=${dto.buyNum}&page=${page}';">수정</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn" disabled="disabled">수정</button>
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
						<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn" disabled="disabled">삭제</button>
					</c:otherwise>
				</c:choose>
				<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/list.do?page=${page}';">이전</button>
			</td>
			<td align="right">
				<c:choose>
					<c:when test="${dto.enterCount >= dto.min_peo || dto.def < -1}">
						<button type="button" class="btn" disabled="disabled">신청하기</button>					
					</c:when>
					<c:otherwise>
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/join.do?buyNum=${dto.buyNum}&page=${page}';">신청하기</button>					
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
</div>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>