package nl.tkp;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// TODO: Documentatie
// TODO: Exception handling, ook van XmlWriter
// TODO: Naamgeving
public class XmlReader {

    // http://blog.sanaulla.info/2013/05/23/parsing-xml-using-dom-sax-and-stax-parser-in-java/
    public static void main(String[] args) throws XMLStreamException {

        int aantalPersonen=0;
        // TODO: Geen absolute paden, bestandsnamen als command-line arguments (?)
        File inputXml = new File("e:/dev/VVR_alles.xml");
        String naamKoppelbestand = "e:/dev/koppelbestand_video.txt";
        String naamStrippedXml = "e:/dev/stripped_xml.xml";
        String elementContent = null;
        List<String> elementenVoorVideo = getElementList();

        String localName;

        // Open het invoerbestand.
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader =  null;
        try {
            reader = factory.createXMLStreamReader(new FileInputStream(inputXml));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Open het uitvoerbestand.
        XmlWriter strippedXml = new XmlWriter();
        try {
            strippedXml.createDocument(naamStrippedXml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Open het bestand waarin de koppeling tussen het psn-id en de hash daarvan komt te staan.
        // NB: bestaand bestand wordt overschreven.
        try (PrintWriter koppelbestandWriter = new PrintWriter(naamKoppelbestand, "UTF-8")) {

            // Lezen van inhoud van xml.
            while (reader.hasNext()) {
                int event = reader.next();


                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        localName = reader.getLocalName();
                        if (localName.equalsIgnoreCase("TotaalAllePolissen")
                                || localName.equalsIgnoreCase("data")
                                ) {
                            strippedXml.createStartElement(localName);
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        elementContent = reader.getText().trim();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        localName = reader.getLocalName();
                        if (localName.equalsIgnoreCase("M_1")) {
                            // TODO System.out verwijderen
                            System.out.println("Persoon gevonden: " + elementContent);
                            aantalPersonen++;
                            // psn_id komt als hash in strippedXml terecht.
                            // In het koppelbestand wordt het psn_id samen met de hash weggeschreven.
                            String hashedString = getHash(elementContent);
                            // Schrijf naar koppelbestand
                            koppelbestandWriter.printf("%s,%s\n",elementContent,hashedString);
                            // Schrijf naar uitvoer XML
                            strippedXml.createNode(localName, hashedString);
                        } else if (localName.equalsIgnoreCase("TotaalAllePolissen")) {
                            strippedXml.createEndElement(localName);
                            strippedXml.endDocument();
                        } else if (localName.equalsIgnoreCase("data")) {
                                strippedXml.createEndElement(localName);
                        } else if (elementenVoorVideo.contains(localName)) {
                            strippedXml.createNode(localName, elementContent);
                        }
                        break;

                }

            }
            System.out.println("Xml bestand bevat " + aantalPersonen + " personen.");
            koppelbestandWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode om van de opgegeven String een SHA-256 hash te genereren.
     *
     * @param content
     * @return Hash van de inputparameter.
     */
    private static String getHash(String content) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            md.update(content.getBytes("UTF-8"));
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
        return hexString.toString();
    }

    /**
     * Geeft de elementnamen terug van de elementen die in de uiteindelijke output-xml moeten overblijven.
     * @return Een lijst van elementnamen.
     */
    //TODO: configuratiebestand?
    private static ArrayList<String> getElementList() {
        return new ArrayList<>(Arrays.asList("M_1",
                "M_7",
                "M_20",
                "M_21",
                "M_22",
                "M_24",
                "M_26",
                "M_31",
                "M_33",
                "M_39",
                "M_40",
                "M_46",
                "M_51",
                "M_58",
                "M_81",
                "M_100",
                "M_101",
                "M_102",
                "M_103",
                "M_107",
                "M_108",
                "M_109",
                "M_110",
                "M_135",
                "M_136",
                "M_140",
                "M_144",
                "M_145",
                "M_148",
                "M_152",
                "M_153",
                "M_156",
                "M_237",
                "M_238",
                "M_280",
                "M_375",
                "M_377",
                "M_384",
                "M_385",
                "M_387",
                "M_388",
                "M_389",
                "M_391",
                "M_392",
                "M_394",
                "M_395",
                "M_397",
                "M_398",
                "M_400",
                "M_401",
                "M_403",
                "M_404",
                "M_429",
                "M_735",
                "M_741",
                "M_742",
                "M_750",
                "M_751",
                "M_752",
                "M_753"
        ));
    }
}
