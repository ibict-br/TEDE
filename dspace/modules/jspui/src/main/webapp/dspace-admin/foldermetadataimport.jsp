<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - Form to upload a csv metadata file
--%>

<%@ page import="org.dspace.app.webui.servlet.constants.FolderMetadataImportConstants"%>
<%@ page import="java.util.Map"%>
<%@ page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace"%>

<%@ page import="java.util.List"%>
<%@ page import="org.dspace.content.Collection"%>

<%

	List<Collection> collections = (List<Collection>) request.getAttribute("collections");
	Map<Integer, String> supportedTypes = (Map<Integer, String>) request.getAttribute("supportedTypes");
	Map<Long, String> userDataSelection = (Map<Long, String>) request.getSession().getAttribute(FolderMetadataImportConstants.USER_DATA_READBLE_KEY);
	Map<Long, Long> parentFolderMapping = (Map<Long, Long>) request.getSession().getAttribute(FolderMetadataImportConstants.PARENT_FOLDER_MAPPPING);
	String exportDateTime = (String) request.getAttribute("exportDateTime");
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

	<%
		if(exportDateTime != null)
		{
	%>
	
		<div class="alert alert-info">
			<fmt:message key="jsp.dspace-admin.foldermetadataimport.dateexport" >
				<fmt:param value="<%=  exportDateTime %>"/>
			</fmt:message>
		</div>
		
	<%
		}
	%>

	<form method="post" action="" id="folder_form">

	
		<!-- 
			Folder location / Checkbox
		 -->
		<%
			if (userDataSelection != null && !userDataSelection.isEmpty()) {
		%>
		<div class="form-group">
			<label for="import_type"> <fmt:message
					key="jsp.dspace-admin.foldermetadataimport.folder" />
			</label>

			<%
				for (Map.Entry<Long, String> typeEntry : userDataSelection.entrySet()) {
			%>
			<div class="input-group">
				<span class="input-group-addon"> 
				
				<input type="checkbox" type="checkbox" id="folder_<%=typeEntry.getKey() + (parentFolderMapping.get(typeEntry.getKey()) != null ? "_parent_" + parentFolderMapping.get(typeEntry.getKey()) : "")%>" name="selected_folders" value="true" />
				</span> 
					<label class="form-control" for="folder_<%=typeEntry.getKey() + (parentFolderMapping.get(typeEntry.getKey()) != null ? "_parent_" + parentFolderMapping.get(typeEntry.getKey()) : "")%>"><%=typeEntry.getValue()%>
				</label>
			</div>
			<%
				}
			%>

		</div>
		<%
			}
		%>

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

		<input class="btn btn-success" type="submit_not_working_yet" name="submit"
			value="<fmt:message key="jsp.dspace-admin.general.upload"/>" />
			
		<input class="btn btn-default" type="submit" name="submit_return"
			value="<fmt:message key="jsp.dspace-admin.foldermetadataimport.button.back"/>" />

	</form>
	
	
	<script type="text/javascript">

		$(document).ready(function(){

			$("input[id^='folder_']").click(function(){
				
				if(this.id.search('parent') != -1)
				{
				}
				else
				{
					selectAllChildren(this);
				}
				
			});
			
		});

		function selectAllChildren(parentObject)
		{
			$("input[id*='parent_" + parentObject.id.split("_")[1] + "']").click();
		}
		
	</script>

</dspace:layout>