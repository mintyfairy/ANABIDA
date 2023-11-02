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
.members-form { max-width: 360px; margin: 0 auto; background: #fefeff; padding: 30px 25px; box-shadow: 0 0 15px 0 rgb(2 59 109 / 10%); }
.members-form .row { margin-bottom: 1.5rem; }
.members-form label { display: block; font-weight: 500; margin-bottom: 0.5rem; font-family: Verdana, sans-serif; }
.members-form input { display: block; width: 100%; padding: 7px 5px; }
.members-form button { padding: 8px 30px; font-size: 15px; width: 97%; }

.members-message { margin: 0 auto; padding: 20px 5px; }
.members-message p { color: #023b6d; }

.text-center { text-align: center; }

  body {
            background-color: #FAFAFA;

        }
        .login_wrap{
            display: flex;
            margin: 138px auto;
            justify-content: center;
        }
        .login {
            margin-right: 30px;
            border: 1px solid #ccc;
            background-color: white;
            width: 350px;
            height: 400px;
            text-align: center;
            display: inline-block;
            border-radius: 10px;
        }

        .image1 {
            border: 1px solid #ccc;
            background-color: white;
            float: right;
            width: 350px;
            height: 600px;
            border-radius: 10px;
        }

        .inputid {
        	width : 200px;
        	height : 30px;
            margin-top: 20px;
            margin-bottom: 10px;
            box-shadow: 0px 1px 5px 0px black;
        }

        .inputpw {
        	width : 200px;
        	height : 30px;
            margin-bottom: 20px;
            box-shadow: 0px 1px 5px 0px black;
        }

        .buttonlogin {
            font-family:'Quattrocento Sans',sans-serif;
            font-size: 20px;
            width : 200px;
        	height : 30px;
        }

        .buttonlogin.on {
            background-color: rgba(9,18,88,0.65);
        }

        .buttonjoin {
            font-family:'Quattrocento Sans',sans-serif;
            font-size: 20px;
            width: 173px;
            color: white;
        }

        .slide {
            position: relative;
        }

        .item {
            width: 348px;
            height: 650px;
            position: absolute;
            opacity:0;
            transition:all 0.3s ;
        }

        .ontheSlide {
            opacity: 1;
            transition: all 0.3s;
        }
        .login_img{
         	width: 350px;
            height: 600px;
            border-radius: 10px;
            object-fit: cover;
        }

		.hr-sect {
        display: flex;
        flex-basis: 100%;
        align-items: center;
        color: rgba(0, 0, 0, 0.35);
        font-size: 15px;
        margin: 8px 0px;
      }
      .hr-sect::before,
      .hr-sect::after {
        content: "";
        flex-grow: 1;
        background: rgba(0, 0, 0, 0.35);
        height: 1px;
        font-size: 0px;
        line-height: 0px;
        margin: 0px 16px;
      }

</style>

<script type="text/javascript">
function sendLogin() {
    const f = document.loginForm;

	let str = f.userId.value;
    if(!str) {
        alert("아이디를 입력하세요. ");
        f.userId.focus();
        return;
    }

    str = f.userPwd.value;
    if(!str) {
        alert("패스워드를 입력하세요. ");
        f.userPwd.focus();
        return;
    }

    f.action = "${pageContext.request.contextPath}/member/login_ok.do";
    f.submit();
}
</script>

</head>
<body>


	
<main>
	
		
		<div class="login_wrap">
			
			<div class="login" style="float: left">
				<br />
				<br />
				<br />
				<img src="${pageContext.request.contextPath}/resource/images/an.png" width="150px;"height="60px;" style="margin: 0px;">
				
				<form name="loginForm" method="post">
					<input type="text" placeholder="ID" name=userId class="inputid" id="login-userId" ><br /> 
					<input type="password" placeholder="PW" name="userPwd" class="inputpw" id="login-password" autocomplete="off" ><br /> 
					<button type="button" class="btn btn-primary" onclick="sendLogin();">로그인</button>
					<br/>
					<br/>
				</form>
				<div class="hr-sect">또는</div>
				<br/>
				
				<div style="font-size: 14px;">
				<img width="15px;" height="15px;" src="${pageContext.request.contextPath}/resource/images/kakao.png">
				카카오톡으로 로그인</div>
				<br>
				<div style="font-size: 13px;"><a>비밀번호를 잊으셨나요?</a></div>
			</div>	
			<div>asdas</div>
			
			
			
			
			<div class="image1">
				<div class="slide">
					<div class="item">
						<img src="${pageContext.request.contextPath}/resource/images/3.jpg" class="login_img">
					</div>
					<div class="item">
						<img src="${pageContext.request.contextPath}/resource/images/4.jpg" class="login_img">
					</div>
					<div class="item">
						<img src="${pageContext.request.contextPath}/resource/images/5.jpg" class="login_img">
					</div>
					<div class="item">
						<img src="${pageContext.request.contextPath}/resource/images/6.jpg" class="login_img">
					</div>
				</div>
			</div>
		</div>
		
	
		<div class="members-message">
			<p class="text-center">${message}</p>
		</div>
	
	</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
<script>
function sliderOn () {
    const slide = document.querySelector('.slide'); // 슬라이드뼈대 감지
    const item = slide.getElementsByClassName('item'); // 슬라이드 아이템 획득

    const firstEle = item[0]; // 첫번째 슬라이드 아이템
    firstEle.classList.add('ontheSlide'); //첫번째 슬라이드 아이템에 ontheSlide 클래스 추가

    const gogogo = setInterval(sliderGo, 3000); // 4초마다 함수 sliderGo 함수 발동시키기
    function sliderGo () {

        const currentItem = document.querySelector('.ontheSlide'); // 현재 활성화된 슬라이드 아이템 감지
        currentItem.classList.remove('ontheSlide') //현재 활성화된 슬라이드 아이템 비활성화

        if (!currentItem.nextElementSibling) { // 만약 마지막 슬라이드 아이템이라면
            item[0].classList.add('ontheSlide') //첫번째 아이템을 활성화

        }
        else { // 그 외의 경우
            currentItem.nextElementSibling.classList.add('ontheSlide') //다음 엘리먼트를 활성화
        }
    }
}
sliderOn();
</script>
</html>