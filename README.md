# HashgraphMachine
This project is a JVM implementation of the **Swirlds Hashgraph** by *Leemon Baird* - a **fair**, **fast**, **replicated**, **Byzantine** state machine.

Once it reaches maturity, ```HashgraphMachine``` is intended to be a **correct**, **performant** and **reusable** **implementation** of the consensus protocol.

# Project status
Please, be warned that **this project is in a very early, proof-of-concept stage**. Both APIs and implementation can and will change without notice.

# About the algorithm
Swirlds Hashgraph is fair, fast, replicated, Byzantine consensus algorithm, which can be used to implement distributed state machines.

Key properties of Hashgraph, which make it a very powerful tool for building real-world, large scale distributed systems (f.e. databases) include:
  - **Byzantine fault tolerancy**, that is an ability to tolerate both classic node failures and network partitions as well as malicious, adversarial nodes - as long as at least ~2/3 of all peers are available and conforming.
  - **No single point of failure** due to a leaderless approach to arriving at consensus.
  - **Scalability, peformance and energy efficiency** due to a novel use of gossip protocol enabling the concept of *virtual voting*, which results in a very limited utilization of both network bandwidth and processing power.  

For more detailed description, please consult the whitepaper: http://www.swirlds.com/downloads/SWIRLDS-TR-2016-01.pdf

//TODO: consider elaborating more

# Specification and APIs
//TODO: write down specs of the implementation and APIs

# Implementation architecture
//TODO: document architecture once there is some ;)
