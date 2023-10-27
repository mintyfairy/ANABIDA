<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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

<script type="text/javascript">
function sendOk() {
	const f = document.joinForm;
	
	let str;
	
	str = f.subject.value.trim();
	if(!str) {
		alert("제목을 입력하세요.");
		f.subject.focus();
		return;
	}
	
	/*
	if(! f.selectFile.value()) {
		alert("이미지 파일이 선택되지 않았습니다.");
		f.selectFile.focus();
		return;
	}
	*/
	
	
	str = f.content.value.trim();
	if(! str) {
		alert("양식이 누락 되었습니다.");
		f.content.focus();
		return;
	}
	
	str = f.minpeo.value.trim();
	if(!str) {
		alert("최소 모집인원을 입력해주세요.");
		f.minpeo.focus();
		return;
	}
	
	f.action = "${pageContext.request.contextPath}/join/${mode}_ok.do";
	f.submit;
}



</script>
</head>
<body>

<div class="board">
	<div class="title">
	    <h3><span>|</span> 공동구매 모집 등록</h3>
	</div>

	<form name="joinForm" method="post" enctype="multipart/form-data">
		<table class="table table-border table-form">
			<tr> 
				<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
				<td> 
					<input type="text" name="subject" maxlength="100" class="form-control" value="">
				</td>
			</tr>
			
			<tr> 
				<td>작성자</td>
				<td> 
					<input type="text" name="name" maxlength="10" class="form-control" value="${dto.userId}">
				</td>
			</tr>
			
			<tr>
				<td>이미지</td>
				<td> 
					<input type="file" name="selectFile" accept="image/*" class="form-control">
				</td>
			</tr>
			
			<tr> 
				<td>내&nbsp;&nbsp;&nbsp;&nbsp;용 <br> (양&nbsp;&nbsp;&nbsp;&nbsp;식)</td>
				<td valign="top"> 
					<textarea name="content" class="form-control" placeholder="자세히 작성해주세요"></textarea>
				</td>
			</tr>
			
			<tr>
				<td>최소 모집인원</td>
				<td> 
					<input type="text" name="minpeo" maxlength="10" class="form-control">
					(반드시 기재 필요 !!!)
				</td>
			</tr>
		</table>
			
		<table class="table">
			<tr> 
				<td align="center">
					<button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
					<button type="reset" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/write.do';">다시입력</button>
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/list.do';">등록취소</button>
				</td>
			</tr>
		</table>

	</form>
	
</div>

</body>
</html>