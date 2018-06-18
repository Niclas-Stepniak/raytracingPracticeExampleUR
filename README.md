# Raytracing Practice Example UR
###### Java

## Beschleunigen sie diese Funktion

```java
private SurfaceInformation findFirstIntersection(Ray ray){
  Surface Information closestIntersection = null;
  double distanceToClosestIntersection = Double.POSITIVE_INFINTY;
  
  for(var triangle : triangles){
    var intersection = triangle.intersectWith(ray);
    if (intersection != null){
      double distanceToIntersection = intersection.getPosition().distance(ray.getOrigin());
      if (distanceToIntersection < distanceToClosestIntersection) {
        distanceToClosestIntersection = distanceToIntersecton;
        closestIntersection = intersection;
      }
    }
  }

  return closestIntersection;
}
```

### Verbesserung der Laufzeitkomplexität
--------------
#### Bisher:
für jeden Strahl:
- Schneide Strahl mit allen Dreiecken, behalte nähesten zum Strahl-Ursprung

#### Jetzt:
für jeden Strahl:
- Schneide Strahl mit einer geschickt ausgewählten, kleinen Teilmenge aller Dreiecke, aber finde sicher den gleichen Schnittpunkt wie oben

### Wie? Raumunterteilungsstruktur (Space Partitioning)
--------------
- Dreiecke der Szene nicht mehr in einer Liste (mit willkürlicher Reihenfolge) speichern …
- … sondern in einer Datenstruktur, die es erlaubt, Dreiecke anhand ihrer ungefähren Position im Raum nachzuschlagen
- Sobald diese Datenstruktur mit allen Dreiecken gefüllt ist: Für jeden Strahl nur noch nachschlagen, welche Dreiecke “ungefähr in der Nähe des Strahls” liegen und nur diese einem Schnitttest unterziehen



### Grid / Binning / Spatial Hashing
--------------
- Alle Gitterzellen sind gleich groß
- (Wie groß wäre sinnvoll?)
- Jede Zelle enthält alle berührten Dreiecke bzw. ein Dreieck ist in allen Zellen, die es berührt

### Frage: Welche Zellen müssen durchsucht werden?
--------------
- Antwort: alle Zellen, die der Strahl schneidet
- Folgefrage: Welche Zellen schneidet der Strahl?
- Und in welcher Reihenfolge?

### Enumeriere alle vom Strahl berührten Zellen
--------------
Zwei grundsätzliche Möglichkeiten:

- Obermenge + Filtern + Sortieren:
  1. Bestimme eine unsortierte Obermenge aller Zellen, die der Strahl berührt (kann also ggf. auch einige Zellen enthalten, die er nicht berührt)
  2. Filtere diese Menge, so dass nur wirklich berührte Zellen übrigbleiben
  3. Sortiere die Zellen in der Reihenfolge wie sie der Strahl durchlaufen wird

- Startzelle (welche ist das?) + wiederholtes Springen zwischen jeweils benachbarten Zellen, in exakt gleicher Reihenfolge wie der Strahl zwischen ihnen übergeht (eleganter, aber geometrielastiger)

*This is a project which is based on the work of many. Most of the reused code should be referenced in the project.*
