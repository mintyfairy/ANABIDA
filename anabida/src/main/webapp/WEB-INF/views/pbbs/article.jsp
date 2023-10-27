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
	max-width: 700px;
	padding-top: 15px;
}

.table-article tr > td { padding-left: 5px; padding-right: 5px; }

.table-article .omg{max-width: 100%;height: auto;resize: both;}
</style>


<c:if test="${sesseionScope.member.userId==dto.userId || sessionScope.member.userId=='admin' }">
	<script type="text/javascript">
	function deletePhoto() {
		if(confirm(' 게시글을 삭제 하시겠습니까?')){
			location.href='${pageContext.request.contextPath}/pbbs/delete.do?num=${dto.num}&page=${page}';
			
			
		}
	}
	</script>
</c:if>
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>
	
<main>
	<div class="container body-container">
	    <div class="body-main mx-auto">
				
					<div style="width:380px; height:220px; overflow:hidden; margin:0 auto;">
						
							<img src="<c:url value='/uploads/pbbs/${dto.imageFilename}'/>" class="img" style="width:100%; height:100%;object-fit:cover">
						
					</div>
				
			<table class="table table-border table-article">
				
				<tbody>
					<tr>
						<td width="50%">
							이름 : ${dto.userName}
						</td>
						<td align="right">
							${dto.regdate}
						</td>
					</tr>
				</tbody>
			</table>
					<p style="font-size: 16px;font-weight: bold">
						${dto.subject}
					</p>
					<p style="font-size: 13px;color:gray">
						${dto.catString}
					</p>
					<p style="font-size: 16px;font-weight: bold"	>
						${dto.cost} 원
					</p>
			<table class="table table-border table-article">
				
				<tbody>	
					<tr>
						<td colspan="2" style="font-size: 16px;" >
							${dto.content}
						</td>
					</tr>
					<tr>
						<td style="font-size: 13px;color:gray">
								찜 : ${dto.plike} 
								조회수: ${dto.hitCount}
						</td>
					</tr>
				</tbody>
			</table>
			<table class="table">
				<tr>
					<td width="50%">
						<c:choose>
							<c:when test="${sessionScope.member.userId==dto.userId}">
								<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/pbbs/update.do?num=${dto.num}&page=${page}'">수정</button>
							</c:when>
							<c:otherwise>
								<button type="button" class="btn" disabled="disabled">수정</button>
							</c:otherwise>
						</c:choose>
					
						<c:choose>
							<c:when test="${sesseionScope.member.userId==dto.userId || sessionScope.member.userId=='admin' }">
								<button type="button" class="btn" onclick="deletePhoto();">삭제 </button>
							</c:when>
							<c:otherwise>
								<button type="button" class="btn" disabled="disabled">삭제 </button>
							</c:otherwise>
						</c:choose>
					
						
					</td>
					<td align="right">
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/pbbs/list.do?page=${page}';">리스트</button>
					</td>
				</tr>
			</table>

		</div>
	</div>
</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>