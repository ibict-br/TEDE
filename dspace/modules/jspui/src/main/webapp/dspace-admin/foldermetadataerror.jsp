<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Form to upload a csv metadata file
--%>

<%@page import="java.io.File"%>
<%@page import="org.dspace.folderimport.dto.ErrorImportRegistry"%>
<%@page import="org.dspace.folderimport.constants.FolderMetadataImportConstants"%>
<%@page import="java.util.Map"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace"%>

<%@ page import="java.util.List"%>
<%@ page import="org.dspace.content.Collection"%>

<%
	Map<Long, ErrorImportRegistry> parentFolderMapping = (Map<Long, ErrorImportRegistry>) request.getSession().getAttribute(FolderMetadataImportConstants.ITEMS_WITH_ERROR_ON_IMPORT_KEY);
	String message = (String)request.getAttribute("message");

%>

<dspace:layout style="submission"
	titlekey="jsp.dspace-admin.foldermetadataerror.title" navbar="admin"
	locbar="link" parenttitlekey="jsp.administer"
	parentlink="/dspace-admin" nocache="true">

	<h1>
		<fmt:message key="jsp.dspace-admin.foldermetadataerror.title" />
	</h1>

	<%
	if (message != null){
	%>
		<div class="alert alert-warning">
			<fmt:message key="<%= message %>"></fmt:message>
		</div>
	<%  
    }
	%>
	
	<% if(parentFolderMapping != null && !parentFolderMapping.isEmpty())  {%>
	
		<div class="row" style="margin-top: 40px;">
			
			<div class="panel panel-primary">
				<div class="panel-heading text-center">
                	
                	<fmt:message key="jsp.dspace-admin.foldermetadataerror.table.item" /> 
					<span class="pull-right">
		            	<fmt:message key="jsp.dspace-admin.foldermetadataerror.table.files" /> 
					</span>
					   	
	         </div>
	         <ul class="list-group">
	         <% for(Map.Entry<Long, ErrorImportRegistry> currentError :  parentFolderMapping.entrySet())  
	         { 
	         	ErrorImportRegistry currentValue = currentError.getValue();
	         %>
	         	
       	      	<li class="list-group-item">
       	      		<table style="width: 100%">
       	      			<tr>
       	      				<td>
		         				<%= currentValue.getTitle() %>
       	      				</td>
       	      				<td style="text-align: right;">
		         				<% 	int i = 0;
		         					for(Map.Entry<Long, File> currentFile :  currentValue.getItemFiles().entrySet())  { %>
		         				
		        						<% i++; %>
		        						<a href="<%= request.getContextPath() + "/dspace-admin/foldermetadataerror?item=" + currentError.getKey() + "&file=" + currentFile.getKey() %>"><%= currentFile.getValue().getName() %></a>
		        						<% if(i < currentValue.getItemFiles().size())  { %>
		        							<br/>
		      							<% } %>
		         				
		         				<% } %>
       	      				</td>
       	      			</tr>
       	      		</table>
				</li>
	         
		 	<% } %>
		 </ul>
		 
	<% } %>

</dspace:layout>