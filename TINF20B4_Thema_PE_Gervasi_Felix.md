# Themeneinreichung Programmentwurf

Kurs: TINF20B4  
Student: Felix Gervasi  
Thema: Aufgabenplaner

## Themenbeschreibung

Der Aufgabenplaner gibt dem Benutzer die Möglichkeit Aufgaben zu erstellen und Fälligkeitsdaten festzulegen und seine Aufgaben in Listen zu organisieren. Der Aufgabenplaner soll dem Benutzer einen Überblick über seine noch zu erledigenden und bereits erledigten Aufgaben geben und durch Erinnerungen dafür sorgen, dass nichts Wichtiges vergessen wird. Die Verwendung findet in Form einer Java-Desktop-Anwendung statt. Nachdem der Anwender sich registriert hat kann er den Planer auch auf mehreren Geräten verwenden. Dies soll aber optional bleiben. Ein Dashboard gibt dem Anwender eine zusätzliche Übersicht und die Möglichkeit seine Produktivität selbst einzuschätzen und zu bewerten.

## Use Cases

### Neue Liste anlegen

Es soll möglich sein Listen anzulegen, denen anschließend Aufgaben hinzugefügt werden können. Eine Liste hat dabei einen eindeutigen Namen und eine Liste ihrer zugeordneten Aufgaben.

### Neue Aufgaben anlegen

Innerhalb einer Liste kann dieser eine neue Aufgabe hinzugefügt werden. Eine Aufgabe speichert...

- Titel (String),
- Erinnerungsdatum (String),
- Arbeitsschritte (List\<Task>),
- Erledigt (boolean),
- Beschreibung/Notiz (String) und
- Fälligkeitsdatum (Date).

Mehrere Aufgaben mit gleichem Titel (auch innerhalb einer Liste) sind möglich. Bei den Daten ist darauf zu achten, dass kein Datum, welches in der Vergangenheit liegt, ausgewählt werden kann. Eine Aufgabe kann weitere Aufgaben als Arbeitsschritte enthalten. Die Verschachtelungstiefe ist dabei auf eins beschränkt.

### Alte Aufgaben löschen, modifizieren und verschieben

Aufgaben die bereits angelegt wurden können gelöscht und modifiziert werden. Auch ein Verschieben einer Aufgabe von einer Liste in eine andere Liste soll ermöglicht werden. Wird eine Aufgabe als erledigt markiert sollte diese nicht mehr in der entsprechenden Liste zu sehen sein. Als erledigt markierte Aufgaben müssen aber auch nachträglich noch abrufbar und bspw. als nicht erledigt markriert werden können.

### Option zur Benutzung auf mehreren Geräten

Es soll dem Benutzer möglich sein einen Account zu erstellen über den er sich identifizieren kann. Die Speicherung der Applikationsdaten erfolgt dann in der Cloud anstatt auf dem tatsächlichen Endgerät, sodass eine Benutzung der Anwendung auf verschiedenen Geräten möglich ist.

### Anzeigen von Statistiken

Auf einem Dashboard sollen Informationen über die Bearbeitung der Aufgaben für den Benutzer sichtbar sein:

- Durchschnittliche Bearbeitungsdauer von Aufgaben
- Durchschnittliche Anzahl abgeschlossener Aufgaben pro Woche
- Anzahl insgesamt abgeschlossener Aufgaben
- Zeitliche Entwicklung durch Auftragen der Daten in einem Graphen

### Anhängen von Dateien zu einer Aufgabe

Es soll möglich sein einer Aufgabe ein oder mehrere Dateien anzuhängen (bspw. PDF, Excel, Bild- oder Videodateien).

### Sich wiederholende Aufgaben

Bei den Aufgaben soll ein Intervall einstellbar sein, in dem sich die Aufgabe wiederholt.

## Technologien

- Java (Programmiersprache)
- Java-Swing/JavaFX (Frontend-Bibliotheken)
