<%@ page session="true"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form commandName="user" action="register" method="POST">

	<label>Username</label>
	<form:input type="text" value="" class="input-xlarge" path="username" />
	<form:errors path="username" cssClass="alert alert-error" element="div" />
	<label>Email</label>
	<form:input type="email" value="" class="input-xlarge" path="email" />
	<form:errors path="email" cssClass="alert alert-error" element="div" />
	<label>First Name</label>
	<form:input type="text" value="" class="input-xlarge" path="firstName" />
	<form:errors path="firstName" cssClass="alert" element="div" />
	<label>Last Name</label>
	<form:input type="text" value="" class="input-xlarge" path="lastName" />
	<form:errors path="lastName" cssClass="alert alert-error" element="div" />
	<label>Password</label>
	<form:input type="password" value="" class="input-xlarge"
		path="password" />
	<form:errors path="password" cssClass="alert alert-error" element="div" />
	<div>
		<button class="btn btn-primary">Create Account</button>
	</div>
</form:form>