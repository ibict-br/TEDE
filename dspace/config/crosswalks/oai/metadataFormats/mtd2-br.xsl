<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:dim="http://www.dspace.org/xmlns/dspace/dim"
                xmlns:mtd2-br="http://oai.ibict.br/mtd2-br/"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                version="1.0">

    <xsl:template match="@* | node()">

    </xsl:template>

    <xsl:template match="dim:dim">

        <xsl:choose>
            <xsl:when test="(contains(dim:field[@element = 'type'], 'Thesis') or contains(dim:field[@element = 'type'], 'Dissertation'))">
                <xsl:element name="mtd2-br:mtd2br">

                    <xsl:attribute name="xsi:schemaLocation">
                        <xsl:text>http://oai.ibict.br/mtd2-br/ http://oai.ibict.br/mtd2-br/mtd2-br.xsd</xsl:text>
                    </xsl:attribute>

                    <xsl:element name="mtd2-br:Controle">
                        <xsl:element name="mtd2-br:Sigla">
                            <xsl:text>UNIVATES</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:DataAtualizacao">
                            <xsl:value-of select="dim:field[@element = 'date' and @qualifier = 'accessioned']"/>
                        </xsl:element>
                        <xsl:element name="mtd2-br:IdentificacaoDocumento">
                            <xsl:value-of select="dim:field[@element = 'library' and @qualifier = 'identifier']"/>
                        </xsl:element>


                        <xsl:element name="mtd2-br:Tipo">
                            <xsl:choose>
                                <xsl:when test="count(dim:field[@lang = 'pt_BR']) > 0">
                                    <xsl:text>Texto</xsl:text>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:text>Text</xsl:text>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:element>
                    </xsl:element>

                    <xsl:element name="mtd2-br:BibliotecaDigital">
                        <xsl:element name="mtd2-br:Nome">
                            <xsl:text>BDU - Biblioteca Digital da UNIVATES</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:Sigla">
                            <xsl:text>BDU</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:URL">
                            <xsl:text>http://www.univates.br/bdu</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:ProvedorServico">
                            <xsl:element name="mtd2-br:Nome">
                                <xsl:text>Centro Universitário UNIVATES</xsl:text>
                            </xsl:element>
                            <xsl:element name="mtd2-br:Sigla">
                                <xsl:text>UNIVATES</xsl:text>
                            </xsl:element>
                            <xsl:element name="mtd2-br:Pais">
                                <xsl:text>BR</xsl:text>
                            </xsl:element>
                            <xsl:element name="mtd2-br:UF">
                                <xsl:text>RS</xsl:text>
                            </xsl:element>
                            <xsl:element name="mtd2-br:CNPJ">
                                <xsl:text>04008342000109</xsl:text>
                            </xsl:element>
                            <xsl:element name="mtd2-br:URL">
                                <xsl:text>http://www.univates.br</xsl:text>
                            </xsl:element>
                        </xsl:element>
                    </xsl:element>

                    <xsl:apply-templates select="dim:field[@element ='title']" />
                    <xsl:element name="mtd2-br:Arquivo">
                        <xsl:element name="mtd2-br:URL">
                            <xsl:value-of select="dim:field[@element='identifier' and @qualifier='uri']" />
                        </xsl:element>
                        <xsl:element name="mtd2-br:NivelAcesso">
                            <xsl:text>Publico</xsl:text>
                        </xsl:element>
                    </xsl:element>

                    <xsl:element name="mtd2-br:Idioma">
                        <xsl:choose>
                            <xsl:when test="count(dim:field[@lang = 'pt_BR']) > 0">
                                <xsl:text>pt</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="dim:field[@lang != 'pt_BR'][1]/@lang"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:element>

                    <xsl:element name="mtd2-br:Grau">
                        <xsl:if test="(contains(dim:field[@element = 'type'], 'Thesis'))">
                            <xsl:text>Doutor</xsl:text>
                        </xsl:if>
                        <xsl:if test="contains(dim:field[@element = 'type'], 'Dissertation')">
                            <xsl:text>Mestre</xsl:text>
                        </xsl:if>
                    </xsl:element>

                    <xsl:element name="mtd2-br:Titulacao">
                        <xsl:if test="contains(dim:field[@element = 'type'], 'Thesis')">
                            <xsl:text>Doutor em </xsl:text>
                        </xsl:if>
                        <xsl:if test="contains(dim:field[@element = 'type'], 'Dissertation')">
                            <xsl:text>Mestre em </xsl:text>
                        </xsl:if>
                        <xsl:value-of select="substring-after(dim:field[@element ='program' and @qualifier='name'],';')" />
                    </xsl:element>


                    <xsl:apply-templates select="dim:field[@element ='description' and @qualifier = 'abstract']" />

                    <xsl:apply-templates select="dim:field[@element ='subject']" />

                    <xsl:element name="mtd2-br:LocalDefesa">
                        <xsl:element name="mtd2-br:Cidade">
                            <xsl:text>Lajeado</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:UF">
                            <xsl:text>RS</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:Pais">
                            <xsl:text>BR</xsl:text>
                        </xsl:element>
                    </xsl:element>

                    <xsl:element name="mtd2-br:DataDefesa">
                       <xsl:value-of select="dim:field[@element ='date' and @qualifier = 'submitted']" />
                    </xsl:element>
                    
                    <xsl:element name="mtd2-br:Autor">
                        <xsl:element name="mtd2-br:Nome">
                            <xsl:choose>
                                <xsl:when test="contains(dim:field[@element = 'contributor' and @qualifier = 'author'],',')">
                                    <xsl:value-of select="substring-after(dim:field[@element = 'contributor' and @qualifier = 'author'], ', ')"/>
                                    <xsl:text> </xsl:text>
                                    <xsl:value-of select="substring-before(dim:field[@element = 'contributor' and @qualifier = 'author'], ',')"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="dim:field[@element = 'contributor' and @qualifier = 'author']"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:element>
                        <xsl:element name="mtd2-br:Citacao">
                            <xsl:choose>
                                <xsl:when test="contains(dim:field[@element = 'contributor' and @qualifier = 'author'],',')">
                                    <xsl:value-of select="dim:field[@element = 'contributor' and @qualifier = 'author']"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:value-of select="substring-after(dim:field[@element = 'contributor' and @qualifier = 'author'], ', ')"/>
                                    <xsl:text> </xsl:text>
                                    <xsl:value-of select="substring-before(dim:field[@element = 'contributor' and @qualifier = 'author'], ',')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:element>
                        <xsl:element name="mtd2-br:Lattes">
                            <xsl:value-of select="dim:field[@element = 'author' and @qualifier = 'lattes']"/>
                        </xsl:element>
                        <xsl:element name="mtd2-br:Afiliacao">
                            <xsl:element name="mtd2-br:Nome">
                                <xsl:text>Centro Universitário UNIVATES</xsl:text>
                            </xsl:element>
                        </xsl:element>
                    </xsl:element>

                    <xsl:element name="mtd2-br:Contribuidor">
                        <xsl:attribute name="Papel">
                            <xsl:text>Orientador</xsl:text>
                        </xsl:attribute>
                        <xsl:element name="mtd2-br:Nome">
                            <xsl:value-of select="dim:field[@element = 'advisor' and @qualifier = 'name']"/>
                        </xsl:element>
                        <xsl:element name="mtd2-br:Lattes">
                            <xsl:value-of select="dim:field[@element = 'advisor' and @qualifier = 'lattes']"/>
                        </xsl:element>
                        <xsl:element name="mtd2-br:Afiliacao">
                            <xsl:element name="mtd2-br:Nome">
                                <xsl:value-of select="dim:field[@element = 'advisor' and @qualifier = 'instituation']"/>
                            </xsl:element>
                        </xsl:element>
                    </xsl:element>

                    <xsl:if test="count(dim:field[@element = 'co-advisor' and @qualifier = 'name']) > 0">
                        <xsl:element name="mtd2-br:Contribuidor">
                            <xsl:for-each select="dim:field[@element = 'co-advisor' and @qualifier = 'name']">
                                <xsl:attribute name="Papel">
                                    <xsl:text>Co-Orientador</xsl:text>
                                </xsl:attribute>
                                <xsl:element name="mtd2-br:Nome">
                                    <xsl:value-of select="."/>
                                </xsl:element>
                            </xsl:for-each>
                        </xsl:element>
                    </xsl:if>

                    <xsl:element name="mtd2-br:InstituicaoDefesa">
                        <xsl:element name="mtd2-br:Nome">
                            <xsl:text>Centro Universitário UNIVATES</xsl:text>
                        </xsl:element>

                        <xsl:element name="mtd2-br:Sigla">
                            <xsl:text>UNIVATES</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:Pais">
                            <xsl:text>BR</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:UF">
                            <xsl:text>RS</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:CNPJ">
                            <xsl:text>04008342000109</xsl:text>
                        </xsl:element>
                        <xsl:element name="mtd2-br:URL">
                            <xsl:text>http://www.univates.br</xsl:text>
                        </xsl:element>

                        <xsl:element name="mtd2-br:Programa">
                            <xsl:element name="mtd2-br:Nome">
                                <xsl:value-of select="substring-before(dim:field[@element ='program' and @qualifier='name'],';')" />
                            </xsl:element>
                        </xsl:element>
                    </xsl:element>
                </xsl:element>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="dim:field[@element ='title']">
        <xsl:element name="mtd2-br:Titulo">
            <xsl:attribute name="Idioma">
                <xsl:value-of select="substring(./@lang,1,2)"/>
            </xsl:attribute>
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="dim:field[@element ='subject']">
        <xsl:element name="mtd2-br:Assunto">
            <xsl:attribute name="Idioma">
                <xsl:value-of select="substring(./@lang,1,2)"/>
            </xsl:attribute>
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="dim:field[@element ='description' and @qualifier = 'abstract']">
        <xsl:element name="mtd2-br:Resumo">
            <xsl:attribute name="Idioma">
                <xsl:value-of select="substring(./@lang,1,2)"/>
            </xsl:attribute>
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>
