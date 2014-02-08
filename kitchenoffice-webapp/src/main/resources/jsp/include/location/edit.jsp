<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row-fluid">
	<form name="locationForm" class="form-horizontal ko-form-fluid">
		<div class="span6">
			<div class="row-fluid">
				<div class="span3">
					<label class="pull-right help-inline">Name</label>
				</div>
				<div class="span9">
					<input name="locationName" type="text" class="input-block-level" min="3" data-ng-model="location.name" placeholder="Location name" required>
				</div>
				<span class="error" data-ng-show="locationForm.locationName.$error.min">Required!</span>
			</div>
			<div class="row-fluid">
				<div class="span3">
					<label class="pull-right help-inline">Address</label>
				</div>
				<div class="span9">
					<input name="locationAddress" type="text" class="input-block-level" min="3" data-ng-model="location.address" placeholder="Location address" required>
				</div>
			</div>
			<div class="row-fluid">
				<div class="span3">
					<label class="pull-right help-inline">Website</label>
				</div>
				<div class="span9">
					<input name="locationWebsite" type="url" class="input-block-level" data-ng-model="location.website" placeholder="Location website">
				</div>
			</div>
			<div class="row-fluid">
				<div class="span3">
					<label class="pull-right help-inline">Description</label>
				</div>
				<div class="span9">
					<textarea name="locationDescription" rows="4" class="input-block-level" data-ng-model="location.description" placeholder="Location description"></textarea>
					<span class="help-block"> <i class="icon-lightbulb"></i> You can use <a href="http://daringfireball.net/projects/markdown/" target="_blank">Markdown</a>.
					</span>
				</div>
			</div>
			<hr>
			<div class="row-fluid">
				<!-- form id="fileupload" action="rest/sound/upload/multiple" method="POST" enctype="multipart/form-data" data-fileupload="options" data-ng-class="{true: 'fileupload-processing'}[!!processing() || loadingFiles]">
					<div id="dropzone" class="fade well">Drop files here</div>
				</form-->
				<div class="span3">
					<label class="pull-right help-inline">Image</label>
				</div>
				<div class="span9">
					<div class="row-fluid">
						<input name="locationWebsite" type="url" class="input-block-level" data-ng-model="location.imageUrl" placeholder="paste image URL here">
					</div>
					<div class="row-fluid ko-image-preview">
						<img data-ng-src="{{location.imageUrl}}" class="image-preview" />
					</div>
				</div>
			</div>
			<hr>
			<div class="row-fluid">
				<div class="span3">
					<label class="pull-right help-inline">Tags</label>
				</div>
				<div class="span9">
					<tag-input tags="location.tags"></tag-input>
				</div>
			</div>
		</div>
		<div class="span6">
			<div class="row-fluid">
				<input type="text" class="search-query input-block-level" data-ng-model="locationSearchString" data-maps-search="locationMap" placeholder="Enter location name here to search with Google">
			</div>
			<div id="map_canvas" class="ko-map-canvas" data-ui-map="locationMap" data-ui-options="mapOptions"></div>
			<hr>
			<div class="row-fluid">
				<div class="span3">
					<label class="pull-right help-inline">Latitude</label>
				</div>
				<div class="span3">
					<input type="number" class="input-block-level" data-ng-model="location.latitude" placeholder="Latitude" disabled="disabled" required>
				</div>
				<div class="span3">
					<label class="pull-right help-inline">Longitude</label>
				</div>
				<div class="span3">
					<input type="number" class="input-block-level" data-ng-model="location.longitude" placeholder="Longitude" disabled="disabled" required>
				</div>
			</div>
		</div>

	</form>
</div>