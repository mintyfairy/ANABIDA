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
	padding-top: 15px;
}
.x{
	width: 100%;
	height: 100%;
	align-items: center;
}
.img-box img {
	width: 37px; height: 37px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}


.table-form td { padding: 7px 0; }
.table-form p { line-height: 200%; }
.table-form tr:first-child { border-top: 2px solid #212529;  }
.table-form tr > td:first-child { width: 110px; text-align: center; background: #f8f8f8; }
.table-form tr > td:nth-child(2) { padding-left: 10px; }

.table-form input[type=text], .table-form input[type=file], .table-form textarea {
	width: 96%; }
</style>

<script type="text/javascript">
function sendcs() {
    const f = document.boardForm;
	let str;
	
    str = f.title.value.trim();
    if(!str) {
        alert("제목을 입력하세요. ");
        f.title.focus();
        return;
    }

    str = f.content.value.trim();
    if(!str) {
        alert("내용을 입력하세요. ");
        f.content.focus();
        return;
    }
    
    <c:if test="${mode=='write'}">
    if (!f.selectFile.value ) {
        alert("이미지 파일을 추가 하세요. ");
        f.selectFile.focus();
        return;
    }
    </c:if>
    
    

    f.action = "${pageContext.request.contextPath}/csbbs/${mode}_ok.do";
    f.submit();
}
<c:if test="${mode=='update'}">
function deleteFile(qanum) {
	if(! confirm("이미지를 삭제 하시겠습니까 ?")) {
		return;
	}
	
	let query = "qnum=${dto.qnum}&qanum=" + qanum + "&page=${page}";
	let url = "${pageContext.request.contextPath}/csbbs/deleteFile.do?" + query;
	location.href = url;
}
</c:if>
</script>
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>
	
<main>
	<div class="container body-container">
	    <div class="body-title">
			<h2><i class="fa-regular fa-square"></i> 문의 게시판 </h2>
	    </div>
	    
	    <div class="body-main mx-auto">
			<form name="boardForm" method="post" enctype="multipart/form-data">
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
					<c:if test="${sessionScope.member.userId == 'admin'}">
					<tr>
						<td>답변 확인</td>
						<td>
							<p><input type="checkbox" name="answeryes" value=1></p>
						</td>	
					</tr>		
					</c:if>		
					<tr> 
						<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
						<td> 
							<textarea name="content" class="form-control">${dto.content}</textarea>
						</td>
					</tr>
					<tr>
						<td>첨부 사진</td>
						<td> 
							<input type="file" name="selectFile" accept="image/*" multiple="multiple" class="form-control">
						</td>
						<c:if test="${mode=='update'}">
						<tr>
							<td>등록이미지</td>
							<td>
								<div class="img-box">
									<c:forEach var="vo" items="${listFile}">
										<img class="x" name="files" src="${pageContext.request.contextPath}/uploads/csbbs/${vo.filename}"
											onclick="deleteFile('${vo.qanum}');">
									</c:forEach>
								</div>
							</td>
						</tr>
					</c:if>
					</tr>
				</table>
					
				<table class="table">
					<tr> 
						<td align="center">
							<button type="button" class="btn" onclick="sendcs();">${mode=='update'?'수정완료':(mode=='answer'? '답변완료':'등록하기')}</button>
							<button type="reset" class="btn">다시입력</button>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/csbbs/list.do';">${mode=='update'?'수정취소':(mode=='answer'? '답변취소':'등록취소')}</button>
							<c:if test="${mode=='update'}">
								<input type="hidden" name="qnum" value="${dto.qnum}">
								<input type="hidden" name="page" value="${page}">
								<input type="hidden" name="filename" value="${dto.filename}">
							</c:if>
							<c:if test="${mode=='answer'}">
								<input type="hidden" name="groupNum" value="${dto.groupNum}">
								<input type="hidden" name="orderNo" value="${dto.orderNo}">
								<input type="hidden" name="depth" value="${dto.depth}">
								<input type="hidden" name="parent" value="${dto.qnum}">
								<input type="hidden" name="page" value="${page}">
								<input type="hidden" name="answerUserId" value="${dto.answerUserId}">
								<input type="hidden" name="answerUserId2" value="${dto.answerUserId2}">
								<input type="hidden" name="filename" value="${dto.filename}">
								<input type="hidden" name="answeryes" value="${dto.answeryes}">
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