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
	
	
	<div id="TEDEtext>
	<h2> <b>Sobre o TEDE</b></h2>			

<p align="justify">O Sistema de Publicação Eletrônica de Teses e Dissertações (TEDE), desenvolvido e mantido pelo <a target="_blank" href="http://www.ibict.br/">Ibict</a>,
tem por objetivo proporcionar a implantação de bibliotecas digitais de teses e dissertações nas instituições de ensino pesquisa e, com isso,
a sua integração à <a target="_blank" href="http://bdtd.ibict.br/">Biblioteca Digital Brasileira de Teses e Dissertações (BDTD).</a> 
Após dez anos de desenvolvido, o TEDE foi totalmente atualizado. A nova versão do sistema, chamada aqui de TEDE2, está configurado no software livre
DSpace, a mesma plataforma utilizada para a criação de Repositórios Digitais de Acesso Aberto. O TEDE2 já está configurado de acordo com o Novo Padrão de Metadados da BDTD estando totalmente interoperável com outros sistemas. Ainda, é possível
fazer as customizações na interface e definições do fluxo de trabalho dentro do sistema. As instituições que utilizam o antigo TEDE podem migrar diretamente as suas teses e dissertações para o TEDE2. Para isso, o IBICT desenvolveu
uma ferramenta que possibilita a migração das informações sem a sua perda e sem trabalho duplicado. Entre em contato com a Equipe BDTD e 
obtenha mais informações sobre esse processo. </p>

<p align="justify">A execução do projeto do TEDE 2 toma como base o Software DSpace, com a interface JSPUI, 
e possui customizações específicas (configurações, modificações de layout e itens de desenvolvimento) para adaptação às características das teses e dissertações. Muitas dessas customizações 
apresentam-se úteis não só para essa tipologia documental restrita, mas para a comunidade geral de usuários do DSpace. Para tanto, definiu-se que as novas funcionalidades seriam organizadas 
em grupos que poderiam dar origens a plugins do projeto original do DSpace.</p> 

<p>Para mais informações consulte: &lt;<a href="http://wiki.ibict.br/index.php/TEDE">http://wiki.ibict.br/index.php/TEDE</a></p>
 
				</br>
				<table style="width:100%">
  				<tbody><tr><td>
				<p><strong>Realização</strong></p>
				<blockquote>
				<p><strong>Instituto Brasileiro de Informação em Ciência e Tecnologia (Ibict)</strong></p> 
				</blockquote>
  				</td>
  				<td>
				<p><strong>Colaborações</strong></p>
				<blockquote>
					<p><strong>Universidade Federal de Goiás (UFG)</strong></p>
					<p><strong>Universidade Federal de Pelotas (UFPel)</strong></p>
				</blockquote>			
  				</td></tr>
				</tbody></table> 
				

	</div>

			<div class="jumbotron">
			
				
				<%=topNews%>

				

		</div>

	</div>
</dspace:layout>
