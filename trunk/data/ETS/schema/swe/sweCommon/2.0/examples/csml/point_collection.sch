<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">
    <sch:ns uri="http://www.opengis.net/swe/2.0" prefix="swe"/>
    <sch:pattern>
        <sch:rule context="//swe:DataRecord[@definition='urn:ogc:def:data:CSML:PointCollection']">
            <!-- -->
            <sch:assert test="swe:field[1]/@name = 'time'"/>
            <sch:assert test="swe:field[1]/swe:Time"/>
            <sch:assert test="swe:field[1]/swe:Time/@definition = 'urn:ogc:def:property:OGC::SamplingTime'"/>
            <!-- -->
            <sch:assert test="swe:field[2]/@name = 'domainSet'"/>
            <sch:assert test="swe:field[2]/swe:DataArray"/>
            <sch:assert test="swe:field[2]/swe:DataArray/swe:elementType/@name = 'location'"/>
            <sch:assert test="swe:field[2]/swe:DataArray/swe:elementType/swe:Vector"/>
            <sch:assert test="swe:field[2]/swe:DataArray/swe:elementType/swe:Vector/@definition = 'urn:ogc:def:property:OGC::SamplingLocation'"/>
            <sch:assert test="swe:field[2]/swe:DataArray/swe:elementType/swe:Vector/@referenceFrame"/>
            <!-- -->
            <sch:assert test="swe:field[3]/@name = 'rangeValues'"/>
            <sch:assert test="swe:field[3]/swe:DataArray"/>
            <sch:assert test="swe:field[3]/swe:DataArray/swe:elementType/@name = 'rangeSet'"/>
            <sch:assert test="swe:field[3]/swe:DataArray/swe:elementType/swe:DataRecord"/>
            <sch:assert test="count(swe:field[3]/swe:DataArray/swe:elementType/swe:DataRecord/swe:field) > 1"/>
            <!-- -->
        </sch:rule>
    </sch:pattern>
    <sch:pattern>
        <sch:rule context="//swe:DataRecord[@definition='urn:ogc:def:data:CSML:PointCollection:Interleaved']">
            <!-- -->
            <sch:assert test="swe:field[1]/@name = 'time'"/>
            <sch:assert test="swe:field[1]/swe:Time"/>
            <sch:assert test="swe:field[1]/swe:Time/@definition = 'urn:ogc:def:property:OGC::SamplingTime'"/>
            <!-- -->
            <sch:assert test="swe:field[2]/@name = 'collectionData'"/>
            <sch:assert test="swe:field[2]/swe:DataArray"/>
            <sch:assert test="swe:field[2]/swe:DataArray/swe:elementType/@name = 'collectionPoint'"/>
            <sch:assert test="swe:field[2]/swe:DataArray/swe:elementType/swe:DataRecord"/>
            <!-- -->
            <sch:let name="point" value="swe:field[2]/swe:DataArray/swe:elementType/swe:DataRecord"/>
            <sch:assert test="$point/swe:field[1]/@name = 'location'"/>
            <sch:assert test="$point/swe:field[1]/swe:Vector"/>
            <sch:assert test="count($point/swe:field) > 1"/>
            <!-- -->
        </sch:rule>
    </sch:pattern>
</sch:schema>