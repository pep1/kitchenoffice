<%@ page language="java" pageEncoding="UTF-8"%>
<div data-modal="doSave" data-close="saveModal.close()" data-options="saveModal.opts">
	<div class="modal-header">
		<h4>Save Event</h4>
	</div>
	<div class="modal-body">
		<p>are you sure to save following event?</p>
		<div class="well">
			<jsp:include page="viewThumb.jsp"></jsp:include>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn cancel" data-ng-click="saveModal.close()" data-ng-disabled="processing">Cancel</button>
		<button class="btn btn-primary" data-ng-click="doSaveEvent()" data-ng-disabled="processing">Save</button>
	</div>
</div>