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
<style type="text/css">
.body-main {
	max-width: 700px;
	padding-top: 15px;
}

.table-article tr>td { padding-left: 5px; padding-right: 5px; }
.file-item { padding: 7px; margin-bottom: 3px; border: 1px solid #ced4da; color: #777777; }

.reply { padding: 20px 0 10px; }
.reply .bold { font-weight: 600; }
.reply .form-header { padding-bottom: 7px; }

.reply-form  tr>td { padding: 2px 0 2px; }
.reply-form textarea { width: 100%; height: 75px; }
.reply-form button { padding: 8px 25px; }

.reply .reply-info { padding-top: 25px; padding-bottom: 7px; }
.reply .reply-info  .reply-count { color: #3EA9CD; font-weight: 700; }

.reply .reply-list tr>td { padding: 7px 5px; }
.reply .reply-list .bold { font-weight: 600; }

.reply .deleteReply, .reply .deleteReplyAnswer { cursor: pointer; }
.reply .notifyReply { cursor: pointer; }

.reply-list .list-header { border: 1px solid #cccccc; background: #f8f8f8; }
.reply-list tr>td { padding-left: 7px; padding-right: 7px; }

.reply-answer { display: none; }
.reply-answer .answer-list { border-top: 1px solid #cccccc; padding: 0 10px 7px; }
.reply-answer .answer-form { display: flex; padding: 3px 10px 5px; }
.reply-answer .answer-left { display: flex; width: 5%; }
.reply-answer .answer-right { display: flex; width: 95%; align-items: center; }
.reply-answer .answer-form textarea { width: 100%; height: 75px; }
.reply-answer .answer-footer { padding: 0 13px 10px 10px; text-align: right; }

.answer-article .answer-article-header { padding-top: 5px; display: flex; align-items: center; }
.answer-article .answer-left { align-items: center; }
.answer-article .answer-right { justify-content: space-between; align-items: center; }
.answer-article .answer-article-body { padding: 5px 5px 7px; border-bottom: 1px solid #cccccc; word-break: break-all; } 

.w-btn {
    position: relative;
    border: none;
    display: inline-block;
    padding: 5px 15px;
    border-radius: 15px;
    font-family: "paybooc-Light", sans-serif;
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
    text-decoration: none;
    font-weight: 600;
    transition: 0.25s;
}

.w-btn-indigo {
    background-color: aliceblue;
    color: #1e6b7b;
}

</style>


</head>
 <link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">

  <!-- Google Fonts -->
  <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Raleway:300,300i,400,400i,500,500i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">

  <!-- Vendor CSS Files -->
  <link href="${pageContext.request.contextPath}/resource/assets5/vendor/aos/aos.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets5/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets5/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets5/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets5/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets5/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">

  <!-- Template Main CSS File -->
  <link href="${pageContext.request.contextPath}/resource/assets5/css/style.css" rel="stylesheet">
<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
<script type="text/javascript">
//게시글 삭제
function deleteBoard() {
	console.log("asdas");
	    if(confirm("게시글을 삭제 하시 겠습니까 ? ")) {
		    let query = "num=${dto.num}&${query}";
		    let url = "${pageContext.request.contextPath}/cbbs/cdelete_k.do?" + query;
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
	<%-- <div class="container body-container">
	    
	    
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
							<br>
							<br><br>
							<br><br><br>
							<br><br><br><br>
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
			
			<div class="reply" style="width: 820px;">
				<form name="replyForm" method="post">
					<div class='form-header'>
						<span class="bold">댓글쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
					</div>
					
					<table class="table reply-form">
						<tr>
							<td>
								<textarea class='form-control' name="content"></textarea>
							</td>
						</tr>
						<tr>
						   <td align='right'>
						        <button type='button' class='btn btnSendReply'>댓글 등록</button>
						    </td>
						 </tr>
					</table>
				</form>
				
				<div id="listReply"></div>
			</div>

		</div>
	</div> --%>
	
	<br>
	<!-- ======= Portfolio Details Section ======= -->
    <section id="portfolio-details" class="portfolio-details">
      <div class="container">

        <div class="row gy-4">

          <div class="col-lg-8">
         
            <div class="portfolio-details-slider swiper">
              <div class="swiper-wrapper align-items-center">
 				<c:forEach var="vo" items="${listFile}">
                	<div class="swiper-slide">
                	  <img src="${pageContext.request.contextPath}/uploads/cbbs/${vo.picFileName}" style="width:900px; height:500px; object-fit: cover; alt="">
                	</div>
				</c:forEach>
             

              </div>
              
              <div class="swiper-pagination"></div>
              <br>
                <c:if test="${dto.ccategory == 1}">
              <strong>모임장소 <i class="fa-solid fa-map-location-dot"></i></strong>
              <span>모임날짜 : ${meet.mreg_date }</span>
              <div id="map" style="width:100%;height:350px;"></div>
              </c:if>
              
            </div>
            
          </div>

          <div class="col-lg-4">
            <div class="portfolio-info">
              <h3>${dto.ctitle }               
              <c:choose>
              	<c:when test="${page == '' }">
              	    <button type="button" style="padding-left:5px; " class="btn" onclick="location.href='${pageContext.request.contextPath}/cbbs/clist.do?page=1'" ><i class="fa-solid fa-list"></i></button>
              	</c:when>
              	<c:when test="${page !=null }">
              	    <button type="button" style="padding-left:5px; " class="btn" onclick="location.href='${pageContext.request.contextPath}/cbbs/clist.do?${query}'" ><i class="fa-solid fa-list"></i></button>
              	</c:when>
				<c:otherwise>
              		<button type="button" style="padding-left:5px; " class="btn" onclick="location.href='${pageContext.request.contextPath}/cbbs/clist.do?page=1'" ><i class="fa-solid fa-list"></i></button>
              	</c:otherwise>
              	
			  </c:choose>
              
              </h3> 
              <button style="padding-left: 0px; padding-bottom: 9px;" type="button" class="btn btnSendBoardLike" title="좋아요"><i class="fa-regular fa-heart" style="color: ${isUserLike? 'red' : 'black'}"></i>&nbsp;&nbsp;<span id="boardLikeCount">${dto.likeCount}</span></button>
              <span style="">조회수 : ${dto.chitCount}</span>
              <ul>
                <li><strong>작성자 : </strong> ${dto.userName}</li>
                <li><strong>카테고리 : </strong>
                 <c:if test="${dto.ccategory == 1}">모임마당</c:if>
				 <c:if test="${dto.ccategory == 2}">질문마당</c:if>
				 <c:if test="${dto.ccategory == 3}">사건사고마당</c:if>
				 <c:if test="${dto.ccategory == 4}">취미마당</c:if>
				 <c:if test="${dto.ccategory == 5}">생활마당</c:if></li>
                <li><strong>작성날짜 : </strong> ${dto.creg_date} </li>
                
                <!--  -->
                <c:if test="${dto.ccategory == 1}">
				<li style="float: left; padding-right: 10px; padding-top: 8px;"><strong>모임 참여인원 : ${meetmember}/${meet.cmember} </strong>  </li>
            	<li >
                	<form name="insertmember" method="post">
								<input type="hidden" name="mnum" value = "${meet.mnum}">
								<input type="hidden" name="userId" value = "${sessionScope.member.userId}">
								<input type="hidden" name="num" value = "${dto.num}">
								<input type="hidden" name="page" value = "${page}">
								<button class="w-btn w-btn-indigo" type="button" onclick="participate();">참여</button>
								
								<c:forEach var="username" items="${username}" varStatus="status">
										<c:if test="${username.userName == sessionScope.member.userName}">
											<button class="w-btn w-btn-indigo" type="button" onclick="deletemember();">참여취소</button>
										</c:if>
								</c:forEach> 
								
					</form>
								
                </li>
            	<li><i class="fa-solid fa-users fa-bounce"></i>
            		<c:forEach var="username" items="${username}" varStatus="status">
								 ${username.userName} |
					</c:forEach> 
				</li>
				
					 
				</c:if>
				
				<li style="padding-top: 10px;">
					<c:choose>

										<c:when test="${dto.userId eq sessionScope.member.userId}">
											<button type="button" class="w-btn w-btn-gray"
												onclick="location.href='${pageContext.request.contextPath}/bbs/update.do?num=${dto.num}&page=${page}';">수정</button>
											<button type="button" class="w-btn w-btn-gray" onclick="deleteBoard();">삭제</button>
										</c:when>

										<c:when
											test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
											<button type="button" class="w-btn w-btn-gray" onclick="deleteBoard();">삭제</button>
										</c:when>

					</c:choose>
				</li>
				
				
				
            </ul>
            </div>
            <div class="portfolio-description">
              <h2>글</h2>
              <p>
               <i class="fa-solid fa-share"></i> ${dto.ccontent}

              </p>
            </div>
          </div>
          
          
          

        </div>
        <br><br>
        <!-- 댓글 -->
        <div class="reply" style="width: 820px;">
				<form name="replyForm" method="post">
					<div class='form-header'>
						<span class="bold">댓글쓰기</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
					</div>
					
					<table class="table reply-form">
						<tr>
							<td>
								<textarea class='form-control' name="content"></textarea>
							</td>
						</tr>
						<tr>
						   <td align='right'>
						        <button type='button' class='btn btnSendReply'>댓글 등록</button>
						    </td>
						 </tr>
					</table>
				</form>
				
				<div id="listReply"></div>
		</div>

      </div>
      
    </section><!-- End Portfolio Details Section -->
</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=?&libraries=services"></script>

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
	if(confirm("모임에 참여 하시겠습니까 ? ")) {
  	let f = document.insertmember;
  	f.action = "${pageContext.request.contextPath}/cbbs/participate.do";
	f.submit();
	 }
}

function deletemember() {
	 if(confirm("모임에 참여취소 하시겠습니까 ? ")) {   
		    
 	let f = document.insertmember;
 	f.action = "${pageContext.request.contextPath}/cbbs/participate_un.do";
	f.submit();
	 }
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

$(function(){
	listPage(1);
});

function listPage(page) {
	let url = "${pageContext.request.contextPath}/cbbs/clistReply.do";
	let query = "num=${dto.num}&pageNo="+page;
	let selector = "#listReply";
	
	const fn = function(data){
		$(selector).html(data);
	};
	ajaxFun(url, "get", query, "text", fn);
}

//리플 등록
$(function(){
	$(".btnSendReply").click(function(){
		let num = "${dto.num}";
		const $tb = $(this).closest("table");
		let content = $tb.find("textarea").val().trim();
		if(! content) {
			$tb.find("textarea").focus();
			return false;
		}
		content = encodeURIComponent(content);
		
		let url = "${pageContext.request.contextPath}/cbbs/insertReply.do";
		let query = "num=" + num + "&content=" + content + "&answer=0";
		
		const fn = function(data){
			$tb.find("textarea").val("");
			
			let state = data.state;
			if(state === "true") {
				listPage(1);
			} else if(state === "false") {
				alert("댓글을 추가 하지 못했습니다.");
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});


//댓글 삭제
$(function(){
	$(".reply").on("click", ".deleteReply", function(){
		if(! confirm("게시물을 삭제하시겠습니까 ? ")) {
		    return false;
		}
		
		let replyNum = $(this).attr("data-replyNum");
		let page = $(this).attr("data-pageNo");
		
		let url = "${pageContext.request.contextPath}/cbbs/deleteReply.do";
		let query = "replyNum="+replyNum;
		
		const fn = function(data){
			// let state = data.state;
			listPage(page);
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

// 댓글 좋아요 / 싫어요
$(function(){
	// 댓글 좋아요 / 싫어요 등록
	$(".reply").on("click", ".btnSendReplyLike", function(){
		let replyNum = $(this).attr("data-replyNum");
		let replyLike = $(this).attr("data-replyLike");
		const $btn = $(this);
		
		let msg = "게시물이 마음에 들지 않으십니까 ?";
		if(replyLike === "1") {
			msg="게시물에 공감하십니까 ?";
		}
		
		if(! confirm(msg)) {
			return false;
		}
		
		let url = "${pageContext.request.contextPath}/cbbs/insertReplyLike.do";
		let query = "replyNum=" + replyNum + "&replyLike=" + replyLike;
		
		const fn = function(data){
			let state = data.state;
			if(state === "true") {
				let likeCount = data.likeCount;
				let disLikeCount = data.disLikeCount;
				
				$btn.parent("td").children().eq(0).find("span").html(likeCount);
				$btn.parent("td").children().eq(1).find("span").html(disLikeCount);
			} else if(state === "liked") {
				alert("게시물 공감 여부는 한번만 가능합니다. !!!");
			} else {
				alert("게시물 공감 여부 처리가 실패했습니다. !!!");
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

// 댓글별 답글 리스트
function listReplyAnswer(answer) {
	let url = "${pageContext.request.contextPath}/cbbs/listReplyAnswer.do";
	let query = "answer=" + answer;
	let selector = "#listReplyAnswer" + answer;
	
	const fn = function(data){
		$(selector).html(data);
	};
	ajaxFun(url, "get", query, "text", fn);
}

// 댓글별 답글 개수
function countReplyAnswer(answer) {
	let url = "${pageContext.request.contextPath}/cbbs/countReplyAnswer.do";
	let query = "answer=" + answer;
	
	const fn = function(data){
		let count = data.count;
		let selector = "#answerCount"+answer;
		$(selector).html(count);
	};
	
	ajaxFun(url, "post", query, "json", fn);
}

// 답글 버튼(댓글별 답글 등록폼 및 답글리스트)
$(function(){
	$(".reply").on("click", ".btnReplyAnswerLayout", function(){
		const $trReplyAnswer = $(this).closest("tr").next();
		
		let isVisible = $trReplyAnswer.is(':visible');
		let replyNum = $(this).attr("data-replyNum");
			
		if(isVisible) {
			$trReplyAnswer.hide();
		} else {
			$trReplyAnswer.show();
            
			// 답글 리스트
			listReplyAnswer(replyNum);
			
			// 답글 개수
			countReplyAnswer(replyNum);
		}
	});
	
});

// 댓글별 답글 등록
$(function(){
	$(".reply").on("click", ".btnSendReplyAnswer", function(){
		let num = "${dto.num}";
		let replyNum = $(this).attr("data-replyNum");
		const $td = $(this).closest("td");
		
		let content = $td.find("textarea").val().trim();
		if(! content) {
			$td.find("textarea").focus();
			return false;
		}
		content = encodeURIComponent(content);
		
		let url = "${pageContext.request.contextPath}/cbbs/insertReply.do";
		let query = "num=" + num + "&content=" + content + "&answer=" + replyNum;
		
		const fn = function(data){
			$td.find("textarea").val("");
			
			let state = data.state;
			if(state === "true") {
				listReplyAnswer(replyNum);
				countReplyAnswer(replyNum);
			}
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

// 댓글별 답글 삭제
$(function(){
	$(".reply").on("click", ".deleteReplyAnswer", function(){
		if(! confirm("게시물을 삭제하시겠습니까 ? ")) {
		    return false;
		}
		
		let replyNum = $(this).attr("data-replyNum");
		let answer = $(this).attr("data-answer");
		
		let url = "${pageContext.request.contextPath}/cbbs/deleteReply.do";
		let query = "replyNum=" + replyNum;
		
		const fn = function(data){
			listReplyAnswer(answer);
			countReplyAnswer(answer);
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});


</script>

 <!-- Vendor JS Files -->
  <script src="${pageContext.request.contextPath}/resource/assets5/vendor/purecounter/purecounter_vanilla.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets5/vendor/aos/aos.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets5/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets5/vendor/glightbox/js/glightbox.min.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets5/vendor/isotope-layout/isotope.pkgd.min.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets5/vendor/swiper/swiper-bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets5/vendor/typed.js/typed.umd.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets5/vendor/waypoints/noframework.waypoints.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets5/vendor/php-email-form/validate.js"></script>

  <!-- Template Main JS File -->
  <script src="${pageContext.request.contextPath}/resource/assets5/js/main.js"></script>


</html>