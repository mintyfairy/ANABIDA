<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util-jquery.js"></script>
  <style type="text/css">
 #hero {
  width: 100%;
  height: 100vh;
  background: url("${pageContext.request.contextPath}/resource/images/anaback.png") center right;
  background-size: 400px 400px;
}
 </style>
 <link rel="stylesheet" href="https://use.fontawesome.com/releases/v6.4.2/css/all.css">
<style type="text/css">
.popup-wrap * { padding: 0; margin: 0; }
.popup-wrap *, .popup-wrap *::after, .popup-wrap *::before { box-sizing: border-box; }

.popup-wrap {
	background-color: rgba(213, 213, 213, 0.3);
	justify-content: center;
	align-items: center;
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	display: none;
	padding: 15px;
	opacity: 0;
}
.popup-wrap .popup-content {
	width: 100%;
	max-width: 450px;
	background-color: #ffffff;
	border-radius: 7px;
	overflow: hidden;
	border: 1px solid #cccccc;
	box-shadow: 3px 5px 5px 1px rgba(0,0,0,.2);
}
.popup-wrap .popup-header {
	width: 100%;
	padding: 7px 10px;
	border-bottom: 1px solid #cccccc;
	display: flex;
	align-items: center;
	justify-content: space-between;
}
.popup-wrap .popup-title {
	font-size: 19px;
	padding: 10px 7px;
	font-weight: 500;
}
.popup-wrap .popup-body {
	width:100%;
	background-color:#ffffff;
	padding:30px;
	display: block;
}

.popup-wrap .popup-footer {
	display: flex;
	width: 100%;
	padding: 10px 7px;
	border-top: 1px solid #cccccc;
	align-items: center;
	justify-content: flex-end;
	flex-direction: column;
}
.popup-wrap .btn-popup {
	color: #333;
	border: 1px solid #555;
	background-color: #fff;
	padding: 5px 10px;
	border-radius: 4px;
	font-weight: 500;
	cursor: pointer;
	font-size: 14px;
	font-family: "맑은 고딕", 나눔고딕, 돋움, sans-serif;
	vertical-align: baseline;
}
.popup-header .btn-icon {
	/* border: none; */
	border-color: #ffffff;
}

.popup-wrap .popup-footer .btn-popup {
	margin-right: 5px;
}

.popup-wrap .btn-popup:hover, .popup-wrap .btn-popup:active, .popup-wrap .btn-popup:focus {
	background-color: #e6e6e6;
	border-color: #adadad;
	color:#333;
}
.popup-header .btn-icon:hover,  .popup-header .btn-icon:active, .popup-header .btn-icon:focus {
	background-color: #ffffff;
	border: 1px solid #adadad;
}

.popup-footer .confirm { background: #106eea; border:1px solid #00a6eb; color:#fff; }
.popup-footer .confirm:hover, .popup-footer .confirm:active, .popup-footer .confirm:focus {
	background: #3284f1; border:1px solid #00a6eb; color:#fff;
}
.filebox label {
  display: inline-block;
  padding: .5em .75em;
  color: #fff;
  font-size: inherit;
  line-height: normal;
  vertical-align: middle;
  background-color: #5cb85c;
  cursor: pointer;
  border: 1px solid #4cae4c;
  border-radius: .25em;
  -webkit-transition: background-color 0.2s;
  transition: background-color 0.2s;
}

.filebox label:hover {
  background-color: #6ed36e;
}

.filebox label:active {
  background-color: #367c36;
}

.filebox input[type="file"] {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}

</style>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
function fadeIn(target) {
	let level = 0;
	let inTimer = null;
	inTimer = setInterval(function(){
		level = fadeInAction(target, level, inTimer);
	}, 50);
}

function fadeInAction(target, level, inTimer) {
	level = level + 0.1;
	if(level < 1) {
		target.style.opacity = level;
	} else {
		clearInterval(inTimer);
	}
	
	return level;
}

function fadeOut(target) {
	let level = 1;
	let inTimer = null;
	inTimer = setInterval(function(){
		level = fadeOutAction(target, level, inTimer);
	}, 50);
}

function fadeOutAction(target, level, inTimer) {
	level = level - 0.1;
	if(level > 0) {
		target.style.opacity = level;
	} else {
		clearInterval(inTimer);
	}
	
	return level;
}

function modalOpen(selector){
	const modal = document.querySelector(selector);
	
	modal.style.display = 'flex';
	fadeIn(modal);
}

function modalClose(selector){
	const modal = document.querySelector(selector);
	
	fadeOut(modal);
	modal.style.display = 'none';
}

window.addEventListener('load', (e) => {
	const popupELS = document.querySelectorAll('.popup-wrap');
	
	for(let popupEL of popupELS) {
		let closeELS = popupEL.querySelectorAll('.popup-close');
		
		for(let closeEL of closeELS) {
			closeEL.addEventListener('click', (e)=> {
				const modal = closeEL.closest('.popup-wrap');
				fadeOut(modal);
				modal.style.display = 'none';
				modalClose('#modal-container');
				return;
			});
		}
	}
});
</script>
<script type="text/javascript">
window.addEventListener('load', (e) => {
	const btnOpen = document.querySelector('#modal-open');
	// const btnConfirm = document.querySelector('#modal-confirm');
	const btnClose = document.querySelector('#modal-close');
	
	btnOpen.addEventListener('click', () => {
		modalOpen('#modal-container')
	});

	
	btnClose.addEventListener('click', () => {
		modalClose('#modal-container')
	});
});
</script>

</head>
<body>


	<!-- ======= Header ======= -->
	<header id="header">
		<div class="d-flex flex-column">

			<div class="profile">


				<button type="button" id="modal-open" title="프로필 사진 바꾸기"
					style="all: unset; padding-left: 75px; cursor: pointer;">
					<div id="profileimg">

						<c:choose>
							<c:when test="${dto.proFile eq 'proFile.png' }">
								<img
									src="${pageContext.request.contextPath}/uploads/member/${dto.proFile}"
									alt="" class="img-fluid rounded-circle uploadimg"
									width="200px;" height="200px;"  />
							</c:when>
							<c:otherwise>
								<img
									src="${pageContext.request.contextPath}/uploads/member/${dto.proFile}"
									alt="" class="img-fluid rounded-circle uploadimg"
									width="200px;" height="200px;" />
							</c:otherwise>
						</c:choose>

					</div>
				</button>



				<div class="container">
					<div class="popup-wrap" id="modal-container">
						<div class="popup-content">
							<div class="popup-body"
								style="padding: 20px; text-align: center;">
								<h4>프로필 사진 바꾸기</h4>
							</div>
							<hr>
							<div class="popup-body"
								style="padding: 10px; text-align: center;">

								<form name="proFileUp">
									<!-- <div class="filebox">
										<label for="ex_file">업로드</label> 
										<input type="file" id="ex_file" name="proFile">
									</div> -->
									<input type="file" name="proFile" id="proFile">
									<button type="button" class="btn-popup confirm " id="btnSend">변경하기</button>
								</form>

							</div>
							<hr>
							<div style="padding-top: 15px; padding-bottom: 15px; text-align: center;">
								<input type="hidden" name="defalut" id="defalut" value="proFile.jpg">
							<button type="button" class="btn-popup confirm" style="color: blue;" id="btnDelete">프로필 삭제하기</button>
							</div>

							<div class="popup-footer" style="text-align: center;">
								<button class="btn-popup popup-close" id="modal-close">취소하기</button>
							</div>
						</div>
					</div>
				</div>


				<!--  -->
				<h1 class="text-light">
					<a href="">${dto.userName }</a>
				</h1>
				<div class="social-links mt-3 text-center">
					<a href="#" class="twitter"><i class="bx bxl-twitter"></i></a> <a
						href="#" class="facebook"><i class="bx bxl-facebook"></i></a> <a
						href="#" class="instagram"><i class="bx bxl-instagram"></i></a> <a
						href="#" class="google-plus"><i class="bx bxl-skype"></i></a> <a
						href="#" class="linkedin"><i class="bx bxl-linkedin"></i></a>
				</div>
			</div>

			<nav id="navbar" class="nav-menu navbar">
				<ul>
					<li><a href="#hero" class="nav-link scrollto active"><i
							class="bx bx-home"></i> <span>Home</span></a></li>
					
					<li><a href="#about" class="nav-link scrollto">
					<i class="fa-regular fa-user"></i><span>내 소개</span></a></li>
					
					<li><a href="#resume" class="nav-link scrollto"><i class="fa-regular fa-heart"></i>
					<span> 관심목록</span></a></li>	
					
					<li><a href="#portfolio" class="nav-link scrollto"><i class="fa-solid fa-receipt">
					</i> <span>판매내역</span></a></li>
					
					<li><a href="#services" class="nav-link scrollto"><i class="fa-solid fa-bag-shopping"></i>
					<span>구매내역</span></a></li>
					
					<li><a href="#contact" class="nav-link scrollto"><i class="fa-solid fa-question"></i>
					<span>문의내역</span></a></li>
				</ul>
			</nav>
			<!-- .nav-menu -->
		</div>
	</header>
	<!-- End Header -->

	<!-- ======= Hero Section ======= -->
  <section id="hero" class="d-flex flex-column justify-content-center align-items-center">
    <div class="hero-container" data-aos="fade-in">
      <h1><a href="${pageContext.request.contextPath}/main.do">ANABIDA</a></h1>
      <p>I'm <span class="typed" data-typed-items="ANA비다,회원,${dto.userName}"></span></p>
    </div>
  </section><!-- End Hero -->

  <main id="main">

    <!-- ======= About Section ======= -->
    <section id="about" class="about">
      <div class="container">

        <div class="section-title">
          <h2>My page</h2>
        </div>

        <div class="row">
          <div class="col-lg-4" data-aos="fade-right">
          
          
          <c:choose>
							<c:when test="${dto.proFile eq 'proFile.png' }">
								<img src="${pageContext.request.contextPath}/uploads/member/${dto.proFile}"
									alt="" class="img-fluid  uploadimg" />
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/uploads/member/${dto.proFile}"
									alt="" class="img-fluid uploadimg"/>
							</c:otherwise>
						</c:choose>
          
            <img src="assets/img/profile-img.jpg" class="img-fluid" alt="">
          
          
          
          
          </div>
          <div class="col-lg-8 pt-4 pt-lg-0 content" data-aos="fade-left">
            <h3>내 소개</h3>
            
            <div class="row">
              <div class="col-lg-6">
                <ul>
                  <li><i class="bi bi-chevron-right"></i> <strong>생일:</strong> <span>${dto.birth}</span></li>
                  <li><i class="bi bi-chevron-right"></i> <strong>이름:</strong> <span>${dto.userName }</span></li>
                  <li><i class="bi bi-chevron-right"></i> <strong>전화번호:</strong> <span>${dto.tel }</span></li>
                  <li><i class="bi bi-chevron-right"></i> <strong>사는곳:</strong> <span>${dto.addr1 }</span></li>
                </ul>
              </div>
              <div class="col-lg-6">
                <ul>
                  <li><i class="bi bi-chevron-right"></i> <strong>나이:</strong> <span>${dto.age}</span></li>
                  <li><i class="bi bi-chevron-right"></i> <strong>오잉:</strong> <span>오잉</span></li>
                  <li><i class="bi bi-chevron-right"></i> <strong>이메일:</strong> <span>${dto.email }</span></li>
                  <li><i class="bi bi-chevron-right"></i> <strong>Freelance:</strong> <span>Available</span></li>
                </ul>
              </div>
            </div>
            
          </div>
        </div>

      </div>
    </section><!-- End About Section -->

   

   

    <!-- ======= Resume Section ======= -->
    <section id="resume" class="resume">
      <div class="container">

        <div class="section-title">
          <h2>관심목록</h2>
        </div>

        <div class="row">
          <div class="col-lg-6" data-aos="fade-up">
          <h3 class="resume-title">관심품목</h3>
          <c:forEach var="to" items="${wlist}" varStatus="status">
            <div class="resume-item pb-0">
              <h4>${to.wsubject }</h4>
              <ul>
                <li>${to.wcost } 원</li>
                <li>${to.wreg_date }</li>
                <li>${to.wsellstate }</li>
              </ul>
            </div>
            
			</c:forEach>
            <!-- <h3 class="resume-title">Education</h3>
            <div class="resume-item">
              <h4>Master of Fine Arts &amp; Graphic Design</h4>
              <h5>2015 - 2016</h5>
              <p><em>Rochester Institute of Technology, Rochester, NY</em></p>
              <p>Qui deserunt veniam. Et sed aliquam labore tempore sed quisquam iusto autem sit. Ea vero voluptatum qui ut dignissimos deleniti nerada porti sand markend</p>
            </div>
            <div class="resume-item">
              <h4>Bachelor of Fine Arts &amp; Graphic Design</h4>
              <h5>2010 - 2014</h5>
              <p><em>Rochester Institute of Technology, Rochester, NY</em></p>
              <p>Quia nobis sequi est occaecati aut. Repudiandae et iusto quae reiciendis et quis Eius vel ratione eius unde vitae rerum voluptates asperiores voluptatem Earum molestiae consequatur neque etlon sader mart dila</p>
            </div> -->
          </div>
          
          <div class="col-lg-6" data-aos="fade-up" data-aos-delay="100">
            <h3 class="resume-title">내가 참여한 모임</h3>
 			<c:forEach var="meet1" items="${meet1}" varStatus="status">
            <div class="resume-item pb-0">
              <h4>제목 : ${meet1.ctitle }</h4>
              <p><em>모임날짜 : ${meet1.mreg_date}</em></p>
             
            </div>
            </c:forEach>
          </div>
        </div>

      </div>
    </section><!-- End Resume Section -->

     <!-- ======= 구매 Section ======= -->
    <section id="portfolio" class="services" style="background: skyblue;">
      <div class="container">

        <div class="section-title">
          <h2>판매내역</h2>
        </div>

        <div class="row">
        <c:forEach var="vo" items="${slist}" varStatus="status">
          <div class="col-lg-4 col-md-6 icon-box" data-aos="fade-up">
            <div class="icon"><i class="fa-solid fa-check fa-flip"></i></div>
            <h4 class="title"><a href="">${vo.ssubject}</a></h4>
            <p class="description">${vo.scost  } 원</p>
            <p class="description">${vo.sreg_date  }</p>
            <p class="description">${vo.sellstate}</p>
          </div>
         </c:forEach>
        </div>

      </div>
    </section><!-- End Services Section -->

    <!-- ======= Services Section ======= -->
    <section id="services" class="services">
      <div class="container">

        <div class="section-title">
          <h2>구매목록</h2>
        </div>
        <div class="row">
        <c:forEach var="bo" items="${blist}" varStatus="status">
          <div class="col-lg-4 col-md-6 icon-box" data-aos="fade-up">
            <div class="icon"><i class="fa-solid fa-money-bill"></i></div>
            <h4 class="title"><a href="">${bo.bsubject}</a></h4>
            <p class="description">${bo.bcost} 원</p>
            <p class="description">${bo.bhdate}</p>
          </div>
       </c:forEach>
        </div>
      </div>

    </section><!-- End Services Section -->

   

    <!-- ======= Contact Section ======= -->
   <section id=contact class="testimonials section-bg">
      <div class="container">

        <div class="section-title">
          <h2>문의내역</h2>
        </div>

        <div class="testimonials-slider swiper" data-aos="fade-up" data-aos-delay="100">
          <div class="swiper-wrapper">

			<c:forEach var="zo" items="${qlist}" varStatus="status">
            <div class="swiper-slide">
              <div class="testimonial-item" data-aos="fade-up">
                <p>
                  <i class="bx bxs-quote-alt-left quote-icon-left"></i>
					${zo.qcontent }                  
					<i class="bx bxs-quote-alt-right quote-icon-right"></i>
                </p>
                <img src="assets/img/testimonials/testimonials-1.jpg" class="testimonial-img" alt="">
                <h3>${zo.qtitle}</h3>
                <h4>${zo.qanswer}</h4>
              </div>
            </div><!-- End testimonial item -->
            </c:forEach>

          </div>
          <div class="swiper-pagination"></div>
        </div>

      </div>
    </section><!-- End Testimonials Section -->

  </main><!-- End #main -->

  <!-- ======= Footer ======= -->
  <footer id="footer">
    <div class="container">
      <div class="copyright">
        &copy; Copyright <strong><span>iPortfolio</span></strong>
      </div>
      <div class="credits">
        <!-- All the links in the footer should remain intact. -->
        <!-- You can delete the links only if you purchased the pro version. -->
        <!-- Licensing information: https://bootstrapmade.com/license/ -->
        <!-- Purchase the pro version with working PHP/AJAX contact form: https://bootstrapmade.com/iportfolio-bootstrap-portfolio-websites-template/ -->
        Designed by <a href="https://bootstrapmade.com/">BootstrapMade</a>
      </div>
    </div>
  </footer><!-- End  Footer -->

  <a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

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
	
	
		$("#btnSend").on("click",function (e) {
			
			
			//let query = $("form[name=proFileUp]").serialize();
			
			//let proFile = $("#proFile").val().split('/').pop().split('\\').pop();
			
			//let form_data = new FormData();
			//form_data.append('proFile',proFile);//회원이 업로드한 이미지
			
			
			//let form_data = new FormData();
	        //form_data.append('proFile',proFile);
	        
			// console.log('asdasdasdasdasdsa : '+proFile);
			//console.log('qurty' + query);
		
			var form_data = new FormData();
			
			var inputFile = $('input[name="proFile"]');
			var files = inputFile[0].files;
			
			form_data.append('proFile',files[0]);
			let url = "${pageContext.request.contextPath}/member/profileUpload.do";
			
			console.log(files.length);
			
			$.ajax({
				type:"post",
				url:url,
				enctype: 'multipart/form-data',
				data:form_data, // data:{num:n1,num2:n2,oper:o}
				dataType : "json",	
				processData: false,
				contentType: false,
				success:function(data){
					modalClose('#modal-container');
					imgaPage();
				},
				beforeSend : function(xhr){
					// ajax가 서버에 요청전에 실행하는 로직
					if(files.length<0){
						alert('파일을 올려주세요');
						return;
					}
				},
				complete : function(){
					// 성곡 또는 에러 발생 후 실행하는 로직
				},
				error : function(){
					// 에러가 발생한 경우
					alert('파일을 올려주세요');
					console.log(e.responseText);
				}
			});
			
			
		});
		//let proFile = $("#proFile").val().split('/').pop().split('\\').pop();

		$("#btnDelete").on("click",function (e) {
			
			
			////////////////////////
			var inputFile = $("input:hidden[name=defalut]").val();
			let url = "${pageContext.request.contextPath}/member/profileDelete.do";
			let query = "proFile="+inputFile;
		
			console.log(inputFile);
			
			$.ajax({
				type:"post",
				url:url,
				data:query, // data:{num:n1,num2:n2,oper:o}
				dataType : "json",
				success:function(data){
					modalClose('#modal-container');
					imgaPage();
				},
				beforeSend : function(xhr){
				},
				complete : function(){
					// 성곡 또는 에러 발생 후 실행하는 로직
				},
				error : function(){
					// 에러가 발생한 경우
					console.log(e.responseText);
				}
			});
			
			
		});
		
		function imgaPage() {
			let url = "${pageContext.request.contextPath}/member/myPagephoto.do";
			let query = "userId=${dto.userId}";
			let selector = "#profileimg";
			
			const fn = function(data){
				// $(selector).html(data);
				$(".uploadimg").attr("src", "${pageContext.request.contextPath}/uploads/member/"+data.proFile);
				
			};
			ajaxFun(url, "get", query, "json", fn);
		}

		
		

  </script>
  
 

</body>
</html>