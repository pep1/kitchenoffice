<%@ page language="java" pageEncoding="UTF-8"%>
<div data-modal="doAttend" data-close="attendModal.close()" data-options="attendModal.opts">
	<div class="modal-header">
		<h3 data-ng-switch="EXTERNAL">Attend food event {{selectedEvent.location.name}}</h3>
	</div>
	<div class="modal-body">
		<div class="well">
			<jsp:include page="viewThumb.jsp"></jsp:include>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn cancel" data-ng-click="attendModal.close()">Cancel</button>
		<button class="btn btn-primary" data-ng-click="attendEvent(event, job)">Attend</button>
	</div>
</div>