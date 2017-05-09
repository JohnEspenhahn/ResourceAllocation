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

GIPC also provides a "registry" which all of the processes connect to at startup. This allows processes not on the same local network to learn about each other, and makes implementing Paxos simpler. After the initial learning of each other, the processes doesn't care if the registry dies. One caveat is that for a node to come back online after it dies, it must be able to connect to the registry *or* one surviving node to learn about the existance of the other nodes.

#### Leader Election

To guarantee progress will be made in a distributed consensus algorithm, we need to avoid [livelock](https://en.wikipedia.org/wiki/Deadlock#Livelock). Paxos does this by "electing" a leader, and in fact Paxos itself can be used to elect a leader.

##### Simple Example (3 nodes, with 1 initially down)

Using my debugging library (which allows me to force error cases), I have processes A reject the first round of proposals it receives. [Note: The proposal number is encoded as "{node local proposal number}.{node globally unique id}"]

```
[A] [Learner] Forcing Largest Proposal Number 1.0
[A] Setup as 9001
[C] Setup as 9003
[B] Setup as 9002
[B] [Proposer] Sending proposal 0.9002 # B makes initial proposal
[A] [Acceptor] Ignoring proposal 0.9002 (needs 1.0) # A is simulated as down
[C] [Acceptor] Accepting proposal 0.9002
[B] [Acceptor] Accepting proposal 0.9002
[B] [Proposer] Proposal accepted 0.9002 (1/2)
[B] [Proposer] Proposal accepted 0.9002 (2/2)
[B] [Proposer] Proposal accepted quorum for 0.9002 with previous value null # B received quorum from B/C
[B] [Proposer] Sending accept request 0.9002 with value 9002
[B] [Acceptor] Accepting accept request 0.9002 with value 9002
[C] [Acceptor] Accepting accept request 0.9002 with value 9002
[A] [Learner] Accepting learn 0.9002 with value 9002 (1/2)
[B] [Learner] Accepting learn 0.9002 with value 9002 (1/2)
[B] [Learner] Accepting learn 0.9002 with value 9002 (2/2) # Learner receives quorum
[B] [Learner] Learner learned 9002
[C] [Learner] Accepting learn 0.9002 with value 9002 (1/2)
[A] [Learner] Accepting learn 0.9002 with value 9002 (2/2)
[A] [Learner] Learner learned 9002 # A learner is up to date, but A proposer doesn't know this yet
[C] [Learner] Accepting learn 0.9002 with value 9002 (2/2)
[C] [Learner] Learner learned 9002
[A] [Proposer] Sending proposal 0.9001 # now A makes a proposal
[A] [Acceptor] Ignoring proposal 0.9001 (needs 1.0)
[B] [Acceptor] Ignoring proposal 0.9001 (needs 0.9002)
[C] [Acceptor] Ignoring proposal 0.9001 (needs 0.9002)
[A] [Proposer] Sending proposal 1.9001 # retry (needs to catchup sequence number)
[B] [Acceptor] Accepting proposal 1.9001
[A] [Acceptor] Accepting proposal 1.9001
[C] [Acceptor] Accepting proposal 1.9001
[A] [Proposer] Proposal accepted 1.9001 (1/2)
[A] [Proposer] Proposal accepted 1.9001 (2/2)
[A] [Proposer] Proposal accepted quorum for 1.9001 with previous value 9002 # Sequence number caught up, use previous value
[A] [Proposer] Sending accept request 1.9001 with value 9002 # A proposer is now up to date
[B] [Acceptor] Accepting accept request 1.9001 with value 9002
[B] [Learner] Accepting learn 1.9001 with value 9002 (1/2)
[A] [Learner] Accepting learn 1.9001 with value 9002 (1/2)
[C] [Acceptor] Accepting accept request 1.9001 with value 9002
[C] [Learner] Accepting learn 1.9001 with value 9002 (1/2)
[C] [Learner] Accepting learn 1.9001 with value 9002 (2/2)
[C] [Learner] Learner learned 9002
[B] [Learner] Accepting learn 1.9001 with value 9002 (2/2)
[B] [Learner] Learner learned 9002
[A] [Learner] Accepting learn 1.9001 with value 9002 (2/2)
[A] [Learner] Learner learned 9002
```
