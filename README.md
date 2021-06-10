# WorkChain
Java library for handling multi-step workloads async by chaining runnables and executing them in pieces or in one go

```java
WorkThreader threader = new WorkThreader(workers); //Define in how many threads it's working

//Create Workers and start them
threader.createWorkers();
threader.start();

//A chain consists of at least 2 tasks, you can pass any type of variable between the two
Work.firstTask(() -> {
  return "Test Data";
}).lastTask(str -> {
  //Use data passed by previous Task
  System.out.println("Str: "+str);
}).setFullExecute(false).execute(); //Set execution mode to partial or full and execute
```


//Create Queue
WorkQueue myQueue = QueueManager.addQueue("my-queue-id");
WorkThreader threader = new WorkThreader(workers, myQueue); //Define how many threads work on a specific queue instead of global

//Create Workers and start them
threader.createWorkers();
threader.start();

Work.firstTask(() -> {
  return "Test Data";
}).lastTask(str -> {
  System.out.println("Str: "+str);
}).setQueue(myQueue).execute(); //Add Workload to specific queue
```
