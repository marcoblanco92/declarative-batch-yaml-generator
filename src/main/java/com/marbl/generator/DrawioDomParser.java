package com.marbl.generator;

import com.marbl.generator.enums.EdgeType;
import com.marbl.generator.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class DrawioDomParser {

    public ParsedDrawio parse(File file) {
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
            for (int i = 0; i < mxCells.getLength(); i++) {
                Element cell = (Element) mxCells.item(i);

                if (!"1".equals(cell.getAttribute("edge"))) continue;

                String id = cell.getAttribute("id");
                String sourceId = cell.getAttribute("source");
                String targetId = cell.getAttribute("target");
                String value = cell.getAttribute("value"); // Next / OnCondition / SimpleFlow
                String condition = cell.getAttribute("condition_value");

                EdgeType edgeType = switch (value) {
                    case "Next" -> EdgeType.NEXT;
                    case "SimpleFlow" -> EdgeType.SIMPLE_FLOW;
                    case "OnCondition" -> EdgeType.ON_CONDITION;
                    default -> EdgeType.UNKNOWN;
                };

                DrawioEdge edge = DrawioEdge.builder()
                        .id(id)
                        .sourceId(sourceId)
                        .targetId(targetId)
                        .type(edgeType)
                        .condition(condition)
                        .build();

                edges.add(edge);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ParsedDrawio(components, edges);
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
                DataSourceComponent ds = new DataSourceComponent();
                ds.setId(id);
                ds.setSource(source);
                ds.setName(name);
                ds.setType(type);
                ds.setParentId(parentId);
                ds.setMain(Boolean.parseBoolean(obj.getAttribute("main")));
                yield ds;
            }
            case "Job" -> {
                JobComponent job = new JobComponent();
                job.setId(id);
                job.setSource(source);
                job.setName(name);
                job.setType(type);
                job.setParentId(parentId);
                yield job;
            }
            case "Step" -> {
                StepComponent step = new StepComponent();
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
                ReaderComponent r = new ReaderComponent();
                r.setId(id);
                r.setSource(source);
                r.setName(name);
                r.setReaderType(type);
                r.setParentId(parentId);
                yield r;
            }
            case "Processor" -> {
                ProcessorComponent p = new ProcessorComponent();
                p.setId(id);
                p.setSource(source);
                p.setName(name);
                p.setProcessorType(type);
                p.setParentId(parentId);
                yield p;
            }
            case "Writer" -> {
                WriterComponent w = new WriterComponent();
                w.setId(id);
                w.setSource(source);
                w.setName(name);
                w.setWriterType(type);
                w.setParentId(parentId);
                yield w;
            }
            case "Listener" -> {
                ListenerComponent l = new ListenerComponent();
                l.setId(id);
                l.setSource(source);
                l.setName(name);
                l.setListenerType(type);
                l.setParentId(parentId);
                yield l;
            }
            case "Tasklet" -> {
                TaskletComponent t = new TaskletComponent();
                t.setId(id);
                t.setSource(source);
                t.setName(name);
                t.setParentId(parentId);
                yield t;
            }

            default -> new DrawioComponent(id, source, name, type, parentId);
        };
    }

    // -------------------------------------------------------
    // Utility: trova object padre di un mxCell edge
    // -------------------------------------------------------
    private Element findParentObject(NodeList objects, String mxCellId) {
        for (int i = 0; i < objects.getLength(); i++) {
            Element obj = (Element) objects.item(i);
            NodeList cells = obj.getElementsByTagName("mxCell");
            if (cells.getLength() > 0) {
                Element cell = (Element) cells.item(0);
                if (mxCellId.equals(cell.getAttribute("id"))) {
                    return obj;
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        File file = new File("Poc_example.drawio"); // Inserisci il percorso corretto
        DrawioDomParser parser = new DrawioDomParser();
        ParsedDrawio components = parser.parse(file);

        for (DrawioComponent component : components.getComponents()) {
            System.out.println(component);
        }
        for (DrawioEdge edge : components.getEdges()) {
            System.out.println(edge);
        }
    }
}
