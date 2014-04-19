<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wro" uri="/WEB-INF/classes/tld/wroResource.tld"%>
<!DOCTYPE html>
<html>
<head>
<title>Kitchenoffice</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="${project.description}">
<meta name="author" content="Johanned Schüth, Leonard Osang">
<link href="<wro:resource path='/assets/css' resource='vendor.css'/>" rel="stylesheet">
<link href="<wro:resource path='/assets/css' resource='app.css'/>" rel="stylesheet">
</head>
<body>
	<div class="content">
		<div class="well center-box">
			<h3>
				<spring:message code="pages.login.header" />
			</h3>

			<ul class="nav nav-tabs">
				<li class="active"><a href="#login" data-toggle="tab"><spring:message code="pages.login.login" /></a></li>
				<li><a href="#create" data-toggle="tab"><spring:message code="pages.login.createaccount" /></a></li>
			</ul>
			<div id="myTabContent" class="tab-content">
				<div class="tab-pane active in" id="login">
					<form class="form-horizontal" method="POST" action="j_spring_security_check">
						<c:if test="${not empty param.error}">
							<!-- Display error message -->
							<div class="alert alert-error">
								<button type="button" class="close" data-dismiss="alert">×</button>
								<strong>Access Denied!</strong> Please provide valid authorization.
							</div>
						</c:if>
						<c:if test="${not empty message}">
							<!-- Display error message -->
							<div class="alert alert-success">
								<button type="button" class="close" data-dismiss="alert">×</button>
								<c:out value="${message}" />
							</div>
						</c:if>

						<div class="control-group">
							<label class="control-label" for="inputUsername">Username</label>
							<div class="controls">
								<input class="input-large" type="text" id="inputUsername" name="j_username" placeholder="">
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="inputPassword">Password</label>
							<div class="controls">
								<input class="input-large" type="password" id="inputPassword" name="j_password" placeholder="">
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<label class="checkbox"> <input type="checkbox" name="_spring_security_remember_me" id="_spring_security_remember_me" checked> Remember me
								</label>
								<button type="submit" class="btn btn-primary">Sign In</button>
							</div>
						</div>
					</form>
				</div>
				<div class="tab-pane fade" id="create">
					<jsp:include page="include/registerForm.jsp" />
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="<wro:resource path='/assets/js' resource='vendor.js'/>"></script>
</body>
</html>