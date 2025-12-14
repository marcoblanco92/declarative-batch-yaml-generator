package com.marbl.generator.parser;

import com.marbl.generator.BulkYmlWriter;
import com.marbl.generator.enums.EdgeType;
import com.marbl.generator.mapper.drawio.DrawioToDtoMapper;
import com.marbl.generator.mapper.dto.DtoToYmlMapper;
import com.marbl.generator.model.drawio.DrawioComponent;
import com.marbl.generator.model.drawio.DrawioEdge;
import com.marbl.generator.model.drawio.DrawioParsed;
import com.marbl.generator.model.mapper.BulkDto;
import com.marbl.generator.model.yml.RootYml;
import com.marbl.generator.parser.factory.DrawioComponentFactory;
import com.marbl.generator.parser.utils.DrawioUtils;
import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Utility class to parse Draw.io XML files into DrawioParsed objects.
 *
 * <p>This parser extracts components (jobs, steps, tasklets, readers, writers, etc.)
 * and edges (Next, SimpleFlow, OnCondition) from the XML DOM.
 */
@UtilityClass
public class DrawioDomParser {

    /**
     * Parses a Draw.io XML file into a DrawioParsed object containing components and edges.
     *
     * @param file the Draw.io XML file
     * @return a DrawioParsed object with components and edges
     */
    public DrawioParsed parse(File file) {
        try {
            Document doc = buildDocument(file);

            List<DrawioComponent> components = parseComponents(doc);
            List<DrawioEdge> edges = parseEdges(doc, components);

            return new DrawioParsed(components, edges);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Draw.io file", e);
        }
    }

    /**
     * Builds a normalized DOM Document from a file.
     *
     * @param file the input XML file
     * @return the normalized Document
     * @throws Exception if parsing fails
     */
    private Document buildDocument(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        return doc;
    }

    /**
     * Parses Draw.io components from the DOM document.
     *
     * <p>It creates domain objects for Jobs, Steps, Tasklets, Readers, Writers, Listeners,
     * and also handles orphaned mxCells as generic components.
     *
     * @param doc the DOM document
     * @return a list of DrawioComponent objects
     */
    private List<DrawioComponent> parseComponents(Document doc) {
        NodeList objects = doc.getElementsByTagName("object");
        NodeList mxCells = doc.getElementsByTagName("mxCell");

        Map<String, Element> mxCellMap = new HashMap<>();
        for (int i = 0; i < mxCells.getLength(); i++) {
            Element cell = (Element) mxCells.item(i);
            String id = cell.getAttribute("id");
            if (!id.isEmpty()) mxCellMap.put(id, cell);
        }

        Set<String> processedMxCells = new HashSet<>();
        List<DrawioComponent> components = new ArrayList<>();

        for (int i = 0; i < objects.getLength(); i++) {
            Element obj = (Element) objects.item(i);
            if (DrawioUtils.isEdgeSource(obj.getAttribute("source"))) continue;

            DrawioComponent component = DrawioComponentFactory.createComponent(obj);
            components.add(component);

            // Mark mxCell as processed
            NodeList childCells = obj.getElementsByTagName("mxCell");
            for (int j = 0; j < childCells.getLength(); j++) {
                processedMxCells.add(((Element) childCells.item(j)).getAttribute("id"));
            }
        }

        // Add orphan mxCells as generic components
        for (Element cell : mxCellMap.values()) {
            String id = cell.getAttribute("id");
            if (!processedMxCells.contains(id)) {
                components.add(DrawioComponent.builder()
                        .id(id)
                        .parentId(cell.getAttribute("parent"))
                        .build());
            }
        }

        return components;
    }

    /**
     * Parses Draw.io edges (Next, SimpleFlow, OnCondition) from the DOM document.
     *
     * @param doc        the DOM document
     * @param components the list of parsed components to resolve references
     * @return a list of DrawioEdge objects
     */
    private List<DrawioEdge> parseEdges(Document doc, List<DrawioComponent> components) {
        List<DrawioEdge> edges = new ArrayList<>();
        Map<String, DrawioComponent> componentById = new HashMap<>();
        components.forEach(c -> componentById.put(c.getId(), c));

        NodeList objects = doc.getElementsByTagName("object");

        for (int i = 0; i < objects.getLength(); i++) {
            Element obj = (Element) objects.item(i);
            String objSource = obj.getAttribute("source");
            EdgeType type = DrawioUtils.edgeTypeFromSource(objSource);
            if (type == null) continue;

            NodeList childCells = obj.getElementsByTagName("mxCell");
            for (int j = 0; j < childCells.getLength(); j++) {
                Element cell = (Element) childCells.item(j);
                if (!"1".equals(cell.getAttribute("edge"))) continue;

                String id = obj.getAttribute("id");
                String sourceId = cell.getAttribute("source");
                String targetId = cell.getAttribute("target");
                String condition = null;

                if (type == EdgeType.ON_CONDITION) {
                    condition = findConditionValue(obj, doc);
                }

                edges.add(DrawioEdge.builder()
                        .id(id)
                        .type(type)
                        .sourceId(sourceId)
                        .targetId(targetId)
                        .condition(condition)
                        .build());
            }
        }

        return edges;
    }

    /**
     * Searches for the condition value associated with an OnCondition edge.
     *
     * @param obj the object representing the OnCondition
     * @param doc the DOM document
     * @return the condition value if found, null otherwise
     */
    private String findConditionValue(Element obj, Document doc) {
        NodeList allObjects = doc.getElementsByTagName("object");
        String parentId = obj.getAttribute("id");

        for (int i = 0; i < allObjects.getLength(); i++) {
            Element possibleCondObj = (Element) allObjects.item(i);
            NodeList possibleCells = possibleCondObj.getElementsByTagName("mxCell");
            for (int j = 0; j < possibleCells.getLength(); j++) {
                Element cell = (Element) possibleCells.item(j);
                if (parentId.equals(cell.getAttribute("parent"))) {
                    return possibleCondObj.getAttribute("condition_value");
                }
            }
        }

        return null;
    }
}
