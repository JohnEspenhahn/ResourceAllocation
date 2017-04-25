# ResourceAllocation
Various resource allocation schemes

## compilerlike

Allocate resources like registers are allocated in a compiler. See "UsageExample" in the corresponding package for an example.

### Dataflow Analysis

Find resources that are "live" at each point of execution. A resource is "live" if its current state is used in a computation at a later point. Resources that are live at the same time interfere with each other.

We can solve this problem by iteratively solving dataflow equations for each point of execution. Iteration stops when we hit a fixed-point for all sets of live resources (don't change from one step to the next). Then for each set, we create edges between all nodes to all others in the same set to give us an interference graph. This graph can then attempted to be colored with a fixed number of colors to allocate static resources. Graph coloring with a fixed number of colors is an NP complete problem, so coming up with a good method to attempt coloring is non-trivial.

##### Basic Operations
For a primitive operation of the form below, where DEST/IN[] are temporary (virtual) resources

```
DEST = Operation(IN[1], IN[2], ..., IN[j])
```

The dataflow equation is as follows, where L[x] is the set of live (virtual) resources at point x and "U", "-" are set operations

```
L[i] = (L[i+1] - DEST) U { IN[1], IN[2], ..., IN[j] }
```

##### Branching Operations
For a branch operation, K is the set of locations where the branch can go to. IN[] is the set of resources used to determine which branch to take.

```
L[i] = L[K[1]] U L[K[2]] U ... U L[K[i]] U { IN[1], IN[2], ..., IN[j] }
```
