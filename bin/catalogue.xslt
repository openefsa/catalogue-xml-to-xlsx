<?xml version = "1.0" encoding = "UTF-8"?>
<xsl:stylesheet version = "1.0" xmlns:xsl = "http://www.w3.org/1999/XSL/Transform">   

	<!-- Omit the xml declaration and strip spaces -->
	<xsl:output omit-xml-declaration="yes" indent="yes"/>
	<xsl:strip-space elements="*"/>

	<!-- Get the catalogue node and filter only the catalogue 
	desc, version and groups fields. Add also the catalogue
	node name to preserve the root of the xml -->
	<xsl:template match="catalogue">
		<catalogue>
			<xsl:copy-of select="catalogueDesc"/>
			<xsl:copy-of select="catalogueVersion"/>
			<xsl:copy-of select="catalogueGroups"/>
		</catalogue>
	</xsl:template>
	
	<!-- Remove unnecessary fields -->
	<xsl:template match="catalogueHierarchies"/>
	<xsl:template match="catalogueAttributes"/>
	<xsl:template match="catalogueTerms"/>
	<xsl:template match="releaseNotes"/>
	
</xsl:stylesheet>