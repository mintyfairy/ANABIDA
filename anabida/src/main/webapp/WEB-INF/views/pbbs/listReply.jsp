<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class='reply-info'>
	<span class='reply-count'>제안 ${replyCount}개</span>
	<span>[목록, ${pageNo}/${total_page} 페이지]</span>
</div>

<table class='table reply-list'>
	<c:forEach var="vo" items="${listReply}">
		<tr class='list-header'>
			<td width='50%'>
				<span class='bold'>${vo.userName}</span>
			</td>
			<td width='50%' align='right'>
				<span>${vo.reg_date}</span> |
				<c:choose>
					<c:when test="${sessionScope.member.userId==vo.userId || sessionScope.member.userId=='admin'}">
						<span class='deleteReply' data-replyNum='${vo.replyNum}' data-pageNo='${pageNo}'>삭제</span>
					</c:when>
					<c:otherwise>
						<span   style="color: gray">삭제</span>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td colspan='2' valign='top'>${vo.content}</td>
		</tr>
		<tr>
			<td>
					<button type='button' class='btn btnReplyAnswerLayout' data-replyNum='${vo.replyNum}'>대화 <span id="answerCount${vo.replyNum}">${vo.answerCount}</span></button>
			</td>
			<c:if test="${sessionScope.member.userId==vo.writerId}">
				<td align='right'><!-- 이건 거래선정버튼으로 써보자 -->
					<button type='button' class='btn btnchoose'  data-replyNum='${vo.replyNum}'  title="이 사람이 내 물건을 구매했습니다"> <span>구매자 선택</span></button>	        
					
				</td>
			</c:if>
			
			
		</tr>
	
	    <tr class='reply-answer'>
	        <td colspan='2'>
	            <div id='listReplyAnswer${vo.replyNum}' class='answer-list'></div>
	            <div class="answer-form">
	                <div class='answer-left'>└</div>
	                <div class='answer-right'><textarea class='form-control'></textarea></div>
	            </div>
	             <div class='answer-footer'>
				<c:choose>
					<c:when test="${sessionScope.member.userId==vo.userId || sessionScope.member.userId==vo.writerId }">
		                <button type='button' class='btn btnSendReplyAnswer' data-replyNum='${vo.replyNum}' >대화 등록</button>
					</c:when>
					<c:otherwise>
		                <button type='button' class='btn ' style="color: gray;"  title="제안자와 판매자만 등록할 수 있습니다." >대화 등록</button>
					</c:otherwise>
				</c:choose>
				
	            </div>
			</td>
	    </tr>
	</c:forEach>	
</table>

<div class="page-navigation">
	${paging}
</div>			
