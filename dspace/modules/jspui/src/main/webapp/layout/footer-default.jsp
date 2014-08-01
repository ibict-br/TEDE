<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Footer for home page
  --%>

<%@page import="org.dspace.core.ConfigurationManager"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page contentType="text/html;charset=UTF-8"%>

<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.dspace.app.webui.util.UIUtil"%>

<%
	String sidebar = (String) request
			.getAttribute("dspace.layout.sidebar");
%>

<%-- Right-hand side bar if appropriate --%>
<%
	if (sidebar != null) {
%>
</div>
<div class="col-md-3">
	<%=sidebar%>
</div>
</div>
<%
	}
%>
</div>
</main>
<%-- Page footer --%>
<footer class="navbar navbar-inverse navbar-bottom">

	<div id="tede-footer" class="container text-muted" style="padding-left: 0px;">
		
		<div class="text-center col-md-12">
			<a href="http://www.ibict.br/" target="_blank">
			<img class="footer-logo pull-left" src="<%= request.getContextPath() %>/image/logo-ibict.png"></a>
       		<a href="http://bdtd.ibict.br/" target="_blank">
			<img class="pull-left" src="<%= request.getContextPath() %>/image/logo-bdtd-rodape.png"></a>
       		
       		
       		<div class="footer-logo pull-right">
			
	       		
				<a target="_blank" href="<%= request.getContextPath() %>/feedback">
				<img class="pull-left" src="<%= request.getContextPath() %>/image/logo-instituicao-rodape.png"></a>
	       		<div class="pull-left" style="padding-left: 5px;">
		       		<div class="footer-institution-data" style="font-weight: bold;">
		       			<%= ConfigurationManager.getProperty("dspace.institution.heading1") %>
		       		</div>
		       		<div class="footer-institution-data">
		       			<%= ConfigurationManager.getProperty("dspace.institution.phone") %>
		       		</div>
		       		<div class="footer-institution-data">
		       			<%= ConfigurationManager.getProperty("dspace.institution.email") %>
		       		</div>
	       		</div>
			
       		</div>
			
			
			
		</div>
		
	</div>

</footer>
</body>
</html>