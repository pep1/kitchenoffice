<%@ page language="java" pageEncoding="UTF-8"%>
<div data-modal="doSave" data-close="saveModal.close()" data-options="saveModal.opts">
	<div class="modal-header">
		<h4>Save Location</h4>
	</div>
	<div class="modal-body">
		<p>are you sure to save following location?</p>
		<div class="well">
			<jsp:include page="viewThumb.jsp"></jsp:include>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn cancel" data-ng-click="saveModal.close()">Cancel</button>
		<button class="btn btn-primary" data-ng-click="doSaveLocation()">Save</button>
	</div>
</div>