<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row-fluid">
	<h1>
		<i class="icon-food"></i> Create new food event
	</h1>
</div>
<hr>
<form name="eventForm">
	<h4>1. When?</h4>
	<div class="control-group input-append">
		<input type="text" ng-model="dateString" bs-datepicker>
		<button type="button" class="btn" data-toggle="datepicker">
			<i class="icon-calendar"></i>
		</button>
	</div>
	<div class="control-group input-append">
		<input type="text" ng-model="timeString" bs-timepicker>
		<button type="button" class="btn" data-toggle="timepicker">
			<i class="icon-time"></i>
		</button>
	</div>
	<span class="help-inline">{{dateFromNow()}}</span>
	<h4>2. What type?</h4>
	<div class="row-fluid">
		<div class="span4">
			<button type="button" class="btn btn-large btn-block btn-info"
				ng-model="event.type" btn-radio="'EXTERNAL'">
				<spring:message code="event.goout.name" />
			</button>
			<p>
				<spring:message code="event.goout.desc" />
			</p>
		</div>
		<div class="span4">
			<button type="button" class="btn btn-large btn-block btn-info" ng-model="event.type" btn-radio="'INTERNAL'">
				<spring:message code="event.cook.name" />
			</button>
			<p>
				<spring:message code="event.cook.desc" />
			</p>
		</div>
		<div class="span4">
			<button type="button" class="btn btn-large btn-block btn-info" ng-model="event.type" btn-radio="'ORDER'">
				<spring:message code="event.order.name" />
			</button>
			<p>
				<spring:message code="event.order.desc" />
			</p>
		</div>
	</div>

	<div ng-switch on="event.type">
		<div ng-switch-when="EXTERNAL">
			<h4>3. Where to go?</h4>
			<jsp:include page="../../include/location/select.jsp"></jsp:include>
		</div>
		<div ng-switch-when="INTERNAL">
			<h4>3. What to eat?</h4>
		</div>
		<div ng-switch-when="ORDER">
			<h4>3. Where to order?</h4>
			<jsp:include page="../../include/location/select.jsp"></jsp:include>
		</div>
	</div>
</form>
<hr>
<button class="btn btn-primary btn-large" ng-click="saveEvent(eventForm)" ng-disabled="!isValid(eventForm)">save event</button>
<hr>
<pre>
DEBUG:
Event {{event}}
</pre>

<jsp:include page="../../include/event/createModal.jsp"></jsp:include>