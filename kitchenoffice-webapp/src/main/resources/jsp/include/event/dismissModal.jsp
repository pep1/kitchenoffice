<%@ page language="java" pageEncoding="UTF-8"%>
<div data-modal="doDismiss" data-close="dismissModal.close()" data-options="dismissModal.opts">
	<div class="modal-header">
		<h3>Dismiss food event</h3>
	</div>
	<div class="modal-body">
		<div class="well">
			<jsp:include page="viewThumb.jsp"></jsp:include>
		</div>
	</div>
	<div class="modal-footer">
		<span class="help-inline">do you really want to dismiss this event?&nbsp;&nbsp;</span>
		<button class="btn cancel" data-ng-click="dismissModal.close()" data-ng-disabled="processing">Cancel</button>
		<button class="btn btn-warning" data-ng-click="dismissEvent(event)" data-ng-disabled="processing">Dismiss</button>
	</div>
</div>