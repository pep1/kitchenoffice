<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="gravatar" uri="/WEB-INF/classes/tld/gravatarTag.tld"%>
<div class="navbar navbar-inverse navbar-fixed-top" >
	<div class="navbar-inner">
		<div class="container">
			<button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="brand" href="/${project.build.finalName}/home"> <spring:message code="header.brand" />
			</a>
			<div class="nav-collapse in collapse">
				<p class="navbar-text pull-right">
					<spring:message code="header.greeting" />
					<a href="#" class="navbar-link">${user.username}</a> <img src='<gravatar:gravatar email="${user.email}" size="40" https="true" />' />
				</p>
				<ul class="nav">
					<li class="dropdown" >
						<a href="#" class="dropdown-toggle" data-active-dropdown="active" data-toggle="dropdown"><i class="icon-food"></i> Events<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="/${project.build.finalName}/home" data-active-link="active"><i class="icon-fixed-width icon-home"></i> show Events</a></li>
							<li><a href="/${project.build.finalName}/event/create" data-active-link="active"><i class="icon-fixed-width icon-edit"></i> create Event</a></li>
						</ul>
					</li>
					<li>
						<a href="/${project.build.finalName}/event/rss" target="_blank">
							<i class="icon-rss"></i>
						</a>
					</li>
					<li class="divider-vertical"></li>
					<li class="dropdown" data-active-dropdown="active">
						<a href="#" class="dropdown-toggle" data-active-dropdown="active" data-toggle="dropdown"><i class="icon-map-marker"></i> Locations <b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="/${project.build.finalName}/location" data-active-link="active"><i class="icon-fixed-width icon-map-marker"></i> show Locations</a></li>
							<li><a href="/${project.build.finalName}/location/create" data-active-link="active"><i class="icon-fixed-width icon-edit"></i> add Location</a></li>
						</ul>
					</li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
</div>