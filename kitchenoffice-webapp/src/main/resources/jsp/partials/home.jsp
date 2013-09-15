<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row">

	<div class="span9">
		<h1>
			<i class="icon-food"></i> Selectable food ...
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
		<button data-ng-show="event.creator.id == me.id" type="button" class="close" aria-hidden="true" data-ng-click="deleteModal.open(event)">×</button>
		<jsp:include page="../include/event/viewThumb.jsp"></jsp:include>
		<div>
			<a class="btn btn-small" data-ng-href="event/{{event.id}}">View details »</a>
			<button data-ng-show="(event.participants | filter:isMe).length == 0" class="btn btn-small btn-primary" data-ng-click="attendModal.open(event)"><i class="icon-flag-alt"></i> attend</button>
			<button data-ng-hide="(event.participants | filter:isMe).length == 0" class="btn btn-small btn-warning" data-ng-click="dismissModal.open(event)"><i class="icon-flag"></i> dismiss</button>
		</div>
	</div>
	<div data-ng-show="areEventsEmpty">
		<p class="center" >there are no events yet<br/>
			<a href="event/create" class="btn btn-large btn-primary"> <i class="icon-edit"></i> <spring:message code="event.create" /></a>
		</p>
	</div>
</div>

<jsp:include page="../include/event/attendModal.jsp"></jsp:include>
<jsp:include page="../include/event/dismissModal.jsp"></jsp:include>
<jsp:include page="../include/event/deleteModal.jsp"></jsp:include>