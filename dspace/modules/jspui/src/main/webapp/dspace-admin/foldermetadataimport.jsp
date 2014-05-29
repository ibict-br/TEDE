<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Form to upload a csv metadata file
--%>

<%@page import="java.util.Map"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace"%>

<%@ page import="java.util.List"%>
<%@ page import="org.dspace.content.Collection"%>

<%

	List<Collection> collections = (List<Collection>) request.getAttribute("collections");
	Map<Integer, String> supportedTypes = (Map<Integer, String>) request.getAttribute("supportedTypes");
	String hasErrorS = (String)request.getAttribute("has-error");
	boolean hasError = (hasErrorS==null) ? true : (Boolean.parseBoolean((String)request.getAttribute("has-error")));
    String message = (String)request.getAttribute("message");
%>

<dspace:layout style="submission"
	titlekey="jsp.dspace-admin.foldermetadataimport.title" navbar="admin"
	locbar="link" parenttitlekey="jsp.administer"
	parentlink="/dspace-admin" nocache="true">

	<h1>
		<fmt:message key="jsp.dspace-admin.foldermetadataimport.title" />
	</h1>

	<%
	if (hasErrorS == null){
	
	}
	else if (hasError && message!=null){
%>
	<%= message %>
	<%  
    }
	else if (hasError && message==null){
%>
	<div class="alert alert-warning">
		<fmt:message key="jsp.dspace-admin.batchmetadataimport.genericerror" />
	</div>
	<%  
	}
	else {
%>
	<div class="alert alert-info">
		<fmt:message key="jsp.dspace-admin.batchmetadataimport.success" />
	</div>
	<%  
	}
%>

	<form method="post" action="">

		<!-- 
			Folder location
		 -->
		<div class="form-group">
			<label for="folder_location">
				<fmt:message key="jsp.dspace-admin.foldermetadataimport.folder" />
			</label> 
			<input type="text" size="150" id="folder_location" name="folder_location" class="form-control">
		</div>
		
		<!-- 
			Import type
		 -->
		<div class="form-group">
			<label for="import_type">
				<fmt:message key="jsp.dspace-admin.foldermetadataimport.type" />
			</label> 
			<select	class="form-control" name="import_type">
			<% 
		 		for (Map.Entry<Integer, String> typeEntry : supportedTypes.entrySet()){
			%>
						<option value="<%= typeEntry.getKey() %>"><fmt:message key="<%= typeEntry.getValue() %>" /></option>
			<%
		 		}
	 		%>			
 		</select>
		</div>
		<div class="form-group">
			<label for="collection">
				<fmt:message key="jsp.dspace-admin.foldermetadataimport.collection" />
			</label> 
			<select class="form-control" name="collection">
			<% 
		 		for (Collection collection : collections){
			%>
					<option value="<%= collection.getID() %>"><%= collection.getName() %></option>
			<%
			 		}
			 %>
			</select>
		</div>

		<input class="btn btn-success" type="submit" name="submit"
			value="<fmt:message key="jsp.dspace-admin.general.upload"/>" />

	</form>

</dspace:layout>