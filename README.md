# ResourceAllocation
Various resource allocation schemes

## compilerlike

Allocate resources like registers are allocated in a compiler. See "UsageExample" in the corresponding package for an example.

### Dataflow Analysis

Find resources that are live at each point of execution. This is done by iteratively solving dataflow equations for each point of execution. Iteration stops when all sets in L don't change from iteration i to i+1. The result is a list of sets of the resources in use at each point of execution. This can be used to create an interferance graph (a graph of resources that are in use at the same time). This graph can then attempted to be colored with a fixed number of colors to allocate static resources. Graph coloring with a fixed number of colors is an NP complete problem, so coming up with a good method to attempt coloring is non-trivial.

For a primitive operation of the form below, where DEST/IN[] are temporary (virtual) resources

```
DEST = Operation(IN[1], IN[2], ..., IN[j])
```

The dataflow equation is as follows, where L[x] is the set of live (virtual) resources at point x and "U", "-" are set operations

```
L[i] = (L[i+1] - DEST) U { IN[1], IN[2], ..., IN[j] }
```

For a branch operation, K is the set of locations where the branch can go to. IN[] is the set of resources used to determine which branch to take.

```
L[i] = L[K[1]] U L[K[2]] U ... U L[K[i]] U { IN[1], IN[2], ..., IN[j] }
```
