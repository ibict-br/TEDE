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

<script type="text/javascript" src="<%= request.getContextPath() %>/static/js/jQCloud/jqcloud-1.0.3.js"></script>
<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/jQCloud/jqcloud.css" type="text/css" />

<%

	List<CloudTagResult> tagResult = (List<CloudTagResult>) request.getAttribute("cloudResult");

%>
    	<%
		if(tagResult != null && !tagResult.isEmpty())
		{
		%>
			<div class="cloud-tag-label">
				<h3>Nuvem de tags</h3>
			</div>
			<div id="cloud2" class="tag-cloud-container"></div>
		<%
		}
		%>

<script type="text/javascript">
    	<%
		if(tagResult != null && !tagResult.isEmpty())
		{
		%>
	     var word_list = [
			<%
				int i = 0;
				for(CloudTagResult cloudTag : tagResult)
				{
			%>
	                {text: "<%= cloudTag.getCloudName() %>", weight: <%= cloudTag.getRelevance() %>, link: "<%= request.getContextPath() + "/simple-search?query="+URLEncoder.encode(cloudTag.getCloudName(),"UTF-8") %>"}<%= (++i < tagResult.size()) ? "," : "" %>
			<%
				}
			%>
			];
			
			
			jQuery(document).ready(function(){
	         	jQuery("#cloud2").jQCloud(word_list);
	       });
		<%
		}
		%>
        
</script>