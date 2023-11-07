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
}
.img-box {
	max-width: 700px;
	padding: 5px;
	box-sizing: border-box;
	border: 1px solid #ccc;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
	flex-wrap: nowrap;
	overflow-x: auto;
}
.img-box img {
	width: 100px; height: 100px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}
.x{
	width: 100%;
	height: 100%;
	border: 1px solid black;
	align-items: center;
}

.photo-layout img { width: 100%; height: 100%; }
.table-article tr > td { 	padding-left: 5px; padding-right: 5px; }
</style>
<script type="text/javascript">
<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
		function deleteBoard() {
		    if(confirm("게시글을 삭제 하시 겠습니까 ? ")) {
		        let query = "qnum=${dto.qnum}&${query}";
		        let url = "${pageContext.request.contextPath}/csbbs/delete.do?" + query;
		    	location.href = url;
		    }
		}
</c:if>
		
function imageViewer(img) {
	const viewer = $(".photo-layout");
	let s="<img src='"+img+"'>";
	viewer.html(s);
			
	$(".dialog-photo").dialog({
		title:"이미지",
		width: 600,
		height: 530,
		modal: true
	});
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
			<h2><i class="fa-regular fa-square"></i> 문의 게시판 </h2>
	    </div>
	    
	    <div class="body-main mx-auto">
			<table class="table table-border table-article">
				<thead>
					<tr>
						<td colspan="2" align="center">
							<c:if test="${dto.depth!=0 }">[Re] </c:if>
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
						<td colspan="2" valign="top" height="200">
							${dto.content}
						</td>
					</tr>
					<tr>
						<td colspan="2" height="110">
							<div class="img-box">
							<c:if test="${not empty listFile}">
								<c:forEach var="vo" items="${listFile}">
									<img src="${pageContext.request.contextPath}/uploads/csbbs/${vo.filename}"
										onclick="imageViewer('${pageContext.request.contextPath}/uploads/csbbs/${vo.filename}');">
								</c:forEach>
							</c:if>
							</div>
						</td>	
					</tr>
					
				</tbody>
			</table>
			
			<table class="table">
				<tr>
					<td width="50%">
						<c:choose>
							<c:when test="${sessionScope.member.userId==dto.userId}">
								<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/csbbs/update.do?qnum=${dto.qnum}&page=${page}';">수정</button>
							</c:when>
							<c:otherwise>
								<button type="button" class="btn" disabled>수정</button>
							</c:otherwise>
						</c:choose>
				    	
						<c:choose>
				    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
				    			<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
				    		</c:when>
				    		<c:otherwise>
				    			<button type="button" class="btn" disabled>삭제</button>
				    		</c:otherwise>
				    	</c:choose>
				    	<c:choose>
				    		<c:when test="${sessionScope.member.userId=='admin'}">
								<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/csbbs/answer.do?qnum=${dto.qnum}&page=${page}&answerUserId=${dto.answerUserId}&answerUserId2=${dto.answerUserId}';">답변</button>
				        	</c:when>
				        	<c:otherwise>
				        		<button type="button" class="btn" disabled>답변</button>
				        	</c:otherwise>
				        </c:choose>				
					</td>
					<td align="right">
					<c:choose>
						
						<c:when test="${page == ''}">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/csbbs/list.do?page=1';">리스트</button>
						</c:when>
						
						<c:when test="${page !=null }">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/csbbs/list.do?${query}';">리스트</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/csbbs/list.do?page=1';">리스트</button>
						</c:otherwise>
					</c:choose>
					</td>
				</tr>
			</table>

		</div>
	</div>
</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<div class="dialog-photo">
      <div class="photo-layout"></div>
</div>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>