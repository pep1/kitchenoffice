<%@ page language="java" pageEncoding="UTF-8"%>
<div data-modal="doAttend" data-close="attendModal.close()" data-options="attendModal.opts">
	<div class="modal-header">
		<h3 data-ng-switch="EXTERNAL">Attend food event {{selectedEvent.location.name}}</h3>
	</div>
	<div class="modal-body">
		<p>
			<span class="label label-info" data-ng-switch="EXTERNAL"><i class="icon-food"></i> go out eating to</span>
		</p>
		<p>
			<i class="icon-time"></i> {{fromNow(selectedEvent.date)}}
		<p>
		<p data-ng-switch="EXTERNAL">
			<i class="icon-map-marker"></i> {{selectedEvent.location.address}}
		</p>
		<p data-ng-hide="isEmpty(event.creator)">
			<i class="icon-user"></i>
			<gravatar-image data-email="event.creator.email" data-size="30" data-secure="true"></gravatar-image>
			{{selectedEvent.creator.username}}
		<p>
	</div>
	<div class="modal-footer">
		<button class="btn cancel" data-ng-click="attendModal.close()">Cancel</button>
		<button class="btn btn-primary" data-ng-click="attendEvent(selectedEvent)">Attend</button>
	</div>
</div>