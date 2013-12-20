<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row-fluid" data-ng-hide="areLocationsEmpty">
	<div class="pull-left" data-ng-submit="doSearch(locationSearchString)">
		<div class="input-append">
			<input type="text" placeholder="Search for locations" data-ng-model="locationSearchString">
			<button type="submit" class="btn" data-ng-click="cleanSearch()">
				<i class="icon-eraser"></i>
			</button>
			<button type="submit" class="btn" data-ng-click="doSearch(locationSearchString)">
				<i class="icon-search"></i>
			</button>
		</div>
	</div>
	<div class="pull-right">
		<span>Can't find the right location?&nbsp;&nbsp;</span><a class="btn btn-primary" href="/kitchenoffice-webapp/location/create"><i class="icon-edit"></i> add new location</a>
	</div>
</div>
<div class="row-fluid">
	<carousel class="ko-location-carousel well"> <slide ng-repeat="page in pages" active="page.active">
	<div class="row-fluid">
		<div class="span3" data-ng-repeat="location in page.locations">
			<div class="ko-location-thumb" data-ng-class="{selected: selectedLocation==location }" data-ng-click="selectLocation(location)">
				<jsp:include page="viewThumb.jsp"></jsp:include>
			</div>
		</div>
	</div>
	</slide>
	<div data-ng-show="areLocationsEmpty() && locationSearchString.length > 0" class="center">
		<p class="center">
			there are no location entries yet<br>
			<a href="/kitchenoffice-webapp/location/create" class="btn btn-large btn-primary">
			<i class="icon-edit"></i> create location</a>
		</p>
	</div>
	<div data-ng-show="areLocationsEmpty() && locationSearchString.length < 0" class="center">
		<p class="center">
			Sorry there is no location called "{{locationSearchString}}"<br>
			<a href="/kitchenoffice-webapp/location/create" class="btn btn-large btn-primary">
			<i class="icon-edit"></i> create location</a>
		</p>
	</div>
	</carousel>
</div>