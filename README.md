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

### Usage Example
If we have at most 4 "real" resources, and the following "virtual" resources used in our tuple code: (t0, t1, t2, t3, x, y, z). Our tuple code is as follows (implementing a simple while-loop):

```
0: t0 = GTR(x,z)
1: NOT_IF t0 GOTO 8 // if t0 is false (== 0)
2: t1 = SUB(x,z)
3: t2 = 3
4: x  = MULT(t1,t2)
5: t3 = 5
6: z  = ADD(x,t3)
7: GOTO 0
8: y  = x
```

We get the following result, where *tx*[*y*] means virtual resource *tx* becomes real resource *y*, and *{...}* is the set of live variables at a point in code execution
```
0: t0[1] = GTR(x[0],z[2])    {[x[0], z[2]]}
1: NOT_IF t0[1] GOTO 8       {[x[0], t0[1], z[2]]}
2: t1[1] = SUB(x[0],z[2])    {[x[0], z[2]]}
3: t2[0] = 3                 {[t1[1]]}
4: x[0]  = MULT(t1[1],t2[0]) {[t1[1], t2[0]]}
5: t3[1] = 5                 {[x[0]]}
6: z[2]  = ADD(x[0],t3[1])   {[x[0], t3[1]]}
7: GOTO 0                    {[x[0], z[2]]}
8: y[0]  = x[0]              {[x[0]]}
```

Which, written more simply, is the following code using only real resources (*r*)
```
r0 = x
r2 = z
0: r1 = GTR(r0,r2)
1: NOT_IF r1 GOTO 8
2: r1 = SUB(r0,r2)
3: r0 = 3
4: r0 = MULT(r1,r0)
5: r1 = 5
6: r2 = ADD(r0,r1)
7: GOTO 0
8: r0 = r0 // Because this is end of execution, we get a non-op
```
So our code has been simplified from 7 virtual resources, to 3 real ones
