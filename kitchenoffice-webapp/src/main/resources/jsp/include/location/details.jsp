<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row-fluid">
	<div class="span9">
		<h3>
			<i class="icon-map-marker"></i> {{location.name}}
		</h3>
	</div>
	<div class="span3">
		<div class="pull-right">
			<a class="btn btn-primary" data-ng-href="/kitchenoffice-webapp/location/{{location.id}}/edit" data-ng-disabled="processing"> <i class="icon-edit"></i> Edit this location
			</a>
		</div>
	</div>
</div>
<div class="row-fluid">
	<div class="span6">
		<div class="ko-thumb-container">
			<div class="row-fluid">
				<div class="span6">
					<h5>Details</h5>
					<p>
						<i class="icon-map-marker"></i> {{location.address}}
					</p>
					<p data-ng-show="location.website">
						<i class="icon-globe"></i> <a target="_blank" data-ng-href="{{location.website}}">Location Website</a>
					</p>
				</div>
				<div class="span6">
					<div class="thumbnail pull-right">
						<img ng-src="{{location.getThumbURL(278, 160)}}" />
					</div>
				</div>
			</div>
		</div>
		<div class="ko-thumb-container tags" data-ng-show="location.tags">
			<h5>
				<i class="icon-tags"></i> Tags
			</h5>
			<div class="ko-tag-list">
				<span data-ng-repeat="(idx, tag) in location.tags" class="badge badge-info tag">{{tag.name}}</span>
			</div>
		</div>
		<div class="ko-thumb-container" data-ng-hide="location.description.length == 0">
			<h5>
				<i class="icon-info"></i> Description
			</h5>
			<p class="ko-tag-list" data-markdown data-input="{{location.description}}"></p>
		</div>
		<div class="pull-left"></div>
		<div class="pull-right">
			<div class="ko-switch">
				E-Mail Subscription: <input bs-switch ng-model="subscribeLocation" switch-size="small" switch-on-label="on" switch-off-label="off" switch-on="warning" switch-type="checkbox" switch-animate="true" class="make-switch" switch-active="{{!processing}}" switch-icon="icon-envelope-alt">
			</div>
		</div>
	</div>
	<div class="span6">
		<div id="map_canvas" class="ko-map-canvas" ui-map="locationMap" ui-options="mapOptions"></div>
	</div>
</div>