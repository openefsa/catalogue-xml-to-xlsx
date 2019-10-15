<?xml version = "1.0" encoding = "UTF-8"?>
<xsl:stylesheet version = "1.0" xmlns:xsl = "http://www.w3.org/1999/XSL/Transform">   

	<!-- Omit the xml declaration and strip spaces -->
	<xsl:output omit-xml-declaration="yes" indent="yes"/>
	<xsl:strip-space elements="*"/>

	<!-- Get the catalogue node and filter only hierarchy data -->
	<xsl:template match="catalogue">
			<xsl:copy-of select="catalogueHierarchies"/>
	</xsl:template>
	
	<!-- Remove unnecessary fields -->
	<xsl:template match="catalogueDesc"/>
	<xsl:template match="catalogueAttributes"/>
	<xsl:template match="catalogueTerms"/>
	<xsl:template match="releaseNotes"/>
	
</xsl:stylesheet>