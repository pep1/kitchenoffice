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
<div class="cointainer-fluid" ng-switch on="event.type">
	<div ng-switch-when="EXTERNAL">
		<h4>3. Specify where to go</h4>
		<div class="row-fluid">
			<div class="span6">
				<form class="form-inline">
					<input type="text" class="search-query input-block-level" ng-model="locationSearchString" maps-search="locationMap" placeholder="Enter location here">
				</form>
				<div id="map_canvas" class="ko-map-canvas" ui-map="locationMap" ui-options="mapOptions"></div>
			</div>
			<div class="span6">
				<form class="form-horizontal">
					<div class="control-group">
						<label class="control-label">Name</label>
						<div class="controls">
							<input type="text" class="input-xlarge" ng-model="event.location.name" placeholder="Location name">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">Address</label>
						<div class="controls">
							<input type="text" class="input-xlarge" ng-model="event.location.address" placeholder="Location address">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label">Website</label>
						<div class="controls">
							<input type="text" class="input-xlarge" ng-model="event.location.website" placeholder="Location Website">
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div ng-switch-when="INTERNAL">
		<h4>3. Specify what to eat</h4>
	</div>
	<div ng-switch-when="ORDER">
		<h4>3. Specify order</h4>
	</div>
</div>
<hr>
<button class="btn btn-primary btn-large" ng-click="saveEvent()">save event</button>
<hr>
<pre>
DEBUG:
Event {{event}}
</pre>