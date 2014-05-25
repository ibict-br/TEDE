<?xml version="1.0" encoding="UTF-8"?>
<!-- 


    The contents of this file are subject to the license and copyright
    detailed in the LICENSE and NOTICE files at the root of the source
    tree and available online at

    http://www.dspace.org/license/
	Developed by DSpace @ Lyncode <dspace@lyncode.com>
	
	> http://www.openarchives.org/OAI/2.0/oai_dc.xsd

 -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://www.lyncode.com/xoai" version="1.0">
	<xsl:output omit-xml-declaration="yes" method="xml" indent="yes"/>
	<xsl:template match="/">
		<mtd3-br:mtd3br xsi:schemaLocation="http://10.0.0.173/mtd3br/ NOVO_MTD3-BR.xsd" xmlns:mtd3-br="http://10.0.0.173/mtd3br/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='contributor']/doc:element[@name='author']/doc:element/doc:field[@name='value']">
				<mtd3-br:contributorAuthor>
					<xsl:value-of select="."/>
				</mtd3-br:contributorAuthor>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='contributor']/doc:element[@name='authorID']/doc:element/doc:field[@name='value']">
				<mtd3-br:contributorAuthorID>
					<xsl:value-of select="."/>
				</mtd3-br:contributorAuthorID>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='contributor']/doc:element[@name='advisor']/doc:element/doc:field[@name='value']">
				<mtd3-br:contributorAdvisor>
					<xsl:value-of select="."/>
				</mtd3-br:contributorAdvisor>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='contributor']/doc:element[@name='advisor-co']/doc:element/doc:field[@name='value']">
				<mtd3-br:contributorAdvisor-co>
					<xsl:value-of select="."/>
				</mtd3-br:contributorAdvisor-co>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='contributor']/doc:element[@name='referees']/doc:element/doc:field[@name='value']">
				<mtd3-br:contributorReferees>
					<xsl:value-of select="."/>
				</mtd3-br:contributorReferees>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='date']/doc:element[@name='accessioned']/doc:element/doc:field[@name='value']">
				<mtd3-br:dateAccessioned>
					<xsl:value-of select="."/>
				</mtd3-br:dateAccessioned>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='date']/doc:element[@name='available']/doc:element/doc:field[@name='value']">
				<mtd3-br:dateAvailable>
					<xsl:value-of select="."/>
				</mtd3-br:dateAvailable>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='date']/doc:element[@name='issued']/doc:element/doc:field[@name='value']">
				<mtd3-br:dateIssued>
					<xsl:value-of select="."/>
				</mtd3-br:dateIssued>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='identifier']/doc:element[@name='uri']/doc:element/doc:field[@name='value']">
				<mtd3-br:identifierUri>
					<xsl:value-of select="."/>
				</mtd3-br:identifierUri>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='identifier']/doc:element[@name='citation']/doc:element/doc:field[@name='value']">
				<mtd3-br:identifierCitation>
					<xsl:value-of select="."/>
				</mtd3-br:identifierCitation>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='identifier']/doc:element[@name='doi']/doc:element/doc:field[@name='value']">
				<mtd3-br:identifierDoi>
					<xsl:value-of select="."/>
				</mtd3-br:identifierDoi>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='description']/doc:element[@name='abstract']/doc:element/doc:field[@name='value']">
				<mtd3-br:descriptionAbstract>
					<xsl:value-of select="."/>
				</mtd3-br:descriptionAbstract>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='description']/doc:element[@name='sponsorship']/doc:element/doc:field[@name='value']">
				<mtd3-br:descriptionSponsorship>
					<xsl:value-of select="."/>
				</mtd3-br:descriptionSponsorship>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='publish']/doc:element/doc:field[@name='value']">
				<mtd3-br:publisher>
					<xsl:value-of select="."/>
				</mtd3-br:publisher>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='publisher']/doc:element[@name='country']/doc:element/doc:field[@name='value']">
				<mtd3-br:publisherCountry>
					<xsl:value-of select="."/>
				</mtd3-br:publisherCountry>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='publisher']/doc:element[@name='departament']/doc:element/doc:field[@name='value']">
				<mtd3-br:publisherDepartament>
					<xsl:value-of select="."/>
				</mtd3-br:publisherDepartament>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='publisher']/doc:element[@name='program']/doc:element/doc:field[@name='value']">
				<mtd3-br:publisherProgram>
					<xsl:value-of select="."/>
				</mtd3-br:publisherProgram>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='publisher']/doc:element[@name='initials']/doc:element/doc:field[@name='value']">
				<mtd3-br:publisherInitials>
					<xsl:value-of select="."/>
				</mtd3-br:publisherInitials>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='type']/doc:element/doc:field[@name='value']">
				<mtd3-br:type>
					<xsl:value-of select="."/>
				</mtd3-br:type>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='title']/doc:element/doc:field[@name='value']">
				<mtd3-br:title>
					<xsl:value-of select="."/>
				</mtd3-br:title>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='title']/doc:element[@name!='pt_BR']/doc:element/doc:field[@name='value']">
				<mtd3-br:titleAlternative>
					<xsl:value-of select="."/>
				</mtd3-br:titleAlternative>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='language']/doc:element/doc:field[@name='value']">
				<mtd3-br:language>
					<xsl:value-of select="."/>
				</mtd3-br:language>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='rights']/doc:element/doc:element/doc:field[@name='value']">
				<mtd3-br:rights>
					<xsl:value-of select="."/>
				</mtd3-br:rights>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='rights']/doc:element[@name='uri']/doc:element/doc:field[@name='value']">
				<mtd3-br:rightsUri>
					<xsl:value-of select="."/>
				</mtd3-br:rightsUri>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='subject']/doc:element/doc:field[@name='value']">
				<mtd3-br:subject>
					<xsl:value-of select="."/>
				</mtd3-br:subject>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='subject']/doc:element[@name='cnpq']/doc:element/doc:field[@name='value']">
				<mtd3-br:subjectCnpq>
					<xsl:value-of select="."/>
				</mtd3-br:subjectCnpq>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='format']/doc:element/doc:field[@name='value']">
				<mtd3-br:format>
					<xsl:value-of select="."/>
				</mtd3-br:format>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='relation']/doc:element/doc:field[@name='value']">
				<mtd3-br:relation>
					<xsl:value-of select="."/>
				</mtd3-br:relation>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='relation']/doc:element[@name='other']/doc:element/doc:field[@name='value']">
				<mtd3-br:relationOther>
					<xsl:value-of select="."/>
				</mtd3-br:relationOther>
			</xsl:for-each>
			<xsl:for-each select="doc:metadata/doc:element[@name='dc']/doc:element[@name='relation']/doc:element[@name='references']/doc:element/doc:field[@name='value']">
				<mtd3-br:relationReferences>
					<xsl:value-of select="."/>
				</mtd3-br:relationReferences>
			</xsl:for-each>
		</mtd3-br:mtd3br>
	</xsl:template>
</xsl:stylesheet>
