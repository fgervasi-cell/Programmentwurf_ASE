# Schriftliche Ausarbeitung

## Domain Driven Design (DDD)

### Analyse der Ubiquitous Language

<!-- 
- Was ist die Ubiquitous Language? 
- Was sind wichtige Begriffe/Prozesse in meiner Problemdomäne? 
- Was für Regeln gelten bei den jeweiligen Prozessen?
-->

Die Ubiquitous Language ist ein Konzept des Domain Driven Design, welches Verständnisprobleme zwischen Entwicklern und Domänenexperten, durch das Definieren einer gemeinsamen Projektsprache, vorbeugen soll. Die Projektsprache beinhaltet Definitionen für das gemeinsame Verständnis von Konzepten, Prozessen und Regeln aus der Domäne.

Wichtige Begriffe und Prozesse innerhalb der Problemdomäne "Aufgabenliste" sind:

- Aufgabe (Task)
- Fälligkeitsdatum (Due Date)
- Aufgabenliste (Task List)
- Arbeitsschritt (Sub Task)
- Erinnerung (Reminder)
- Liste anlegen
- Aufgabe verschieben/löschen/bearbeiten
- Liste löschen/bearbeiten

Aufgabe (Task):

Eine Aufgabe besteht aus Name/Titel, Erinnerungsdatum, Arbeitsschritte, Beschreibung/Notiz, Fälligkeitsdatum. Zudem hat eine Aufgabe mehrere mögliche Zustände:

- Erledigt und nicht erledigt
- Fälligkeitsdatum überschritten/nicht überschritten

Alle Bestandteile, außer dem Namen, sind optional. Die Anzahl Arbeitsschritte ist logisch nicht begrenzt.

Fälligkeitsdatum (Due Date):

Ein Fälligkeitsdatum kann auf einer Aufgabe gesetzt werden. Es ist darauf zu achten, dass das Fälligkeitsdatum nicht in der Vergangenheit liegen darf. Ist das Fälligkeitsdatum auf einer Aufgabe erreicht muss dies in der Aufgabe gekennzeichnet werden.

Aufgabenliste (Task List):

Eine Aufgabenliste kann mehrere (theoretisch unbegrenzt viele) Aufgaben beinhalten. Sie kann aber auch keine Aufgabe beinhalten. Aufgabenlisten können vom Benutzer erstellt werden. Dabei wird ein Name vom Benutzer festgelegt, welcher über alle Listen eindeutig sein muss. Die Bestandteile einer Aufgabenliste sind folglich ihr Name und eine Reihe von Aufgaben.

Arbeitsschritt (Sub Task):

Ein Arbeitsschritt kann selbst wieder als Aufgabe verstanden werden, mit dem Unterschied, dass ein Arbeitsschritt keine weiteren Arbeitsschritte enthalten darf.

Erinnerung (Reminder):

Eine Erinnerung kann auf einer Aufgabe gesetzt werden. Es ist erneut darauf zu achten, dass das Erinnerungsdatum nicht in der Vergangenheit liegt. Ist das Erinnerungsdatum auf einer Aufgabe erreicht, muss der Benutzer darüber in Kenntnis gesetzt werden.

Liste anlegen:

Eine neue Liste anzulegen heißt ihr einen eindeutigen Namen zu geben. Nach dem Anlegen der Liste ist diese zunächst leer. Der Benutzer hat jetzt die Möglichkeit zu dieser Liste Aufgaben hinzuzufügen.

Aufgabe verschieben/löschen/bearbeiten:

Eine Aufgabe zu verschieben heißt, sie von einer Liste in eine andere zu bewegen. Dafür muss die entsprechende Aufgabe aus der Aufgabenliste, in der sie sich momentan befindet, gelöscht und anschließend einer anderen Liste hinzugefügt zu werden.  
Eine Aufgabe zu löschen heißt alle mit ihr assoziierten Daten aus der Applikation zu entfernen, sodass diese anschließend nicht mehr abrufbar sind.  
Eine Aufgabe zu bearbeiten, heißt eines der oben genannten Bestandteile einer Liste zu verändern oder die Aufgabe als erledigt bzw. unerledigt zu markieren. Wird eine Aufgabe als erledigt markiert wird sie aus ihrer aktuellen Liste entfernt und einer nicht vom Benutzer verwalteten Liste von erledigten Aufgaben zugeteilt, damit diese später noch abgerufen werden können. Eine Aufgabe als erledigt zu markieren ist also nicht dasselbe wie sie zu löschen! Wird eine Aufgabe wieder als unerledigt markiert wird ihr Status entsprechend verändert und sie wird der ursprünglichen Liste hinzugefügt aus der sie gekommen ist.

Liste löschen/bearbeiten:

Wird eine Liste gelöscht, dann löschen sich auch alle in ihr enthaltenen Aufgaben. Eine Liste zu bearbeiten bedeutet ihren Namen zu verändern. Dabei muss erneut geprüft werden, dass der Name eindeutig ist.

### Analyse und Begründung der verwendeten Muster

#### Value Objects

<!--
- Was sind Value Objects (VOs)?
- Wo habe ich VOs in meinem Projekt verwendet und warum?
- Bspw. DueDate(Date)?, EMail(String)
-->

Value Objects (VOs) kapseln einen oder mehrere Werte in einem neuen Wert/Typ. Das Value Object kann daraufhin die Einhaltung von bestimmten Regeln, die in der Problemdomäne gegenüber dem gekapselten Wert bestehen, prüfen. Ein VO muss unveränderlich sein. Das hat den Vorteil, dass der Zustand nach der Erzeugung eines VOs nicht mehr ungültig gemacht werden kann. Zwei VOs sind gleich, wenn ihre gekapselten Werte übereinstimmen.

Die Klasse _dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture_ ist ein solches Value Object. Es dient der Kapselung eines normalen Werts vom Typ _java.util.Date_ um zu kontrollieren, dass dieses nicht in der Vergangenheit liegt. Nach der Instanziierung lässt sich der gekapselte Wert nicht mehr ändern (immutability). Die beiden Methoden _hashCode()_ und _equals()_ wurden überschrieben, um die Gleichheit zweier Objekte vom Typ _DueDate_ bei Gleicheit des gekapselten Datums zu gewährleisten.

<!-- Weitere Value Objects im Projekt? -->
Weitere Value Objects sind:

- dev.fg.dhbw.ase.tasktracker.domain.vo.Title (der Titel darf nicht _null_ oder leer sein)
- dev.fg.dhbw.ase.tasktracker.domain.vo.EMail (kapselt einen String um zu überprüfen, ob dieser dem Format einer E-Mail entspricht)
- dev.fg.dhbw.ase.tasktracker.domain.vo.Password (prüft die Anforderungen an das Passwort in der Anwendung)

#### Entities

<!--
- Was sind Entities?
- Wo habe ich Entities in meinem Projekt verwendet und warum?
- Bspw. Task oder TaskList, User?
-->

Entitäten zeichnen sich durch eine eindeutige Identität und einen eigenen Lebenszyklus aus. Entitäten aggregieren Werte (auch VOs) zu einem Ganzen und repräsentieren die, für die Domäne relevanten, Daten/"Dinge", mit denen die Applikation arbeitet. Entitäten werden in der Persistenzschicht persistiert.

Entitäten im vorliegenden Projekt sind:

- dev.fg.dhbw.ase.tasktracker.domain.entities.Task
- dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList
- dev.fg.dhbw.ase.tasktracker.domain.entities.User

Beispielsweise ist die Klasse _dev.fg.dhbw.ase.tasktracker.domain.entities.Task_ eine Entität, da sie das Kernstück der Daten darstellt mit dem ein Aufgabenplaner/eine Aufgabenliste offensichtlicher Weise arbeitet. Außerdem ist sie eindeutig durch eine ID identifizierbar und muss, um für den Anwender und die Anwendung von Nutzen zu sein, persistiert werden. Sie aggregiert dafür mehrere Daten darunter auch die VOs _dev.fg.dhbw.ase.tasktracker.domain.vo.Title_ und _dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture_. Die Daten, die von der Entität aggregiert werden, können des Weiteren vom Anwender geändert werden d.h., es existiert ein eigener Lebenszyklus.

#### Domain Services

<!--
- Was ist ein Domain Service?
- Wo und warum habe ich einen Domain Service bei mir eingesetzt?
-->

#### Aggregates

<!--
- Was ist die "Root Entity" in meinem Aggregat und warum?
- Keine direkten Abhängigkeiten! Nur Abhängigkeit über IDs!!!
- Bspw. über die TaskList komme ich zu einem bestimmten Task
- Mögliches Aggregat: TaskListAggregate(TaskList, Task)?
-->

Aggregate sind Zusammenfassungen von Entitäten oder VOs, die miteinander in Beziehung stehen (1:n, n:m oder 1:1 usw.). Dabei hat jedes Aggregat eine sog. _Aggregate Root_, über die auf Elemente des Aggregats zugegriffen werden kann. Aggregate dienen dazu Objektbeziehungen zu entkoppeln und Domänenregeln einzuhalten, indem die _Aggregate Root_ den direkten Zugriff auf, in der Beziehungshierarchie weiter unten liegende Objekte, verhindert und die Änderung von Daten kontrollieren kann.

Als Aggregate wurden gewählt das _User-Aggregat_ (enthält nur die Entität _User_) und das _Task-List-Aggregat_ (enthält die Entitäten _TaskList_ und _Task_).

![draw.io diagram](./entities_and_value_objects_3.png)

#### Repositories

<!--
- Beschreibung und Realisierung in unterschiedlichen Packages! Keine Vermischung von essential und accidental complexity!
- Nur je Aggregate Root! -> TaskListRepository, TaskListLocalRepository?, TaskListDBRepository?
-->

Ein Repository ist die Schnittstelle zwischen der Domäne und der Persistenzschicht. Für jedes _Aggregate Root_ (s.o.) wird ein Repository benötigt. In diesem Fall sind dies das _dev.fg.dhbw.ase.tasktracker.persistence.TaskListRepository_ und das _dev.fg.dhbw.ase.tasktracker.persistence.UserRepository_. Ein Repository ist zunächst nur eine Schnittstelle (interface) die aber eine Vielzahl von Ausprägungen haben kann. Wichtig ist, dass diese konkreten Ausprägungen nicht mit der Domäne vermischt werden, da diese sich nicht für die konkrete Implementierung der Persistenz interessiert. Der Domäne wird nur das Interface präsentiert. Dies hat den Vorteil, dass im Hintergrund beliebige Implementierungen genutzt und diese auch reibungslos ausgetauscht werden können. Folgende Implementierungen sind in dieser Applikation vorhanden:

- _TaskListDatabaseRepository_: Verwendet eine Datenbankverbindung (_MySQL_) und den _Object Relational Mapper_ Hibernate um Aufgabenlisten und Aufgaben zu persistieren (Laden, Ändern, Löschen).
- _TaskListFileSystemRepository_: Stellt eine Möglichkeit bereit Aufgaben und Aufgabenlisten in dem Dateisystem des Nutzers zu persistieren, falls dieser kein Benutzerkonto anlegen möchte.
- _UserDatabaseRepository_: Wie _TaskListDatabaseRepository_ aber zur Verwaltung von Nutzern in der Datenbank.

Um die _accidental complexity_ (Peristierung) von der _essential complexity_ zu trennen wird nur das Repository-Interface nach außen gegeben und alle Klassen die mit der Persistenzschicht zu tun haben sind in einem separaten Package abgelegt.

#### Factories

Factories dienen der Erzeugung von Objekten unter Einhaltung domänenspezifischer Reglementierungen. Sie sind v.a. dann nützlich, wenn diese Reglementierungen und damit die Erzeugung des Objekts sehr komplex sind. Dabei kann eine Factory eine dedizierte Klasse zur Erzeugung anderer Objekte oder eine simple Methode sein. Ein Beispiel für eine Factory ist die Klasse _dev.fg.dhbw.ase.tasktracker.domain.factories.TaskFactory_. Diese stellt einige statische Methoden zur vereinfachten Erzeugung von Objekten des Typs _Task_ bereit. Der Standardkonstruktor wird versteckt damit keine Objekte vom Typ _TaskFactory_ erzeugt werden können.

## Clean Architecture

### Schichtarchitektur planen und begründen

## Programming Principles

### Analyse und Begründung für SOLID

#### Single Responsibility Principle (SRP)

#### Open/Closed Principle (OCP)

#### Liskov Substitution Principle (LSP)

#### Interface Segregation Principle (ISP)

#### Dependency Inversion Principle (DIP)

### Analyse und Begründung für GRASP

<!--
- GRASP = General Responsibility Assignment Software Patterns
- Was ist GRASP?
- Low Coupling
- High Cohesion
- Information Expert
- Polymorphism
- Pure Fabrication
- Indirection/Delegation
- Protected Variations
- Controller
- Creator
- Jedes Prinzip/Muster erklären und ein Beispiel im Projekt angeben
- Warum wurde das Prinzip an der jeweiligen Stelle eingesetzt?
- "Das Prinizip xyz besagt, ... Dies wird z.B. in der Klasse xyz berücksichtigt, weil..."
-->

GRASP ist ein Akronym und steht für _General Responsibility Assignment Software Patterns_. GRASP vereint eine Reihe an Prinzipien und Mustern die im Folgenden näher erläutert werden sollen.

#### Low Coupling (lose Kopplung)

<!-- Was versteht man unter Low Coupling? -->

Unter dem Begriff der Kopplung versteht man den Grad der Abhängigkeit von zwei Softwarekomponenten. Ziel bei der Entwicklung von Software ist meist eine lose Kopplung d.h., dass die Abhängigkeiten zwischen den Komponenten auf ein Minimum reduziert werden sollen.

<!-- Wo habe ich in meinem Projekt Low Coupling eingesetzt und warum? -->

#### High Cohesion (hohe Kohäsion)

<!-- Was versteht man unter dem Begriff High Cohesion? -->

Unter Kohäsion versteht man den Grad von Zusammengehörigkeit innerhalb einer Softwarekomponente. Es sollte versucht werden eine möglichst hohe Kohäsion zu erreichen d.h., dass bspw. Variablen und Methoden einer Klasse inhaltlich zusammenpassen. Innerhalb einer Softwarekomponente versucht man also den Zusammenhalt der Bestandteile hoch zu halten wohingegen die Softwarekomponenten untereinander möglichst schwach aneinander gebunden sein sollten.

<!-- Wo habe ich in meinem Projekt High Cohesion eingesetzt und warum? -->

#### Information Expert (Informationsexperte)

<!-- Was versteht man unter dem Begriff Information Expert? -->

Hinter dem Experten-Prinzip verbirgt sich der Gedanke, dass eine neue Aufgabe von derjenigen Softwarekomponente übernommen werden sollte, die bereits das meiste _Wissen_ zur Erfüllung der Aufgabe besitzt. Dadurch werden beispielsweise unnötige _Hilfsklassen_ verhindert.

<!-- Wo habe ich in meinem Projekt Information Expert eingesetzt und warum? -->

#### Polymorphism (Polymorphie)

<!-- Was versteht man unter dem Begriff Polymorphism? -->

Unter dem Begriff der Polymorphie versteht man im Zusammenhang mit GRASP das Prinzip unterschiedliches Verhalten eines Typs durch Polymorphie auszudrücken.

<!-- Wo habe ich in meinem Projekt Polymorphism eingesetzt und warum? -->

#### Pure Fabrication (reine Erfindung)

<!-- Was versteht man unter dem Begriff Pure Fabrication? -->

Unter der _Pure Fabrication_ versteht man Klassen oder Module die in der Problemdomäne nicht existieren (deshalb _reine Erfindung_). Sie implementieren Methoden für die sie nicht Experte sind. Dies wird in diesem Fall toleriert, da somit eine Trennung zwischen Technologiewissen und Domänenwissen stattfinden kann. Allerdings sollte mit _reinen Erfindungen_ sehr sparsam umgegangen werden (nur wenn es wirklich notwendig ist).

<!-- Wo habe ich in meinem Projekt Pure Fabrication eingesetzt und warum? -->

#### Indirection/Delegation (Indirektion/Delegation)

<!-- Was versteht man unter dem Begriff Indirection/Delegation? -->

Mit Indirektion/Delegation ist gemeint, dass ein Objekt Aufgaben an ein weiteres Objekt delegiert. Das Objekt zu dem deligiert wird ist meist besser zur Erfüllung der Aufgabe geeignet (kann bspw. Experte sein). Das Prinzip kann Vererbung ersetzen da die Funktionsweise analog ist zu einer Kindklasse die Methodenaufrufe zu ihrer Elternklasse delegiert. Außerdem kann dadurch eine hohe Kohäsion gefördert werden, da inhaltlich zusammengehörige Betsandteile in Klassen ausgelagert und die Kommunikation über Delegation geregelt werden kann.

<!-- Wo habe ich in meinem Projekt Indirection/Delegation eingesetzt und warum? -->

#### Protected Variations (geschützte Veränderungen)

<!-- Was versteht man unter dem Begriff Protected Variations? -->

Das Prinzip der geschützten Veränderungen besagt, dass konkrete Implementierungen mit Hilfe von Interfaces _versteckt_ werden sollten. Die Softwarekomponenten greifen nur auf das Interface zu. Dadurch können im Hintergund die Implementierungen ausgetauscht oder angepasst werden ohne, dass dies Auswirkungen auf das restliche System hat. Das System ist also _geschützt vor Veränderungen_.

<!-- Wo habe ich in meinem Projekt Protected Variations eingesetzt und warum? -->

In dem Package _dev.fg.dhbw.ase.tasktracker.persistence_ befinden sich die Klassen zur Persistierung und Bereitstellung von Daten mit denen die Anwendung arbeitet. Für jede Aggregate-Root existiert ein Repository welches die Schnittstellen für den domänenspezifischen Teil der Anwendung definiert. Innerhalb diesen Teils der Anwendung wird nur mit dem Interface (bzw. dem Repository) gearbeitet, sodass die Implementierungsdetails der Persistenzschicht für die Domäne unsichtbar bleiben und beliebig ausgetauscht werden können. Dadurch bleibt die Domäne nicht nur von _Accidental Complexity_ befreit sondern es wird auch das _Protected Variations Pattern_ unterstützt, da durch die Abstraktion der Implementierung mit Hilfe von Interfaces die Domäne vor Veränderungen geschützt bleibt.

#### Controller (Steuereinheit)

<!-- Was versteht man unter dem Begriff Controller? -->

Ein Controller stell die erste Instanz nach dem GUI dar. Er nimmt Events aus der Benutzerschnittstelle entgegen und delegiert diese an andere Klassen, welche die Events verarbeiten können. Der Controller beinhaltet das Domänenwissen und sollte möglichst wenig selbst tun und stattdessen Aufgaben delegieren.

<!-- Wo habe ich in meinem Projekt Controller eingesetzt und warum? -->

Das in diesem Projekt eingesetzte UI-Framework _JavaFX_ unterstützt die Verwendung von Controllern standardmäßig. Dabei kann eine beliebige Java-Klasse als Controller auf einem Root-Element der UI gesetzt werden. Die UI kann vollständig vom Code entkoppelt und im XML-Format in sog. FXML-Dateien ausgelagert werden (s. _/main/resources/fxml/_). Beispiele für Controller-Klassen sind im Package _dev.fg.dhbw.ase.tasktracker.domain.controller_ zu finden. Beispielsweise wird der _ListViewController_ als Controller-Klasse auf dem Root-Element der _ListView.fxml_ gesetzt. Durch Annotationen können Elemente aus FXML-Dateien im Code zugegriffen und Events verarbeitet werden. Dadurch sind alle Controller-Klasse die erste Instanz nach der UI und die Logik liegt im Controller und ist von der UI entkoppelt.

#### Creator (Erzeuger)

<!-- Was versteht man unter dem Begriff Creator? -->

Das Erzeuger-Prinzip definiert Regeln die vorgeben wer für die Erzeugung von Instanzen zuständig sein darf. Klasse A darf eine Instanz von Klasse B erzeugen, wenn:

- A eine Aggregation von B ist oder Objekte von B enthält
- A Objekte von B verarbeitet
- A von B abhängt (starke Kopplung)
- A der Informationsträger/-experte für die Erzeugung von B ist (bspw. Factory)

<!-- Wo habe ich in meinem Projekt Creator eingesetzt und warum? -->

### Analyse und Begründung für DRY

<!--
- DRY = Don't Repeat Yourself
- Was bedeutet das?
- An welchen Stellen habe ich DRY angewendet und warum?
-->

## Refactoring

### Code Smells identifizieren

<!-- 
- Code Smells können im aktuellen Code existieren oder können im Laufe der Entwicklung beseitigt worden sein (dann Verweis auf Commit hinzufügen)
- An in der Vorlesung genannten Code Smells oder https://refactoring.guru/refactoring/smells orientieren
- Keine schwachen Code Smells wie "unused import", "unused variable", "parameter could be final" etc.
- Mindestens 4 Stück!!!
 -->

#### Code Smell 1: TODO

#### Code Smell 2: TODO

#### Code Smell 3: TODO

#### Code Smell 4: TODO

### Begründung durchgeführter Refactorings

<!--
- Welche Art von Refactoring habe ich durchgeführt?
- Wo habe ich das Refactoring durchgeführt? (Stellen im Code und Commits angeben)
- Warum habe ich das Refactoring durchgeführt?
- Evtl. Screenshots vom Code?
- An in der Vorlesung genannten Refactoring oder  https://refactoring.guru/refactoring/techniques orientieren
-->

#### Refactoring 1: TODO

#### Refactoring 2: TODO

## Entwurfsmuster

### Observer

Das Observer-Pattern gehört zur Klasse der Benachrichtigungsmuster. Es ermöglicht den Austausch von Nachrichten zwischen Softwarekomponenten ohne, dass eine starke Kopplung zwischen den Komponenten entsteht. Beim Observer-Pattern existieren zwei Arten von Entitäten: Observer und Observables. Ein Observable ist eine Komponente, die von einem Observer _beobachtet_ werden kann. Ein Observer ist eine Komponente, die sich bei einem Observable registrieren und anschließend Nachrichten von dem Observable erhalten kann (s. _dev.fg.dhbw.ase.tasktracker.domain.observer.Observer_ und _dev.fg.dhbw.ase.tasktracker.domain.observer.Observable_).

### Einsatz begründen

Das Observer-Pattern wird in diesem Projekt genutzt um einen Großteil der Kommunikation zwischen den einzelnen Komponenten des User Interfaces zu gestalten. So implementiert bspw. die Klasse _dev.fg.dhbw.ase.tasktracker.domain.components.TaskComponent_ das Observable-Interface um registierte Observer bei dem Löschen einer Aufgabe oder dem Markieren einer Aufgabe als abgeschlossen zu benachrichtigen. Die Klasse repräsentiert die UI-Komponente einer Aufgabe innerhalb einer Liste. Die Klasse _dev.fg.dhbw.ase.tasktracker.domain.controller.ListViewController_ implementiert das Observer-Interface und registriert sich bei einem _TaskComponent_, um über diese Events benachrichtigt zu werden und das UI zu aktualisieren. Eine ähnliche Vorgehensweise wurde auch bei den anderen UI-Komponenten gewählt. Der Aufzählungstyp _dev.fg.dhbw.ase.tasktracker.domain.components.ComponentEvent_ definiert zudem die Arten der Events die auftreten können. Die Observables definieren dort die Events die sie an die Observer verteilen. Dadurch kann der Observer auf die unterschiedlichen Events verschieden reagieren.
