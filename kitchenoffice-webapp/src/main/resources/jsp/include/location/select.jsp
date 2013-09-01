<!-- Location Carousel -->
<div ng-controller="LocationSelectController">
	<carousel> 
		<slide ng-repeat="slide in slides">
			<div class="carousel-caption">
				<h4>{{slide.name}}</h4>
				<p>{{slide.description}}</p>
			</div>
		</slide> 
	</carousel>
</div>