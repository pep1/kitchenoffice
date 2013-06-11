<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row-fluid">
	<h1>
		<i class="icon-food"></i> Create new food event
	</h1>
</div>
<hr>
<h4>1. When?</h4>

<div class="control-group input-append">
  <input type="text" ng-model="dateString" data-date-format="DD yyyy.mm.dd" bs-datepicker>
  <button type="button" class="btn" data-toggle="datepicker"><i class="icon-calendar"></i></button>
</div>
<div class="control-group input-append">
  <input type="text" ng-model="timeString" bs-timepicker>
  <button type="button" class="btn" data-toggle="timepicker"><i class="icon-time"></i></buttoni>
</div>

<h4>2. What type?</h4>
<div class="row-fluid" ng-model="event.type" bs-buttons-radio>
	<div class="span4">
		<button type="button" class="btn btn-large btn-block btn-info" value="EXTERNAL">
			<spring:message code="event.goout.name" />
		</button>
		<p>
			<spring:message code="event.goout.desc" />
		</p>
	</div>
	<!--/span-->
	<div class="span4">
		<button type="button" class="btn btn-large btn-block btn-info" value="INTERNAL">
			<spring:message code="event.cook.name" />
		</button>
		<p>
			<spring:message code="event.cook.desc" />
		</p>
	</div>
	<!--/span-->
	<div class="span4">
		<button type="button" class="btn btn-large btn-block btn-info" value="ORDER">
			<spring:message code="event.order.name" />
		</button>
		<p>
			<spring:message code="event.order.desc" />
		</p>
	</div>
	<!--/span-->
</div>
<hr>

<pre>
DEBUG:
Event {{event}}

timeString {{timeString}}
dateString {{dateString}}

date {{date()}}

</pre>