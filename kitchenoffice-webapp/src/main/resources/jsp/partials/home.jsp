<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row-fluid">

	<div class="span9">
		<h1>
			<i class="icon-food"></i> Selectable food...
		</h1>
	</div>
	<div class="span3">
		<div class="pull-right">
			or&nbsp;&nbsp;&nbsp;<a href="event/create"
				class="btn btn-large btn-primary">
				<i class="icon-edit"></i> <spring:message code="event.create" /></a>
		</div>
	</div>
</div>
<hr>
<div class="well row-fluid">
	<div ng-switch on="event.type" class="span4" ng-repeat="event in homeEvents">
		<div class="container-fluid" ng-switch="EXTERNAL">
			<h3>{{event.location.name}}</h3>
			<span>go out eating to</span>
			<p>{{event.location.address}}</p>
			<p>
				<a class="btn" href="#">View details »</a>
			</p>
		</div>
	</div>
</div>
<!--/span-->
<!--/row-->