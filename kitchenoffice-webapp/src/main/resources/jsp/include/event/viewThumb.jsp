<%@ page language="java" pageEncoding="UTF-8"%>
<h3 data-ng-switch="EXTERNAL">{{event.location.name}}</h3>
<p>
	<span class="label label-info" data-ng-switch="EXTERNAL"><i class="icon-food"></i> go out eating to</span>
</p>
<p>
	<i class="icon-time"></i> {{fromNow(event.startDate)}}
<p>
<p data-ng-switch="EXTERNAL">
	<i class="icon-map-marker"></i> {{event.location.address}}
</p>
<p ng-hide="isEmpty(event.creator)">
	<i class="icon-user"></i>
	<gravatar-image data-email="event.creator.email" data-size="30" data-secure="true"></gravatar-image>
	{{event.creator.username}}
<p>