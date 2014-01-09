<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url var="findPersonByNameUrl" value="/person"/>
<h3>Find all users by name (cn)</h3>
<form method="POST" action="${findPersonByNameUrl}">
    Name: <input name="name" type="text"/> 
    <input type="submit" value="Find by name (cn)" />
</form>

<c:url var="bindCheckUrl" value="/bindcheck"/>
<h3>Authenticate user</h3>
<form method="POST" action="${bindCheckUrl}">
    DN: <input name="dn" type="text"/> 
    Password: <input name="pwd" type="password" /> 
    <input type="submit" value="Check bind" />
</form>

<c:if test="${not empty message}">
	<h2>Results</h2>
	<p>${message}</p>
</c:if>
