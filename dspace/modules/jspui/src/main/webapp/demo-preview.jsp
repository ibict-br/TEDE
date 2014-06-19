<!DOCTYPE html>
<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>
<%--
  - HTML header for main home page
  --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="org.dspace.app.webui.util.JSPManager" %>
<%@ page import="org.dspace.core.ConfigurationManager" %>
<%@ page import="org.dspace.app.util.Util" %>
<%@ page import="javax.servlet.jsp.jstl.core.*" %>
<%@ page import="javax.servlet.jsp.jstl.fmt.*" %>
<!-- 
 * Markup for jQuery Reveal Plugin 1.0
 * www.ZURB.com/playground
 * Copyright 2010, ZURB
 * Free to use under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 -->
	<head>
		<meta charset="utf-8" />
		<title>Reveal Demo</title>
		
		<!-- Attach our CSS -->
	  	<link rel="stylesheet" href="reveal.css">	
		<script type="text/javascript" src="jquery.reveal.js"></script>
	  	
		<!-- Attach necessary scripts -->
		<!-- <script type="text/javascript" src="jquery-1.4.4.min.js"></script> -->
		<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.min.js"></script>
		
		<style type="text/css">
			body { font-family: "HelveticaNeue","Helvetica-Neue", "Helvetica", "Arial", sans-serif; }
			.big-link { display:block; margin-top: 100px; text-align: center; font-size: 70px; color: #06f; }
		</style>
		
	</head>
	<body>

		<a href="#" class="big-link" data-reveal-id="myModal">
			Fade and Pop
		</a>	
		
		<a href="#" class="big-link" data-reveal-id="myModal" data-animation="fade">
			Fade
		</a>
		
		<a href="#" class="big-link" data-reveal-id="myModal" data-animation="none">
			None
		</a>

		<div id="myModal" class="reveal-modal">
			<iframe src="http://docs.google.com/viewer?url=http://livroaberto.ibict.br/bitstream/1/906/1/controlada_energia_liberada.pdf&embedded=true" width="700" height="780" style="border: none;"></iframe>
		</div>
			
	</body>
</html>