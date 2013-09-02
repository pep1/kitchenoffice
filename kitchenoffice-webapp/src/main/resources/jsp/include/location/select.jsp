<!-- location select carousel -->
<div ng-controller="LocationSelectController">
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
	<span>Can't find the right location?&nbsp;&nbsp;</span><a class="btn" href="/kitchenoffice-webapp/location/create" >add new location</a>
</div>