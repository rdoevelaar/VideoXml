package nl.tkp;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileOutputStream;

public class XmlWriter {
    private String filename;

    private XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    private XMLEventWriter eventWriter;
    private XMLEventFactory eventFactory = XMLEventFactory.newInstance();

    private final XMLEvent end = eventFactory.createDTD("\n");
    private final XMLEvent tab = eventFactory.createDTD("\t");


    // Output aanmaken als file met eerste regels.
    public void createDocument(String filename) throws Exception {
        eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(filename));
        // create and write Start Tag
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        eventWriter.add(end);
    }

    public void createStartElement(String name) throws XMLStreamException {
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        eventWriter.add(end);

    }

    public void createEndElement(String name) throws XMLStreamException {
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);

    }

    public void createNode(String name,
                           String value) throws XMLStreamException {
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);

    }

    public void endDocument() throws XMLStreamException {
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.flush();
        eventWriter.close();
    }
}
