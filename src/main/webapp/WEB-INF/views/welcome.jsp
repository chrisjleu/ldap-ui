<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h2>Welcome Page (welcome.jsp)</h2>
 
<sec:authorize access="isAnonymous()">

    <!-- use param.error assuming FormLoginConfigurer#failureUrl contains the query parameter error -->
    <c:if test="${param.error != null}">
        <div>
            Failed to login.
            <c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}">
              Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
            </c:if>
        </div>
    </c:if>
    <!-- the configured LogoutConfigurer#logoutSuccessUrl is /login?logout and contains the query param logout -->
    <c:if test="${param.logout != null}">
        <div>
            You have been logged out.
        </div>
    </c:if>

	<c:url var="loginUrl" value="/login"/>
    <form method="POST" action="${loginUrl}">
        Username: <input name="username" type="text" value="${SPRING_SECURITY_LAST_USERNAME}" /> 
        Password: <input name="password" type="password" /> 
        <input type="submit" value="Sign in" />
    </form>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
	<c:url var="logoutUrl" value="/logout"/>
    <form method="POST" action="${logoutUrl}">
        <input type="submit" value="Sign out" />
    </form>
</sec:authorize>

<p>${message}</p>	
</body>
</html>