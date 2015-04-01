package nl.tkp;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XmlReader {

    // http://blog.sanaulla.info/2013/05/23/parsing-xml-using-dom-sax-and-stax-parser-in-java/
    public static void main(String[] args) throws XMLStreamException {
        int aantalPersonen=0;
        File file = new File("c:/dev/VVR_alles.xml");
        File koppeltabel = new File("c:/dev/koppeltabel.txt");
        String tagContent = null;
        List<String> elementNamen = new ArrayList<>(Arrays.asList("M_7","M_20","M_21"));

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader =  null;
        String localName;

        XmlWriter outputfile = new XmlWriter();
        outputfile.setFile("c:/dev/stripped_xml.xml");
        try {
            outputfile.createDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            reader = factory.createXMLStreamReader(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Lezen van inhoud van xml.
        while(reader.hasNext()){
            int event = reader.next();


            switch(event){
                case XMLStreamConstants.START_ELEMENT:
                    localName = reader.getLocalName();
                    if (localName.equalsIgnoreCase("TotaalAllePolissen")
                    ||  localName.equalsIgnoreCase("data")
                       ){
                        outputfile.createStartElement(localName);
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    localName = reader.getLocalName();
                    if (localName.equalsIgnoreCase("M_1")) {
                        System.out.println("Persoon gevonden: " + tagContent);
                        aantalPersonen++;
                        // tagContent moet verhaspeld worden en weggeschreven
                        // naar separaat bestand.
                        MessageDigest md = null;
                        try {
                            md = MessageDigest.getInstance("SHA-256");
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        try {
                            md.update(tagContent.getBytes("UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        byte[] digest = md.digest();
                        //convert the byte to hex format
                        StringBuilder hexString = new StringBuilder();
                        for (byte aDigest : digest) {
                            String hex = Integer.toHexString(0xff & aDigest);
                            if (hex.length() == 1) hexString.append('0');
                            hexString.append(hex);
                        }
                        outputfile.createNode(localName, hexString.toString());
                    }
                    else if (localName.equalsIgnoreCase("TotaalAllePolissen")) {
                        outputfile.createEndElement(localName);
                        outputfile.endDocument();
                    }
                    else if (localName.equalsIgnoreCase("data")) {
                            outputfile.createEndElement(localName);
                    }
                    else if (elementNamen.contains(localName)) {
                        outputfile.createNode(localName, tagContent);
                    }
                    break;

            }

        }
        System.out.println("Xml bestand bevat " + aantalPersonen + " personen.");

    }
}
