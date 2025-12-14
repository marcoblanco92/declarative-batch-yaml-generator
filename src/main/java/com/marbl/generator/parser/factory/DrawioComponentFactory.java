package com.marbl.generator.parser.factory;

import com.marbl.generator.model.drawio.*;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class DrawioComponentFactory {

    public static DrawioComponent createComponent(Element obj) {
        String id = obj.getAttribute("id");
        String source = obj.getAttribute("source");
        String name = obj.getAttribute("name");
        String type = obj.getAttribute("type");
        String parentId = null;

        NodeList childCells = obj.getElementsByTagName("mxCell");
        if (childCells.getLength() > 0) {
            parentId = ((Element) childCells.item(0)).getAttribute("parent");
        }

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
                step.setOrder(Integer.parseInt(obj.getAttribute("order")));
                String chunk = obj.getAttribute("chunk_size");
                if (!chunk.isEmpty()) step.setChunkSize(Integer.parseInt(chunk));
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
            case "Listeners" -> {
                DrawioListeners l = new DrawioListeners();
                l.setId(id);
                l.setSource(source);
                l.setName(name);
                l.setParentId(parentId);
                yield l;
            }
            case "Tasklet" -> {
                DrawioTasklet t = new DrawioTasklet();
                t.setId(id);
                t.setSource(source);
                t.setName(name);
                t.setParentId(parentId);
                t.setOrder(Integer.parseInt(obj.getAttribute("order")));
                yield t;
            }
            default -> new DrawioComponent(id, source, name, type, parentId);
        };
    }
}
