package com.marbl.generator;

import com.marbl.generator.enums.EdgeType;
import com.marbl.generator.model.drawio.*;
import com.marbl.generator.model.mapper.BulkDto;
import com.marbl.generator.model.yml.RootYml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.*;

public class DrawioDomParser {

    public DrawioParsed parse(File file) {
        List<DrawioComponent> components = new ArrayList<>();
        List<DrawioEdge> edges = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList objects = doc.getElementsByTagName("object");
            NodeList mxCells = doc.getElementsByTagName("mxCell");

            // -------------------------------------------------------
            // 1️⃣ Mappa id -> mxCell
            // -------------------------------------------------------
            Map<String, Element> mxCellMap = new HashMap<>();
            for (int i = 0; i < mxCells.getLength(); i++) {
                Element cell = (Element) mxCells.item(i);
                String id = cell.getAttribute("id");
                if (!id.isEmpty()) {
                    mxCellMap.put(id, cell);
                }
            }

            Set<String> processedMxCells = new HashSet<>();

            // -------------------------------------------------------
            // 2️⃣ Parsing COMPONENTI (object)
            // -------------------------------------------------------
            for (int i = 0; i < objects.getLength(); i++) {
                Element obj = (Element) objects.item(i);

                String id = obj.getAttribute("id");
                String source = obj.getAttribute("source");
                String name = obj.getAttribute("name");
                String type = obj.getAttribute("type");

                String parentId = null;
                NodeList childCells = obj.getElementsByTagName("mxCell");
                if (childCells.getLength() > 0) {
                    Element cell = (Element) childCells.item(0);
                    parentId = cell.getAttribute("parent");
                    processedMxCells.add(cell.getAttribute("id"));
                }

                if (isEdgeSource(source)) {
                    // NON creare DrawioComponent per edge
                    continue;
                }

                DrawioComponent component = createComponent(id, source, name, type, parentId, obj);
                components.add(component);
            }

            // -------------------------------------------------------
            // 3️⃣ mxCell ORFANE (gruppi / contenitori)
            // -------------------------------------------------------
            for (Element cell : mxCellMap.values()) {
                String id = cell.getAttribute("id");
                if (processedMxCells.contains(id)) continue;

                String parentId = cell.getAttribute("parent");

                DrawioComponent generic = DrawioComponent.builder()
                        .id(id)
                        .parentId(parentId)
                        .build();

                components.add(generic);
            }

            // -------------------------------------------------------
// 4️⃣ Parsing EDGE direttamente dalle mxCell edge="1"
// -------------------------------------------------------
            for (int i = 0; i < objects.getLength(); i++) {
                Element obj = (Element) objects.item(i);

                String objSource = obj.getAttribute("source"); // Next, SimpleFlow, OnCondition
                EdgeType type = switch (objSource) {
                    case "Next" -> EdgeType.NEXT;
                    case "SimpleFlow" -> EdgeType.SIMPLE_FLOW;
                    case "OnCondition" -> EdgeType.ON_CONDITION;
                    default -> null;
                };
                if (type == null) continue;

                // Leggiamo eventuali mxCell figli (edge veri e propri)
                NodeList childCells = obj.getElementsByTagName("mxCell");
                for (int j = 0; j < childCells.getLength(); j++) {
                    Element cell = (Element) childCells.item(j);
                    if (!"1".equals(cell.getAttribute("edge"))) continue; // solo edge

                    String id = obj.getAttribute("id");
                    String sourceId = cell.getAttribute("source");
                    String targetId = cell.getAttribute("target");
                    String condition = null;

                    if (type == EdgeType.ON_CONDITION) {
                        // Cerca un object il cui mxCell ha parent = id dell'object OnCondition
                        NodeList allObjects = obj.getOwnerDocument().getElementsByTagName("object");
                        for (int k = 0; k < allObjects.getLength(); k++) {
                            Element possibleCondObj = (Element) allObjects.item(k);
                            NodeList possibleCells = possibleCondObj.getElementsByTagName("mxCell");
                            for (int m = 0; m < possibleCells.getLength(); m++) {
                                Element condCell = (Element) possibleCells.item(m);
                                if (id.equals(condCell.getAttribute("parent"))) {
                                    condition = possibleCondObj.getAttribute("condition_value");
                                    break;
                                }
                            }
                            if (condition != null) break;
                        }
                    }

                    DrawioEdge edge = DrawioEdge.builder()
                            .id(id)
                            .type(type)
                            .sourceId(sourceId)
                            .targetId(targetId)
                            .condition(condition)
                            .build();

                    edges.add(edge);
                }
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

        return new DrawioParsed(components, edges);
    }

    private boolean isEdgeSource(String source) {
        return "Next".equals(source)
                || "SimpleFlow".equals(source)
                || "OnCondition".equals(source);
    }


    // -------------------------------------------------------
    // FACTORY COMPONENTI
    // -------------------------------------------------------
    private DrawioComponent createComponent(
            String id,
            String source,
            String name,
            String type,
            String parentId,
            Element obj
    ) {
        return switch (source) {
            case "DataSource" -> {
                DrawioDataSource ds = new DrawioDataSource();
                ds.setId(id);
                ds.setSource(source);
                ds.setName(name);
                ds.setType(type);
                ds.setParentId(parentId);
                ds.setMain(Boolean.parseBoolean(obj.getAttribute("main")));
                yield ds;
            }
            case "Job" -> {
                DrawioJob job = new DrawioJob();
                job.setId(id);
                job.setSource(source);
                job.setName(name);
                job.setType(type);
                job.setParentId(parentId);
                yield job;
            }
            case "Step" -> {
                DrawioStep step = new DrawioStep();
                step.setId(id);
                step.setSource(source);
                step.setName(name);
                step.setType(type);
                step.setParentId(parentId);

                String chunk = obj.getAttribute("chunk_size");
                if (!chunk.isEmpty()) {
                    step.setChunkSize(Integer.parseInt(chunk));
                }
                yield step;
            }
            case "Reader" -> {
                DrawioReader r = new DrawioReader();
                r.setId(id);
                r.setSource(source);
                r.setName(name);
                r.setReaderType(type);
                r.setParentId(parentId);
                yield r;
            }
            case "Processor" -> {
                DrawioProcessor p = new DrawioProcessor();
                p.setId(id);
                p.setSource(source);
                p.setName(name);
                p.setProcessorType(type);
                p.setParentId(parentId);
                yield p;
            }
            case "Writer" -> {
                DrawioWriter w = new DrawioWriter();
                w.setId(id);
                w.setSource(source);
                w.setName(name);
                w.setWriterType(type);
                w.setParentId(parentId);
                yield w;
            }
            case "Listener" -> {
                DrawioListener l = new DrawioListener();
                l.setId(id);
                l.setSource(source);
                l.setName(name);
                l.setListenerType(type);
                l.setParentId(parentId);
                yield l;
            }
            case "Tasklet" -> {
                DrawioTasklet t = new DrawioTasklet();
                t.setId(id);
                t.setSource(source);
                t.setName(name);
                t.setParentId(parentId);
                yield t;
            }

            default -> new DrawioComponent(id, source, name, type, parentId);
        };
    }


    public static void main(String[] args) throws IOException {
        File file = new File("Poc_example.drawio"); // Inserisci il percorso corretto
        DrawioDomParser parser = new DrawioDomParser();
        DrawioParsed components = parser.parse(file);

        for (DrawioComponent component : components.getComponents()) {
            System.out.println(component);
        }
        for (DrawioEdge edge : components.getEdges()) {
            System.out.println(edge);
        }

        DrawioToDtoMapper mapper = new DrawioToDtoMapper();
        BulkDto bulkDto = mapper.mapToBulkDto(components);
        System.out.println(bulkDto);

        DtoToYmlMapper mapper2 = new DtoToYmlMapper();
        RootYml rootYml = mapper2.mapToRootYaml(bulkDto);
        System.out.println(rootYml);

        BulkYmlWriter bulkYmlWriter = new BulkYmlWriter();
        String yml = bulkYmlWriter.writeAsString(rootYml);
        System.out.println(yml);
    }
}
