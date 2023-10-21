// Small class file to enable entity execution without pointers

package SimObs;

/**
* A way of implementing activity execution without method pointers.
* Needed because, when scheduling an entity to some future B or when causing
* that entity to do this B, the B needs to be passed as a parameter. The Bs
* themselves are really methods but, by implementing this interface, can be
* treated as classes that can be instantiated.
*/
public interface Activity {
  void doThisNow();
} 