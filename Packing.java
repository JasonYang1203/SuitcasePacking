import java.util.ArrayList;

/**
 * Class used for packing a 2D suitcase with items using various strategies.
 */
public class Packing {

  /**
   * Helper method to check if at least one item can be packed into the suitcase.
   * @param suitcase The current state of the suitcase.
   * @return true if at least one item can be packed, false otherwise.
   */
  private static boolean canPackAny(Suitcase suitcase) {
    for (Item item : suitcase.getUnpackedItems()) {
      // Check if it can be packed
      if (suitcase.canPackItem(item)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Tries to pack each item in the items list in-order.
   * If an item can fit, it must be packed. Otherwise, it should be skipped.
   * Must be a recursive method.
   *
   * @param suitcase current Suitcase object
   * @return a Suitcase representing the outcome of a strategy in which the items
   * were attempted to be packed in-order.
   */
  public static Suitcase rushedPacking(Suitcase suitcase) {
    ArrayList<Item> unpackedItems = suitcase.getUnpackedItems();

    // Base case: No items left to pack or none can be packed
    if (unpackedItems.isEmpty() || !canPackAny(suitcase)) {
      return suitcase;
    }

    // Attempt to pack the first unpackable item in the for loop
    for (Item item : new ArrayList<>(unpackedItems)) {
      if (suitcase.canPackItem(item)) { // Check if it can be packed
        Suitcase newSuitcase = suitcase.packItem(item); // Pack the item
        return rushedPacking(newSuitcase); // Recursive call until reach the base case
      }
    }

    return suitcase; // Return the suitcase if no further items can be packed
  }


  /**
   * Packs items by greedily packing the largest item which can currently be packed.
   * Must be a recursive method.
   *
   * @param suitcase current Suitcase object
   * @return a Suitcase representing the outcome of a greedy strategy in which at each
   * point the largest item that can fit is packed.
   */
  public static Suitcase greedyPacking(Suitcase suitcase) {
    // Base case: If there are no items left to pack, return the suitcase.
    if (suitcase.getUnpackedItems().isEmpty() || !canPackAny(suitcase)) {
      return suitcase;
    }

    // Start with no item packed as the best case.
    Item largestItem = null;

    // Find the largest item that fits and pack it.
    for (Item item : suitcase.getUnpackedItems()) {
      // Check if the item can be packed in the suitcase
      if (suitcase.canPackItem(item)) {
        // if the new item have area greater than the largestitem
        if (largestItem == null || item.width * item.height > largestItem.width * largestItem.height) {
          largestItem = item;// update to the new item
        }
      }
    }

    // If we found an item that can be packed, pack it and continue packing.
    if (largestItem != null) {
      Suitcase newSuitcase = suitcase.packItem(largestItem);
      return greedyPacking(newSuitcase);// recursively call the method itself pack each position with the largest item
    }

    // If no items can be packed, just return the current state of the suitcase.
    return suitcase;
  }

  /**
   * Finds the optimal packing of items by trying all packing orders.
   * Must be a recursive method.
   *
   * @param suitcase current Suitcase
   * @return a Suitcase representing the optimal outcome.
   */
  public static Suitcase optimalPacking(Suitcase suitcase) {

    // Base case: if there are no unpacked items or no items can be packed, return the current suitcase
    if (suitcase.getUnpackedItems().isEmpty() || !canPackAny(suitcase)) {
      return suitcase;
    }

    // Store the best suitcase found so far and the max area packed in it
    Suitcase bestSuitcase = suitcase;
    int maxArea = suitcase.areaPacked();

    // Iterate through each unpacked item
    for (Item item : new ArrayList<>(suitcase.getUnpackedItems())) {
      // Check if the item can be packed in the suitcase
      if (suitcase.canPackItem(item)) {
        // Pack the item and update the current suitcase and area
        Suitcase currentSuitcase = suitcase.packItem(item);
        int currentArea = currentSuitcase.areaPacked();

        // Early pruning: If the potential max area is not greater than the current max, skip the branch
        int potentialMaxArea = currentArea + totalUnpackedArea(currentSuitcase);
        if (potentialMaxArea <= maxArea) {
          continue;
        }

        // Recursive call to try packing the rest of the items and get the best result
        Suitcase resultSuitcase = optimalPacking(currentSuitcase);
        int resultArea = resultSuitcase.areaPacked();
        // Update the best suitcase if a better packing is found
        if (resultArea > maxArea) {// if the result item's packed area greater than the max area we store
          maxArea = resultArea;// update the max area
          bestSuitcase = resultSuitcase;// update the optimal suitcase
        }
      }
    }

    return bestSuitcase; // Return the best packing found
  }

  /**
   * Helper method to calculate the total area of unpacked items.
   * This helps in estimating the potential maximum area that can be achieved.
   *
   * @param suitcase the current Suitcase object
   * @return the total area of unpacked items.
   */
  private static int totalUnpackedArea(Suitcase suitcase) {
    int totalArea = 0;
    // Calculate the total area of all unpacked items
    for (Item item : suitcase.getUnpackedItems()) {
      totalArea += item.width * item.height; // pre-calculate the area for save time
    }
    return totalArea;
  }

}