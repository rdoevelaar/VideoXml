package nl.tkp;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XmlReader {

    // http://blog.sanaulla.info/2013/05/23/parsing-xml-using-dom-sax-and-stax-parser-in-java/
    public static void main(String[] args) throws XMLStreamException {
        int teller=0;
        File file = new File("c:/dev/VVR_alles.xml");
        String tagContent = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader =  null;
        try {
            reader = factory.createXMLStreamReader(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(reader.hasNext()){
            int event = reader.next();


            switch(event){
//                case XMLStreamConstants.START_ELEMENT:
//                    if ("psn_id".equals(reader.getLocalName())){
//                        currEmp = new Employee();
//                        currEmp.id = reader.getAttributeValue(0);
//                    }
//                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    switch(reader.getLocalName()){
                        case "psn_id":
                            System.out.println("Persoon gevonden: " + tagContent);
                            teller++;
                            break;
//                        case "firstName":
//                            currEmp.firstName = tagContent;
//                            break;
                    }
                    break;

//                case XMLStreamConstants.START_DOCUMENT:
//                    empList = new ArrayList<>();
//                    break;
            }

        }

                System.out.println("Xml bestand bevat " + teller + " personen.");
//            }

    }
}
//}

