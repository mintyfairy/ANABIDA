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
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<style type="text/css">
* { padding: 0; margin: 0; }
*, *::after, *::before { box-sizing: border-box; }

body {
	font-family:"Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
	font-size: 13px;
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

.table-border thead > tr { border-top: 2px solid #ced4da; border-bottom: 1px solid #ced4da; }
.table-border tbody > tr { border-bottom: 1px solid #ced4da; }
.table-border tfoot > tr { border-bottom: 1px solid #ced4da; }
.td-border td { border: 1px solid #ced4da; }


td .content-box{width:30%; height:100%; display: flex;}
.img {display:flex; justify-content: center;}
.hi { padding: 60px 20px;}
.subtitle {text-align: left;}


/* board */
.board { margin: 30px auto; width: 700px; }

.title { width:100%; font-size: 16px; font-weight: bold; padding: 13px 0; }

.table-list thead > tr { background: #feefef; }

.table-list th, .table-list td { text-align: center; }
.table-list td:nth-child(5n+3) { text-align: left;  height: 150px; width: 30%;}

.table-list .num { width: 50px; color: #787878; }
.table-list .subject { color: #787878; }
.table-list .name { width: 100px; color: #787878; }
.table-list .date { width: 100px; color: #787878; }
.table-list .hit { width: 50px; color: #787878; }
.table-list .joincount { width: 60px; color: #787878; }


.btn {
  width: 80px;
  height: 30px;
  text-align: center;
  background: #feefef;
  border: solid 2px #ced4da ;
  border-radius: 5px;
}



/* paginate */
.page-navigation { clear: both; padding: 20px 0; text-align: center; }

.paginate {
	text-align: center;
	white-space: nowrap;
	font-size: 14px;	
}
.paginate a {
	border: 1px solid #ccc;
	color: #000;
	font-weight: 600;
	text-decoration: none;
	padding: 3px 7px;
	margin-left: 3px;
	vertical-align: middle;
}
.paginate a:hover, .paginate a:active {
	color: #6771ff;
}
.paginate span {
	border: 1px solid #e28d8d;
	color: #cb3536;
	font-weight: 600;
	padding: 3px 7px;
	margin-left: 3px;
	vertical-align: middle;
}
.paginate :first-child {
	margin-left: 0;
}



</style>

<script type="text/javascript">
function searchList() {
	const f = document.searchForm;
	f.submit();
}


</script>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"/>
</header>

<main>



<div class="board">
	<div class="container body-container">
		<div class="body-title">
			<h2>
				| 공동구매 리스트
			</h2>
		</div>
		
		<table class="table">
			<tr>
				<td width="50%">${dataCount}개(${page}/${total_page} 페이지)</td>
				<td align="right">&nbsp;</td>
			</tr>
		</table>
		
		<table class="table table-border table-list">
			<thead>
				<tr>
					<th class="num">번호</th>
					<th class="subject" colspan="2">제목</th>
					<th class="name">작성자</th>
					<th class="date">기한</th>
					<th class="joincount">참여자수</th>
					<th class="hit">조회수</th>
				</tr>
			</thead>
			
			<tbody>
			  <c:forEach var="dto" items="${list}" varStatus="status">
				<tr>
					<td>${dataCount - (page-1) * size - status.index}</td>
					<td class="">
						<a href="${pageContext.request.contextPath}/join/article.do?buyNum=${dto.buyNum}&page=${page}">
						<img src="${pageContext.request.contextPath}/uploads/join/${dto.imageFilename}" width="100" height="103" class="img">
						</a>
					</td>
					<td> 
						<a href="${pageContext.request.contextPath}/join/article.do?buyNum=${dto.buyNum}&page=${page}">
						${dto.title}
						</a>
					</td>
					<td>${dto.userName}</td>
					<td>(${dto.reg_date}<br>~${dto.exp_date})</td>
					<td>(${dto.joinCount}/${dto.min_peo})</td>
					<td>${dto.hitCount}</td>
				</tr>
			   </c:forEach>
			<tbody>
			
			
		</table>
		
		<div class="page-navigation">
			${dataCount == 0 ? "등록된 게시물이 없습니다." : paging}
		</div>
		
		<table class="table">
			<tr>
				<td width="100">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/list.do';">새로고침</button>
				</td>
				<td align="center">
					<form name="searchForm" action="${pageContext.request.contextPath}/join/list.do" method="post">
						<select name="schType" class="form-select">
							<option value="all" ${schType=="all"?"selected":""}>제목+내용</option>
							<option value="name" ${schType=="userId"? "selected":"" }>작성자</option>
							<option value="reg_date"  ${schType=="reg_date"?"selected":"" }>등록일</option>
							<option value="title" ${schType=="title"?"selected":"" }>제목</option>
							<option value="content"${schType=="content"?"selected":"" }>내용</option>
						</select>
						<input type="text" name="kwd" value="${kwd}" class="form-control">
						<button type="button" class="btn" onclick="searchList();">검색</button>
					</form>
				</td>
				<td align="right" width="100">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/write.do';">글올리기</button>
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