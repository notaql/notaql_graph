# NotaQL Graph-Transformation Platform

Uses: https://github.com/notaql/notaql

The platform can be used to define and execute graph transformations. A graph database (Neo4J, Titan, Tinkergraph, ...) is connected via the Blueprints API. To transform data from a graph database to a different kind of NoSQL database (or vice versa), this platform communicates with the Spark-based NotaQL platform via an intermediate JSON file.

## Some examples

### Vertex transformation / property mapping

```
IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db2.db'),
OUT._id <- IN._id
OUT.city <- IN.place
OUT.age <- IN.age + 1
```

### Attribute Dereference / copy all vertex properties

```
IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db2.db'),
OUT._id <- IN._id
OUT.$(IN._k)) <- IN._v
```
### Drop Projection / copy almost all vertex properties


```
IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db2.db'),
OUT._id <- IN._id
OUT.$(IN._k?(!age)) <- IN._v
```

### Input Filter / transform only those vertices fulfilling a predicate
```
IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db2.db'),
IN-FILTER: age = 68		// Zeilenpraedikat
OUT._id <- IN._id
OUT.$(IN._k?(!age)) <- IN._v
```

### Aggregate data (AVG, SUM, COUNT, MIN, MAX, LIST)
```
IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db2.db'),
OUT._id <- IN.city
OUT.avg_age <- AVG(IN.age)
```

### Access edge properties
```
OUT.something <- IN._e.edgeProperty
```

### Access edge label
```
OUT.something <- IN._e._l
```

### Access property of a neighbor vertex
Here, so-called local aggregation functions are used, i.e., aggregation is performed before vertex grouping.
```
OUT.avg_neighbors_age  <- avg(IN._e_.age),
OUT.num_neighbors <- count(IN._e_._id)
```

### Edge-traversing predicates / follow only edges fulfilling the predicate
```
OUT.numFollowers2013 <- count(IN._e?('follows' && _outgoing && since=2013)_._id)
```

### Neigbor-vertex predicates / follow only edges to neighbor vertices that fulfill the predicate
```
OUT.num_old_friends <- count(IN._e_?(age>65)._id)
```

### Path-length operator / follow edges multiple hops
Use [min,max] syntax. [x] stands for [x,x]. max=* is possible (transitive closure)
```
OUT.num_friends_of_friends <- count(IN._e?('friend')_[2]._id),
OUT.num_friends_and_friends_of_friends <- count(IN._e?('friend')_[1,2]._id)
```

### Edge creation
```
OUT._e_?(TARGET) <- EDGE(DIRECTION, LABEL, PROPERTIES)
```
* TARGET = predicate; dn edge is created to every vertex in the graph fulfilling this predicate.
* DIRECTION: _outgoing or _incoming
* LABEL: _l <- 'theLabel'
* PROPERTIES: (property <- value (, property <- value)*)? 
```
OUT._e_?(name='Anton') <- EDGE(_outgoing, _l <- 'friend', since <- 2017),
```

### Inverting edge directions
```
IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db2.db'),
OUT._id <- IN._id,
OUT.$(IN._k) <- IN._v,
OUT._e_?(_id=IN._e?(_incoming)_._id) <- EDGE(_outgoing, _l <- IN._e[@]._l, since <- IN._e[@].since);
```

### REPEAT clause: Iterative computations
* REPEAT: n (n times; stops at <n iterations when nothing changes anymore)
* REPEAT: -1 (unlimited; stops when nothing changes anymore)
* REPEAT: property(p%) (stops when the value of the given property changes less than p% between two iterations for every vertex)

### Distance to a given vertex / Breath-first search
```
IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db.db'),
IN-FILTER: name='Caesar',
OUT._id <- IN._id
OUT.dist <- 0;		

IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db.db'),
IN-FILTER: dist,
REPEAT: -1,			
OUT._id <- IN._e_?(!(dist))._id,	
OUT.dist <- MIN(IN.dist+1);
```

### PageRank
```
IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db.db'),
IN-FILTER: Label='Page',
OUT._id <- IN._id,
OUT.Pagerank <- 1/5;

IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: neo4j(path <- '/tmp/db.db'),
IN-FILTER: Label='Page',
REPEAT: Pagerank(0.0005%),
OUT._id <- IN._e?(_outgoing)_._id,
OUT.Pagerank <- SUM(IN.Pagerank/count(IN._e?(_outgoing)._id));
```

### Using a non-graph database as input or output
```
IN-ENGINE: tinkergraph(path <- '/tmp/db.db'),
OUT-ENGINE: redis(database<-0),
OUT._k <- IN._id,
OUT._v <- count(IN._e?('friends')_._id);
```

```
IN-ENGINE: redis(database<-0),
OUT-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT._id <- IN._k
OUT.number_of_visits <- IN._v
```

### Exporting graph data in a file or different database (edges become foreign-key arrays)
```
IN-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT-ENGINE: json(path <- '/tmp/db.json'),
OUT._id <- IN._id,
OUT.name <- IN.name,
OUT.following <- list(IN._e?(_outgoing)_._id),
OUT.follower <- list(IN._e?(_incoming)_._id);
```

### Importing graph data from a file or different database to a graph database
```
IN-ENGINE: json(path <- '/tmp/db.json'),
OUT-ENGINE: neo4j(path <- '/tmp/db.db'),
OUT._id <- IN._id,
OUT.name <- IN.name,
OUT._e_?(_id=IN.following) <- EDGE (_outgoing, _l <- 'follows');
```

# About
This project is a research prototype created at the
[Heterogeneous Information Systems Group](http://wwwlgis.informatik.uni-kl.de/cms/his/) at the Technical University of Kaiserslautern.
Contact person is [jschildgen](https://github.com/jschildgen).
We open sourced it in order to allow new contributors to add new features, bug fixes, or simply
use it for their own purposes.
