<carousel interval="myInterval">
  <slide ng-repeat="location in locations" active="slide.active">
    <img ng-src="{{slide.image}}" style="margin:auto;">
    <div class="carousel-caption">
      <h4>Slide {{$index}}</h4>
      <p>{{slide.text}}</p>
    </div>
  </slide>
</carousel>
