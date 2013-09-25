<%@ page language="java" pageEncoding="UTF-8"%>
<h3 data-ng-switch="EXTERNAL">{{event.location.name}}</h3>
<p data-ng-switch data-on="event.type">
	<span data-ng-switch-when="EXTERNAL" class="label label-info" data-ng-switch="e"><i class="icon-fixed-width icon-food"></i> go out eating</span>
	<span data-ng-switch-when="INTERNAL" class="label label-info" data-ng-switch="e"><i class="icon-fixed-width icon-food"></i> cook something to eat</span> 
	<span data-ng-switch-when="ORDER" class="label label-info" data-ng-switch="e"><i class="icon-fixed-width icon-food"></i> order something to eat</span> 
	<span data-ng-switch-when="FETCH" class="label label-info" data-ng-switch="e"><i class="icon-fixed-width icon-food"></i> fetch something to eat</span>
</p>
<div class="row-fluid">
	<div class="span6">
		<div class="ko-thumb-container">
			<h5>Details</h5>
			<p>
				<i class="icon-fixed-width icon-time"></i> {{calendar(event.startDate)}} <small>{{fromNow(event.startDate)}}</small>
			<p>
			<p data-ng-show="event.location">
				<i class="icon-fixed-width icon-map-marker"></i> {{event.location.address}}
			</p>
			<p data-ng-show="event.location && event.location.website">
				<i class="icon-fixed-width icon-home"></i> <a target="_blank" data-ng-href="{{event.location.website}}" > Location Website</a>
			</p>
			<p data-ng-show="event.creator">
				<i class="icon-fixed-width icon-user"></i>
				<gravatar-image data-email="event.creator.email" data-size="30" data-secure="true"></gravatar-image>
				&nbsp;&nbsp;{{event.creator.username}}
			</p>
		</div>
		<div class="ko-thumb-container tags" data-ng-hide="event.location.tags.length == 0" >
			<h5><i class="icon-fixed-width icon-tags"></i> Tags</h5>
			<div class="ko-tag-list">
				<span data-ng-repeat="(idx, tag) in event.location.tags" class="badge badge-info tag">{{tag.name}}</span>
			</div>
		</div>
		<div class="ko-thumb-container tags" data-ng-hide="event.description.length == 0" >
			<h5><i class="icon-fixed-width icon-info"></i> Description</h5>
			<p class="ko-tag-list" data-markdown data-input="{{event.description}}">
			</p>
		</div>
		<div class="ko-thumb-container" data-ng-hide="event.participants.length == 0" >
			<h5>
				<i class="icon-fixed-width icon-group"></i> Attendees <small>({{event.participants.length}})</small>
			</h5>
			<ul class="unstyled ko-attendees-list">
				<li data-ng-repeat="participant in event.participants"><jsp:include page="../user/participantItem.jsp"></jsp:include></li>
			</ul>
		</div>
		<div class="pull-left">
			<button data-ng-hide="event.canAttend" class="btn btn-small btn-primary" data-ng-click="attendModal.open(event)"><i class="icon-flag-alt"></i> attend to event</button>
			<button data-ng-show="event.canAttend" class="btn btn-small btn-warning" data-ng-click="dismissModal.open(event)"><i class="icon-flag"></i> dismiss event</button>
			&nbsp;&nbsp;&nbsp;<button data-ng-show="event.creator.id == me.id" class="btn btn-small btn-danger" type="button" data-ng-click="deleteModal.open(event)"><i class="icon-trash"></i> delete event</button>
		</div>
		<div class="pull-right">
			<a class="btn btn-small" data-ng-show="event.location" data-ng-href="/${project.build.finalName}/location/{{event.location.id}}">Show location details »</a>
		</div>
	</div>
	<div class="span6" data-ng-show="event.location">
		<div id="map_canvas" class="ko-map-canvas" ui-map="locationMap" ui-options="mapOptions"></div>
	</div>
</div>