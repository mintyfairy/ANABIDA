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

* {box-sizing:border-box}

/* Slideshow container */
.slideshow-container {
  max-width: 1000px;
  position: relative;
  margin: auto;
  z-index:-1;
}

/* Hide the images by default */
.mySlides {
  display: none;
}

/* Next & previous buttons */
 .prev, .next {
  cursor: pointer;
  position: absolute;
  top: 50%;
  width: auto;
  margin-top: -22px;
  padding: 16px;
  color: white;
  font-weight: bold;
  font-size: 18px;
  transition: 0.6s ease;
  border-radius: 0 3px 3px 0;
 user-select: none;
}

/* Position the "next button" to the right */
.next {
  right: 0;
  border-radius: 3px 0 0 3px;
}

/* On hover, add a black background color with a little bit see-through */
.prev:hover, .next:hover {
  background-color: rgba(0,0,0,0.8);
}

/* Caption text */
.text {
  color: #f2f2f2;
  font-size: 15px;
  padding: 8px 12px;
  position: absolute;
  bottom: 8px;
  width: 100%;
  text-align: center;
}

/* Number text (1/3 etc) */
.numbertext {
  color: #f2f2f2;
  font-size: 12px;
  padding: 8px 12px;
  position: absolute;
  top: 0;
}

/* The dots/bullets/indicators */
.dot {
  cursor: pointer;
  height: 15px;
  width: 15px;
  margin: 0 2px;
  background-color: #bbb;
  border-radius: 50%;
  display: inline-block;
  transition: background-color 0.6s ease;
}

 .active, .dot:hover {
  background-color: #717171;
}

/* Fading animation */
.fade {
  -webkit-animation-name: fade;
  -webkit-animation-duration: 1.5s;
  animation-name: fade;
  animation-duration: 1.5s;
}

@-webkit-keyframes fade {
  from {opacity: .4} 
  to {opacity: 1}
}

@keyframes fade {
  from {opacity: .4} 
  to {opacity: 1}
} 
</style>


<script type="text/javascript">
	<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin' }">
		function deletePhoto() {
			if(confirm(' 게시글을 삭제 하시겠습니까?')){
				location.href='${pageContext.request.contextPath}/pbbs/delete.do?num=${dto.num}&page=${page}${category}';
				
				
			}
		};
	
		
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
		};
	function changeMind() {
		
			location.href='${pageContext.request.contextPath}/pbbs/like.do?num=${dto.num}&page=${page}${category}';
		
	};
</script>
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
							<div class="slideshow-container" >
								
									<div class="mySlides fade">
									    <img src="<c:url value='/uploads/pbbs/${dto.imageFilename}'/>" class="img" style=" width:100%; height: 450px;overflow:hidden;object-fit:cover " 
										onclick="imageViewer('${pageContext.request.contextPath}/uploads/pbbs/${dto.imageFilename}');">
									    <div class="text" style="background: black;color:white;opacity: 0.5 "> <span >${listNum} / ${listSize}</span></div>
									</div>
								<c:forEach var="vo" items="${listFile}">
									<div class="mySlides fade">
									    <img src="${pageContext.request.contextPath}/uploads/pbbs/${vo.imageFilename}" style="bottom : 0 ;width:100%;height: 450px; overflow:hidden;object-fit:cover" 
										onclick="imageViewer('${pageContext.request.contextPath}/uploads/pbbs/${vo.imageFilename}');">
									    <div class="text" style="background: black;color:white;opacity: 0.5 "> <span >${listNum=listNum+1} / ${listSize}</span></div>
									</div>
								</c:forEach>
								<!-- Next and previousbuttons -->
	 							<a class="prev" onclick="plusSlides(-1)">&#10094;</a>
	  							<a class="next" onclick="plusSlides(1)">&#10095;</a>
							</div>
							<br>

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

<div class="dialog-photo">
      <div class="photo-layout"></div>
</div>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
<script>
var slideIndex = 1;
showSlides(slideIndex);

// Next/previous controls
function plusSlides(n) {
  showSlides(slideIndex += n);
}

// Thumbnail image controls
function currentSlide(n) {
  showSlides(slideIndex = n);
}

function showSlides(n) {
  var i;
  var slides = document.getElementsByClassName("mySlides");
  var dots = document.getElementsByClassName("dot");
  if (n > slides.length) {slideIndex = 1} 
  if (n < 1) {slideIndex = slides.length}
  for (i = 0; i < slides.length; i++) {
      slides[i].style.display = "none"; 
  }
  for (i = 0; i < dots.length; i++) {
      dots[i].className = dots[i].className.replace(" active", "");
  }
  slides[slideIndex-1].style.display = "block"; 
} 
</script>

</body>
</html>