<%@ page language="java" pageEncoding="UTF-8"%>
<h3 data-ng-switch="EXTERNAL">{{event.location.name}}</h3>
<p data-ng-switch data-on="event.type">
		<span data-ng-switch-when="EXTERNAL" class="label label-info" data-ng-switch="e"><i class="icon-fixed-width icon-food"></i> go out eating</span>
		<span data-ng-switch-when="INTERNAL" class="label label-info" data-ng-switch="e"><i class="icon-fixed-width icon-food"></i> cook something to eat</span>
		<span data-ng-switch-when="ORDER" class="label label-info" data-ng-switch="e"><i class="icon-fixed-width icon-food"></i> order something to eat</span>
		<span data-ng-switch-when="FETCH" class="label label-info" data-ng-switch="e"><i class="icon-fixed-width icon-food"></i> fetch something to eat</span>
</p>
<div class="ko-thumb-container">
	<h5>Details</h5>
	<p>
		<i class="icon-fixed-width icon-time"></i> {{calendar(event.startDate)}} <small>{{fromNow(event.startDate)}}</small>
	<p>
	<p data-ng-switch="EXTERNAL">
		<i class="icon-fixed-width icon-map-marker"></i> {{event.location.address}}
	</p>
	<p data-ng-hide="isEmpty(event.creator)">
		<i class="icon-fixed-width icon-user"></i>
		<gravatar-image data-email="event.creator.email" data-size="30" data-secure="true"></gravatar-image>&nbsp;&nbsp;{{event.creator.username}}
	</p>
</div>
<div class="ko-thumb-container tags" data-ng-hide="event.location.tags.length == 0" >
	<h5><i class="icon-tags"></i> Tags</h5>
	<div class="ko-tag-list">
		<span data-ng-repeat="(idx, tag) in event.location.tags" class="badge badge-info tag">{{tag.name}}</span>
	</div>
</div>
<div class="ko-thumb-container" data-ng-hide="event.participants.length == 0" >
	<h5><i class="icon-fixed-width icon-group"></i> Attendees <small>({{event.participants.length}})</small></h5>
	<ul class="unstyled ko-attendees-list" >
		<li data-ng-repeat="participant in event.participants"><jsp:include page="../user/participantItem.jsp"></jsp:include></li>
	</ul>
</div>