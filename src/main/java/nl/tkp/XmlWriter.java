package nl.tkp;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileOutputStream;

public class XmlWriter {
    private String configFile;

    // create an XMLOutputFactory
    private XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    // create XMLEventWriter
    private XMLEventWriter eventWriter;
    // create an EventFactory
    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
    XMLEvent end = eventFactory.createDTD("\n");
    XMLEvent tab = eventFactory.createDTD("\t");

    public void setFile(String configFile) {
        this.configFile = configFile;
    }

    // Output aanmaken als file met eerste regels.
    public void createDocument() throws Exception {
        eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(configFile));
        // create an EventFactory
//        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        // create and write Start Tag
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);
        eventWriter.add(end);
    }

    public void createStartElement(String name) throws XMLStreamException {

//        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        eventWriter.add(end);

    }

    public void createEndElement(String name) throws XMLStreamException {
//        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);

    }

    public void createNode(String name,
                           String value) throws XMLStreamException {

//        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
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
//        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
//        eventWriter.add(eventFactory.createEndElement("", "", "config"));
//        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());
        eventWriter.flush();
        eventWriter.close();

    }
}
