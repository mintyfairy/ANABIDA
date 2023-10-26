﻿<%@ page contentType="text/html; charset=UTF-8" %>
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
	max-width: 700px;
	padding-top: 15px;
}

.table-form td { padding: 7px 0; }
.table-form p { line-height: 200%; }
.table-form tr:first-child { border-top: 2px solid #212529;  }
.table-form tr > td:first-child { width: 110px; text-align: center; background: #f8f8f8; }
.table-form tr > td:nth-child(2) { padding-left: 10px; }

.table-form input[type=text], .table-form input[type=file], .table-form textarea {
	width: 96%; }
	
.table-foem .img{width:37px;height: 37px;  border: none; vertical-align:middle; 
 }
.table-foem .img{
vertical-align:middle; font-size: 11px; color: #333333;
} 

	
</style>

<script type="text/javascript">
function sendOk(){
	  const f = document.photoForm;
		let str;
		
	    str = f.subject.value.trim();
	    if(!str) {
	        alert("제목을 입력하세요. ");
	        f.subject.focus();
	        return;
	    }

	    str = f.content.value.trim();
	    if(!str) {
	        alert("내용을 입력하세요. ");
	        f.content.focus();
	        return;
	    }
	    
	    let mode="${mode}";
	    if(mode==="write"&&(!f.selectFile.value)){
	    	alert('이미지파일추가바람');
	    	f.selectFile.focus();
	    	return;
	    }
	    

	    f.action = "${pageContext.request.contextPath}/photo/${mode}_ok.do";
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
			<h2><i class="far fa-image	"></i> photo gallery </h2>
	    </div>
	    
	    <div class="body-main mx-auto">
			<form name="photoForm" method="post" enctype="multipart/form-data">
				<table class="table table-border table-form">
					<tr> 
						<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
						<td> 
							<input type="text" name="subject" maxlength="100" class="form-control" value="${dto.subject}"
							>
						</td>
					</tr>
					
					<tr> 
						<td>작성자</td>
						<td> 
							<p>${sessionScope.member.userName}</p>
						</td>
					</tr>
					
					<tr> 
						<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
						<td> 
							<textarea name="content" class="form-control" > ${dto.content}   </textarea>
						</td>
					</tr>
					
					<tr>
						<td>이미지</td>
						<td> 
							<input type="file" name="selectFile" accept="image/*" class="form-control" >
						</td><!-- accept: 이미지만 받ㄱ함 -->
					</tr>
				</table>
					
				<table class="table">
					<tr> 
						<td align="center">
							<button type="button" class="btn" onclick="sendOk();">${mode=="update"?"수정완료":"등록완료"}</button>
							<button type="reset" class="btn">다시입력</button>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/photo/list.do';">${mode=="update"?"수정취소":"등록취소"}</button>
							<c:if test="${mode=='update'}">
								<input type="hidden" name="num" value="${dto.num }"> <!-- 업데이트일때 페이지와 글번호를 넘겨줘야 수정가능 -->
								<input type="hidden" name="page" value="${page}"> 	<!-- 그래서 업데이트일떄나타나게한다 -->
								<input type="hidden" name="postImgname" value="${postImgname}"> 	<!-- 그래서 업데이트일떄나타나게한다 -->
							</c:if>
						</td>
					</tr>
				</table>
		
			</form>

	    </div>
	</div>
</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>