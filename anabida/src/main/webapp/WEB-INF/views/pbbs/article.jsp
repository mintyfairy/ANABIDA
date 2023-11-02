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
.changeMind{
	display:inline-block ;
	text-align:right
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
  z-index:2;
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
.file-item { padding: 7px; margin-bottom: 3px; border: 1px solid #ced4da; color: #777777; }

.reply { padding: 20px 0 10px; }
.reply .bold { font-weight: 600; }
.reply .form-header { padding-bottom: 7px; }

.reply-form  tr>td { padding: 2px 0 2px; }
.reply-form textarea { width: 100%; height: 75px; }
.reply-form button { padding: 8px 25px; }
.buttonDisabled{ color:gray;}
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
								<c:if test="${pstate==1}">
									<div style="position: absolute; top: 0; ;opacity: 0.5; "><img src="${pageContext.request.contextPath}/resource/images/saled.png" style="width: 100%;height:450px;"/></div>
								</c:if>
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
						<td align="right" class="JJim">
							${dto.regdate} 
							<a  class="changeMind" title="찜">
								<i class="${plike==0?'fa-regular':'fa-solid'} fa-heart"></i>
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
				<div class="reply">
				<form name="replyForm" method="post">
					<div class='form-header'>
						<span class="bold">제안등록</span><span> - 타인을 비방하거나 개인정보를 유출하는 글의 게시를 삼가해 주세요.</span>
						<input type="hidden" name="pstate" value="${pstate}"><!-- 정보 보관용 -->
					</div>
					
					<table class="table reply-form">
						<tr>
							<td>
								<textarea class='form-control' name="content"></textarea>
							</td>
						</tr>
						<tr>
							<td align='right'>
								<c:choose>
									<c:when test="${sessionScope.member.userId!=dto.userId &&dto.chkReply=='0'}">
								        <button type='button' class='btn btnSendReply'>제안 등록</button>
									</c:when>
									<c:otherwise>
						                <button type='button' style="color: gray" title="한번만 등록가능합니다." >제안 등록</button>
									</c:otherwise>
								</c:choose>
						    </td>
						  
						 </tr>
					</table>
				</form>
				
				<div id="listReply"></div>
			</div>
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
function ajaxFun1(url, method, query, dataType, fn) {
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

$(function(){
	listPage(1);
});
function listPage(page) {
	let url = "${pageContext.request.contextPath}/pbbs/listReply.do";
	let query = "num=${dto.num}&pageNo="+page;
	let selector = "#listReply";
	
	const fn = function(data){
		$(selector).html(data);
	};
	ajaxFun(url, "get", query, "text", fn);
	// ajaxFun(url, "get", query, "html", fn); // 가능
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
		
		let url = "${pageContext.request.contextPath}/pbbs/insertReply.do";
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
		
		let url = "${pageContext.request.contextPath}/pbbs/deleteReply.do";
		let query = "replyNum="+replyNum;
		
		const fn = function(data){
			// let state = data.state;
			listPage(page);
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});

//댓글별 답글 리스트
function listReplyAnswer(answer) {
	let url = "${pageContext.request.contextPath}/pbbs/listReplyAnswer.do";
	let query = "answer=" + answer;
	let selector = "#listReplyAnswer" + answer;
	
	const fn = function(data){
		$(selector).html(data);
	};
	ajaxFun(url, "get", query, "text", fn);
}
//댓글별 답글 개수
function countReplyAnswer(answer) {
	let url = "${pageContext.request.contextPath}/pbbs/countReplyAnswer.do";
	let query = "answer=" + answer;
	
	const fn = function(data){
		let count = data.count;
		let selector = "#answerCount"+answer;
		$(selector).html(count);
	};
	
	ajaxFun(url, "post", query, "json", fn);
}
//답글 버튼(댓글별 답글 등록폼 및 답글리스트)
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
//c:if로 작성자만 이게 작동하게 하자
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
		
		let url = "${pageContext.request.contextPath}/pbbs/insertReply.do";
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
// 구매확정 버튼
//c:if로 작성자만 이게 작동하게 하자

//구매확저0ㅇ버튼
$(function(){
	$(".reply").on("click", ".btnchoose", function(){
		if(${pstate}===1){
			alert("이미 판매완료된 게시글 입니다.")
			return;
		}
		if(! confirm("이 댓글의 작성자와 거래 하셨습니까? ")) {
		    return false;
		}
		let num = "${dto.num}";
		
		let buyer = "${dto.userId}";
		
		let replyNum = $(this).attr("data-replyNum");
		let $this=$(this);
		let url = "${pageContext.request.contextPath}/pbbs/choose.do";
		let query = "num=" + num +"&replynum="+replyNum +"&buyer="+buyer;
		const fn = function(data){
			 let pstate = data.pstate;
			if(pstate==1){
				$this.removeClass("btnchoose")
				$this.addClass("btndisabled")
		        window.location.href = '${pageContext.request.contextPath}/pbbs/article.do?num=${dto.num}&page=${page}${category}';
			}
		};
		ajaxFun(url, "post", query, "json", fn);
	});
});
$(function(){
	$(".JJim").on("click", ".changeMind", function(){
		const $i=$(this).find("i");
		
		
		
		let num = "${dto.num}";
		
		let url = "${pageContext.request.contextPath}/pbbs/like.do";
		let query = "num=" + num;
		
		const fn = function(data){
			 let plike = data.plike;
			if(plike=="0"){
				$i.removeClass("fa-solid fa-heart")
				$i.addClass("fa-regular fa-heart")
			}else if(plike=="1"){
				$i.removeClass("fa-regular fa-heart")
				$i.addClass("fa-solid fa-heart")
			}
			
		};
		
		ajaxFun(url, "post", query, "json", fn);
	});
});
</script>

</body>
</html>