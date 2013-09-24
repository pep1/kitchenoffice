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
			<div class="nav-collapse collapse">
				<p class="navbar-text pull-right">
					<spring:message code="header.greeting" />
					<a href="#" class="navbar-link">${user.username}</a> <img src='<gravatar:gravatar email="${user.email}" size="40" />' />
				</p>
				<ul class="nav">
					<li><a href="/${project.build.finalName}/home" data-active-link="active"><i class="icon-home"></i> Home</a></li>
					<li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">Events <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                          <li><a href="/${project.build.finalName}/event/create" data-active-link="active"><i class="icon-edit"></i> create Event</a></li>
                        </ul>
                      </li>
					<li><a href="/${project.build.finalName}/location/create" data-active-link="active"><i class="icon-map-marker"></i> add Location</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</div>
</div>