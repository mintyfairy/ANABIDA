<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>아나비다</title>
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
<script type="text/javascript">
function searchList() {
	const f = document.searchForm;
	f.submit();
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
			<h2><i class="fa-regular fa-square"></i> 커뮤니티마당 </h2>
	    </div>
	    
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
						<!--  <button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/cbbs/cwrite.do';">글올리기</button>-->
					</td>
				</tr>
			</table>
		
		</div>
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
					<h2>카테고리를 선택해주세요!!</h2>
					<form name="cate" method="post"
						action="${pageContext.request.contextPath}/cbbs/cwrite.do">
						<p>
							<span><input type="radio" name="ccategory" value="1">모임</span>
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
</html>