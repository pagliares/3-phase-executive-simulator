// Queue.java
// Used to exend the Vector type into a queue
// M. Pidd 11/8/2000

package SimObs;

import java.util.Vector;

/**
* Simple class that extends Vector so as to model queues.
*/
public class Queue extends Vector {
  /** Total number added to the queue since its creation*/
  private int numAdded;
  //* Total number removed from the queue since its creation*/
  private int numRemoved;

  /**
  * Constructor, just sets the two data fields to zero.
  */
  public Queue() {
    numAdded = 0;
    numRemoved = 0;
  }

  /**
  * Puts the object on the tail of the queue.
  * Updates numAdded.
  */
  public void addToTail(Object thisObject) {
    addElement(thisObject);
    numAdded++;
  }

  /** Returns with the object currently at the head of the queue.
  * Throws a fatal error if the queue is empty.
  */
  public Object takeFromHead() {
    int qSize = size();
    Object thisObject = firstElement();
    try {
      if (qSize == 0) {
        throw new Exception("Queue empty in takeFromHead");
      }
      for (int loop = 0; loop < qSize-1; loop++)
        setElementAt(elementAt(loop+1), loop);
      removeElementAt(qSize-1);
    }
    catch (Exception e) {
      System.err.println("ERROR: " + e.getMessage());
      System.exit(99);
    }
    numRemoved++;
    return thisObject;
  }

  /** Returns with the object currently at the tail of the queue.
  * Throws a fatal error if the queue is empty.
  */
  public Object takeFromTail() {
    int qSize = size();
    Object thisObject = lastElement();
    try {
      if (qSize == 0) {
        throw new Exception("Queue empty in takeFromTail");
      }
      removeElementAt(qSize-1);
    }
    catch (Exception e) {
      System.err.println("ERROR: " + e.getMessage());
      System.exit(99);
    }
    numRemoved++;
    return thisObject;
  }

  /**
  * Returns with the number added to the queue so far.
  */
  public int getNumAdded() {
    return numAdded;
  }

  /**
  * Returns with the number removed from the queue so far.
  */
  public int getNumRemoved() {
    return numRemoved;
  }

  /**
  * Writes the queue contents to tracefile.
  */
  public void show() {
    try {
    if (size() == 0)
      throw new Error("Queue " + this + " empty");
    TraceFile.traceFile.println("Showing queue ..");
        System.out.println("Showing queue ..");
    for (int loop=0; loop <= size()-1; loop++) {
      TraceFile.traceFile.println(elementAt(loop));
        System.out.println(elementAt(loop));
    }
    }
    catch (Error e) {
      System.err.println("ERROR: " + e.getMessage());
      System.exit(99);
    }
  }
}
