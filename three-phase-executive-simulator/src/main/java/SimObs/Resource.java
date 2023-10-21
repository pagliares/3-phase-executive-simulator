// Resource.java
// Resource class
// M.Pidd
package SimObs;

/**
 * Used to represent simulation objects that need not be represented as
 * individual entities since they are identical and can be taken and returned by
 * objects that are represented as entities. A major theme of most discrete
 * simulations is resource contention, in which two or more entities require the
 * same resource. In effect, a resource is a pool of identical items.
 */
public class Resource {

    
    private static String resourcePoolName;
    // The total number of items in this resource pool*/
    private static int amountOfResources;
    // The number of items currently available for use from this resource pool*/
    private static int freeAmountOfResources;

    
    public Resource(String resourcePoolName, int amountOfResources) {
        this.resourcePoolName = resourcePoolName;
        this.amountOfResources = amountOfResources;
        this.freeAmountOfResources = amountOfResources;
    }

   
    public Resource() {
        resourcePoolName = " ";
        amountOfResources = 0;
        freeAmountOfResources = 0;
    }

    /**
     * Adds extra resource units to this resource pool. The extra are all made
     * available.
     */
    public void add(int more) {
        amountOfResources = +more;
        freeAmountOfResources = +more;
    }

    /**
     * Returns with the no of units currently unused in this resource pool.
     */
    public int getFreeAmount() {
        return freeAmountOfResources;
    }

    /**
     * DANGEROUS, but needed if the
     * constructor used has no parameters.
     */
    public void setFreeAmount(int thisAmount) {
        freeAmountOfResources = thisAmount;
    }

    /**
     * Returns with the total amount in this resource pool. Includes both free
     * resource and that currently committed.
     */
    public int getAmount() {
        return amountOfResources;
    }

    /**
     * DANGEROUS, but needed if the
     * constructor used has no parameters.
     */
    public void setAmount(int thisAmount) {
        amountOfResources = thisAmount;
    }

    /**
     * Commits thisAmount from the freeAmount available in this resource pool.
     * Throws a fatal error if insufficient resource is available to meet the
     * demand.
     */
    public void commitFreeResource(int amount) {
        try {
            if (freeAmountOfResources < 0) {
                throw new Error("Tried to over-commit a resource " + freeAmountOfResources);
            }
            freeAmountOfResources -= amount;
        } catch (Error e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(99);
        }
    }

    /**
     * Release thisAmount of resource, thus increasing freeAmount by thisAmount.
     * Throws a fatal error if not enough resource is available to meet this
     * demand. Estava acontecendo comigo. Como controlar???.
     */
    public static void makeResourceAvailable(int amount) {
        TraceFile.traceFile.println(resourcePoolName + " has " + freeAmountOfResources + ", releasing " + amountOfResources);
        System.out.println(resourcePoolName + " has " + freeAmountOfResources + ", releasing " + amountOfResources);
        try {
            if (freeAmountOfResources > amountOfResources) {
                throw new Error("Tried to over-free a resource");
            }
            freeAmountOfResources += amount;
        } catch (Error e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(99);
        }
    }

    @Override
    public String toString() {
        return "Resource";
    }
    
    
}
