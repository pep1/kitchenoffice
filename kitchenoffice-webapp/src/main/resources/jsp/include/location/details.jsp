<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row-fluid">
	<div class="span9">
		<h3>
			<i class="icon-map-marker"></i> {{location.name}}
		</h3>
	</div>
	<div class="span3">
		<div class="pull-right">
			<a class="btn btn-primary" data-ng-href="/kitchenoffice-webapp/location/{{location.id}}/edit" data-ng-disabled="processing"><i class="icon-edit"></i> Edit this event</a>
		</div>
	</div>
</div>
<div class="row-fluid">
	<div class="span6">
		<div class="ko-thumb-container">
			<h5>Details</h5>
			<p>
				<i class="icon-map-marker"></i> {{location.address}}
			</p>
			<p data-ng-hide="location.website.length == 0">
				<i class="icon-home"></i> <a target="_blank" ng-href="{{location.website}}" >website</a>
			</p>
		</div>
		<div class="ko-thumb-container tags" data-ng-hide="location.tags == 0" >
			<h5><i class="icon-tags"></i> Tags</h5>
			<div class="ko-tag-list">
				<span data-ng-repeat="(idx, tag) in location.tags" class="badge badge-info tag">{{tag.name}}</span>
			</div>
		</div>
		<div class="ko-thumb-container" data-ng-hide="location.description.length == 0">
			<h5><i class="icon-info"></i> Description</h5>
			<p class="ko-tag-list">
				{{location.description}}
			</p>
		</div>
	</div>
	<div class="span6">
		<div id="map_canvas" class="ko-map-canvas" ui-map="locationMap" ui-options="mapOptions"></div>
	</div>
</div>