# Shapes

## Creating a shape
```java
Shape shape = new Shape();
shape.addPoint(-0.5f, -0.5f);
shape.addPoint(0.5f, -0.5f);
shape.addPoint(0.0f, 0.5f);
```

Check if a point is inside a shape
This function casts rays from this point.
If the ray intersects with the boundaries of the shape an even amount of times
the point is outside of the shape. Otherwise the shape contains the point.
```java
boolean inside = shape.contains(0.5f, 0.2f);
```

## Check whether two shapes intersect

First checks whether the bounding boxes collide using AABB collision detection.
After that it checks whether one of the shapes contains a single point of the other shape. Lastly it checks intersections of the boundaries.

```java
boolean intersects = shape.intersects(otherShape);
```
