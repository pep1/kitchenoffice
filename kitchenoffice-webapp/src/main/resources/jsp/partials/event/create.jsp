<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row-fluid">
	<h1>
		<i class="icon-food"></i> Create new food event
	</h1>
</div>
<hr>
<h4>1. When?</h4>
<form class="form-inline">
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
			</buttoni>
	</div>
	<span class="help-inline">{{dateFromNow()}}</span>
</form>
<h4>2. What type?</h4>
<div class="row-fluid">
	<div class="span4">
		<button type="button" class="btn btn-large btn-block btn-info" ng-model="event.type" btn-radio="'EXTERNAL'">
			<spring:message code="event.goout.name" />
		</button>
		<p>
			<spring:message code="event.goout.desc" />
		</p>
	</div>
	<!--/span-->
	<div class="span4">
		<button type="button" class="btn btn-large btn-block btn-info" ng-model="event.type" btn-radio="'INTERNAL'">
			<spring:message code="event.cook.name" />
		</button>
		<p>
			<spring:message code="event.cook.desc" />
		</p>
	</div>
	<!--/span-->
	<div class="span4">
		<button type="button" class="btn btn-large btn-block btn-info" ng-model="event.type" btn-radio="'ORDER'">
			<spring:message code="event.order.name" />
		</button>
		<p>
			<spring:message code="event.order.desc" />
		</p>
	</div>
	<!--/span-->
</div>

<div id="map_canvas" ui-map="myMap" style="height: 300px; width: 400px; border: 2px solid #777777; margin: 3px; border: 1px solid" ui-options="mapOptions" ui-event="{'map-idle' : 'onMapIdle()'}"></div>
<div class="row-fluid" ng-switch on="event.type">
	<div ng-switch-when="EXTERNAL">
		<h4>3. Specify where to go</h4>
		<form class="form-inline">
			<input type="text" ng-model="locationSearchString" placeholder="Enter location here">
			<button class="btn" ng-click="findLocation(locationSearchString)" href="">
				<i class="icon-screenshot"></i> find
			</button>
		</form>
	</div>
	<div ng-switch-when="INTERNAL">
		<h4>3. Specify what to eat</h4>
	</div>
	<div ng-switch-when="ORDER">
		<h4>3. Specify order</h4>
	</div>

</div>
<hr>
<pre>
DEBUG:
Event {{event}}

Location {{locationSearch}}
</pre>