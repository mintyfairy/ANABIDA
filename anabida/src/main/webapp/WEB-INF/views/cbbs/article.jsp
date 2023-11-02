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

.table-article tr > td { 	padding-left: 5px; padding-right: 5px; }




* {box-sizing: border-box}
body {font-family: Verdana, sans-serif; margin:0}
.mySlides {display: none}
img {vertical-align: middle;}

/* Slideshow container */
.slideshow-container {
  max-width: 1000px;
  position: relative;
  margin: auto;
}

/* Next & previous buttons */
.prev, .next {
  cursor: pointer;
  position: absolute;
  top: 50%;
  width: auto;
  padding: 16px;
  margin-top: -22px;
  color: white;
  font-weight: bold;
  font-size: 18px;
  transition: 0.6s ease;
  border-radius: 0 3px 3px 0;
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

/* On smaller screens, decrease text size */
@media only screen and (max-width: 300px) {
  .prev, .next,.text {font-size: 11px}
}
</style>
</head>

<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
<script type="text/javascript">
//게시글 삭제
function deleteBoard() {
	console.log("asdas");
	    if(confirm("게시글을 삭제 하시 겠습니까 ? ")) {
		    let query = "num=${dto.num}&${query}";
		    let url = "${pageContext.request.contextPath}/cbbs/cdelete.do?" + query;
	    	location.href = url;
	    }
	}
</script>
</c:if>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>
	
<main>
	<div class="container body-container">
	    
	    
	    <div class="body-main mx-auto">
			<table class="table table-border table-article">
				<thead>
					<tr >
						<td colspan="2" align="center" style="border-top: none;">
							${dto.ctitle }
						</td>
					</tr>
				</thead>
				
				<tbody>
					<tr>
						<td width="50%">
							이름 : ${dto.userName }
						</td>
						<td align="right">
							<button type="button" class="btn btnSendBoardLike" title="좋아요"><i class="fa-regular fa-heart" style="color: ${isUserLike? 'red' : 'black'}"></i>&nbsp;&nbsp;<span id="boardLikeCount">${dto.likeCount}</span></button>
							
							<c:if test="${dto.ccategory == 1}">모임마당</c:if>
							<c:if test="${dto.ccategory == 2}">질문마당</c:if>
							<c:if test="${dto.ccategory == 3}">사건사고마당</c:if>
							<c:if test="${dto.ccategory == 4}">취미마당</c:if>
							<c:if test="${dto.ccategory == 5}">생활마당</c:if>
							|${dto.creg_date} | 조회 ${dto.chitCount}
						</td>
					</tr>
					
					
					<tr style="border-bottom: none;">
							<td colspan="2" style="padding-bottom: 0;">
								<!--  <img src="${pageContext.request.contextPath}/uploads/cbbs/${dto.picFileName}" class="img">-->

								<div class="slideshow-container">
								
								
								
								<c:forEach var="vo" items="${listFile}">
									<div class="mySlides fade">
										<img src="${pageContext.request.contextPath}/uploads/cbbs/${vo.picFileName}" style="width:800px; height:400px; object-fit: cover; " 
										onclick="imageViewer('${pageContext.request.contextPath}/uploads/cbbs/${vo.picFileName}');">
									</div>								
								</c:forEach>
								
									<a class="prev" onclick="plusSlides(-1)">&#10094;</a> 
									<a class="next" onclick="plusSlides(1)">&#10095;</a>
								
								</div> 
								<br>

								<div style="text-align: center">
								<c:forEach var="i" begin="1" end="${cdataCount}">
								  <span class="dot" onclick= "currentSlide(${i})"></span>
								</c:forEach>
								</div>
								
							</td>
						</tr>
					
					<tr>
						
						<td colspan="2" valign="top" height="200">
							${dto.ccontent}
							
							<c:if test="${not empty meet.addr1}">
							<div id="map" style="width:100%;height:350px;"></div>
							</c:if>
						</td>
					</tr>
					<c:if test="${dto.ccategory == 1}">
					<tr>
						 
						<td> 
							<form name="insertmember" method="post">
								<input type="hidden" name="mnum" value = "${meet.mnum}">
								<input type="hidden" name="userId" value = "${sessionScope.member.userId}">
								<input type="hidden" name="num" value = "${dto.num}">
								<input type="hidden" name="page" value = "${page}">
								<button type="button" class="btn btnSendparticipate" onclick="participate();" >참여하기</button>
							</form> 
						</td>
					</tr>
														
					
					<tr>
						<td>참여인원 (${meetmember}/${meet.cmember})  |
						<c:forEach var="username" items="${username}" varStatus="status">
								 ${username.userName} |
						</c:forEach>
						</td>
					</tr>
						</c:if>
					<tr>
						<td colspan="2">
							이전글 : 
							<!--  
							<c:if test="${not empty prevDto }">
								<a href="${pageContext.request.contextPath}/bbs/article.do?${query}&num=${prevDto.num}">${prevDto.subject} </a>
							</c:if>
							-->
						</td>
					</tr>
					<tr>
						<td colspan="2">
							
							다음글 :
							<!-- 
							<c:if test="${not empty nextDto }">
								<a href="${pageContext.request.contextPath}/bbs/article.do?${query}&num=${nextDto.num}">${nextDto.subject} </a>
							</c:if>
							 -->
						</td>
					</tr>
			
				<tr>
						<td width="50%"><c:choose>

								<c:when test="${dto.userId eq sessionScope.member.userId}">
									<button type="button" class="btn"
										onclick="location.href='${pageContext.request.contextPath}/bbs/update.do?num=${dto.num}&page=${page}';">수정</button>
									<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
								</c:when>

				    			<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
									<button type="button" class="btn" onclick="deleteBoard();">삭제</button>
								</c:when>

							</c:choose>
						</td>
						<td align="right">
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/cbbs/clist.do?${query}'" >리스트</button>
					</td>
				</tr>
				</tbody>	
			</table>

		</div>
	</div>
</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=60e66caa443255c7eacf898657309fe6&libraries=services"></script>

<script>


		

var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
    mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };  

// 지도를 생성합니다    
var map = new kakao.maps.Map(mapContainer, mapOption); 

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();

// 주소로 좌표를 검색합니다
geocoder.addressSearch('${meet.addr1}', function(result, status) {

    // 정상적으로 검색이 완료됐으면 
     if (status === kakao.maps.services.Status.OK) {

        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);

        // 결과값으로 받은 위치를 마커로 표시합니다
        var marker = new kakao.maps.Marker({
            map: map,
            position: coords
        });

        // 인포윈도우로 장소에 대한 설명을 표시합니다
        var infowindow = new kakao.maps.InfoWindow({
            content: '<div style="width:150px;text-align:center;padding:6px 0;">모임장소:${meet.addr1}</div>'
        });
        infowindow.open(map, marker);

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setCenter(coords);
    } 
});   
</script>

<script type="text/javascript">

var slideIndex = 1;
showSlides(slideIndex);

function plusSlides(n) {
  showSlides(slideIndex += n);
}

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
  dots[slideIndex-1].className += " active";
}

function participate() {
  	const f = document.insertmember;
  	f.action = "${pageContext.request.contextPath}/cbbs/participate.do";
	f.submit();
}

</script>

<script type="text/javascript">

function login() {
	location.href="${pageContext.request.contextPath}/member/login.do";
}

function ajaxFun(url, method, query, dataType, fn) {
	$.ajax({
		type:method,
		url:url,
		data:query,
		dataType:dataType,
		success:function(data) {
			fn(data);
		},
		beforeSend:function(jqXHR) {
			jqXHR.setRequestHeader("AJAX", true);
		},
		complete: function () {
		},
		error:function(jqXHR) {
			if(jqXHR.status === 403) {
				login();
				return false;
			} else if(jqXHR.status === 400) {
				alert("요청 처리가 실패 했습니다.");
				return false;
			}
	    	
			console.log(jqXHR.responseText);
		}
	});
	
}

//게시글 공감 여부
$(function(){
	$(".btnSendBoardLike").click(function(){
		const $i = $(this).find("i");
		let isNoLike = $i.css("color") == "rgb(0, 0, 0)";
		let msg = isNoLike ? "게시글에 공감하십니까 ? " : "게시글 공감을 취소하시겠습니까 ? ";
		
		if(! confirm( msg )) {
			return false;
		}
		
		let url = "${pageContext.request.contextPath}/cbbs/insertBoardLike.do";
		let num = "${dto.num}";
		// var query = {num:num, isNoLike:isNoLike};
		let query = "num=" + num + "&isNoLike=" + isNoLike;;
		
		const fn = function(data) {
			let state = data.state;
			if(state === "true") {
				let color = "black";
				if( isNoLike ) {
					color = "red";
				}
				$i.css("color", color);
				let count = data.likeCount;
				$("#boardLikeCount").text(count);
			} else if(state === "liked") {
				alert("좋아요는 한번만 가능합니다. !!!");
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});



</script>
</html>