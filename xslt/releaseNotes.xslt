<?xml version = "1.0" encoding = "UTF-8"?>
<xsl:stylesheet version = "1.0" xmlns:xsl = "http://www.w3.org/1999/XSL/Transform">   

	<!-- Omit the xml declaration and strip spaces -->
	<xsl:output omit-xml-declaration="yes" indent="yes"/>
	<xsl:strip-space elements="*"/>

	<!-- Get the release note node -->
	<xsl:template match="releaseNotes">
		<releaseNotes>
			<xsl:copy-of select="*"/>
		</releaseNotes>
	</xsl:template>
	
	<!-- Remove unnecessary fields -->
	<xsl:template match="catalogueDesc"/>
	<xsl:template match="catalogueAttributes"/>
	<xsl:template match="catalogueTerms"/>
	<xsl:template match="catalogueHierarchies"/>
	<xsl:template match="catalogueVersion"/>
	<xsl:template match="catalogueGroups"/>
	
</xsl:stylesheet>