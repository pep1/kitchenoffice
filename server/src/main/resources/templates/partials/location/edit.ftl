<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row-fluid">
	<h1>
		<i class="icon-edit"></i> Edit location
	</h1>
</div>
<hr>
<jsp:include page="../../include/location/edit.jsp"></jsp:include>
<hr>
<pre>
DEBUG:
Location {{location}}
</pre>
<hr>
<button class="btn btn-primary btn-large" ng-click="saveLocation(locationForm)" ng-disabled="!isValid(locationForm)"><i class="icon-save"></i> save location</button>
<jsp:include page="../../include/location/createModal.jsp"></jsp:include>