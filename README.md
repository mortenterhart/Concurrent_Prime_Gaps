# Concurrency / Parallelisierung (PAR) - Task 11

## Assigned Task
Task T11: **Distinct, Increasing, Decreasing Prime Gaps**

## Task Description
Gap between consecutive primes is defined as

`g_i = p_{i + 1} - p_i`

All the gaps are even numbers.
1. `n` **distinct** consecutive gaps
2. `n` **increasing** consecutive gaps
3. `n` **decreasing** consecutive gaps

**Examples**:
```text
1) n = 2: 2 (1)  3 (2) 5;  n = 3: 17 (2) 19 (4) 23 (6) 29
2) n = 2: 2 (1)  3 (2) 5;  n = 3: 19 (2) 19 (4) 23 (6) 29
3) n = 2: 7 (4) 11 (2) 13; n = 3: 31 (6) 37 (4) 41 (2) 43
```

Aufgabe: Parallelisiert sind für `n = [2..10]` Lösungen zu ermitteln.

Screenshots von Visual VM befinden sich unter `vm-snapshots/`.

## Zielsetzungen
* Training der Kompetenz bezüglich der Implementierung von anspruchsvollen Algorithmen.
* Wiederholung und Vertiefung des Wissens zu Parallelisierung mit `CyclicBarrier`.
* Praktische Anwendung des Wissens auf rechenintensive/komplexe Aufgabenstellungen.

## JVM Parameter
```bash
java -server -Xms6G -Xmx6G -XX:MaxDirectMemorySize=1024M -XX:NewSize=1G \
     -XX:MaxNewSize=1G -XX:+UseParNewGC -XX:MaxTenuringThreshold=2 \
     -XX:SurvivorRatio=8 -XX:+UnlockDiagnosticVMOptions \
     -XX:ParGCCardsPerStrideChunk=32768
```

## Aufgabenstellung
* Implementierung einer technisch einwandfrei lauffähigen Applikation in Java 8.
  Graphische Benutzeroberfläche in JavaFX.
* Dynamische Ermittlung der Cores (`Runtime.getRuntime().availableProcessors()`)
  und ausbalancierte Verteilung der Berechnung auf die verfügbaren Cores.
* Test der Implementierung mit JUnit und Gewährleistung der Funktionsweise.
* Lösungsverfahren: Brute-Force.
* Nutzung leistungsfähiger Datenstrukturen und Optimierung der Performance mit VisualVM.
* Maximale Laufzeit der Evaluierung: 15 Minuten

## Wichtige Hinweise
* Pro Student wird eine Aufgabe bearbeitet.
* Die Zuordnung einer Aufgabe zu einem Studierenden erfolgt mit einem Zufallsgenerator.
* Nutzung der camelCase-Notation, um die Lesbarkeit zu vereinfachen.
* Zulässige externe Bibliotheken: `junit-jupiter-api.jar` und `opentest4j.jar`.
* Verwendung geeigneter englischer Begriffe für Namen und Bezeichnungen.
* Erstellung einer vollständigen und verschlüsselten 7-Zip-Datei unter Beachtung
  des Prozedere für die Abgabe von Prüfungsleistungen und der Namenskonvention.
* Zeitansatz: 10 Stunden
* Abgabetermin: Sonntag, 18.02.2018
* Bewertung: Testat
