<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<div class="header-top">
		<div class="header-left">
			&nbsp;
		</div>
		<div class="header-center">
			<h1 class="logo"><a href="#"><img src="">아나비다</a><span></span></h1>
		</div>
		<div class="header-right">
            <c:if test="${empty sessionScope.member}">
                <a href="${pageContext.request.contextPath}/member/login.do" title="로그인"><i class="fa-solid fa-arrow-right-to-bracket"></i></a>
				&nbsp;
                <a href="${pageContext.request.contextPath}/member/member.do" title="회원가입"><i class="fa-solid fa-user-plus"></i></a>
            </c:if>
            <c:if test="${not empty sessionScope.member}">
            	<a href="#" title="알림"><i class="fa-regular fa-bell"></i></a>
            	&nbsp;
				<a href="${pageContext.request.contextPath}/member/logout.do" title="로그아웃"><i class="fa-solid fa-arrow-right-from-bracket"></i></a>
            </c:if>
            <c:if test="${sessionScope.member.userId == 'admin'}">
            	&nbsp;
				<a href="#" title="관리자"><i class="fa-solid fa-gear"></i></a>
            </c:if>
		</div>
	</div>

	<nav>
		<ul class="main-menu">
			<li><a href="${pageContext.request.contextPath}/">홈</a></li>

			<li><a href="#">마이페이지</a>
				<ul class="sub-menu">
					<li><a href="#" aria-label="submenu">판매내역</a></li>
					<li><a href="#" aria-label="submenu">구매내역</a></li>
					<li><a href="#" aria-label="submenu">찜목록</a></li>
					<li><a href="#" aria-label="submenu">정보수정</a></li>
				</ul>
			</li>

			<li><a href="#">공동구매</a>
				<ul class="sub-menu">
					<li><a href="#" aria-label="submenu">참여하기</a></li>
					<li><a href="#" aria-label="submenu">모집하기</a></li>
				</ul>
			</li>
			
			<li><a href="#">커뮤니티마당</a>
			</li>
			
			<li><a href="#">고객센터</a>
				<ul class="sub-menu">
					<li><a href='<c:url value="/member/cs.do"/>' aria-label="submenu">고객문의</a></li>
					<li><a href="#" aria-label="submenu">공지사항</a></li>
				</ul>
			</li>

		</ul>
	</nav>
