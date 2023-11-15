package com.sth.eventservice.util;

import com.sth.eventservice.model.dto.EventDTO;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventXmlParser {
    public List<EventDTO> parseXml(String xml) {
        List<EventDTO> events = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));

            NodeList eventList = document.getElementsByTagName("row");

            for (int i = 0; i < eventList.getLength(); i++) {
                Node node = eventList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    EventDTO eventDTO = new EventDTO();
                    eventDTO.setCodename(getTagValue("codename", element));
                    eventDTO.setGuname(getTagValue("guname", element));
                    eventDTO.setTitle(getTagValue("title", element));
                    eventDTO.setPlace(getTagValue("place", element));
                    eventDTO.setUseFee(getTagValue("useFee", element));
                    eventDTO.setPlayer(getTagValue("player", element));
                    eventDTO.setProgram(getTagValue("program", element));
                    eventDTO.setLot(Double.parseDouble(getTagValue("lot", element)));
                    eventDTO.setLat(Double.parseDouble(getTagValue("lat", element)));

                    events.add(eventDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node == null ? "" : node.getNodeValue();
    }
}
