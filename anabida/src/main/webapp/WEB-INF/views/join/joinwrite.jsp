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

.table-form td { padding: 7px 0; }
.table-form p { line-height: 200%; }
.table-form tr:first-child { border-top: 2px solid #212529; }
.table-form tr > td:first-child { width: 110px; text-align: center; background: #f8f8f8; }
.table-form tr > td:nth-child(2) { padding-left: 10px; }

.table-form input[type=text], .table-form input[type=file], .table-form textarea {
	width: 96%; }
.form-check-input { width: 1em; height: 1em; vertical-align: middle; background-color: #fff; border: 1px solid rgba(0,0,0,.25); margin-top: 7px; margin-bottom: 7px; }
.form-check-input:checked { background-color: #0d6efd; border-color: #0d6efd; }
.form-check-label { cursor: pointer; vertical-align: middle; margin-top: 7px; margin-bottom: 7px; }	
</style>

<script type="text/javascript">
function sendOk() {
    const f = document.questionForm;
	let str;
	

    str = f.quantity.value.trim();
    if(!str) {
        alert("수량을 입력하세요. ");
        f.quantity.focus();
        return;
    }
    
    
    str = f.email1.value.trim();
    if( !str ) {
        alert("이메일을 입력하세요. ");
        f.email1.focus();
        return;
    }

    str = f.email2.value.trim();
    if( !str ) {
        alert("이메일을 입력하세요. ");
        f.email2.focus();
        return;
    }

    
    str = f.tel1.value;
    if( !str ) {
        alert("전화번호를 입력하세요. ");
        f.tel1.focus();
        return;
    }

    str = f.tel2.value;
    if( !/^\d{3,4}$/.test(str) ) {
        alert("숫자만 가능합니다. ");
        f.tel2.focus();
        return;
    }

    str = f.tel3.value;
    if( !/^\d{4}$/.test(str) ) {
    	alert("숫자만 가능합니다. ");
        f.tel3.focus();
        return;
    }
    


    f.action = "${pageContext.request.contextPath}/join/join_ok.do?";
    f.submit();
    
    alert("신청이 완료되었습니다.");
}

function changeEmail() {
    const f = document.questionForm;
	    
    let str = f.selectEmail.value;
    if(str !== "direct") {
        f.email2.value = str; 
        f.email2.readOnly = true;
        f.email1.focus(); 
    }
    else {
        f.email2.value = "";
        f.email2.readOnly = false;
        f.email1.focus();
    }
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
			<h2><i class="fa-solid fa-person-circle-question"></i> 공동구매 신청폼 </h2>
	    </div>
	    
	    <div class="body-main mx-auto">
			<form name="questionForm" method="post">
				<table class="table table-border table-form">
					<tr> 
						<td>제목</td>
						<td> 
						<input type="text" readonly="readonly" name="title" value="${dto.title}" class="form-select">
						</td>
					</tr>
					
					<tr> 
						<td>작성자</td>
						<td> 
							<input type="text" readonly="readonly" name="userName" value="${dto.userName}" class="form-select">
						</td>
					</tr>
					
					<tr>
						<td>희망구매 갯수</td>
						<td>
							<input type="text" name="quantity" class="form-select">
						</td>
					</tr>
					
					<tr>
					<td>이 메 일</td>
					<td>
						  <select name="selectEmail" class="form-select" onchange="changeEmail();" >
								<option value="${dto.email1}">선 택</option>
								<option value="naver.com"   ${dto.email2=="naver.com" ? "selected" : ""}>네이버 메일</option>
								<option value="hanmail.net" ${dto.email2=="hanmail.net" ? "selected" : ""}>한 메일</option>
								<option value="gmail.com"   ${dto.email2=="gmail.com" ? "selected" : ""}>지 메일</option>
								<option value="hotmail.com" ${dto.email2=="hotmail.com" ? "selected" : ""}>핫 메일</option>
								<option value="direct">직접입력</option>
						  </select>
						  <input type="text" name="email1" maxlength="30" class="form-control" value="${dto.email1}" style="width: 33%;" value="${dto.email1}"> @ 
						  <input type="text" name="email2" maxlength="30" class="form-control" value="${dto.email2}" style="width: 33%;" readonly>
					</td>
				</tr>
				
				<tr>
					<td>전화번호</td>
					<td>
						  <select name="tel1" class="form-select">
								<option value="">선택</option>
								<option value="010" ${dto.tel1=="010" ? "selected" : ""}>010</option>
								<option value="02"  ${dto.tel1=="02"  ? "selected" : ""}>02</option>
								<option value="031" ${dto.tel1=="031" ? "selected" : ""}>031</option>
								<option value="032" ${dto.tel1=="032" ? "selected" : ""}>032</option>
								<option value="033" ${dto.tel1=="033" ? "selected" : ""}>033</option>
								<option value="041" ${dto.tel1=="041" ? "selected" : ""}>041</option>
								<option value="042" ${dto.tel1=="042" ? "selected" : ""}>042</option>
								<option value="043" ${dto.tel1=="043" ? "selected" : ""}>043</option>
								<option value="044" ${dto.tel1=="044" ? "selected" : ""}>044</option>
								<option value="051" ${dto.tel1=="051" ? "selected" : ""}>051</option>
								<option value="052" ${dto.tel1=="052" ? "selected" : ""}>052</option>
								<option value="053" ${dto.tel1=="053" ? "selected" : ""}>053</option>
								<option value="054" ${dto.tel1=="054" ? "selected" : ""}>054</option>
								<option value="055" ${dto.tel1=="055" ? "selected" : ""}>055</option>
								<option value="061" ${dto.tel1=="061" ? "selected" : ""}>061</option>
								<option value="062" ${dto.tel1=="062" ? "selected" : ""}>062</option>
								<option value="063" ${dto.tel1=="063" ? "selected" : ""}>063</option>
								<option value="064" ${dto.tel1=="064" ? "selected" : ""}>064</option>
								<option value="070" ${dto.tel1=="070" ? "selected" : ""}>070</option>
						  </select>
						  <input type="text" name="tel2" maxlength="4" class="form-control" value="${dto.tel2}" style="width: 33%;" > -
						  <input type="text" name="tel3" maxlength="4" class="form-control" value="${dto.tel3}" style="width: 33%;">
					</td>
				</tr>
			
				<tr>
					<td>우편번호</td>
					<td>
						<input type="text" name="zip" id="zip" maxlength="7" class="form-control" value="${dto.zip}" readonly style="width: 50%;">
						<button type="button" class="btn" onclick="daumPostcode();">우편번호검색</button>
					</td>
				</tr>
				
				<tr>
					<td valign="top">주&nbsp;&nbsp;&nbsp;&nbsp;소</td>
					<td>
						<p>
							<input type="text" name="addr1" id="addr1" maxlength="50" class="form-control" value="${dto.addr1}" readonly style="width: 96%;">
						</p>
						<p class="block">
							<input type="text" name="addr2" id="addr2" maxlength="50" class="form-control" value="${dto.addr2}" style="width: 96%;">
						</p>
					</td>
				</tr>
				</table>
					
				<table class="table">
					<tr> 
						<td align="center">
							<button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'신청하기'}</button>
							<button type="reset" class="btn">다시입력</button>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/join/article.do?buyNum=?${dto.buyNum}&page=${page}';">${mode=='update'?'수정취소':'신청취소'}</button>
							<input type="hidden" name="buyNum" value="${dto.buyNum}">
							<c:if test="${mode=='update'}">
								<input type="hidden" name="num" value="">
								<input type="hidden" name="page" value="">
							</c:if>
						</td>
					</tr>
				</table>
		
			</form>

	    </div>
	</div>

</main>

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

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>