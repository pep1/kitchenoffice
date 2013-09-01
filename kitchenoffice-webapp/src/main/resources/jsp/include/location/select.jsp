<!-- Carousel -->
<div ng-controller="LocationSelectController">
	<div> 
		<div ng-repeat="location in locations">
			<div class="carousel-caption">
				<h4>location.name</h4>
				<p>location.description</p>
			</div>
		</div> 
	</div>
</div>