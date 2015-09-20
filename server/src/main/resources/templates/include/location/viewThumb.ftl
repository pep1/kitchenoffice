<%@ page language="java" pageEncoding="UTF-8"%>
<h4>{{location.name}}</h4>
<div class="ko-thumb-container">
	<h5>Details</h5>
	<p>
		<i class="icon-map-marker"></i> {{location.address}}
	</p>
</div>
<div class="ko-thumb-container tags" data-ng-hide="location.tags == 0" data-ng-switch="EXTERNAL">
	<h5><i class="icon-tags"></i> Tags</h5>
	<div class="ko-tag-list">
		<span data-ng-repeat="(idx, tag) in location.tags" class="badge badge-info tag">{{tag.name}}</span>
	</div>
</div>