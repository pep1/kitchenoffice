<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="include/header.jsp" />
<jsp:include page="include/navigation.jsp" />

<div class="container" data-ng-view></div>

<div class="container">
	<hr>

	<!-- Button to trigger modal -->
	<a href="#myModal" role="button" class="btn" data-toggle="modal">Launch
		demo modal</a>

	<footer>
		<p>© Genetics! 2013 - Project Version: ${project.version}</p>
	</footer>
</div>

<!-- Modal -->
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal"
			aria-hidden="true">×</button>
		<h3 id="myModalLabel">Modal header</h3>
	</div>
	<div class="modal-body">
		<p>One fine body…</p>
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
		<button class="btn btn-primary">Save changes</button>
	</div>
</div>
<jsp:include page="include/footer.jsp" />