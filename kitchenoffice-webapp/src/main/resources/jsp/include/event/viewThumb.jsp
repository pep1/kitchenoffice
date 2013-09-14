<%@ page language="java" pageEncoding="UTF-8"%>
<h3 data-ng-switch="EXTERNAL">{{event.location.name}}</h3>
<p data-ng-switch data-on="event.type">
		<span data-ng-switch-when="EXTERNAL" class="label label-info" data-ng-switch="e"><i class="icon-food"></i> go out eating</span>
		<span data-ng-switch-when="INTERNAL" class="label label-info" data-ng-switch="e"><i class="icon-food"></i> cook something to eat</span>
		<span data-ng-switch-when="ORDER" class="label label-info" data-ng-switch="e"><i class="icon-food"></i> order something to eat</span>
		<span data-ng-switch-when="FETCH" class="label label-info" data-ng-switch="e"><i class="icon-food"></i> fetch something to eat</span>
</p>
<p>
	<i class="icon-time"></i> {{fromNow(event.startDate)}}
<p>
<p data-ng-switch="EXTERNAL">
	<i class="icon-map-marker"></i> {{event.location.address}}
</p>
<p data-ng-hide="isEmpty(event.creator)">
	<i class="icon-user"></i>
	<gravatar-image data-email="event.creator.email" data-size="30" data-secure="true"></gravatar-image>
	{{event.creator.username}}
<p>