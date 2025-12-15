# Declarative Batch YAML Generator

## Overview

`declarative-batch-yaml-generator` è un **plugin Maven custom** sviluppato ad hoc per il progetto **spring-declarative-batch**.

L'obiettivo del plugin è permettere la **generazione automatica di un file `application-generated.yml`** partendo da un diagramma **draw.io**, mantenendo uno scheletro YAML compatibile con Spring Batch dichiarativo e coerente con i componenti modellati graficamente.

In questo modo, il design del batch avviene **in modo visuale**, mentre la configurazione tecnica viene generata in maniera **deterministica e standardizzata**.

---

## Funzionamento ad alto livello

Il plugin esegue i seguenti passi:

1. Legge un file `.drawio` contenente il diagramma del batch
2. Effettua il parsing del diagramma tramite DOM
3. Mappa i componenti grafici in DTO interni
4. Converte i DTO in una struttura YAML
5. Scrive il file `application-generated.yml` nella directory di output configurata

Pipeline concettuale:

```
Draw.io → DOM Parser → DTO Mapping → YAML Mapping → application-generated.yml
```

---

## Prerequisiti

* Java 17+
* Maven 3.9+
* Progetto basato su **Spring Batch dichiarativo**
* Diagrammi creati con **draw.io** secondo le convenzioni del progetto

---

## Installazione

Il plugin è di tipo **maven-plugin** e viene richiamato direttamente nel `pom.xml` del progetto che utilizza Spring Batch.

```xml
<plugin>
    <groupId>com.marbl.generator</groupId>
    <artifactId>declarative-batch-yaml-generator</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <executions>
        <execution>
            <id>generate-batch-yaml</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <inputFile>${project.basedir}/src/main/resources/example.drawio</inputFile>
                <outputDir>${project.build.directory}/generated-resources/batch</outputDir>
            </configuration>
        </execution>
    </executions>
</plugin>
```

---

## Goal Maven

Il plugin espone il seguente goal:

```bash
mvn declarative-batch:generate
```

Il goal viene normalmente eseguito automaticamente nella fase `generate-resources`.

---

## Parametri di configurazione

| Parametro             | Descrizione                                | Obbligatorio | Default                                                |
| --------------------- | ------------------------------------------ | ------------ | ------------------------------------------------------ |
| `inputFile`           | File `.drawio` di input                    | ✅            | -                                                      |
| `outputDir`           | Directory di output del file YAML          | ❌            | `${project.build.directory}/generated-resources/batch` |
| `logging.basePackage` | Base package per la configurazione logging | ❌            | `${project.groupId}`                                   |

---

## Output generato

Il plugin genera un file:

```
application-generated.yml
```

### Esempio di YAML generato (snippet concettuale)

```yaml
bulk:
  datasources:
    ExampleDb1:
      name: ExampleDb1
      main: true
      url: ""
      username: ""
      password: ""
      driver-class-name: ""

  batch-job:
    name: ExampleJob
    steps:
      - name: ExampleStep1
        chunk: 100
        reader:
          name: ExampleReader
          type: FlatFileItemReader
        processor:
          name: ExampleProcessor
          type: ItemProcessor
        writer:
          name: ExampleWriter
          type: FlatFileItemWriter
```

> ⚠️ I nomi dei componenti (`Example*`) vengono derivati direttamente dal diagramma draw.io.

---

## Integrazione con Spring Batch

Il file `application-generated.yml` è pensato per essere:

* incluso tra le **generated-resources**
* caricato automaticamente dal contesto Spring
* interpretato dal framework **spring-declarative-batch**

Questo consente di separare:

* **design funzionale** (draw.io)
* **configurazione tecnica** (YAML)
* **logica applicativa** (reader / processor / writer)

---

## Struttura interna del plugin

Componenti principali:

* `GenerateBatchYamlMojo` → entry point Maven
* `DrawioDomParser` → parsing del file draw.io
* `DrawioToDtoMapper` → mapping diagramma → DTO
* `DtoToYmlMapper` → mapping DTO → YAML
* `BulkYmlWriter` → scrittura file YAML

---

## Limitazioni

* Il plugin è **ad hoc** per il progetto `spring-declarative-batch`
* Il file draw.io deve rispettare convenzioni strutturali precise
* Non valida la correttezza semantica del batch (solo strutturale)

---

## Roadmap (possibili evoluzioni)

* Validazione avanzata dei diagrammi
* Supporto a listener e flow complessi
* Integrazione con CI

---

## License

© 2025 – Tutti i diritti riservati.

Il presente software e il relativo codice sorgente sono di esclusiva proprietà dell'autore.

All rights reserved.

Nessuna parte di questo software può essere riprodotta, distribuita, modificata, pubblicata o utilizzata, in alcuna forma e con alcun mezzo, senza la preventiva autorizzazione scritta dell'autore.
