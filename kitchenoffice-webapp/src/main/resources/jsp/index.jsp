<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="include/header.jsp" />
<jsp:include page="include/navigation.jsp" />

<flash-messages></flash-messages>

<div class="container" data-ng-view></div>

<div class="container">
	<hr>
	<footer>
		<p><i class="icon-info"></i> Project Version: ${project.version} | <i class="icon-bug"></i> Found a bug? Got ideas? Please report here: <a target="_blank" href="${project.issueManagement.url}">${project.issueManagement.system}</a></p>
	</footer>
</div>

<jsp:include page="include/footer.jsp" />