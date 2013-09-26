<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row-fluid">
	<h1>
		<i class="icon-edit"></i> Create new food event
	</h1>
</div>
<hr>
<form name="eventForm">
	<h4>1. When?</h4>
	<div class="row-fluid form-inline" >
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
	</div>
	<h4>2. What type?</h4>
	<div class="row-fluid">
		<div class="span3">
			<button type="button" class="btn btn-large btn-block btn-info" ng-model="event.type" btn-radio="'EXTERNAL'">
				<spring:message code="event.goout.name" />
			</button>
			<p>
				<spring:message code="event.goout.desc" />
			</p>
		</div>
		<div class="span3">
			<button type="button" class="btn btn-large btn-block btn-info" ng-model="event.type" btn-radio="'INTERNAL'">
				<spring:message code="event.cook.name" />
			</button>
			<p>
				<spring:message code="event.cook.desc" />
			</p>
		</div>
		<div class="span3">
			<button type="button" class="btn btn-large btn-block btn-info" ng-model="event.type" btn-radio="'ORDER'">
				<spring:message code="event.order.name" />
			</button>
			<p>
				<spring:message code="event.order.desc" />
			</p>
		</div>
		<div class="span3">
			<button type="button" class="btn btn-large btn-block btn-info" ng-model="event.type" btn-radio="'FETCH'">
				<spring:message code="event.fetch.name" />
			</button>
			<p>
				<spring:message code="event.fetch.desc" />
			</p>
		</div>
	</div>

	<div data-ng-switch data-on="event.type">
		<div data-ng-switch-when="EXTERNAL">
			<h4>3. Where to go?</h4>
			<jsp:include page="../../include/location/select.jsp"></jsp:include>
		</div>
		<div data-ng-switch-when="INTERNAL">
			<h4>3. What to prepare to eat?</h4>
			<p><strong>TODO</strong></p>
		</div>
		<div data-ng-switch-when="ORDER">
			<h4>3. Where to order?</h4>
			<jsp:include page="../../include/location/select.jsp"></jsp:include>
		</div>
		<div data-ng-switch-when="FETCH">
			<h4>3. Where to fetch the food?</h4>
			<jsp:include page="../../include/location/select.jsp"></jsp:include>
		</div>
	</div>
	
	<div class="row-fluid" ng-show="event.location || event.recipe || event.type == 'INTERNAL'">
		<h4>4. Some optional description</h4>
		<textarea rows="4" class="input-block-level" ng-model="event.description"
						placeholder="Event description"></textarea>
		<span class="help-block"><i class="icon-info"></i> You can use <a href="http://daringfireball.net/projects/markdown/" target="_blank" >Markdown</a>.</span>
	</div>
</form>
<hr>
<button class="btn btn-primary btn-large" ng-click="saveEvent(eventForm)" ng-disabled="!isValid(eventForm)"><i class="icon-save"></i> save event</button>
<hr>
<pre>
DEBUG:
Event {{event}}
</pre>

<jsp:include page="../../include/event/createModal.jsp"></jsp:include>