<%@ page language="java" pageEncoding="UTF-8"%>
<div class="row-fluid">
	<h4><i class="icon-comments-alt"></i> Comments</h4>
	<form ng-submit="comment(newComment)">
		<textarea class="input-block-level" ng-model="newComment" placeholder="Comment this event ..." rows="1"></textarea>
		<input type="submit" >
	</form>
	<div data-ng-hide="event.comments.length == 0">
		<div class="media" data-ng-repeat="comment in event.comments | orderBy:'timeStamp':true">
			<a class="pull-left" href="#">
				<gravatar-image data-email="comment.user.email" data-size="50" data-secure="true"></gravatar-image>
			</a>
			<div class="media-body">
				<h5 class="media-heading">{{comment.user.username}} wrote <small>{{fromNow(comment.timeStamp)}}</small></h5>
				{{comment.comment}}
			</div>
		</div>
	</div>
</div>