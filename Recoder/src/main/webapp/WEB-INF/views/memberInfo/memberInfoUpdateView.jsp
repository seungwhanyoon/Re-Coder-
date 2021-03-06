<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>칼로리 처방</title>
  <meta charset="utf-8">
<style type="text/css">
	
	div form pre span{
		color: gray;
	}
	.kcalForm{
		margin-top: 80px;
	    width: 800px;
	    padding: 20px;
	    margin-left: auto;
	    margin-right: auto;
	
	    background-color: #EEEFF1;
	    border-radius: 5px;
	    border: 0;
	}
	.inputBox{
		width: 120px;
		height: 30px;
	}
</style>
<script src="https://code.jquery.com/jquery-3.4.0.min.js"></script>
    <script>
      $(function() {
        $('#kcalForm').submit(function() {
          var gender = $('#gender').val();
          var age = $('#age').val();
          var height = $('#height').val();
          var weight = $('#weight').val();
          var activity = $('#activity').val();
          
          
          
          if (gender == '') {
            alert('성별을 선택해주세요.');
            $('#gender').focus();
            return false;
          }

          if (age == '') {
            alert('나이를 입력해주세요.');
            $('#age').focus();
            return false;
          }
          
           if(isNaN($('#age').val())){
        	 alert("나이를 정확히 입력해 주세요");
        	 $('#age').select();
        	return false;
          }
          if (height == '') {
              alert('신장을 입력해주세요.');
              $('#height').focus();
              return false;
            }
           if(isNaN($('#height').val())){
           alert("키를 정확히 입력해 주세요");
           $('#height').select();
			return false;
          }
          if (weight == '') {
              alert('몸무게를 입력해주세요.');
              $('#weight').focus();
              return false;
            }
          if(isNaN($('#weight').val())){
        	             alert("몸무게를 정확히 입력해 주세요");
        	             $('#weight').select();
        	  			return false;
        	            }
          
          if (activity == '') {
              alert('활동량을 선택해주세요.');
              $('#activity').focus();
              return false;
            }
        });
      });
    </script>
</head>
<body>
<jsp:include page="../common/header.jsp"></jsp:include>

  	
    <c:if test="${sessionScope.login_memberVo == null }">
    	<span>로그인 후 사용가능합니다.</span>
    </c:if>

	<c:if test="${sessionScope.login_memberVo != null }">
		<div>
		    <form action="memberInfoUpdateView.do" method="post" class="kcalForm" id="kcalForm">
					<pre style="font-size: medium; font-weight: bolder;">
					<input type="hidden" name="memberNo" value="${_memberInfoVo.memberNo}">
					<c:choose>
						<c:when test="${_memberInfoVo.memberInfoGender == 'M'.charAt(0)}">
					성별		<input type="radio" id="gender" name="memberInfoGender" value="M" checked="checked">남자 <input type="radio" name="memberInfoGender" value="F">여자<br>
					</c:when> 
						<c:otherwise>
					성별		<input type="radio" id="gender" name="memberInfoGender" value="M" >남자 <input type="radio" name="memberInfoGender" value="F" checked="checked">여자<br>
					</c:otherwise>
					</c:choose>
					나이		<input type="text" id="age" name="memberInfoAge" class="text-field" placeholder="${_memberInfoVo.memberInfoAge}" value="${_memberInfoVo.memberInfoAge}"><br>
					신장(cm) 	<input type="text" id="height" name="memberInfoHeight" class="text-field" placeholder="${_memberInfoVo.memberInfoHeight}" value="${_memberInfoVo.memberInfoHeight}"><br>
					몸무게(kg)	<input type="text" id="weight" name="memberInfoWeight" class="text-field" placeholder="${_memberInfoVo.memberInfoWeight}" value="${_memberInfoVo.memberInfoWeight}"><br>
					<c:choose>
					<c:when test="${_memberInfoVo.memberInfoActivity == 1}">	
					평소 활동량	<input type="radio" id="activity" name="memberInfoActivity" value="1" checked="checked" onclick="checkFnc();">활동안함 <span>(운동을 전혀 안함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="2" >가벼운 활동 <span>(일주일에 1~3일 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="3" >보통 활동 <span>(일주일에 3~5일 스포츠 또는 적당한 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="4" >많은 활동<span>(일주일에 6~7일 격렬한 스포츠 또는 힘든 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="5" >격심한 활동 <span>(육체적으로 아주 힘든 일이나 하루 2번 운동을 함)</span><br>
					</c:when>
					<c:when test="${_memberInfoVo.memberInfoActivity == 2}">	
					평소 활동량	<input type="radio" name="memberInfoActivity" value="1"  >활동안함 <span>(운동을 전혀 안함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="2" checked="checked" >가벼운 활동 <span>(일주일에 1~3일 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="3" >보통 활동 <span>(일주일에 3~5일 스포츠 또는 적당한 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="4" >많은 활동<span>(일주일에 6~7일 격렬한 스포츠 또는 힘든 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="5" >격심한 활동 <span>(육체적으로 아주 힘든 일이나 하루 2번 운동을 함)</span><br>
					</c:when>
					<c:when test="${_memberInfoVo.memberInfoActivity == 3}">	
					평소 활동량	<input type="radio" name="memberInfoActivity" value="1"  >활동안함 <span>(운동을 전혀 안함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="2">가벼운 활동 <span>(일주일에 1~3일 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="3" checked="checked">보통 활동 <span>(일주일에 3~5일 스포츠 또는 적당한 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="4" >많은 활동<span>(일주일에 6~7일 격렬한 스포츠 또는 힘든 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="5" >격심한 활동 <span>(육체적으로 아주 힘든 일이나 하루 2번 운동을 함)</span><br>
					</c:when>
					<c:when test="${_memberInfoVo.memberInfoActivity == 4}">	
					평소 활동량	<input type="radio" name="memberInfoActivity" value="1"  >활동안함 <span>(운동을 전혀 안함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="2">가벼운 활동 <span>(일주일에 1~3일 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="3" >보통 활동 <span>(일주일에 3~5일 스포츠 또는 적당한 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="4" checked="checked">많은 활동<span>(일주일에 6~7일 격렬한 스포츠 또는 힘든 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="5" >격심한 활동 <span>(육체적으로 아주 힘든 일이나 하루 2번 운동을 함)</span><br>
					</c:when>
					<c:otherwise>
					평소 활동량	<input type="radio" id="activity" name="memberInfoActivity" value="1"  >활동안함 <span>(운동을 전혀 안함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="2">가벼운 활동 <span>(일주일에 1~3일 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="3" >보통 활동 <span>(일주일에 3~5일 스포츠 또는 적당한 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="4" >많은 활동<span>(일주일에 6~7일 격렬한 스포츠 또는 힘든 운동을 함)</span><br>
							<input type="radio" id="activity" name="memberInfoActivity" value="5" checked="checked">격심한 활동 <span>(육체적으로 아주 힘든 일이나 하루 2번 운동을 함)</span><br>
						
					</c:otherwise>
			     	</c:choose>
			     				<input type="submit" value="칼로리 계산" class="submit-btn">
					</pre>
		    </form>
  		</div>
  	</c:if>
<jsp:include page="../common/tail.jsp"></jsp:include>
</body>
</html>