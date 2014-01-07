<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url var="bindCheckUrl" value="/bindcheck"/>
<h3>Bind Checker</h3>
<form method="POST" action="${bindCheckUrl}">
    DN: <input name="dn" type="text"/> 
    Password: <input name="pwd" type="password" /> 
    <input type="submit" value="Check bind" />
</form>

<p>${message}</p>