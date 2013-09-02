<!-- location select carousel -->
<div ng-controller="LocationSelectController">
	<div class="row-fluid">
		<form class="form-search form-horizontal pull-right">
	    	<div class="input-append span12">
	        	<input type="text" class="location-search-query" placeholder="Search for locations" ng-model="locationSearchString">
	        	<button type="submit" class="btn"><i class="icon-search"></i></button>
	    	</div>
		</form>
	</div>
	<div class="row-fluid">
		<carousel class="ko-location-carousel well"> 
			<slide ng-repeat="page in pages" active="page.active">
				<div class="row-fluid">
					<div class="span3" ng-repeat="location in page.locations">
						<div class="ko-location-thumb" ng-class="{selected: selectedLocation==location }" ng-click="setLocation(location)">
							<jsp:include page="viewThumb.jsp"></jsp:include>
						</div>
					</div>
				</div>
	    	</slide>
		</carousel>
	</div>
	<div class="row-fluid">
		<span>Can't find the right location?&nbsp;&nbsp;</span><a class="btn" href="/kitchenoffice-webapp/location/create" >add new location</a>
	</div>
</div>