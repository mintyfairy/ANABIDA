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

.table-list thead > tr:first-child { background: #f8f8f8; }
.table-list th, .table-list td { text-align: center; }
.table-list .left { text-align: left; padding-left: 5px; }

.table-list .num { width: 60px; color: #787878; }
.table-list .subject { color: #787878; }
.table-list .name { width: 100px; color: #787878; }
.table-list .date { width: 100px; color: #787878; }
.table-list .hit { width: 70px; color: #787878; }
</style>

<style>
/*popup*/
.popup_layer {position:fixed;top:0;left:0;z-index: 10000; width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.4); }
/*팝업 박스*/
.popup_box{position: relative;top:50%;left:50%; overflow: auto; height: 400px; width:375px;transform:translate(-50%, -50%);z-index:1002;box-sizing:border-box;background:#fff;box-shadow: 2px 5px 10px 0px rgba(0,0,0,0.35);-webkit-box-shadow: 2px 5px 10px 0px rgba(0,0,0,0.35);-moz-box-shadow: 2px 5px 10px 0px rgba(0,0,0,0.35);}
/*컨텐츠 영역*/
.popup_box .popup_cont {padding:50px;line-height:1.4rem;font-size:14px;}
.popup_box .popup_cont h2 {padding:15px 0;color:#333;margin:0;}
/*버튼영역*/
.popup_box .popup_btn {display:table;table-layout: fixed;width:100%;height:70px;background:#ECECEC;word-break: break-word;}
.popup_box .popup_btn a {position: relative; display: table-cell; height:70px;  font-size:17px;text-align:center;vertical-align:middle;text-decoration:none; background:#ECECEC;}
.popup_box .popup_btn a:before{content:'';display:block;position:absolute;top:26px;right:29px;width:1px;height:21px;background:#fff;-moz-transform: rotate(-45deg); -webkit-transform: rotate(-45deg); -ms-transform: rotate(-45deg); -o-transform: rotate(-45deg); transform: rotate(-45deg);}
.popup_box .popup_btn a:after{content:'';display:block;position:absolute;top:26px;right:29px;width:1px;height:21px;background:#fff;-moz-transform: rotate(45deg); -webkit-transform: rotate(45deg); -ms-transform: rotate(45deg); -o-transform: rotate(45deg); transform: rotate(45deg);}
.popup_box .popup_btn a.close_day {background:#5d5d5d;}
.popup_box .popup_btn a.close_day:before, .popup_box .popup_btn a.close_day:after{display:none;}
/*오버레이 뒷배경*/
.popup_overlay{position:fixed;top:0px;right:0;left:0;bottom:0;z-index:1001;;background:rgba(0,0,0,0.5);}
</style>

<script type="text/javascript">
function searchList() {
	const f = document.searchForm;
	f.submit();
}



</script>

 <!-- Google Fonts -->
  <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Raleway:300,300i,400,400i,500,500i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">

  <!-- Vendor CSS Files -->
  <link href="${pageContext.request.contextPath}/resource/assets1/vendor/animate.css/animate.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets1/vendor/aos/aos.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets1/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets1/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets1/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets1/vendor/remixicon/remixicon.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/resource/assets1/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">

  <!-- Template Main CSS File -->
  <link href="${pageContext.request.contextPath}/resource/assets1/css/style.css" rel="stylesheet">
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>
	
<main>
	<div class="container body-container">
	   <div class="body-title" style="text-align: center;">
			<h2>커뮤니티 </h2> 
			<span style="text-align: right; margin-left:870px; "><a href="javascript:openPop()" > 글올리기<i class="fa-solid fa-pen-to-square fa-beat-fade"></i> </a></span>
	    </div>
	    
	    <!-- 
	    <div class="body-main mx-auto">
			<table class="table">
				<tr>
					<td width="50%">${dataCount}개(${page}/${total_page } 페이지)</td>
					<td align="right">&nbsp;</td>
				</tr>
			</table>
			
			<table class="table table-border table-list">
				<thead>
					<tr>
						<th class="num">번호</th>
						<th class="subject left">제목</th>
						<th class="name">작성자</th>
						<th class="date">작성일</th>
						<th class="hit">조회수</th>
					</tr>
				</thead>
				
				<tbody>
				<c:forEach var="list" items="${list}" varStatus="status">
					<tr>
						<td>${dataCount-(page-1)*size - status.index }</td>
						<td class="left"><a href="${articleUrl}&num=${list.num}">${list.ctitle}</a></td>
						<td>${list.userName}</td>
						<td>${list.creg_date}</td>
						<td>${list.chitCount} </td>
					</tr>
				</c:forEach>
				<tbody>
				
			</table>
			
			<div class="page-navigation">
				${ dataCount== 0 ? "등록된 게시글이 없다" : paging }
			</div>
			
			<table class="table">
				<tr>
					<td width="100">
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/bbs/list.do';" title="새로고침"><i class="fa-solid fa-arrow-rotate-right"></i></button>
					</td>
					<td align="center">
						<form name="searchForm" action="" method="post">
							<select name="schType" class="form-select">
								<option value="all">제목+내용</option>
								<option value="userName">작성자</option>
								<option value="reg_date">등록일</option>
								<option value="subject">제목</option>
								<option value="content">내용</option>
							</select>
							<input type="text" name="kwd" value="" class="form-control">
							<button type="button" class="btn" onclick="searchList();">검색</button>
						</form>
					</td>
					<td align="right" width="100">
					<a href="javascript:openPop()" >글올리기 </a>
					</td>
				</tr>
			</table>
		
		</div>
		-->
		
		  <!-- ======= Courses Section ======= -->
			<section id="courses" class="courses">
				<div class="container" data-aos="fade-up">
					<div class="row" data-aos="zoom-in" data-aos-delay="100">
							<c:forEach var="list" items="${list}" varStatus="status">
							<div class="col-lg-4 col-md-6 d-flex align-items-stretch" style="margin-top: 10px;">
								<div class="course-item">
									<a href="${articleUrl}&num=${list.num}">
									<img
										src="${pageContext.request.contextPath}/uploads/cbbs/${list.picFileName}"
										style="width: 410px; height: 300px;"></a>
									<div class="course-content">
										<div
											class="d-flex justify-content-between align-items-center mb-3">
												<c:choose>
												<c:when test="${list.ccategory == 1}">
													<h4>모임</h4>
												</c:when>
												<c:when test="${list.ccategory == 2}">
													<h4>질문</h4>
												</c:when>
												<c:when test="${list.ccategory == 3}">
													<h4>사건사고</h4>
												</c:when>
												<c:when test="${list.ccategory == 4}">
													<h4>취미</h4>
												</c:when>
												<c:when test="${list.ccategory == 5}">
													<h4>생활</h4>
												</c:when>
											</c:choose>
											<p class="price">${list.creg_date }</p>
										</div>

										<h3>
											<a href="${articleUrl}&num=${list.num}">${list.ctitle}</a>
										</h3>
										<!-- <p>Et architecto provident deleniti facere repellat nobis
											iste. Id facere quia quae dolores dolorem tempore.</p> -->
										<div
											class="trainer d-flex justify-content-between align-items-center">
											<div class="trainer-profile d-flex align-items-center">
												<img src="assets1/img/trainers/trainer-1.jpg"
													class="img-fluid" alt=""> <span>${list.userName}</span>
											</div>
											<div class="trainer-rank d-flex align-items-center">
												<!-- <i class="bx bx-user"></i>&nbsp;50 &nbsp;&nbsp;  --> 
												조회수 : &nbsp;${list.chitCount }
											</div>
										</div>
									</div>
								</div>
							</div>
							<!-- End Course Item-->
					</c:forEach>
					</div>
					<div class="page-navigation">
						${ dataCount== 0 ? "등록된 게시글이 없다" : paging }
					</div>
				</div>
			</section>
	</div>

	<!-- 팝업창 -->

		<div class="popup_layer" id="popup_layer" style="display: none;">
			<div class="popup_box">
				<div style="height: 10px; width: 375px; float: top;">
					<a href="javascript:closePop();"><img
						src="https://sbinx-phinf.pstatic.net/MjAyMzA5MThfMTg1/MDAxNjk0OTk2NzQwMjAz.EHiUehNMdJexW5d0WdPXtl28rjgz2qaTBIDo1n2iSL8g.xWTUARj2tFg2V9feplWdxXWrrQ_dE4t924l6x0S9O8Ag.JPEG/image.jpg?type=sc960_832"
						class="m_header-banner-close" width="30px" height="30px"></a>
				</div>
				<!--팝업 컨텐츠 영역-->
				<div class="popup_cont">
					<p style="font-size: 20px;">※ 카테고리를 선택해주세요 ※</p>
					<form name="cate" method="post"
						action="${pageContext.request.contextPath}/cbbs/cwrite.do">
						<p>
							<span style="font-size: 15px;"><input type="radio" name="ccategory" value="1">모임</span>
							<span><input type="radio" name="ccategory" value="2">질문</span>
						</p>
							<span><input type="radio" name="ccategory" value="3">사건사고</span>
							<span><input type="radio" name="ccategory" value="4">취미</span>
						<p>
							<input type="radio" name="ccategory" value="5">생활
						</p>
						<button type="submit" onclick="closePop();">선택완료</button>
					</form>
				</div>
			</div>
		</div>

	</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>

<script type="text/javascript">

//팝업 띄우기
function openPop() {
    document.getElementById("popup_layer").style.display = "block";
}

//팝업 닫기
function closePop() {
	const f = document.cate;
	let cateValue = f.ccategory.value;
    document.getElementById("popup_layer").style.display = "none";
   

}
</script>

 <!-- Vendor JS Files -->
  <script src="${pageContext.request.contextPath}/resource/assets1/vendor/purecounter/purecounter_vanilla.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets1/vendor/aos/aos.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets1/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets1/vendor/swiper/swiper-bundle.min.js"></script>
  <script src="${pageContext.request.contextPath}/resource/assets1/vendor/php-email-form/validate.js"></script>

  <!-- Template Main JS File -->
  <script src="${pageContext.request.contextPath}/resource/assets1/js/main.js"></script>
</html>