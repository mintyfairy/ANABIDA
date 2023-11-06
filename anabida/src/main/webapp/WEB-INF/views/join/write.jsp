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
.board { margin: 30px auto; width: 700px; }

.title { width:100%; font-size: 16px; font-weight: bold; padding: 13px 0; }

.table-form { margin-top: 20px; }
.table-form td { padding: 7px 0; }
.table-form tr:first-child {  border-top: 2px solid #212529; }
.table-form tr > td:first-child { width: 110px; text-align: center; background: #feefef; }
.table-form tr > td:nth-child(2) { 	padding-left: 10px; }
.table-form input[type=text], .table-form input[type=file], .table-form textarea { width: 97%; }
.table-form input[type=password] { width: 50%; }
</style>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
<script type="text/javascript" src="https://code.jquery.com/ui/1.8.8/i18n/jquery.ui.datepicker-ko.js"></script>


<script type="text/javascript">
function sendOk() {
	const f = document.joinForm;
	
	let str;
	
	str = f.title.value.trim();
	if(!str) {
		alert("제목을 입력하세요.");
		f.subject.focus();
		return;
	}
	
	
	str = f.content.value.trim();
	if(! str) {
		alert("내용을 작성하세요.");
		f.content.focus();
		return;
	}
	
	str = f.min_peo.value.trim();
	if(!str) {
		alert("최소 모집인원을 입력해주세요.");
		f.minpeo.focus();
		return;
	}
	
	str = f.exp_date.value.trim();
	if(!str) {
		alert("모집종료 기간을 입력해주세요.");
		f.birth.focus();
		return;
	}
	
	f.action = "${pageContext.request.contextPath}/join/${mode}_ok.do";
	f.submit();
}

$(function(){
	$("#date1").datepicker({
		showMonthAfterYear:true
		,defaultDate:"0"
		//,minDate:0, maxDate:"+5D"
		//,minDate:-5, maxDate:"+1M +5D"
		,minDate:"0", maxDate:"+2M"
	});
});



</script>
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>

<div class="board">
	<div class="title">
	    <h3><span><i class="fa-solid fa-upload"></i></span> 공동구매 모집 등록</h3>
	</div>

	<form name="joinForm" method="post" enctype="multipart/form-data">
		<table class="table table-border table-form">
			<tr> 
				<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
				<td> 
					<input type="text" name="title" maxlength="100" class="form-control" value="${dto.title}">
				</td>
			</tr>
			
			<tr> 
				<td>작성자</td>
				<td> 
					<p>${sessionScope.member.userName}</p>
				</td>
			</tr>
			
			<tr>
				<td>모집종료 기한</td>
				<td>
					<input type="text" name="exp_date" class="form-control" value="${dto.exp_date}" style="width: 50%;" id="date1" readonly>
				</td>
			</tr>
			
			<tr>
				<td>이미지</td>
				<td> 
					<input type="file" name="selectFile" accept="image/*" class="form-control" value="${dto.imageFilename}">
				</td>
			</tr>
			
			<tr>
				<td>상세페이지링크</td>
				<td> 
					<input type="text" name="link" class="form-control" value="${dto.link}">
				</td>
			</tr>
			
			<tr> 
				<td>내&nbsp;&nbsp;&nbsp;&nbsp;용 <br> (양&nbsp;&nbsp;&nbsp;&nbsp;식)</td>
				<td valign="top"> 
					<textarea name="content" class="form-control" placeholder="자세히 작성해주세요">${dto.content}</textarea>
				</td>
			</tr>
			
			<tr>
				<td>최소 모집인원</td>
				<td> 
					<input type="text" name="min_peo" maxlength="10" class="form-control" value="${dto.min_peo}" >
					(반드시 기재 필요 !!!)
				</td>
			</tr>
		</table>
			
		<table class="table">
			<tr> 
				<td align="center">
					<button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
					<button type="reset" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/write.do';">다시입력</button>
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/list.do';">${mode=='update'? '수정취소':'등록취소'}</button>
					<c:if test="${mode=='update'}">
						<input type="hidden" name="buyNum" value="${dto.buyNum}">
						<input type="hidden" name="page" value="${page}">
					</c:if>
				</td>
			</tr>
		</table>

	</form>
	
</div>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>