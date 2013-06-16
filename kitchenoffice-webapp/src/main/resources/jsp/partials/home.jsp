<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row">

	<div class="span9">
		<h1>
			<i class="icon-food"></i> Selectable food...
		</h1>
	</div>
	<div class="span3" data-ng-show="!areEventsEmpty">
		<div class="pull-right">
			or&nbsp;&nbsp;&nbsp;<a href="event/create" class="btn btn-large btn-primary"> <i class="icon-edit"></i> <spring:message code="event.create" /></a>
		</div>
	</div>
</div>
<hr>

<div class="row-fluid">
	<div data-ng-switch data-on="event.type" class="span4 well" data-ng-repeat="event in homeEvents">
		<jsp:include page="../include/event/viewThumb.jsp"></jsp:include>
		<div>
			<button class="btn btn-small">View details »</button>
			<button class="btn btn-small btn-primary">attend</button>
		</div>
	</div>
	<div data-ng-show="areEventsEmpty">
		<p class="center" >there are no events yet<br/>
			<a href="event/create" class="btn btn-large btn-primary"> <i class="icon-edit"></i> <spring:message code="event.create" /></a>
		</p>
	</div>
</div>

<!--/span-->
<!--/row-->