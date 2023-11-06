<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp" />
<style type="text/css">
.body-main {
	max-width: 700px;
	padding-top: 15px;
}

.table-form td {
	padding: 7px 0;
}

.table-form p {
	line-height: 200%;
}

.table-form tr:first-child {
	border-top: 2px solid #212529;
}

.table-form tr>td:first-child {
	width: 110px;
	text-align: center;
	background: #f8f8f8;
}

.table-form tr>td:nth-child(2) {
	padding-left: 10px;
}

.table-form input[type=text], .table-form input[type=file], .table-form textarea
	{
	width: 96%;
}

a {
	text-decoration: none;
}

.wrap {
	padding: 10px;
}

.btn_open {
	font-weight: bold;
	margin: 5px;
	padding: 4px 6px;
	background: #000;
	color: #fff;
}

.pop_wrap {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, .5);
	font-size: 0;
	text-align: center;
}

.pop_wrap:after {
	display: inline-block;
	height: 100%;
	vertical-align: middle;
	content: '';
}

.pop_wrap .pop_inner {
	display: inline-block;
	padding: 20px 30px;
	background: #fff;
	width: 500px;
	height : 500px;
	vertical-align: middle;
	font-size: 15px;
}

.img-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, 65px);
	grid-gap: 5px;
}

.img-grid .item {
    object-fit: cover; /* 가로세로 비율은 유지하면서 컨테이너에 꽉 차도록 설정 */
    width: 65px;
    height: 65px;
	cursor: pointer;
}

.img-box {
	max-width: 600px;

	box-sizing: border-box;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
	flex-wrap: nowrap;
	overflow-x: auto;
}
.img-box img {
	width: 65px; height: 65px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}
</style>
<style>
/*popup*/
.popup_layer {position:fixed;top:0;left:0;z-index: 10000; width: 100%; height: 100%; background-color: rgba(0, 0, 0, 0.4); }
/*팝업 박스*/
.popup_box{position: relative;top:50%;left:50%; overflow: auto; height: 600px; width:375px;transform:translate(-50%, -50%);z-index:1002;box-sizing:border-box;background:#fff;box-shadow: 2px 5px 10px 0px rgba(0,0,0,0.35);-webkit-box-shadow: 2px 5px 10px 0px rgba(0,0,0,0.35);-moz-box-shadow: 2px 5px 10px 0px rgba(0,0,0,0.35);}
/*컨텐츠 영역*/
.popup_box .popup_cont {padding:50px;line-height:1.4rem;font-size:14px; }
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

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
	function sendOk() {
		const f = document.boardForm;
		let str;
		str = f.ctitle.value.trim();
		if (!str) {
			alert("제목을 입력하세요. ");
			f.ctitle.focus();
			return;
		}

		str = f.ccontent.value.trim();
		if (!str) {
			alert("내용을 입력하세요. ");
			f.ccontent.focus();
			return;
		}
		
		let mode = "${mode}";
	    if( (mode === "cwrite") && (!f.selectFile.value) ) {
	        alert("이미지 파일을 추가 하세요. ");
	        f.selectFile.focus();
	        return;
	    }
	    
	    
	    str = f.mcount.value;
	    if( !/^\d{1,3}$/.test(str)|| str<0 || str>=999 ) {
	    	alert("최대인원수를 다시 입력해주세요. ");
	        f.tel3.focus();
	        return;
	    }
	  	
		f.action = "${pageContext.request.contextPath}/cbbs/${mode}_ok.do";
		f.submit();
		
	}
</script>

<script type="text/javascript">
$(function(){
	var sel_files = [];
	
	$("body").on("click", ".table-form .img-add", function(event){
		$("form[name=boardForm] input[name=selectFile]").trigger("click"); 
	});
	
	$("form[name=boardForm] input[name=selectFile]").change(function(){
		if(! this.files) {
			let dt = new DataTransfer();
			for(let file of sel_files) {
				dt.items.add(file);
			}
			document.boardForm.selectFile.files = dt.files;
			
	    	return false;
	    }
	    
        for(let file of this.files) {
        	sel_files.push(file);
        	
            const reader = new FileReader();
			const $img = $("<img>", {class:"item img-item"});
			$img.attr("data-filename", file.name);
            reader.onload = e => {
            	$img.attr("src", e.target.result);
            };
			reader.readAsDataURL(file);
            
            $(".img-grid").append($img);
        }
		
		let dt = new DataTransfer();
		for(let file of sel_files) {
			dt.items.add(file);
		}
		document.boardForm.selectFile.files = dt.files;		
	});
	
	$("body").on("click", ".table-form .img-item", function(event) {
		if(! confirm("선택한 파일을 삭제 하시겠습니까 ?")) {
			return false;
		}
		
		let filename = $(this).attr("data-filename");
		
	    for(let i = 0; i < sel_files.length; i++) {
	    	if(filename === sel_files[i].name){
	    		sel_files.splice(i, 1);
	    		break;
			}
	    }
	
		let dt = new DataTransfer();
		for(let file of sel_files) {
			dt.items.add(file);
		}
		document.boardForm.selectFile.files = dt.files;
		
		$(this).remove();
	});
});
</script>
</head>
<body>

	<header>
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<main>
		<div class="container body-container">
			<div class="body-title">
				<h2>
					<i class="fa-regular fa-square"></i> 커뮤니티 게시판
				</h2>
			</div>

			<div class="body-main mx-auto">
				<form name="boardForm" method="post" enctype="multipart/form-data">
					<table class="table table-border table-form">
						<tr>
							<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
							<td><input type="text" name="ctitle" maxlength="100"
								class="form-control" value=""></td>
						</tr>

						<tr>
							<td>작성자</td>
							<td>
								<p>${sessionScope.member.userName}</p>
							</td>
						</tr>
						<tr>
							<td>카테고리</td>
							<td>
							<c:choose>
								<c:when test="${ccategory=='1'}">
									<p>모임마당</p>
								</c:when>
								<c:when test="${ccategory=='2'}">
									<p>질문마당</p>
								</c:when>
								<c:when test="${ccategory=='3'}">
									<p>사건사고마당</p>
								</c:when>
								<c:when test="${ccategory=='4'}">
									<p>취미마당</p>
								</c:when>
								<c:when test="${ccategory=='5'}">
									<p>생활마당</p>
								</c:when>
							</c:choose>
							</td>
						</tr>

						<tr>
							<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
							<td><textarea name="ccontent" class="form-control"></textarea>
							</td>
						</tr>

						<c:if test="${not empty toge}">
							<tr>
								<td>최대모임인원수</td>
								<td><input type="text" name="mcount" class="form-control"
									value=""></td>
							</tr>

							<tr>
								<td>모임 날짜</td>
								<td><input type="date" name="mreg_date"
									class="form-control" value=""></td>
							</tr>

							<tr>
								<td>우편번호</td>
								<td><input type="text" name="zip" id="zip" maxlength="7"
									class="form-control" value="${dto.zip}" readonly
									style="width: 50%;">
									<button type="button" class="btn" onclick="daumPostcode();">우편번호검색</button>
								</td>
							</tr>

							<tr>
								<td valign="top">주&nbsp;&nbsp;&nbsp;&nbsp;소</td>
								<td>
									<p>
										<input type="text" name="addr1" id="addr1" maxlength="50"
											class="form-control" value="${dto.addr1}" readonly
											style="width: 96%;">
									</p>
									<p class="block">
										<input type="text" name="addr2" id="addr2" maxlength="50"
											class="form-control" value="${dto.addr2}" style="width: 96%;">
									</p>
								</td>
							</tr>
						</c:if>

						<tr>
						<td>이미지</td>
						<td>
							<div class="img-grid"><img class="item img-add" src="${pageContext.request.contextPath}/resource/images/add_photo.png"/></div>
							<input type="file" name="selectFile" accept="image/*" multiple="multiple" style="display: none;" class="form-control">
						</td>
						</tr>
					</table>


					<table class="table">
						<tr>
							<td align="center">
								<button type="button" class="btn" onclick="sendOk();">${mode=='update' ?"수정완료":"등록완료"}</button>
								<button type="reset" class="btn">다시입력</button>
								
								<input type="hidden" name = "ccategory" value="${ccategory}">
								
								<c:if test="${mode =='update'}">
									<input type="hidden" name="num" value="${dto.num}">
									<input type="hidden" name="page" value="${page}">
								</c:if>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		
		
		
	</main>

	<footer>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>

	<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp" />
</body>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
    function daumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullAddr = ''; // 최종 주소 변수
                var extraAddr = ''; // 조합형 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    fullAddr = data.roadAddress;

                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    fullAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                if(data.userSelectedType === 'R'){
                    //법정동명이 있을 경우 추가한다.
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있을 경우 추가한다.
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zip').value = data.zonecode; //5자리 새우편번호 사용
                document.getElementById('addr1').value = fullAddr;

                // 커서를 상세주소 필드로 이동한다.
                document.getElementById('addr2').focus();
            }
        }).open();
    }
</script>


</html>