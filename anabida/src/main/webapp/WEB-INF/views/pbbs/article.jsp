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
.img-box {
	max-width: 700px;
	padding: 5px;
	box-sizing: border-box;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
	flex-wrap: nowrap;
	overflow-x: auto;
	align-items: baseline;
}
.img-box img {
	width: 100px; height: 100px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}

.photo-layout img { width: 570px; height: 450px; }
.table-article tr > td { padding-left: 5px; padding-right: 5px; }

.table-article .omg{max-width: 100%;height: auto;resize: both;}
</style>


<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin' }">
	<script type="text/javascript">
	function deletePhoto() {
		if(confirm(' 게시글을 삭제 하시겠습니까?')){
			location.href='${pageContext.request.contextPath}/pbbs/delete.do?num=${dto.num}&page=${page}${category}';
			
			
		}
	}
	function changeMind() {
		if(confirm(' 게시글을 찜 하시겠습니까?')){
			location.href='${pageContext.request.contextPath}/pbbs/like.do?num=${dto.num}&page=${page}${category}';
			
			
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
				
					
				
			<table class="table table-border table-article">
					<tr>
						<td colspan="2" height="110">
							<div class="img-box" >
								<img src="<c:url value='/uploads/pbbs/${dto.imageFilename}'/>" class="img" style=" width:390px; height: 300px;overflow:hidden;object-fit:cover">
								<c:forEach var="vo" items="${listFile}">
									<img src="${pageContext.request.contextPath}/uploads/pbbs/${vo.imageFilename}" style="bottom : 0 ;width:260px; height: 200px;overflow:hidden;object-fit:cover" 
										onclick="imageViewer('${pageContext.request.contextPath}/uploads/pbbs/${vo.imageFilename}');"
										>
								</c:forEach>
							</div>
						</td>	
					</tr>
				<tbody>
					<tr>
						<td width="50%">
							이름 : ${dto.userName}
						</td>
						<td align="right">
							${dto.regdate} 
							<a style="display:inline-block ;text-align:right" onclick="changeMind();" title="찜">
							<i class="${dto.plike==0?'fa-regular':'fa-solid'} fa-heart"></i>
							</a>
						</td>
					</tr>
				</tbody>
			</table>
					<p style="font-size: 16px;font-weight: bold">
						&nbsp;${dto.subject}
					</p>
					<p style="font-size: 13px;color:gray">
						&nbsp;${dto.catString}
					</p>
					<p style="font-size: 16px;font-weight: bold"	>
						&nbsp;${dto.cost} 원
					</p>
					<br><br>
					<p style="font-size: 15px;"	>
						${dto.content}
					</p>
					<br><br>
						
					<p style="font-size: 13px;color:gray">
						찜 : ${dto.plike} 
								조회수: ${dto.hitCount}
					</p>
					<hr>
			
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
							<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin' }">
								<button type="button" class="btn" onclick="deletePhoto();">삭제 </button>
							</c:when>
							<c:otherwise>
								<button type="button" class="btn" disabled="disabled">삭제</button>
							</c:otherwise>
						</c:choose>
					
						
					</td>
					<td align="right">
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/pbbs/list.do?page=${page}${category}';">리스트</button>
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