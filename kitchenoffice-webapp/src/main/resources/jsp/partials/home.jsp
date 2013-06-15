<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row">

	<div class="span9">
		<h1>
			<i class="icon-food"></i> Selectable food...
		</h1>
	</div>
	<div class="span3">
		<div class="pull-right">
			or&nbsp;&nbsp;&nbsp;<a href="event/create" class="btn btn-large btn-primary"> <i class="icon-edit"></i> <spring:message code="event.create" /></a>
		</div>
	</div>
</div>
<hr>

<div class="well">
	<div class="row-fluid">
		<div data-ng-switch data-on="event.type" class="span4" data-ng-repeat="event in homeEvents">
			<h3 data-ng-switch="EXTERNAL">{{event.location.name}}</h3>
			<span class="label label-info" data-ng-switch="EXTERNAL"><i class="icon-food"></i> go out eating to</span> {{fromNow(event)}}
			<p data-ng-switch="EXTERNAL">{{event.location.address}}</p>
			<p data-ng-switch="EXTERNAL">
				<a class="btn" href="#">View details »</a>
			</p>
		</div>
	</div>
</div>

<!--/span-->
<!--/row-->