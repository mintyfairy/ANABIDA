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

.table-form td { padding: 7px 0; }
.table-form p { line-height: 200%; }
.table-form tr:first-child { border-top: 2px solid #212529;  }
.table-form tr > td:first-child { width: 110px; text-align: center; background: #f8f8f8; }
.table-form tr > td:nth-child(2) { padding-left: 10px; }

.table-form input[type=text], .table-form input[type=file], .table-form textarea {
	width: 96%; }
	
.table-foem .img{width:37px;height: 37px;  border: none; vertical-align:middle; 
 }
.table-foem .img{
vertical-align:middle; font-size: 11px; color: #333333;
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
	padding: 5px;
	box-sizing: border-box;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
	flex-wrap: nowrap;
	overflow-x: auto;
}
.img-box img {
	width: 37px; height: 37px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}

	
</style>

<script type="text/javascript">
function sendOk(){
	  const f = document.photoForm;
		let str;
		
	    str = f.subject.value.trim();
	    if(!str) {
	        alert("제목을 입력하세요. ");
	        f.subject.focus();
	        return;
	    }

	    str = f.content.value.trim();
	    if(!str) {
	        alert("내용을 입력하세요. ");
	        f.content.focus();
	        return;
	    }
	    
	    if(!f.catNum.value) {
	        alert("카테고리를 입력하세요. ");
	        f.catNum.focus();
	        return;
	    }
	    if( !/^\d+$/.test(f.cost.value) ) {
	    	alert("숫자만 가능합니다. ");
	        f.cost.focus();
	        return;
	    }
	    let mode="${mode}";
	    if(mode==="write"&&(!f.selectFile.value)){
	    	alert('이미지파일추가바람');
	    	f.selectFile.focus();
	    	return;
	    }
	    

	    f.action = "${pageContext.request.contextPath}/pbbs/${mode}_ok.do";
	    f.submit();
	}
$(function(){
	var sel_files = [];
	
	$("body").on("click", ".table-form .img-add", function(event){
		$("form[name=photoForm] input[name=selectFile]").trigger("click"); 
	});
	
	$("form[name=photoForm] input[name=selectFile]").change(function(){
		if(! this.files) {
			let dt = new DataTransfer();
			for(let file of sel_files) {
				dt.items.add(file);
			}
			document.photoForm.selectFile.files = dt.files;
			
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
		document.photoForm.selectFile.files = dt.files;		
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
		document.photoForm.selectFile.files = dt.files;
		
		$(this).remove();
	});
});
<c:if test="${mode=='update'}">
function deleteFile(fileNum) {
	if(! confirm("이미지를 삭제 하시겠습니까 ?")) {
		return;
	}
	
	let query = "num=${dto.num}&fileNum=" + fileNum + "&page=${page}";
	let url = "${pageContext.request.contextPath}/pbbs/deleteFile.do?" + query;
	location.href = url;
}
</c:if>
</script>
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>
	
<main>
	<div class="container body-container">
	    <div class="body-title">
			<h2><i class="far 	"></i> 팝니다. 내 물건을 </h2>
	    </div>
	    
	    <div class="body-main mx-auto">
			<form name="photoForm" method="post" enctype="multipart/form-data">
				<table class="table table-border table-form">
					
					<tr>
						<td>이미지</td>
						<td> 
								
							<div class="img-grid"><img class="item img-add" src="${pageContext.request.contextPath}/resource/images/add_photo.png"></div>
							<input type="file" name="selectFile" accept="image/*" multiple="multiple" style="display: none;" class="form-control">
							<p style="font-size: 12px;color:gray">첫번째 이미지가 썸네일이 됩니다</p>
						</td><!-- accept: 이미지만 받ㄱ함 -->
						
					</tr>
					
					<c:if test="${mode=='update'}">
						<tr>
							<td>등록이미지</td>
							<td> 
								<div class="img-box">
									<c:forEach var="vo" items="${listFile}">
										<img src="${pageContext.request.contextPath}/uploads/pbbs/${vo.imageFilename}"
											onclick="deleteFile('${vo.fileNum}');">
									</c:forEach>
								</div>
							</td>
						</tr>
					</c:if>
					<tr>
						<td>카테고리</td>
						<td>
							<select name="catNum" class="form-select">
									<option value="${dto.catNum}">${mode=="update"?dto.catString:"선 택"}</option>
									<option value="1"   >명품</option>
									<option value="2"   >의류</option>
									<option value="3"   >전자기기</option>
									<option value="4"   >리빙/생활</option>
									<option value="5"   >도서/음반/문구</option>
									<option value="6"   >반려동물</option>
									<option value="7"   >쿠폰</option>
									<option value="8"   >스포츠</option>
									<option value="999">기타</option>
							  </select>
						  </td>
					</tr>
					
					<tr> 
						<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
						<td> 
							<input type="text" name="subject" maxlength="100" class="form-control" value="${dto.subject}"
							>
						</td>
					</tr>
					
					<tr> 
						<td>작성자</td>
						<td> 
							<p>${sessionScope.member.userName}</p>
						</td>
					</tr>
					
					<tr> 
						<td valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
						<td> 
							<textarea name="content" class="form-control" >${dto.content}</textarea>
						</td>
					</tr>
					<tr> 
						<td>가격</td>
						<td> 
							&#8361; <input type="text" name="cost" maxlength="100" class="form-control" value="${dto.cost}" style="width: 100px"
							> 
						</td>
					</tr>
				</table>
					
				<table class="table">
					<tr> 
						<td align="center">
							<button type="button" class="btn" onclick="sendOk();">${mode=="update"?"수정완료":"등록완료"}</button>
							<button type="reset" class="btn">다시입력</button>
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/pbbs/list.do';">${mode=="update"?"수정취소":"등록취소"}</button>
							<c:if test="${mode=='update'}">
								<input type="hidden" name="num" value="${dto.num}"> <!-- 업데이트일때 페이지와 글번호를 넘겨줘야 수정가능 -->
								<input type="hidden" name="page" value="${page}"> 	<!-- 그래서 업데이트일떄나타나게한다 -->
								<!--  <input type="hidden" name="postImgname" value="${postImgname}">--> 	<!-- 그래서 업데이트일떄나타나게한다 -->
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

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>