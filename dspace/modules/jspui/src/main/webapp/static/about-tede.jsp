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
				<h2>
					<fmt:message key="jsp.home.about"></fmt:message>
				</h2>
				<%=topNews%>
				
				<p><strong>Realização</strong></p>
				<blockquote>
					<p><strong>Instituto Brasileiro de Informação em Ciência e Tecnologia (Ibict)</strong></p>
					<blockquote>
						<p><strong>Coordenação-Geral de Pesquisa e Manutenção de Produtos Consolidadados (CGPM)</strong></p> 
						<p><strong>Lilian M. Araújo de Rezende Alvares</strong></p>
						<blockquote>
							<p><strong>Coordenação de Metodologias de Tratamento e Disseminação da Informação (COLI)</strong></p>
							<p><strong>Bianca Amaro de Melo</strong></p>
							<p>Tainá Batista de Assis</p>
						</blockquote>
						<blockquote>
				        		<p><strong>Coordenação de Articulação, Geração e Aplicação de Tecnologia (COAT)</strong></p>
				        		<p><strong>Milton Shintaku</strong></p>
				        		<p>Washington L. R. de Carvalho Segundo</p>
				        		<p>Diego José Macêdo</p>
				        		<p>Márcio Ribeiro Gurgel do Amaral</p>
				        	</blockquote>
					</blockquote>
				</blockquote>
				<p><strong>Colaboração</strong></p>
				<p><strong>Universidade Federal de Goiás (UFG)</strong></p>
				<blockquote>
					<p><strong>Cláudia Oliveira de Moura Bueno</strong></p>
					<p>Carla L. Ferreira</p>
					<p>Marcos Roberto Ribeiro</p>
					<p>Pablo Henrique Rodrigues</p>
				</blockquote>
				<p><strong>Universidade Federal de Pelotas (UFPel)</strong></p>
				<blockquote>
					<p><strong>Daiane Almeida Schramm</strong></p>
					<p>Aline Herbstrith Batista</p>
					<p>Henrique de Vasconcellos Rippel</p>
					<p>Tiago Camargo Al-Alam</p>
				</blockquote>
				
				<p class="about-bdtd">
					<a href="bdtd.jsp"><button class="btn btn-primary">
							<fmt:message key="jsp.home.about.bdtd" />
						</button></a>
				</p>

		</div>

	</div>
</dspace:layout>
