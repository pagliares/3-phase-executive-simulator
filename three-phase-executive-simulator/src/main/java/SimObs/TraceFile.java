// Trace.java
// Trace file for use with three phase simulation in java
// M. Pidd, started 30/6/97

package SimObs;

import java.io.*;
import java.util.*;

/**
* Class to allow a running simulation program to write run-time debugging
* information to a specified output device.
*/
public class TraceFile {
  
  private String fileName;
  private boolean traceActive;
  /** Whether tracing has ever been active in this simulation*/
  private boolean traceEverOn;
  /** Defines the output device */
  public static PrintWriter traceFile;

  
  public TraceFile(String fileName) {
    this.fileName = fileName;
    try {
      traceFile = new PrintWriter(new FileOutputStream(fileName));
      TraceFile.traceFile.println("HARASSED BOOKING CLERK SIMULATION (JAVA)" + "\n");
      System.out.println("HARASSED BOOKING CLERK SIMULATION (JAVA)" + "\n");
    }
    catch (IOException e) {
      System.err.println(e);
    }
  }

 
  public void setTraceActive(boolean thisState) {
    traceActive = thisState;
    if (traceActive)
      traceEverOn = true;
  }

 
  public boolean isTraceActive() {
    return traceActive;
  }
  
  public boolean isTraceEverOn() {
    return traceEverOn;
  }

  
  public void closeTraceFile() {
    if (traceEverOn)
      traceFile.close();
  }
  
  public void println(String[] args) {
    if (traceActive)
      traceFile.println(args);
  }

 
  public void print(String[] args) {
    if (traceActive)
      traceFile.print(args);
  }
}