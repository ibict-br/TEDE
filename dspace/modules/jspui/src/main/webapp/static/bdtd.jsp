<%--

    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/

--%>

<%--
  - Display hierarchical list of communities and collections
  -
  - Attributes to be passed in:
  -    communities         - array of communities
  -    collections.map  - Map where a keys is a community IDs (Integers) and 
  -                      the value is the array of collections in that community
  -    subcommunities.map  - Map where a keys is a community IDs (Integers) and 
  -                      the value is the array of subcommunities in that community
  -    admin_button - Boolean, show admin 'Create Top-Level Community' button
  --%>

<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.dspace.org/dspace-tags.tld" prefix="dspace" %>


<dspace:layout titlekey="jsp.home.about.bdtd">
<div id="pageContents">

<div id="BDTDtext">
<h2> <b>Sobre a Biblioteca Digital Brasileira de Teses e Dissertações (BDTD) </b></h2>

<p> A Biblioteca Digital Brasileira de Teses e Dissertações (BDTD) tem por objetivo reunir, em um só portal de busca, as teses e dissertações defendidas
nas instituições brasileiras de ensino e pesquisa e por brasileiros no exterior. 
A BDTD utiliza as tecnologias da <i> Open Archives Initiative </i> (OAI), adotando o modelo baseado em padrões de interoperabilidade, consolidada em uma rede
distribuída de diversos sistemas de informação que armazenam teses e dissertações em suas bases. Nesse modelo há a presença fundamental de dois atores principais:
provedor de dados e o provedor de serviços.

<ul TYPE="circle">
 <LI>Provedor de dados <i>(data providers)</i>: administra o depósito expondo os metadados e o texto completo para a coleta automática (<i>harvesting</i>);</LI>
</ul>
<ul TYPE="circle">
 <LI>Provedor de serviços <i>(service providers)</i>: fornece serviços de informação com base nos metadados coletados junto aos provedores de dados.</LI>
</ul>

Na BDTD, portanto, as instituições de ensino e pesquisa atuam como provedores de dados e o IBICT opera como agregador ou provedor de serviço, coletando os metadados
das teses e dissertações dos sistemas de informação das instituições, fornecendo serviços de informação sobre com nos metadados coletados e os expondo para a coleta 
automática de outros provedores de serviços.</p> 

<p> <b>&lt;Conheça a BDTD! Visite, bdtd.ibict.br&gt;</b>. </p> 

<h3> Breve histórico </h3>

<p> A Biblioteca Digital Brasileira de Teses e Dissertações (BDTD), concebida no âmbito do Programa da Biblioteca Digital Brasileira (BDB), com
apoio da Financiadora de Estudos e Pesquisas (FINEP), teve seu lançamento no final do ano de 2002. Para a definição do projeto da BDTD foi criado, para o seu início,
um Comitê Técnico-Consultivo (CTC), instalado em abril de 2002, constituído por representantes do Instituto Brasileiro de Informação em Ciência e 
Tecnologia (IBICT), do Conselho Nacional de Desenvolvimento Científico e Tecnológico (CNPq), o Ministério da Educação (MEC) - CAPES e SESU -, a
Financiadora de Estudos e Projetos (FINEP) e de três universidades que participaram do projeto-piloto da BDTD (Universidade de São Paulo (USP), 
Pontifícia Universidade Católica do Rio de Janeiro (PUC-Rio) e a Universidade Federal de Santa Catarina (UFSC)). </p>

<p> Para a realização do projeto da BDTD foram definidas as seguintes linhas de pesquisa e atuação: </p>
<p>
<ul TYPE="circle">
  <LI> Estudar experiências existentes, no Brasil e no exterior, de desenvolvimento de bibliotecas digitais de teses e dissertações;</LI>
  <LI> Desenvolver, em cooperação com membros da comunidade, um modelo para o sistema; </LI>
  <LI> Definir padrões de metadados e tecnologias a serem utilizadas pelo sistema; </LI>
  <LI> Absorver e adaptar as tecnologias a serem utilizadas na implementação do modelo;</LI>
  <LI> Desenvolver um Sistema de Publicação Eletrônica de Teses e Dissertações para atender àquelas instituições brasileiras de ensino e pesquisa que não
  possuíam sistemas automatizados para o armazenamento, organização, disseminação e preservação de suas teses e dissertações;</LI>
  <LI> Difundir os padrões e tecnologias adotadas para o gerenciamento de teses e dissertações e dar assitência técnica aos parceiros da Rede da BDTD. </LI>
</ul>
  </p> 
 <p> 	
<p> Desde a sua implantação, o IBICT tem buscado atender as demandas das instituições de ensino e pesquisa e, com isso, a integração dessas na Rede da BDTD.

<h3> Padrão Brasileiro de Metadados da BDTD </h3>
<p> O Comitê Técnico-Consultivo, no âmbito de sua atribuição, definiu e aprovou o conjunto de metadados a serem utilizados para a descrição das teses e 
dissertações, o então Padrão Brasileiro de Metadados para Teses e Dissertações (MTD-BR). A utilização do Padrão visava garantir a interoperabilidade entre
a BDTD e os demais sistemas de informação de teses e dissertações e também com os padrões de metadados, como o ETD-MS adotado pela <i> Networked Digital Library
of Theses and Dissertations </i>(NDLTD). Em 2005, o MTD-BR foi revisto no intuito de melhorar a qualidade e controle sobre o conteúdo coletado. Passou a se
denominar MTD2-BR. O MTD2-BR foi definido com um total de 80 campos disponíveis para a descrição das teses e dissertações. Devido a essa extensão de campos 
para preenchimento, observou-se que diversos metadados coletados apresentavam conteúdo inválidos (campos vazios ou com preenchimento indevido). </p>
   
<p> Nesse cenário, o IBICT apontou para a necessidade de uma nova atualização do Padrão de Metadados da BDTD. Assim, em 2012, foi dado início a discussão para
a revisão e atualização do MTD2-BR. Para esse processo, o IBICT definiu um novo conjunto de metadados, com base nos padrões Dublin Core e o ETD-MS, e o apresentou
para 16 instituições participantes da BDTD. Essas instituições, em conjunto com o IBICT, discutiram cada metadado proposto, reformulando a proposta quando necessário.
Ao final dessa consulta, o IBICT analisou todas as considerações levantadas e definiu o Novo Padrão Brasileiro de Metadados da BDTD (MTD3-BR). </p>

<p> O MTD3-BR tem, portanto, ao todo 38 metadados, sendo 18 metadados obrigatórios e 20 metadados opcionais. A nova proposta do Padrão Brasileiro de Metadados da BDTD
foi desenvolvido com base no esquema de metadados do Dublin Core com adaptações para a realidade brasileira. </p>

<h3> Como fazer parte da BDTD </h3> 
<p>
<p> A integração à BDTD não requer das instituições brasileiras de ensino e pesquisa o uso de um software único para o gerenciamento das teses e dissertações. 
Entretanto, é necessário que o sistema exporte os dados das teses e dissertações no formato XML para que, por meio de um conversor, o IBICT faça a coleta 
automática (<i>harvesting</i>) dos metadados. Ainda, o sistema pode exportar diretamente os documentos no padrão de metadados definidos pela BDTD, obedecendo
o cumprimento de preenchimento, no mínimo, dos campos obrigatórios.</p>

<p> As instituições brasileiras de ensino e pesquisa que desejam fazer parte da BDTD devem seguir alguns procedimentos para que a sua integração seja realizada. Para 
tanto, devem entrar em contato, por e-mail, com a <b>Equipe BDTD &lt;equipebdtd@ibict.br&gt;</b> formalizando a solicitação e o interesse da integração da instituição com a BDTD. Assim, 
a Equipe BDTD encaminhará as orientações necessárias diretamente para a instituição. </p>

<p> Basicamente são dois processos que a instituição percorre para a integração à BDTD. Esses processos se referem à parte de informação documental e à parte tecnológica.
A informação documental diz respeito aos trâmites legais para a integração por meio de um Termo de Adesão e demais questões. A parte tecnológica é referente à definição, 
configuração e ajustes necessários dos sistemas de informação da instituição para a coleta automática da BDTD.</p>

<p> Entre em contato e saiba mais. </p>  
 
<h3> Equipe responsável pela BDTD</h3>

<p> Coordenação do Laboratório de Metodologias de Tratamento e Disseminação da Informação </p>

<p> <b>Bianca Amaro</b> - Coordenadora do Laboratório de Metodologias de Tratamento e Disseminação da Informação
<ul TYPE="disc">
   <LI> bianca@ibict.br</LI>
   +55 61 3217-6249
</ul>
</p>

<p> <b>Equipe BDTD</b></p>
<ul TYPE="disc">
   <LI> equipebdtd@ibict.br</LI>
   +55 61 3217-6449
</ul>
</p>  


   
 
</div>





</div>
</dspace:layout>