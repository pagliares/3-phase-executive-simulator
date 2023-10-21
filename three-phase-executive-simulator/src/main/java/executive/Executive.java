package executive;

import SimObs.*;
import java.lang.*;
import java.util.*;

/**
 * Executive class to control a 3 phase simulation in which entity records are
 * held in a List (details, agora entitiesInUse) and from which a temporary List
 * (dueList) is created at each time beat to contain those entities with events
 * due at the current event time. Entities are added to the details List before
 * the start of an aPhase and unwanted entities are also deleted then. Bs are
 * assumed part of application entity classes, represented by integers & Cs as
 * belonging to the application alone Revised August 2000, consistent syntax
 */
public class Executive {

    private final static long checkValue = 1073741824;  // A large value
    private static long simulationDuration = 0;        // Simulation duration
    private static long currentClockTime = 0;              // Current clock time
    private static long previousClockTime = 0;          // Previous clock time
    private static List<Entity> entitiesInUse;              // Entities now in use
    private static List<Entity> entityListDueAtCurrentB;              // Entities due at current B
    private static List<Entity> newEntities;          // List of new entities to be added
    private static List<Entity> entitiesToBeKilled;           // Entities to be removed
    private static Entity currentEntity;
    private static boolean isTraceable;
    private static boolean traceEverOn;         // Trace ever been on?
    private static boolean cStarted;            // True if a C started this time
    private static List<Activity> listOfEventsTypeC;                // Ordered list of active Cs
    private static final long MAXTCELL = 1073741824;   // 2^30

    /**
     * Simple constructor, sets trace and traceEverOn fields to false so that no
     * debug.txt file is created unless it is required.
     */
    public Executive() {
        entitiesInUse = new ArrayList<>();
        entityListDueAtCurrentB = new ArrayList<>();
        newEntities = new ArrayList<>();
        entitiesToBeKilled = new ArrayList<>();
        listOfEventsTypeC = new ArrayList<>();
        isTraceable = true;
        traceEverOn = false;
        cStarted = false;
    }

    public static long getCurrentClockTime() {
        return currentClockTime;
    }

    public static void setSimulationDuration(long duration) {
        simulationDuration = duration;
    }

    public static long getSimulationDuration() {
        return simulationDuration;
    }

    /**
     * Sets the current value of trace to the indicated boolean state. If this
     * is true, then a debug.txt file will be created. It's probably best only
     * to set this at the start of a simulation run. The variable traceEverOn
     * will be true if the value of trace has ever been true during a simulation
     * run.
     */
    public static void setIsTraceable(boolean traceable) {
        isTraceable = traceable;
        if (isTraceable) {
            traceEverOn = true;
        }
    }

    public static boolean getTraceEverOn() {
        return traceEverOn;
    }

    public static boolean isTraceable() {
        return isTraceable;
    }

    /**
     * Returns with the current entity - that is, the entity whose B is about to
     * be processed or is being processed currently. Entity is defined as part
     * of the SimObs package.
     */
    public static Entity getCurrentEntity() {
        return currentEntity;
    }

    /**
     * Called in user program when an entity is to be destroyed. Places the
     * entity on an temporary Vector, toBeKilled. The entities in toBeKilled are
     * actually killed just before the start of the next aPhase.
     */
    public static void destroyEntity(Entity genericEntity) {
        entitiesToBeKilled.add(genericEntity);
        TraceFile.traceFile.println("Added to ToBeKilled");
        System.out.println("Added to ToBeKilled");
        for (int i = 0; i < entitiesToBeKilled.size(); i++) {
            ((Entity) entitiesToBeKilled.get(entitiesToBeKilled.size() - 1)).writeEntityOnTraceFile();  // TENTANDO PEGAR O ULTIMO ELEMENTO - TESTAR
        }
    }

    /**
     * Actually destroys the entity. Works down the toBeKilled Vector and
     * removes elements from details Vector. Called just before the aPhase.
     */
    private static void destroy() {
        if (!entitiesToBeKilled.isEmpty()) {
            for (int i = 0; i < entitiesToBeKilled.size(); i++) {
                Entity entity = (Entity) entitiesToBeKilled.get(i);
                try {
                    if (!entitiesInUse.remove(entity)) {
                        throw new Error("entity to be killed does not exist");
                    }
                    entitiesInUse.remove(entity);
                    TraceFile.traceFile.print("Trying to remove .. ");
                    System.out.println("Trying to remove .. ");
                    entity.writeEntityOnTraceFile();
                } catch (Error e) {
                    System.err.println("ERROR: " + e.getMessage());
                    ((Entity) entitiesToBeKilled.get(i)).writeEntityOnTraceFile();
                    System.exit(99);
                }
            }
            entitiesToBeKilled.removeAll(entitiesToBeKilled);
        }
    }

    /**
     * Called in user program when a new entity is to be added. Places the
     * entity on an temporary List, NewEntities, from which the new entities are
     * added just before the start of the aPhase.
     */
    public static void addNewEntity(Entity entity) {
        newEntities.add(entity);
    }

    /**
     * Actually adds new entities to the details List from the NewEntities List.
     * Works down the NewEntities List and adds elements to the details List,
     * called before the aPhase.
     */
    private static void addNewEntitiesToentitiesInUse() {
        if (!newEntities.isEmpty()) {
            for (Entity entity : newEntities) // Laco refatorado para uso do For Each. Nao encontrado nenhum efeito colateral
            {
                entitiesInUse.add(entity);
            }
            newEntities.removeAll(newEntities);
        }
    }

    private static void writesEntitiesInUseListToTraceFile() {
        TraceFile.traceFile.println("\n Inside method writesEntitiesInUseListToTraceFile. The size of the list Entities is :" + entitiesInUse.size());
        System.out.println("\n Inside method writesEntitiesInUseListToTraceFile. The size of the list Entities is :" + entitiesInUse.size());
        for (Entity entity : entitiesInUse) // Laco refatorado para uso do For Each. Nao encontrado nenhum efeito colateral
        {
            entity.writeEntityOnTraceFile();
        }
    }

    private static void writesDueListToTraceFile() {
        int entNum;
        TraceFile.traceFile.println("In showDueList. The size of the list due at current B is:" + entityListDueAtCurrentB.size());
        System.out.println("In showDueList. The size of the list due at current B is:" + entityListDueAtCurrentB.size());

        for (Entity entityAtCurrentB : entityListDueAtCurrentB) {
            int entityID = entityAtCurrentB.getEntityID();
            Entity entity = entitiesInUse.get(entityID);
            entity.writeEntityOnTraceFile();
        }
    }

    /**
     * Used when an entity needs to be committed to a B at some future time.
     * Checks whether the entity is currently available. If not, this means that
     * it is already committed to a B and so there is a fatal error thrown.
     * Checks whether the interval between the current simulation clock time and
     * the due time of the B is within range, corrects it if not. Calls the
     * commit method of the entity to update its values in the details Vector.
     * Declared as static, permits only a single executive.
     */
    public static void schedule(Entity genericEntity, Activity nextActivity, long time) {
        TraceFile.traceFile.println("\nInside method schedule");
        System.out.println("\nInside method schedule");
        try {
            if (!genericEntity.getAvailable()) {
                throw new Error("Tried to schedule an unavailable entity");
            }
            String saida = "Commit entity..: " + genericEntity.getName() + "\tID..: " + genericEntity.getEntityID();
            TraceFile.traceFile.println(saida);
            System.out.println(saida);
            if (time > MAXTCELL - currentClockTime) {
                time = MAXTCELL;
            } else {
                time = currentClockTime + time;
            }
            genericEntity.commit(time, nextActivity);
        } catch (Error e) {
            System.err.println("ERROR: " + e.getMessage());
            genericEntity.writeEntityOnTraceFile();
            System.exit(99);
        }
    }

    /**
     * Conventional APhase operating on the entitiesInUse List. Throws a fatal
     * error if aList is empty at the start of the aPhase. Destroys any entities
     * in the ToBeKilled List then adds any waiting in the NewEntities List.
     * Finds those entities with the smallest time cells that are currently
     * unavailable and adds this due now entities to the dueList Vector.
     */
    private static void aPhase() {
        destroy();
        addNewEntitiesToentitiesInUse();
        writesEntitiesInUseListToTraceFile();
        long minTimeCell = checkValue;
        Entity entity;
        try {
            if (entitiesInUse.isEmpty()) {
                throw new Error("entities in use list empty in APhase()");
            }
            // Pesquisa na lista de entidades para encontrar as com menor time-cell e adiciona na lista entityListDueAtCurrentB
            for (int i = 0; i < entitiesInUse.size(); i++) {
                entity = (Entity) entitiesInUse.get(i);
                if (!entity.getAvailable()) {
                    if (entity.getTimeCell() < minTimeCell) {
                        minTimeCell = entity.getTimeCell();
                        entityListDueAtCurrentB.removeAll(entityListDueAtCurrentB);  //  Estranho ! dueList.removeAllElements();    dueList.addElement(thisEnt);
                        entityListDueAtCurrentB.add(entity);
                    } else if (entity.getTimeCell() == minTimeCell) {
                        entityListDueAtCurrentB.add(entity);
                    }
                }
            }
            if ((minTimeCell == checkValue) || (minTimeCell == 0)) {
                throw new Error("Minimal Time Cell value at end of aPhase()");
            }
            currentClockTime = minTimeCell;
        } catch (Error e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(99);
        }
    }

    /**
     * Conventional B Phase, causing the next Bs of the due now entities to be
     * executed. Does this by working down the dueList and calling each found
     * there to execute its next B. Throws a fatal error if dueList is empty at
     * the start of bPhase.
     */
    private static void bPhase() {
        System.out.println("Starting B-PHASE " +
                "\n#############################################################################");
        int numEntitiesAtCurrentB = entityListDueAtCurrentB.size();
        try {
            if (numEntitiesAtCurrentB <= 0) {
                throw new Error("dueList empty at start of bPhase");
            }
            for (int i = 0; i < numEntitiesAtCurrentB; i++) {
                currentEntity = (Entity) entityListDueAtCurrentB.get(i);
                currentEntity.doNextB();
            }
            entityListDueAtCurrentB.remove(entityListDueAtCurrentB);
        } catch (Error e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(99);
        }
    }

    /**
     * Adds a new C to the end of the CList
     */
    public static void addC(Activity thisC) {
        listOfEventsTypeC.add(thisC);
        TraceFile.traceFile.println(thisC);
        System.out.println(thisC);
    }

    /**
     * Sets CStarted as true to indicate that a C has been executed during the
     * current C phase.
     */
    public static void setCStarted() {
        cStarted = true;
    }

    /**
     * Causes Cs to be attempted until none succeed. Works down cList, firing
     * each C in turn. Any successful C causes cStarted to be set to true,
     * leading to a re-scan.
     */
    public static void cPhase() {
        try {
            if (listOfEventsTypeC.isEmpty()) {
                throw new Error("listOfEventsTypeC empty at start of CPhase");
            }
            cStarted = true;
            while (cStarted) {
                cStarted = false;
                TraceFile.traceFile.println("IN C PHASE. The size of the list of events type C is: " + listOfEventsTypeC.size());
                System.out.println("IN C PHASE: " + listOfEventsTypeC.size());
                for (int i = 0; i <= listOfEventsTypeC.size() - 1; i++) {
                    ((Activity) listOfEventsTypeC.get(i)).doThisNow();
                }
            }
        } catch (Error e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(99);
        }
    }

    /**
     * Executes a complete 3 phase cycle
     */
    public static void simulate() {
        aPhase();
        
        TraceFile.traceFile.println("\n A-PHASE COMPLETE, current clock time = " + currentClockTime
                + "\n###############################################################################################################");
        System.out.println("\n A-PHASE COMPLETE, current clock time = " + currentClockTime
                + "\n###############################################################################################################");

        bPhase();
        cPhase();
    }
}
