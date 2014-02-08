<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row-fluid">
	<div class="span9">
		<h1>
			<i class="icon-food"></i> Selectable food ...
		</h1>
	</div>
	<div class="span3" data-ng-show="!areHomeEventsEmpty">
		<div class="pull-right">
			or&nbsp;&nbsp;&nbsp;<a href="/${project.build.finalName}/event/create" class="btn btn-large btn-primary"> <i class="icon-edit"></i> <spring:message code="event.create" /></a>
		</div>
	</div>
</div>
<hr>
<div class="row-fluid">
	<div data-ng-switch data-on="event.type" class="span4 well" data-ng-repeat="event in homeEvents">
		<button data-ng-show="event.creator.id == me.id" type="button" class="close" aria-hidden="true" data-ng-click="deleteModal.open(event)">×</button>
		<jsp:include page="../include/event/viewThumb.jsp"></jsp:include>
		<div class="alert" ng-show="event.locked">
			<i class="icon-fixed-width icon-lock"></i> This event is locked !
		</div>
		<div>
			<a class="btn btn-small" data-ng-href="/${project.build.finalName}/event/{{event.id}}">View details »</a>
			<button data-ng-show="!event.participantsContainMe" data-ng-disabled="event.locked" class="btn btn-small btn-primary" data-ng-click="attendModal.open(event)"><i class="icon-flag-alt"></i> attend</button>
			<button data-ng-show="event.participantsContainMe" data-ng-disabled="event.locked" class="btn btn-small btn-warning" data-ng-click="dismissModal.open(event)"><i class="icon-flag"></i> dismiss</button>
		</div>
	</div>
	<div data-ng-show="areHomeEventsEmpty">
		<p class="center" >there are no future events right now<br/>
			<a href="event/create" class="btn btn-large btn-primary"> <i class="icon-edit"></i> <spring:message code="event.create" /></a>
		</p>
	</div>
</div>
<div data-ng-hide="arePastEventsEmpty" data-infinite-scroll="addItems()" class="row-fluid">
	<hr>
	<h3>Past food events</h3>
	<div class="well well-small" data-ng-repeat="event in pastEvents | filter:{hasParticipants:true}">
		<jsp:include page="../include/event/viewBlockList.jsp"></jsp:include>
	</div>
</div>

<jsp:include page="../include/event/attendModal.jsp"></jsp:include>
<jsp:include page="../include/event/dismissModal.jsp"></jsp:include>
<jsp:include page="../include/event/deleteModal.jsp"></jsp:include>