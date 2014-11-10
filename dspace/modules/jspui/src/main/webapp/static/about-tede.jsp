<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>

<%@ page import="org.dspace.core.NewsManager"%>
<%@ page import="javax.servlet.jsp.jstl.fmt.LocaleSupport"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace"%>

<%
	String topNews = NewsManager.readNewsFile(LocaleSupport.getLocalizedMessage(pageContext, "news-top.html"));
%>

<dspace:layout titlekey="jsp.home.about">

	<div id="pageContents">

			<div class="jumbotron">
				<h3>
					<fmt:message key="jsp.home.about"></fmt:message>
				</h3>
				<%=topNews%>
				<p class="about-bdtd">
					<a href="bdtd.jsp"><button class="btn btn-primary">
							<fmt:message key="jsp.home.about.bdtd" />
						</button></a>
				</p>

		</div>

	</div>
</dspace:layout>
