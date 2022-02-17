# Direct Follower Matrix

Utility to extract and visualize the direct followers of the events in
an event log.

# Build and Run

Compile and run tests:
```shell
sbt clean compile test
```

Running:
```shell
sbt run
```

# Assumptions and Limitations
- The console visualization of the direct follower matrix grid is truncated to a 
  maximum matrix size of 20 activities x 20 activities.
- The default timezone for parsing date time values without an explicit timezone 
  in event log CSV file is set to `UTC`.
- Event `Complete` and `Classification` columns are discarded and not used in
  any calculations.
