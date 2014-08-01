<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - fragment JSP to be included in site, community or collection home to show discovery facets
  -
  - Attributes required:
  -    discovery.fresults    - the facets result to show
  -    discovery.facetsConf  - the facets configuration
  -    discovery.searchScope - the search scope 
  --%>


<%@page import="java.net.URLEncoder"%>
<%@page import="org.dspace.app.webui.discovery.dto.CloudTagResult"%>
<%@page import="java.util.List"%>

<%

	List<CloudTagResult> tagResult = (List<CloudTagResult>) request.getAttribute("cloudResult");

%>
<%-- <h3 class="facets"><fmt:message key="jsp.search.cloudtag" /></h3> --%>
<div id="cloud" class="tag-cloud-container">
	<span class="shadow"></span>
	
	<div id="cloud-text">
	
	<%
		if(tagResult != null && !tagResult.isEmpty())
		{
			for(CloudTagResult cloudTag : tagResult)
			{
	%>
		
		<span>
			<a href="<%= request.getContextPath()
	                + "/simple-search?filterquery="+URLEncoder.encode(cloudTag.getCloudName(),"UTF-8")
	                + "&amp;filtername="+URLEncoder.encode(cloudTag.getField(),"UTF-8")
	                + "&amp;filtertype="+URLEncoder.encode("equals","UTF-8") %>" class="cloudtag-<%= cloudTag.getRelevance() %>"><%= cloudTag.getCloudName() %></a>
		</span>
	
	<%
			}
		}
	%>
	</div>
</div>