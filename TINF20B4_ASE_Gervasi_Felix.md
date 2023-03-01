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

Ein Fälligkeitsdatum kann auf einer Aufgabe gesetzt werden. Es ist darauf zu achten, dass das Fälligkeitsdatum nicht in der Vergangenheit liegen darf. Ist das Fälligkeitsdatum auf einer Aufgabe erreicht muss dies in der Aufgabe gekennzeichnet werden. Als Fälligkeitsdatum werden nur Jahr, Monat und Tag betrachtet.

Aufgabenliste (Task List):

Eine Aufgabenliste kann mehrere (theoretisch unbegrenzt viele) Aufgaben beinhalten. Sie kann aber auch keine Aufgabe beinhalten. Aufgabenlisten können vom Benutzer erstellt werden. Dabei wird ein Name vom Benutzer festgelegt, welcher über alle Listen eindeutig sein muss. Die Bestandteile einer Aufgabenliste sind folglich ihr Name und eine Reihe von Aufgaben.

Arbeitsschritt (Sub Task):

Ein Arbeitsschritt kann selbst wieder als Aufgabe verstanden werden, mit dem Unterschied, dass ein Arbeitsschritt keine weiteren Arbeitsschritte enthalten darf.

Erinnerung (Reminder):

Eine Erinnerung kann auf einer Aufgabe gesetzt werden. Es ist erneut darauf zu achten, dass das Erinnerungsdatum nicht in der Vergangenheit liegt. Ist das Erinnerungsdatum auf einer Aufgabe erreicht, muss der Benutzer darüber in Kenntnis gesetzt werden. Als Erinnerungsdatum werden nur Jahr, Monat und Tag betrachtet.

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

<!-- Was versteht man unter dem Begriff "Clean Architecture"? -->

Das Ziel von _Clean Architecture_ ist es, Software so zu gestalten, dass sie über einen langen Zeitraum bestehen bleiben kann. Dies wird unter anderem dadurch erreicht, dass eine klare Trennung zwischen technologieabhängigen und technologieunabhängigen Komponenten stattfindet. Die Software muss so aufgebaut sein, dass die zugrundeliegenden Technologien ausgetauscht werden können ohne, dass der _langlebige Kern_ der Anwendung verändert werden muss.

### Schichtarchitektur planen und begründen

In der _Clean Architecture_ wird eine Software in mehrere Schichten eingeteilt. Dabei ist es wichtig darauf zu achten, dass Abhängigkeiten zwischen den Schichten immer nur von außen nach innen gehen (innere Schichten wissen nichts von den Äußeren).

<!--
- Welche Schichten umfasst meine Anwendung und warum?
- Welche Aufgaben erfüllt die jeweilige Schicht?
- 2 Schichten im Code umsetzen
-->

In dem vorliegenden Projekt lassen sich theoretisch 5 Schichten unterscheiden. Diese sind (von außen nach innen):

1. Plugins: In der Plugin-Schicht liegt das _User Interface_ und sonstige dafür benötigte Ressourcen. Außerdem befindet sich sonstiger Code dort, der direkte Abhängigkeiten zu externen Bibliotheken oder Frameworks besitzt.
2. Adapters: Die Adapter-Schicht enthält zu großen Teilen _Mapping-Code_ der die Daten aus der Plugin-Schicht in ein Format umwandelt, welches von der Applikationsschicht verstanden wird und umgekehrt. Diese Schicht kann oft (zumindest am Anfang) weggelassen werden, da sich die Datenmodelle nicht groß unterscheiden.
3. Application Code: Diese Schicht enthält Code der im direkten Zusammenhang zu den Anwendungsfällen und Anforderungen der vorliegenden Applikation steht. Er fasst Elemente aus dem Domänen-Code zusammen und verwendet diese zur Umsetzung der Anwendungsfälle (benutzt die Entitäten und VOs). Änderungen an dieser Schicht dürfen nicht den Domänencode beeinflussen und Änderungen an der Datenbank oder der graphischen Benutzeroberfläche nicht den _Application Code_. Den _Use Case_ des _Application Code_ darf nicht interessieren wer ihn aufgerufen hat und wie das Ergebnis das er zurückliefert präsentiert wird. Solche _Use Cases_ sind im folgenden Projekt bspw.: Aufgaben einer Liste abrufen, eine neue Aufgabe erstellen, eine neue Liste erstellen, eine Aufgabe löschen, eine Liste löschen, Statistiken abrufen...
4. Domain Code: Wie der Name bereits andeutet enthält diese Schicht den Code, der direkt mit der Problemdomäne im Zusammenhang steht. Wie bereits in dem Abschnitt über das _Domain Driven Design_ festgestellt wurde sind dies im wesentlichen die Entitäten und _Value Objects_, die bereits in den Abschnitten _Entities_ und _Value Objects_ unter der Überschrift _Analyse und Begründung der verwendeten Muster_ herausgearbeitet wurden.
5. Abstraction Code: Diese Schicht enthält domänenübergreifendes Wissen und Funktionalitäten die in vielen oder allen anderen Schichten benötigt werden. Sie abstrahiert bspw. vom Code (bzw. den Abhängigkeiten auf Code) der verwendeten Bibliotheken wie bspw. der verwendeten Bibliothek zur Umsetzung der Persistenz. Für das vorliegende Projekt wäre eine geeignete Abhängigkeit für diese Schicht das JUnit Test-Framework. Oft kann die Schicht aber auch eingespart werden.

<!-- 
- Wo finden sich zumindest 2 der oben beschriebenen Schichten in meinem Code wieder?
- Evtl. kann ich mit den "Schichten" persistence und domain argumentieren
- Dafür müssen aber auch Dinge geändert werden (im Domänen-Paket sollten eigentlich nur die Entitäten und VOs drinnen liegen)  
-->

Zunächst wurde das _TaskTracker_-Projekt ohne Bachtung der Regeln der _Clean Architecture_ entwickelt. Das alte Projekt liegt im Ordner _./tasktracker/_ vor. Das neue modularisierte Projekt befindet sich im Ordner _./tasktracker-modules/_. Die Schichten wurden als einzelne Maven-Module umgesetzt. Das Elternprojekt _tasktracker-parent_ enthält die folgenden Module:

- _tasktracker-abstraction_: Repräsentiert die Abstraktionsschicht und definiert Abhängigkeiten zu der _javax.persistence_ API sowie dem _JUnit_-Test-Framework. Diese Abhängigkeiten befinden sich in der Abstraktionsschicht, da sie öfters im Projekt verwendet werden müssen und über einen langen Zeitraum besetehn bleiben werden. Außerdem befinden sich in der Schicht die Klassen zur Umsetzung des Beobachtermusters. Hier gilt dasselbe: der Code wird sich nur sehr selten ändern und sollte aber innerhalb des gesamten Projektes nutzbar sein.
- _tasktracker-domain_: In der Domänenschicht befinden sich die Entitäten und VOs, da diese aus der Domäne und während des DDD-Prozesses entstanden sind und die dort geltenden Regeln einhalten müssen. Ebenfalls sind dort die Repositories für die vorhandenen Aggregate untergebracht, da die Verwaltung der Entitäten ebenfalls zur Problemdomäne gehört.
- _tasktracker-application_: Die Applikationsschicht enthält Klassen und Methoden zur Umsetzung der Use-Cases. Sie verwendet dafür die Repositories, Entitäten und VOs aus der darunterliegenden Schicht. In diesem Projekt wurden Use-Cases unterschieden die Aufgabenlisten, Aufgaben oder den Benutzer betreffen und diese jeweils in einer Klasse zusammengefasst. Die von den Klassen beereitgestellte Methoden werden von der Plugin-/Adapter-Schicht verwendet.
- _tasktracker-adapters_: Die Adapterschicht wurde in diesem Projekt ausgespart. Hier würde der nötige Mapping-Code liegen, um vom Datenmodell der Plugin-Schicht auf das Datenmodell der Applikationsschicht abzubilden.
- _tasktracker-plugins_: Die Plugin-Schicht ist die am Weitesten außen liegende Schicht deren Code sich am häufigsten ändert, da er am konkretesten ist und direkte Abhängigkeiten zu externen Bibliotheken und Frameworks besitzt. Hier liegen alle UI-Elemente und die dafür benötigten Ressourcen (in diesem Fall abhängig von JavaFX). Außerdem werden die Repository-Interfaces durch konkrete Implementierungen ersetzt.

## Programming Principles

### Analyse und Begründung für SOLID

SOLID ist ein Akronym welches mehrere Programmierparadigmen unter sich vereint, die im Folgenden vorgstellt und analysiert werden sollen.

#### Single Responsibility Principle (SRP)

<!-- Was ist SRP? -->

Das SRP besagt, dass eine Softwarekomponente nur eine einzige bestimmte Aufgabe haben sollte für die sie zuständig ist. Das SRP lässt sich auf mehreren Ebenen anwenden:

- Module
- Klassen
- Methoden
- Variablen

<!-- Wo habe ich SRP eingesetzt? -->

Ein Beispiel für den Einsatz von SRP stellt die Methode `initialize()` dar, die sich in der Klasse `dev.fg.dhbw.ase.tasktracker.domain.controller.ListViewController` befindet. Die Methode wird von dem JavaFX-Framework automatisch aufgerufen, sobald alle mit der Annotation `@FXML` markierten Felder _befüllt_ wurden und erfüllt damit eine ähnliche Funktion wie der Konstruktor. Die Befehle die innerhalb der Methode ausgeführt werden lassen sich in drei Aufgaben unterteilen:

1. Vorbereitung der UI
2. Erstellen der, von der Applikation verwalteten, Liste zur Speicherung abgeschlossener Aufgaben, falls diese nicht vorhanden ist
3. Abrufen aller vorhandenen Aufgabenlisten für den angemeldeten Benutzer und deren Darstellung in der UI

Das SRP fordert, dass diese Aufgaben in einzelne Methoden ausgelagert werden (oder falls dies angebracht ist auch in separate Klassen). Deshalb wurde für die Vorbereitung der UI die Methode `prepareUI` eingeführt. Anschließend wird die Methode `List<TaskList> getTaskListsForUser()` aufgerufen, die die Liste für die abgeschlossenen Aufgaben automatisch hinzufügt falls sie noch nicht vorhanden ist. Der Rückgabewert der Methode wird anschließend verwendet um die UI zu aktualisieren. (vgl. [ListViewController](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/56f652c0950a6215d79719b8ee5f6889679fd951/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/controller/ListViewController.java))

#### Open/Closed Principle (OCP)

<!-- Was ist OCP? -->

Das OCP besagt, dass Module offen für Erweiterungen aber geschlossen für Änderungen sein sollten. Mit anderen Worten sollte eine Software so designt sein, dass sich leicht neue Features hinzufügen lassen ohne große Änderungen an (öffentlichen) Schnittstellen vornehmen zu müssen, da dies zu weiteren Änderungen an allen möglichen Stellen in der Software führt.

<!-- Wo habe ich OCP eingesetzt? -->

TODO

#### Liskov Substitution Principle (LSP)

<!-- Was ist LSP? -->

Das LSP besagt, dass eine Klasse an jeder beliebigen Stelle durch ihre Basisklasse ersetzt werden können muss ohne, dass es zu unerwünschten Nebeneffekten kommt.

<!-- Wo habe ich LSP eingesetzt? -->

TODO

#### Interface Segregation Principle (ISP)

<!-- Was ist ISP? -->

Das LSP besagt, dass es besser ist viele client-spezifische Interfaces zu haben als ein allgemeines, welches alle Clientanforerdungen in sich vereint. Dadurch lässt sich die Anwendung unter Umständen leichter erweitern und es wird verhindert, dass viele leere Implementierungen bei Clients entstehen, die nur einen Bruchteil der Funktionalität des allgemeinen Interfaces brauchen.

<!-- Wo habe ich ISP eingesetzt? -->

TODO

#### Dependency Inversion Principle (DIP)

<!-- Was ist DIP? -->

Module auf höherer Ebene sollten keine Abhängigkeiten zu Modulen auf niedrigerer Ebene aufweisen. Stattdessen sollte eine Abstraktion dazwischen geschaltet werden von denen beide Ebenen abhängen. Abstraktionen sollten wiederum unabhängig von Details sein. Stattdessen sollte die Details von der Abstaktion abhängen.

<!-- Wo habe ich DIP eingesetzt? -->

Dieses Prinzip wurde durch die Einhaltung geltender Regeln der _Clean Architecture_ umgesetzt. Die Anwendung ist in fünf Schichten eingeteilt. Die Abhängigkeiten der einzelnen Schichten gehen ausschließlich von den äußeren (niedrigere Ebene) zu den inneren (höhere Ebene) Schichten.

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

_Low Coupling_ kann bspw. durch den Einsatz von Interfaces erreicht werden, da diese von der eigentlichen Implementierung abstrahieren und die Komponenten, welche die definierte Schnittstelle nutzen, von der eigentlichen Implementierung entkoppelt werden (lose Kopplung). Dies ist bspw. durch den Einsatz von _Repositories_ an diesem Projekt zu sehen (s. `dev.fg.dhbw.ase.tasktracker.persistence`).

#### High Cohesion (hohe Kohäsion)

<!-- Was versteht man unter dem Begriff High Cohesion? -->

Unter Kohäsion versteht man den Grad von Zusammengehörigkeit innerhalb einer Softwarekomponente. Es sollte versucht werden eine möglichst hohe Kohäsion zu erreichen d.h., dass bspw. Variablen und Methoden einer Klasse inhaltlich zusammenpassen. Innerhalb einer Softwarekomponente versucht man also den Zusammenhalt der Bestandteile hoch zu halten wohingegen die Softwarekomponenten untereinander möglichst schwach aneinander gebunden sein sollten.

<!-- 
- Wo habe ich in meinem Projekt High Cohesion eingesetzt und warum? 
- Bspw. Auslagern der einzelnen UI-Komponenten in separate Klassen
- Idee: jede UI-Komponente verwaltet sich selbst und arbeitet nur mit den Daten die sie wirklich benötigt
- Man hätte auch alles in den ListViewController packen können (geringe Kohäsion)
-->

In dem vorliegenden Projekt wurde hohe Kohäsion bspw. dadurch gefördert, dass die einzelnen UI-Komponenten weitestgehend in separate Klassen ausgelagert wurden (s. das Paket `dev.fg.dhbw.ase.tasktracker.domain.components`). Die Idee hierbei ist, dass jede UI-Komponente sich selbst verwaltet und nur mit den Daten arbeitet, die sie wirklich benötigt. Zusammengehörige Variablen und Methoden werden somit in separate Klassen abgekapselt was zu einer erhöhten Kohäsion führt.

#### Information Expert (Informationsexperte)

<!-- Was versteht man unter dem Begriff Information Expert? -->

Hinter dem Experten-Prinzip verbirgt sich der Gedanke, dass eine neue Aufgabe von derjenigen Softwarekomponente übernommen werden sollte, die bereits das meiste _Wissen_ zur Erfüllung der Aufgabe besitzt. Dadurch werden beispielsweise unnötige _Hilfsklassen_ verhindert.

<!-- Wo habe ich in meinem Projekt Information Expert eingesetzt und warum? -->

Das Prinzip des Informationsexperten wurde bspw. bei der Methode `formatDate(DateInFuture): String` eingesetzt die vormals in der Klasse `dev.fg.dhbw.ase.tasktracker.domain.components.TaskComponent` vorlag. Anstatt eine _unnötige_ Hilfsklasse wie bspw. `DateUtils` o.ä. einzuführen wurde versucht die Methode in diejenige Klasse zu verlagern, die zu der vorliegenden Thematik/dem vorliegenden Domänenproblem passt. Deshalb wurde die Methode zu einem späteren Zeitpunkt in die `DateInFuture` Klasse selbst ausgelagert. (vgl. [vorher](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/cf1d3385f2358d5ebbc432a7c9c693434913e5ca/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/components/TaskComponent.java), [nachher](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/b387a18199f501df227210e1dd319e0bc9cc9243/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/vo/DateInFuture.java))

#### Polymorphism (Polymorphie)

<!-- Was versteht man unter dem Begriff Polymorphism? -->

Unter dem Begriff der Polymorphie versteht man im Zusammenhang mit GRASP das Prinzip unterschiedliches Verhalten eines Typs durch Polymorphie auszudrücken.

<!-- Wo habe ich in meinem Projekt Polymorphism eingesetzt und warum? -->

In diesem Projekt kommt Polymorphie bspw. zur Unterscheidung von offenen und abgeschlossenen Aufgaben zum Einsatz (s. `dev.fg.dhbw.ase.tasktracker.domain.components.FinishedTaskComponent` und `dev.fg.dhbw.ase.tasktracker.domain.components.OpenTaskComponent`).

#### Pure Fabrication (reine Erfindung)

<!-- Was versteht man unter dem Begriff Pure Fabrication? -->

Unter der _Pure Fabrication_ versteht man Klassen oder Module die in der Problemdomäne nicht existieren (deshalb _reine Erfindung_). Sie implementieren Methoden für die sie nicht Experte sind. Dies wird in diesem Fall toleriert, da somit eine Trennung zwischen Technologiewissen und Domänenwissen stattfinden kann. Allerdings sollte mit _reinen Erfindungen_ sehr sparsam umgegangen werden (nur wenn es wirklich notwendig ist).

<!-- Wo habe ich in meinem Projekt Pure Fabrication eingesetzt und warum? -->

Ein Beispiel für eine reine Erfindung ist die Klasse `dev.fg.dhbw.ase.tasktracker.persistence.TaskListDatabaseRepository`. Es handelt sich dabei um _Pure Fabrication_, da die Klasse kein Objekt aus der realen Welt bzw. der Problemdomäne darstellt.

#### Indirection/Delegation (Indirektion/Delegation)

<!-- Was versteht man unter dem Begriff Indirection/Delegation? -->

Mit Indirektion/Delegation ist gemeint, dass ein Objekt Aufgaben an ein weiteres Objekt delegiert. Das Objekt zu dem deligiert wird ist meist besser zur Erfüllung der Aufgabe geeignet (kann bspw. Experte sein). Das Prinzip kann Vererbung ersetzen da die Funktionsweise analog ist zu einer Kindklasse die Methodenaufrufe zu ihrer Elternklasse delegiert. Außerdem kann dadurch eine hohe Kohäsion gefördert werden, da inhaltlich zusammengehörige Betsandteile in Klassen ausgelagert und die Kommunikation über Delegation geregelt werden kann.

<!-- Wo habe ich in meinem Projekt Indirection/Delegation eingesetzt und warum? -->

TODO

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

Beispielsweise erzeugt in diesem Projekt die Klasse `dev.fg.dhbw.ase.tasktracker.domain.controller.ListViewController` Objekte vom Typ `...domain.components.TaskListComponent`. Dies ist nach dem Erzeugerprinzip damit zu rechtfertigen, da die Klasse `ListViewController` eine klare semantische Verbindung zu `TaskListComponent` hat und Objekte diesen Typs verarbeiten muss.

### Analyse und Begründung für DRY

<!--
- DRY = Don't Repeat Yourself
- Was bedeutet das?
- An welchen Stellen habe ich DRY angewendet und warum?
-->

DRY ist ein Akronym und steht für _Don't Repeat Yourself_ (z. Dt. wiederhole dich nicht). Die Idee ist hierbei, dass Redundanzen im Code vermieden werden sollten, d.h. bspw. die selbe Logik nicht mehr als einmal im Code implementiert wird.

<!-- Wo habe ich DRY eingesetzt? -->

DRY wurde in diesem Projekt beispielsweise bei der Umstellung des Observable-Interfaces auf eine Klasse eingesetzt. Durch die Umsetzung von Observable als Interface musste jede Implementierung alle Methoden implementieren obwohl deren Implementierung eigentlich immer gleich ist (Observer der Liste hinzufügen, Observer aus der Liste entfernen usw.). Durch die Umsetzung als vollwertige Klasse ist dies nicht mehr der Fall und es können Wiederholungen aus dem Code entfernt werden. (vgl. [Beispielimplementierung vorher](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/1487e48378322f1b1de5bc58b03e270994eb227b/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/controller/AddTaskFormController.java) und [Umsetzung als Klasse](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/0e7d260ef6bd4113676d88797c2ac35f5c6200de/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/observer/Observable.java))

## Refactoring

### Code Smells identifizieren

<!-- 
- Code Smells können im aktuellen Code existieren oder können im Laufe der Entwicklung beseitigt worden sein (dann Verweis auf Commit hinzufügen)
- An in der Vorlesung genannten Code Smells oder https://refactoring.guru/refactoring/smells orientieren
- Keine schwachen Code Smells wie "unused import", "unused variable", "parameter could be final" etc.
- Mindestens 4 Stück!!!
 -->

#### Code Smell 1: Large Class

<!-- 
- e.g. ListViewController 
- um was für eine Art von Smell handelt es sich?
- woran erkennt man ihn?
- was ist schlecht daran?
-->

Dieser Code Smell gehört zur Klasse der _Bloaters_. Diese Klasse fasst Code Smells zusammen, die dafür sorgen, dass der Code an manchen Stellen gigantische Ausmaße annimmt und dadurch schwer zu Pflegen ist. Der Smell _Large Class_ bezieht sich dabei speziell auf besonders große Klassen. Ein Beispiel ist die Klasse `dev.fg.dhbw.ase.tasktracker.domain.controller.ListViewController`, die zum aktuellen Zeitpunkt fast 300 Zeilen umfasst. Die Größe wird sich wahrscheinlich noch weiter verschlimmern, da der `ListViewController` momentan die Hauptaufgaben der Anwendung erfüllt (Listen und Aufgaben anzeigen, erstellen, löschen, ...) und voraussichtlich immer mehr Features hinzugefügt werden.

<!-- Wie könnte man den Smell lösen -->

Der Code Smell kann gelöst werden indem die Refactorings _Extract Class_, _Extract Subclass_ oder _Extract Interface_ angewendet werden. Dabei wird Funktionalität aus einer großen Klasse in andere Klassen ausgelagert. (vgl. [ListViewController](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/fed9e8343b77b490c5c0a9bcccd90bae49094f91/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/controller/ListViewController.java))

#### Code Smell 2: Switch Statements

<!-- 
- e.g. ListViewController.notifyObserver(Object)
- um was für eine Art von Smell handelt es sich?
- woran erkennt man ihn?
- was ist schlecht daran?
-->

Dieser Code Smell gehört zur Klasse der _Object-Orientation Abusers_. Diese Klasse fasst Code Smells zusammen, welche eine unvollständige oder falsche Anwendung von objektorientierten Programmierprinzipien darstellen. Der Smell _Switch Statements_ bezieht sich auf das Vorkommen komplexer `switch`-Statements sowie Abfolgen von `if`-Statements. Ein Beispiel für eine solche Abfolge findet sich in der Klasse `dev.fg.dhbw.ase.tasktracker.domain.controller.ListViewController`. Die Methode `notifyObserver(Object)` führt eine Reihe von Prüfungen durch, die die Art des Events feststellen sollen. Dies führt zu einer überaus langen und unübersichtlichen Methode, die mit der Zeit immer weiter anwächst, falls neue Events dazu kommen. Kommt ein neues Event hinzu muss außerdem die Methode um ein weiteres `if`-Statement erweitert werden. Der Code ist also gegenüber Erweiterungen sehr unflexibel.

<!-- Wie könnte man den Smell lösen -->

Eine Mögliche Lösung für das Problem ist der Einsatz von Polymorphie. Durch die Einführung einer neuen Klasse für jedes Event und einem gemeinsamen Interface könnte der gesamte Körper der Methode durch einen einzigen Methodenaufruf ersetzt werden. Dafür würde jede Event-Klasse das Event-Interface implementieren. Dieses würde dann stellvertretend für die einzelnen Events als Übergabeparameter verwendet werden (`notifyObserver(IEvent)`). Anschließend kann auf dem Interface die Methode aufgerufen werden, die von jeder Event-Klasse implementiert wird. (vgl. [switch-statements](https://refactoring.guru/smells/switch-statements), [oo-abusers](https://refactoring.guru/refactoring/smells/oo-abusers) und [ListViewController](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/598ea36ac2cfd159b2904be4c7b7bb61a487b58b/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/controller/ListViewController.java))

#### Code Smell 3: Long Method

<!-- 
- e.g. ListViewController
- um was für eine Art von Smell handelt es sich?
- woran erkennt man ihn?
- was ist schlecht daran?
-->

Dieser Code Smell gehört ebenfalls zur Klasse der _Bloaters_ und bezieht sich auf besonders lange Methoden mit vielen Codezeilen. Als Daumenregel gilt, dass eine Methode nicht mehr als zehn Zeilen Code enthalten sollte. Eine sehr lange Methode legt nahe, dass diese Methode eine Reihe verschiedener Aufgaben übernimmt, die eigentlich in separate Methoden ausgelagert werden sollten. Beispielsweise wird auch das _Single Responsibility Principle_ dadurch verletzt. Beispiele für lange Methoden finden sich aktuell in der Klasse `dev.fg.dhbw.ase.tasktracker.domain.controller.ListViewController`. Diese Klasse enthält leider viele lange und komplexe Methoden, die teilweise mehrere Aufgaben übernehmen. Als konkretes Beispiel kann wieder die Methode `notifyObserver(Object)` herangezogen werden, die außerdem Gefahr läuft immer weiter anzuwachsen. Diese hat 33 Zeilen also mehr als dreimal so viel wie im optimalen Fall.

<!-- Wie könnte man den Smell lösen -->

Code Smells dieser Art lassen sich durch das extrahieren von Methoden lösen. Dabei werden die Codezeilen, die zusammen eine bestimmte Aufgabe erfüllen, in eine separate Methode ausgelagert. Dadurch entstehen viele kleine, leicht wartbare und verständliche Methoden und die Größe der ursprünglichen Methode wird deutlich verkleinert. (vgl. [bloaters](https://refactoring.guru/refactoring/smells/bloaters), [long-method](https://refactoring.guru/smells/long-method) und [ListViewController](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/598ea36ac2cfd159b2904be4c7b7bb61a487b58b/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/controller/ListViewController.java))

#### Code Smell 4: Verletzung des Informationsexperten-Prinzips

Neue Features sollten immer in der Klasse hinzugefügt werden, die bereits die meisten Informationen für dieses Feature beinhaltet. Dadurch wird dafür gesorgt, dass zusammengehörige Informationen zusammen bleiben und unnötige Hilfsklassen werden verhindert. Eine Verletzung dieses Prinzips (_Smell_) fand in der Methode `TaskComponent.formatDate(DateInFuture)` statt. Zwar wurde eine unnötige Hilfsklasse in diesem Fall ausgespart - dennoch ist das Formatieren eines Datums naheliegenderweise die Aufgabe des Datums selbst und nicht die einer bestimmten UI-Komponente. Durch das anschließende Verlagern wird nicht nur das Informationsexperten-Prinzip eingehalten sondern auch dafür gesorgt, dass die allgemeine und evtl. häufiger benötigte Aufgabe der Datumsformatierung nun für andere Komponenten die das `DateInFuture`-Objekt benutzen verwendbar ist. (vgl. [vorher](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/cf1d3385f2358d5ebbc432a7c9c693434913e5ca/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/components/TaskComponent.java), [nachher](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/b387a18199f501df227210e1dd319e0bc9cc9243/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/vo/DateInFuture.java))

### Begründung durchgeführter Refactorings

<!--
- Welche Art von Refactoring habe ich durchgeführt?
- Wo habe ich das Refactoring durchgeführt? (Stellen im Code und Commits angeben)
- Warum habe ich das Refactoring durchgeführt?
- Evtl. Screenshots vom Code?
- An in der Vorlesung genannten Refactoring oder  https://refactoring.guru/refactoring/techniques orientieren
-->

#### Refactoring 1: Extract Method

<!-- 
- e.g. ListViewController.onAddTaskButtonClicked(Event)
- zu welcher Klasse gehört das Refactoring?
- was soll dabei erreicht werden?
-->

Das Refactoring _Extract Method_ gehört zur Kategorie _Composing Methods_. Diese fasst Refactorings zusammen, die dafür sorgen den Einsatz von Methoden zu verbessern, indem die Methodenlänge reduziert und der Codeduplizierung entgegengewirkt wird. _Extract Method_ bezieht sich im speziellen auf das Aufteilen einer Methode in viele kleine Methoden. Dabei werden Code-Fragmente die inhaltlich zusammengehören in eine separate Methode _extrahiert_. Dadurch wird der Code lesbarer und leichter verständlich, da die ursprüngliche Länge der Methode verringert wird und die neuen Methoden (zumindest sollte dies der Fall sein) mit sprechenden Namen versehen werden aus denen ihre Funktion hervorgeht.

<!-- Wo habe ich dieses Refactoring eingesetzt und warum? -->

Ein Beispiel für eine Methode bei der das Refactoring _Extract Method_ eingesetzt werden kann ist `onAddTaskButtonClicked(Event)` in der Klasse `dev.fg.dhbw.ase.tasktracker.domain.controller.ListViewController`. Diese ist zum einen sehr lang und zum anderen erfüllt sie mehrere Aufgaben in einem, die stattdessen in separate Methoden ausgelagert werden könnten. Diese Aufgaben lassen sich wie folgt zusammenfassen:

1. Prüfe ob aktuell eine Liste ausgewählt ist und ob es sich dabei um eine intern verwaltete Liste handelt (d.h. um die Liste zur Speicherung abgeschlossener Aufgaben oder die Überschrift, die direkt nach dem Start der Applikation angezeigt wird)
2. Öffne das Fenster mit dem Formular zum Hinzufügen einer Aufgabe zu einer Liste

Die beiden Aufgaben wurden in die Methoden `selectedListNameIsInvalid()` und `openAddTaskWindow()` extrahiert. (vgl. [composing-methods](https://refactoring.guru/refactoring/techniques/composing-methods), [extract-method](https://refactoring.guru/extract-method), [vorher](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/120ca8f1a4cf48b493e3225fb7d0b6a4bee42ef2/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/controller/ListViewController.java) und [nachher](https://github.com/fgervasi-cell/Programmentwurf_ASE/blob/40f0ac935823d35f653eb7783b37a4445759ccc9/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/controller/ListViewController.java))

#### Refactoring 2: Extract Class und Extract Superclass

<!-- Um was für Refactorings handelt es sich? -->

Bei dem _Extract Class_ Refactoring geht es um das Extrahieren von Funktionalität (Variablen und Methoden) einer Klasse in eine andere Klasse, d.h. aus einer Klasse werden zwei Klassen. Dies trägt zu einer erhöhten Modularität, Übersichtlichkeit und der Einhaltung des SRP bei. Das Refactoring kann dann sinnvoll eingesetzt werden, wenn eine Klasse mehrere Aufgaben übernimmt, die eigentlich besser von separaten Klassen übernommen werden könnten. Das Refactoring _Extract Superclass_ trägt dazu bei Redundanzen aus dem Code zu entfernen indem zwei Klassen, die ähnliche Felder und Methoden benutzen durch eine gemeinsame Elternklasse ergänzt werden, die die Funktionalität, welche von den beiden Unterklassen genutzt wird zu implementieren.

<!-- 
- Wo habe ich sie eingesetzt und warum? 
- TaskComponent aufgeteilt in 2 Klassen (FinishedTaskComponent und Open...)
- TaskComponent wurde zu komplex
- If-Statements mussten verwendet werden, da die Klasse sowohl für abgeschlossene als auch offene Aufgaben verwendet wurde
- Die beiden entstandenen Klassen teilen sich trotzdem den Großteil der Funktionalität
- Ergibt Sinn die Gemeinsamkeiten in einer gemeinsamen Oberklasse zusammenzufassen
-->

In diesem Projekt wurde das oben beschriebene Refactoring bei der Klasse `dev.fg.dhbw.ase.tasktracker.domain.components.TaskComponent` eingesetzt. Zunächst erüllte die Klasse die Anforderungen von sowohl abgeschlossenen als auch offenen Aufgaben. Dies führte allerdings dazu, dass die Klasse an immer mehr Komplexität gewann und If-Statements verwendet werden mussten. Deshalb wurde die Klasse in zwei separate Klassen `FinishedTaskComponent` und `OpenTaskComponent` aufgeteilt. Die beiden entstandenen Klassen teilen einen Großteil der Funktionalität weshalb zusätzlich die gemeinsame Oberklasse `TaskComponent` eingeführt wurde (_Extract Superclass_). (vgl. [vorher](https://github.com/fgervasi-cell/Programmentwurf_ASE/tree/8fe35d94b7a09c56163697e2f435bd09b5ec07c4/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/components), [nachher](https://github.com/fgervasi-cell/Programmentwurf_ASE/tree/main/tasktracker/src/main/java/dev/fg/dhbw/ase/tasktracker/domain/components), [extract-class](https://refactoring.guru/extract-class) und [extract-superclass](https://refactoring.guru/extract-superclass))

## Entwurfsmuster

### Observer

Das Observer-Pattern gehört zur Klasse der Benachrichtigungsmuster. Es ermöglicht den Austausch von Nachrichten zwischen Softwarekomponenten ohne, dass eine starke Kopplung zwischen den Komponenten entsteht. Beim Observer-Pattern existieren zwei Arten von Entitäten: Observer und Observables. Ein Observable ist eine Komponente, die von einem Observer _beobachtet_ werden kann. Ein Observer ist eine Komponente, die sich bei einem Observable registrieren und anschließend Nachrichten von dem Observable erhalten kann (s. _dev.fg.dhbw.ase.tasktracker.domain.observer.Observer_ und _dev.fg.dhbw.ase.tasktracker.domain.observer.Observable_).

### Einsatz begründen

Das Observer-Pattern wird in diesem Projekt genutzt um einen Großteil der Kommunikation zwischen den einzelnen Komponenten des User Interfaces zu gestalten. So implementiert bspw. die Klasse _dev.fg.dhbw.ase.tasktracker.domain.components.TaskComponent_ das Observable-Interface (später zu Klasse geändert) um registierte Observer bei dem Löschen einer Aufgabe oder dem Markieren einer Aufgabe als abgeschlossen zu benachrichtigen. Die Klasse repräsentiert die UI-Komponente einer Aufgabe innerhalb einer Liste. Die Klasse _dev.fg.dhbw.ase.tasktracker.domain.controller.ListViewController_ implementiert das Observer-Interface und registriert sich bei einem _TaskComponent_, um über diese Events benachrichtigt zu werden und das UI zu aktualisieren. Eine ähnliche Vorgehensweise wurde auch bei den anderen UI-Komponenten gewählt. Der Aufzählungstyp _dev.fg.dhbw.ase.tasktracker.domain.components.ComponentEvent_ definiert zudem die Arten der Events die auftreten können. Die Observables definieren dort die Events die sie an die Observer verteilen. Dadurch kann der Observer auf die unterschiedlichen Events verschieden reagieren.
