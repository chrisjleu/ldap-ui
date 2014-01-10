<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<sec:authorize access="isAnonymous()">
<c:url var="loginProcessingUrl" value="/login" />
 <form action="${loginProcessingUrl}" method="post">
    <fieldset>
        <legend>Please Login</legend>
        <!-- use param.error assuming FormLoginConfigurer#failureUrl contains the query parameter error -->
        <c:if test="${param.error != null}">
            <div>
                Failed to login.
                <c:if test="${SPRING_SECURITY_LAST_EXCEPTION != null}">
                  Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
                </c:if>
            </div>
        </c:if>
        <c:if test="${param.logout != null}">
            <div>
                You have been logged out.
            </div>
        </c:if>
        <p>
        <label for="username">Username</label>
        <input type="text" id="username" name="username"/>
        </p>
        <p>
        <label for="password">Password</label>
        <input type="password" id="password" name="password"/>
        </p>
        <div>
            <button type="submit" class="btn">Log in</button>
        </div>
    </fieldset>
 </form>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
	<p>You are already logged in. <a href="/">Home page</a>.</p>
</sec:authorize>


