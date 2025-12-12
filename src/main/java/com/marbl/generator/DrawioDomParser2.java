//package com.marbl.generator;
//
//import com.marbl.generator.model.*;
//import org.w3c.dom.*;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.File;
//import java.util.*;
//
//public class DrawioDomParser2 {
//
//    public List<DrawioComponent> parse(File file) {
//        Map<String, DrawioComponent> componentMap = new HashMap<>();
//        Map<String, GroupComponent> groupMap = new HashMap<>();
//        List<DrawioComponent> allComponents = new ArrayList<>();
//
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(file);
//            doc.getDocumentElement().normalize();
//
//            NodeList objects = doc.getElementsByTagName("object");
//
//            // --- PRIMO CICLO: crea solo componenti reali ---
//            for (int i = 0; i < objects.getLength(); i++) {
//                Element elem = (Element) objects.item(i);
//                String source = elem.getAttribute("source");
//                String id = elem.getAttribute("id");
//                String parentId = elem.getAttribute("parent");
//                String name = elem.getAttribute("name");
//                String type = elem.getAttribute("type");
//
//                // ignora gli edge
//                if ("Next".equals(source) || "SimpleFlow".equals(source) || "OnCondition".equals(source)) {
//                    continue;
//                }
//
//                DrawioComponent component;
//                switch (source) {
//                    case "DataSource":
//                        DataSourceComponent ds = new DataSourceComponent();
//                        ds.setId(id);
//                        ds.setName(name);
//                        ds.setType(type);
//                        ds.setMain(Boolean.parseBoolean(elem.getAttribute("main")));
//                        component = ds;
//                        break;
//                    case "Reader":
//                        ReaderComponent reader = new ReaderComponent();
//                        reader.setId(id);
//                        reader.setName(name);
//                        reader.setReaderType(type);
//                        component = reader;
//                        break;
//                    case "Processor":
//                        ProcessorComponent processor = new ProcessorComponent();
//                        processor.setId(id);
//                        processor.setName(name);
//                        processor.setProcessorType(type);
//                        component = processor;
//                        break;
//                    case "Writer":
//                        WriterComponent writer = new WriterComponent();
//                        writer.setId(id);
//                        writer.setName(name);
//                        writer.setWriterType(type);
//                        component = writer;
//                        break;
//                    case "Step":
//                        StepComponent step = new StepComponent();
//                        step.setId(id);
//                        step.setName(name);
//                        step.setType(type);
//                        String chunk = elem.getAttribute("chunk_size");
//                        if (chunk != null && !chunk.isEmpty()) step.setChunkSize(Integer.parseInt(chunk));
//                        component = step;
//                        break;
//                    case "Tasklet":
//                        TaskletComponent tasklet = new TaskletComponent();
//                        tasklet.setId(id);
//                        tasklet.setName(name);
//                        tasklet.setType(type);
//                        component = tasklet;
//                        break;
//                    case "Listeners":
//                        ListenerComponent listener = new ListenerComponent();
//                        listener.setId(id);
//                        listener.setName(name);
//                        listener.setListenerType(type);
//                        component = listener;
//                        break;
//                    case "Job":
//                        JobComponent job = new JobComponent();
//                        job.setId(id);
//                        job.setName(name);
//                        component = job;
//                        break;
//                    default:
//                        // Se ha parent, trattiamo come gruppo
////                        if (parentId != null && !parentId.isEmpty()) {
////                            GroupComponent group = new GroupComponent();
////                            group.setId(id);
////                            group.setName(name != null ? name : "Group-" + id);
////                            groupMap.put(id, group);
////                            component = group;
//                        } else {
//                            System.out.println("Warning: Source non gestito: " + source);
//                            component = null;
//                        }
//                        break;
//                }
//
//                if (component != null) {
//                    componentMap.put(id, component);
//                    allComponents.add(component);
//                }
//            }
//
//            // --- Associa componenti ai gruppi ---
////            for (DrawioComponent comp : allComponents) {
////                if (!(comp instanceof GroupComponent)) {
////                    String parentId = getParentId(comp, objects);
////                    if (parentId != null && groupMap.containsKey(parentId)) {
////                        groupMap.get(parentId).addChild(comp);
////                    }
////                }
////            }
//
//            // --- SECONDO CICLO: gestisci solo gli edge ---
//            for (int i = 0; i < objects.getLength(); i++) {
//                Element elem = (Element) objects.item(i);
//                String edgeType = elem.getAttribute("source");
//                if (!"Next".equals(edgeType) && !"SimpleFlow".equals(edgeType) && !"OnCondition".equals(edgeType)) {
//                    continue;
//                }
//
//                String sourceId = elem.getAttribute("sourceId");
//                String targetId = elem.getAttribute("target");
//                if (sourceId == null || targetId == null) continue;
//
//                DrawioComponent sourceComp = componentMap.get(sourceId);
//                DrawioComponent targetComp = componentMap.get(targetId);
//                if (sourceComp == null || targetComp == null) continue;
//
//                if ("Next".equals(edgeType) || "SimpleFlow".equals(edgeType)) {
//                    // collega source a target come gruppo
////                    sourceComp.setGroupComponent(targetComp instanceof GroupComponent ? (GroupComponent) targetComp : null);
//                } else if ("OnCondition".equals(edgeType)) {
//                    String conditionValue = elem.getAttribute("condition_value");
//                    if (sourceComp instanceof ConditionalFlowHolder) {
//                        ((ConditionalFlowHolder) sourceComp).addConditionalTarget(conditionValue, targetComp);
//                    } else {
//                        System.out.println("Warning: componente non supporta flusso condizionale: " + sourceComp.getId());
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            System.out.println("Errore durante il parsing del file drawio: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return allComponents;
//    }
//
//    private String getParentId(DrawioComponent comp, NodeList objects) {
//        for (int i = 0; i < objects.getLength(); i++) {
//            Element elem = (Element) objects.item(i);
//            if (elem.getAttribute("id").equals(comp.getId())) {
//                String parent = elem.getAttribute("parent");
//                return (parent != null && !parent.isEmpty()) ? parent : null;
//            }
//        }
//        return null;
//    }
//
////    public static void main(String[] args) {
////        File file = new File("Poc_example.drawio"); // Inserisci il percorso corretto
////        DrawioDomParser2 parser = new DrawioDomParser2();
////        List<DrawioComponent> components = parser.parse(file);
////
////        for (DrawioComponent c : components) {
////            System.out.println(c.getClass().getSimpleName() + ": " + c.getName() + " (id=" + c.getId() + ")");
////
////            if (c instanceof StepComponent) {
////                StepComponent step = (StepComponent) c;
//////                if (step.getGroupComponent() != null) {
//////                    System.out.println("  -> Next Group: " + step.getGroupComponent().getName());
////                }
////            }
////
////            if (c instanceof ConditionalFlowHolder) {
////                ConditionalFlowHolder cfh = (ConditionalFlowHolder) c;
////                Map<String, DrawioComponent> condTargets = cfh.getConditionalTargets();
////                if (!condTargets.isEmpty()) {
////                    System.out.println("  -> Conditional Targets:");
////                    for (Map.Entry<String, DrawioComponent> entry : condTargets.entrySet()) {
////                        System.out.println("       [" + entry.getKey() + "] -> " + entry.getValue().getName() + " (" + entry.getValue().getId() + ")");
////                    }
////                }
////            }
////
////            if (c instanceof GroupComponent) {
////                GroupComponent group = (GroupComponent) c;
////                if (!group.getChildren().isEmpty()) {
////                    System.out.println("  -> Children:");
////                    for (DrawioComponent child : group.getChildren()) {
////                        System.out.println("       " + child.getName() + " (" + child.getId() + ")");
////                    }
////                }
////            }
////        }
////    }
//}
