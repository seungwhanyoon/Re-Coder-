<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title>로그인 실패</title>
<meta charset="UTF-8" http-equiv="refresh" content="2; URL=login.do">
</head>
<body>
<jsp:include page="../common/header.jsp"></jsp:include>

  <div class="login-form">

    <div class="links">
      <a href="#">이메일, 비밀번호가 일치하지 않습니다.</a>
    </div>
  </div>
</body>
</html>