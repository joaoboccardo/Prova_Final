package com.example.n2atividade2.entity

import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import java.io.StringWriter

fun createGpxDocument(movimentosData: MutableList<Movimento>): String {

    if(movimentosData.size === 0) {
        return "Array Zerado!"
    }

    val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val doc = docBuilder.newDocument()

    val gpxElement = doc.createElement("gpx")
    gpxElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
    gpxElement.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1")

    gpxElement.setAttribute("version", "1.1")

    val metadata = doc.createElement("metadata")
    gpxElement.appendChild(metadata)

    val metaDateTime: Element = doc.createElement("time")
    metaDateTime.appendChild(doc.createTextNode("${movimentosData[0].timestamp}"))
    metadata.appendChild(metaDateTime)

    val trkElement = doc.createElement("trk")

    val name: Element = doc.createElement("name")
    name.appendChild(doc.createTextNode("Afternoon Ride"))
    trkElement.appendChild(name)

    val type: Element = doc.createElement("type")
    type.appendChild(doc.createTextNode("1"))
    trkElement.appendChild(type)

    val trkseg: Element = doc.createElement("trkseg")

    for (movimento in movimentosData) {
        val trkpt: Element = doc.createElement("trkpt")
        trkpt.setAttribute("lat", "${movimento.latitude}")
        trkpt.setAttribute("lon", "${movimento.longitude}")

        val trkptDateTime: Element = doc.createElement(("trkptDateTime"))
        trkptDateTime.appendChild(doc.createTextNode("${movimento.timestamp}"))
        trkpt.appendChild(trkptDateTime)
        
        val trkptAccelerometro: Element = doc.createElement(("trkptAxisX"))
        trkptAccelerometro.setAttribute("AxisX", "${movimento.accelX}")
        trkptAccelerometro.setAttribute("AxisY", "${movimento.accelY}")
        trkptAccelerometro.setAttribute("AxisZ", "${movimento.accelZ}")
        trkpt.appendChild(trkptAccelerometro)

        trkseg.appendChild(trkpt)
    }

    trkElement.appendChild(trkseg)

    gpxElement.appendChild(trkElement)

    doc.appendChild(gpxElement)

    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()
    val source = DOMSource(doc)
    val writer = StringWriter()
    val result = StreamResult(writer)
    transformer.transform(source, result)

    return writer.toString()
}

