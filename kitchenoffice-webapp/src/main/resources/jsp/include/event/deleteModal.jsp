<%@ page language="java" pageEncoding="UTF-8"%>
<div data-modal="doDelete" data-close="deleteModal.close()" data-options="deleteModal.opts">
	<div class="modal-header">
		<h3>Delete food event</h3>
	</div>
	<div class="modal-body">
		<div class="well">
			<jsp:include page="viewThumb.jsp"></jsp:include>
		</div>
	</div>
	<div class="modal-footer">
		<span class="help-inline">do you really want to delete this event?&nbsp;&nbsp;</span>
		<button class="btn cancel" data-ng-click="deleteModal.close()" data-ng-disabled="processing">Cancel</button>
		<button class="btn btn-danger" data-ng-click="deleteEvent(event)" data-ng-disabled="processing"><i class="icon-trash"></i> Delete</button>
	</div>
</div>