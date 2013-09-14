<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row-fluid">
	<h1>
		<i class="icon-map-marker"></i> Add a location to get some food
	</h1>
</div>
<hr>
<div class="row-fluid">
	<div class="span6">
		<form class="form-inline">
			<input type="text" class="search-query input-block-level" ng-model="locationSearchString" maps-search="locationMap" placeholder="Enter location name here to search with Google">
		</form>
		<div id="map_canvas" class="ko-map-canvas" ui-map="locationMap" ui-options="mapOptions"></div>
	</div>
	<div class="span6">
		<form name="locationForm" class="form-horizontal ko-form-fluid">
			<div class="row-fluid">
				<div class="span4">
					<label class="pull-right help-inline">Name</label>
				</div>
				<div class="span8">
					<input name="locationName" type="text" class="input-block-level"
						min="3" ng-model="location.name"
						placeholder="Location name" required>
				</div>
				<span class="error"
					ng-show="locationForm.locationName.$error.min">Required!</span>
			</div>
			<div class="row-fluid">
				<div class="span4">
					<label class="pull-right help-inline">Address</label>
				</div>
				<div class="span8">
					<input name="locationAddress" type="text"
						class="input-block-level" min="3"
						ng-model="location.address"
						placeholder="Location address" required>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span4">
					<label class="pull-right help-inline">Website</label>
				</div>
				<div class="span8">
					<input name="locationWebsite" type="text"
						class="input-block-level" ng-model="location.website"
						placeholder="Location website">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span4">
					<label class="pull-right help-inline">Description</label>
				</div>
				<div class="span8">
					<textarea name="locationDescription" rows="4"
						class="input-block-level" ng-model="location.description"
						placeholder="Location description"></textarea>
				</div>
			</div>
			<hr>
			<div class="row-fluid">
				<div class="span3">
					<label class="pull-right help-inline">Latitude</label>
				</div>
				<div class="span3">
					<input type="number" class="input-block-level"
						ng-model="location.latitude" placeholder="Latitude"
						disabled="disabled" required>
				</div>
				<div class="span3">
					<label class="pull-right help-inline">Longitude</label>
				</div>
				<div class="span3">
					<input type="number" class="input-block-level"
						ng-model="location.longitude" placeholder="Longitude"
						disabled="disabled" required>
				</div>
			</div>
		</form>
	</div>
</div>
<button class="btn btn-primary btn-large" ng-click="saveLocation(locationForm)" ng-disabled="!isValid(locationForm)"><i class="icon-save"></i> save location</button>
<hr>
<pre>
DEBUG:
Location {{location}}
</pre>

<jsp:include page="../../include/location/createModal.jsp"></jsp:include>