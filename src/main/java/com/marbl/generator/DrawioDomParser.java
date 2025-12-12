package com.marbl.generator;

import com.marbl.generator.model.DrawioComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class DrawioDomParser {


    public List<DrawioComponent> parse(File file) {
        List<DrawioComponent> components = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList objects = doc.getElementsByTagName("object");
            NodeList mxCells = doc.getElementsByTagName("mxCell");

            // Creo mappa id -> mxCell
            Map<String, Element> mxCellMap = new HashMap<>();
            for (int i = 0; i < mxCells.getLength(); i++) {
                Element cell = (Element) mxCells.item(i);
                String id = cell.getAttribute("id");
                if (!id.isEmpty()) {
                    mxCellMap.put(id, cell);
                }
            }

            Set<String> processedCells = new HashSet<>();

            // Prima elaboro gli object
            for (int i = 0; i < objects.getLength(); i++) {
                Element objElem = (Element) objects.item(i);
                String id = objElem.getAttribute("id");
                String source = objElem.getAttribute("source");
                String name = objElem.getAttribute("name");
                String type = objElem.getAttribute("type");

                // Trovo mxCell figlio, se presente
                String parentId = null;
                NodeList childCells = objElem.getElementsByTagName("mxCell");
                if (childCells.getLength() > 0) {
                    Element cell = (Element) childCells.item(0);
                    parentId = cell.getAttribute("parent");
                    processedCells.add(cell.getAttribute("id"));
                }

                DrawioComponent component = new DrawioComponent(id, source, name, type, parentId);
                components.add(component);

            }

            // Poi elaboro mxCell “orfani”
            for (Element cell : mxCellMap.values()) {
                String id = cell.getAttribute("id");
                if (processedCells.contains(id)) continue; // già processata con object

                String parentId = cell.getAttribute("parent");

                // Creo un DrawioComponent “generico” per le mxCell orfane
                DrawioComponent component = new DrawioComponent(id, null, null, null, parentId);
                components.add(component);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return components;
    }


    public static void main(String[] args) {
        File file = new File("Poc_example.drawio"); // Inserisci il percorso corretto
        DrawioDomParser parser = new DrawioDomParser();
        List<DrawioComponent> components = parser.parse(file);

        for (DrawioComponent component : components) {
            System.out.println(component);
        }
    }
}
