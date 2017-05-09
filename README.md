# ResourceAllocation
Various resource allocation schemes

[Compiler-Like](#compilerlike)

[Distributed Allocation (Paxos-Like)](#likepaxos)

# compilerlike

Convert an arbitrary set of "virtual" resources into a close to minimal set of "real" resources required to execute a series of tuple instructions (like how registers are allocated in a compiler). See "UsageExample" in the corresponding package for an example.

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

We get the following result, where *tx*[*y*] means virtual resource *tx* becomes real resource *y*, and *{[...]}* is the set of live variables at a point in code execution
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

# likepaxos

Paxos is a protocol for solving consensus problems in a network of unreliable processors. In Paxos there are three types of processes, Proposer/Acceptor/Learner. I have created interfaces for each of these, but in my implemention I treat each process as a "server" which is all three.

#### IPC Mechanism

To simplify the problem, for my IPC mechanism I will be using GIPC. [GIPC](https://github.com/pdewan/GIPC) is an experimental remote procedure call library developed by Dr. Prasun Dewan at the University of North Carolina at Chapel Hill. It similar to Java's RMI, but it is non-blocking (meaning it does *not* block local execution until the remote procedures to completes).

In traditional Paxos we assume an unreliable network, but GIPC is implemented on top of reliable TCP sockets. This means the problem is similfied, as messages between two processes are guaranteed to not be lost in the network, and are guaranteed to be delivered in order. This means my implementation of Paxos will be slow, but it will still have most of the interesting problems Paxos solves. For example, even though TPC provides a reliable abstraction of the network, the end nodes themselves can still die and introduce interesting consistency problems. Also, in order delivery between two nodes provides no guarantees about the order of delivery between a group of asynchronous processes.

GIPC also provides a "registry" which all of the processes connect to at startup. This allows processes not on the same local network to learn about eachother, and make implementing Paxos simpler. Theoretically, after the initial learning of eachother, the processes shouldn't care if the registry dies. One caveat is that for a node to come back online after it dies, it must be able to connect to the registry *or* one surviving node to learn about the existance of the other nodes.

#### Leader Election

To guarantee progress will be made in a distributed consensus algorithm, we need to avoid [livelock](https://en.wikipedia.org/wiki/Deadlock#Livelock). Paxos does this by "electing" a leader, and in fact Paxos itself can be used to elect a leader.

My first implementation of Paxos is specifically targeted to solve the leader election problem.

##### Leader Election Example (3 processes)

Using my debugging library (which allows me to force error cases), I have processes 1 and 2 reject the first round of proposals they receive. The proposal number is encoded as "{client local proposal number}.{client globally unique id}"

Process 1
```
[Proposer] Sending proposal 0.001 # Round 0
[Acceptor] Ignoring proposal 0.001 (needs 1.0)
[Acceptor] Ignoring proposal 0.002 (needs 1.0)
[Acceptor] Ignoring proposal 0.003 (needs 1.0)
[Acceptor] Accepting proposal 1.002 # Round 1, vote for process 2
[Proposer] Sending proposal 1.001
[Acceptor] Ignoring proposal 1.001 (needs 1.002) # Round 1, reject process 1
[Acceptor] Accepting proposal 1.003 # Round 1, change vote to process 3
[Learner] Accepting new learn 1.002 (1/2) # Process 2 was elected, accept it's value
[Learner] Accepting new learn 1.003 (1/2) # Process 3 was elected, replacing 2. Accept it
[Learner] Accepting learn 1.003 (2/2)
[Learner] Learner learned 2 # Received quorum accepting process 3's suggession 
[Proposer] Sending proposal 2.001 # Process 1's proposer hasn't learned of accepted value
[Acceptor] Accepting proposal 2.001
[Proposer] Proposal accepted 2.001 (1/2)
[Proposer] Proposal accepted 2.001 (2/2)
[Proposer] Proposal accepted quorum for 2.001 with previous value 2 # Now process 1 knows accepted value
[Proposer] Sending accept request 2.001 with value 2
[Acceptor] Accepting accept request 2.001 with value 2
[Learner] Accepting new learn 2.001 (1/2)
[Learner] Accepting learn 2.001 (2/2)
[Learner] Learner learned 2
```
