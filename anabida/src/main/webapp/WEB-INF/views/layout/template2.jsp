<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>

<c:import url="/WEB-INF/views/layout/staticHeader.jsp"/>

<style type="text/css">
.body-main {
	max-width: 800px;
}
</style>

</head>
<body>

<header>
	<c:import url="/WEB-INF/views/layout/header.jsp"/>
</header>
	
<main>
	<div class="container body-container">
	    <div class="body-title">
			<h2><i class="fa-regular fa-square"></i> 제목 </h2>
	    </div>
	    
	    <div class="body-main mx-auto">
			내용 입니다. ...
	    </div>
	</div>
</main>

<footer>
	<c:import url="/WEB-INF/views/layout/footer.jsp"/>
</footer>

<c:import url="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>