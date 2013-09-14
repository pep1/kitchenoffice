<%@ page language="java" pageEncoding="UTF-8"%>
<div data-modal="doAttend" data-close="attendModal.close()" data-options="attendModal.opts">
	<div class="modal-header">
		<h3>Attend food event</h3>
	</div>
	<div class="modal-body">
		<div class="well">
			<jsp:include page="viewThumb.jsp"></jsp:include>
		</div>
	</div>
	<div class="modal-footer">
		<span class="help-inline">do you really want to attend this event?&nbsp;&nbsp;</span>
		<button class="btn cancel" data-ng-click="attendModal.close()" data-ng-disabled="processing">Cancel</button>
		<button class="btn btn-primary" data-ng-click="attendEvent(event, job)" data-ng-disabled="processing">Attend</button>
	</div>
</div>