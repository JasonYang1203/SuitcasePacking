import java.util.ArrayList;
/**
 * Class used for testing the methods in the Packing class.
 */
public class PackingTester {

  /**
   * Tester method for the Packing.rushedPacking() method base cases.
   * It should test at least the following scenarios:
   * - There are no items left to pack in the suitcase
   * - There are items left to pack, but none of them fit
   * @return true if all tests pass, false otherwise
   */
  public static boolean rushedPackingBaseTest() {
    // Test with an empty suitcase
    Suitcase emptySuitcase = new Suitcase(10, 10, new ArrayList<>());
    Suitcase result = Packing.rushedPacking(emptySuitcase);
    // Check if the result is Empty, cuz it should be empty
    if (!result.getPackedItems().isEmpty()) {
      return false; // Should be empty
    }

    // Test with items that don't fit
    ArrayList<Item> bigItems = new ArrayList<>();
    bigItems.add(new Item("BigOne", 9, 9));  // Too big to fit
    Suitcase bigItemSuitcase = new Suitcase(3, 3, bigItems);
    result = Packing.rushedPacking(bigItemSuitcase);
    // Check if the result is Empty, cuz it should be empty
    if (!result.getPackedItems().isEmpty()) {
      return false; // Should still be empty, item doesn't fit
    }

    return true; // All tests passed
  }

  /**
   * Tester method for the Packing.rushedPacking() method recursive cases.
   * It should test at least the following scenarios:
   * - All the items remaining can fit in the suitcase
   * - At least one item remaining cannot fit in the suitcase
   * @return true if all tests pass, false otherwise
   */
  public static boolean rushedPackingRecursiveTest() {
    // Initialize success flag for all tests
    boolean allTestsPassed = true;

    // Test 1: All items can be packed
    ArrayList<Item> case1 = new ArrayList<>();
    case1.add(new Item("SmallItem1", 2, 2));
    case1.add(new Item("SmallItem2", 1, 1));
    Suitcase caseFit = new Suitcase(3,3 , case1); // our suitcase exactly give 2 item space to fit in
    Suitcase packedResult1 = Packing.rushedPacking(caseFit);
    boolean test1Success = packedResult1.numItemsUnpacked() == 0 && packedResult1.getPackedItems().equals(case1);
    // check the unpacked number is 0 and the suitcase after packed should be same with the suitcase item we give

    // Test 2: Not all items can be packed
    ArrayList<Item> cannotItems = new ArrayList<>();
    cannotItems.add(new Item("LargeItem", 2, 2));
    cannotItems.add(new Item("sizedItem", 6, 6)); // This one can't fit
    ArrayList<Item> expectedResult = new ArrayList<>();
    expectedResult.add(new Item("LargeItem", 2, 2));// suitcase space only allow the
    // first one to be packed
    Suitcase caseCannotFit = new Suitcase(3, 3, cannotItems);
    Suitcase packedResult2 = Packing.rushedPacking(caseCannotFit);
    boolean test2Success = packedResult2.numItemsPacked() == 1 && packedResult2.getPackedItems().equals(expectedResult);
    // check the packed number is 1 and the suitcase after packed should be same with the 2 item that not be packed

    // Test 3: Suitcase large enough for all items
    Suitcase spaceForAll = new Suitcase(10, 10, cannotItems); // enough space for 2 items
    Suitcase packedResult3 = Packing.rushedPacking(spaceForAll);
    boolean test3Success = packedResult3.numItemsPacked() == 2 && packedResult3.getPackedItems().equals(cannotItems);
    // check the packed number is 2 and the suitcase be packed should be same with the 2 item we give


    // Test 4: Verifying base case
    boolean baseCaseResult = rushedPackingBaseTest();

    // Aggregate results
    allTestsPassed = test1Success && test2Success && test3Success && baseCaseResult;

    return allTestsPassed;
  }

  /**
   * Tester method for the Packing.greedyPacking() method base cases.
   * It should test at least the following scenarios:
   * - There are no items left to pack in the suitcase
   * - There are items left to pack, but none of them fit
   * @return true if all tests pass, false otherwise
   */
  public static boolean greedyPackingBaseTest() {

    boolean allTestsPassed = true; // Assume all tests pass until proven otherwise

    // Test case 1: Empty suitcase, no items to pack
    ArrayList<Item> noItems = new ArrayList<>();// define a null items arraylist
    Suitcase emptySuitcase = new Suitcase(10, 10, noItems);
    Suitcase resultEmpty = Packing.greedyPacking(emptySuitcase);// result after calling method
    if (!resultEmpty.getPackedItems().isEmpty()) {// if the packed items is not empty
      System.out.println("Failed: Items were packed in an empty suitcase.");
      allTestsPassed = false;// update the boolean indication to false
    }

    // Test case 2: Suitcase that cannot fit any items
    ArrayList<Item> largeItems = new ArrayList<>();
    largeItems.add(new Item("Large", 11, 11)); // Too large to fit in the suitcase
    Suitcase largeItemSuitcase = new Suitcase(10, 10, largeItems);// space not enough
    Suitcase resultLarge = Packing.greedyPacking(largeItemSuitcase);// result after calling
    if (!resultLarge.getPackedItems().isEmpty()) {// if the packed items is not empty
      System.out.println("Failed: A too large item was packed.");
      allTestsPassed = false;// update the boolean indication to false
    }

    // Test case 3: Suitcase with items that can be packed
    ArrayList<Item> packableItems = new ArrayList<>();
    packableItems.add(new Item("Small", 1, 1)); // Should fit easily
    Suitcase packableItemSuitcase = new Suitcase(10, 10, packableItems);// enough suitcase space for items
    Suitcase resultPackable = Packing.greedyPacking(packableItemSuitcase);// result after calling
    if (resultPackable.getPackedItems().isEmpty()) {// if the pack items is empty
      System.out.println("Failed: An item that should have been packed was not.");
      allTestsPassed = false;// update the boolean indication
    }

    return allTestsPassed;
  }

  /**
   * Tester method for the Packing.greedyPacking() method recursive cases.
   * It should test at least the following scenarios:
   * - At least one item is packed out of order (an item with a higher index
   *   is packed before an item with a lower index)
   * - A scenario where the greedy packing method packs more of the suitcase
   *   than the rushed packing (you can use the example given in the writeup)
   * @return true if all tests pass, false otherwise
   */
  public static boolean greedyPackingRecursiveTest() {

    boolean allTestsPassed = true; // Assume all tests pass until proven otherwise

    // Scenario 1: Multiple items, ensure the largest by area is packed first
    ArrayList<Item> items = new ArrayList<>();
    items.add(new Item("Item1", 3, 3)); // 9 square units
    items.add(new Item("Item2", 5, 5)); // 25 square units - should be packed first
    items.add(new Item("Item3", 2, 2)); // 4 square units
    Suitcase suitcase = new Suitcase(10, 10, items);
    Suitcase result = Packing.greedyPacking(suitcase);
    // Verify the correct item order
    if (result.getPackedItems().isEmpty() || !"Item2".equals(result.getPackedItems().get(0).name)) {
      System.out.println("Failed: Greedy method did not pack the largest item first.");
      allTestsPassed = false;
    }

    // Scenario 2: Items that tie by area, ensure the first in list is packed when tied
    items = new ArrayList<>();
    items.add(new Item("ItemA", 4, 4)); // 16 square units - should be packed first
    items.add(new Item("ItemB", 4, 4)); // 16 square units, same area but second
    suitcase = new Suitcase(10, 10, items);
    result = Packing.greedyPacking(suitcase);
    if (result.getPackedItems().isEmpty() || !"ItemA".equals(result.getPackedItems().get(0).name)) {
      System.out.println("Failed: Greedy method did not pack the first item in list when tied by area.");
      allTestsPassed = false;
    }

    // Scenario 3: Not enough space for all items, ensure larger items are packed over smaller ones
    items = new ArrayList<>();
    items.add(new Item("Small", 1, 1)); // Should be skipped initially due to small size
    items.add(new Item("Large", 9, 9)); // Should be packed first due to larger size
    items.add(new Item("Medium", 4, 4)); // Should be skipped since Large takes most space
    suitcase = new Suitcase(10, 10, items);
    result = Packing.greedyPacking(suitcase);
    if (result.getPackedItems().size() < 1 || !"Large".equals(result.getPackedItems().get(0).name)) {
      System.out.println("Failed: Greedy method did not prioritize larger items correctly when space is limited.");
      allTestsPassed = false;
    }

    // Scenario 4: Not all items can fit into the suitcase, should recognize incorrect base case
    ArrayList<Item> itemsDoNotFit = new ArrayList<>();
    itemsDoNotFit.add(new Item("C", 6, 6)); // Too big for an empty 5x5 suitcase
    itemsDoNotFit.add(new Item("D", 7, 7)); // Also too big
    Suitcase suitcaseTooSmall = new Suitcase(5, 5, itemsDoNotFit);
    Suitcase resultTooSmall = Packing.rushedPacking(suitcaseTooSmall);
    if (resultTooSmall.getPackedItems().size() > 0) {
      System.out.println("Failed: Items were packed when none should fit.");
      allTestsPassed = false; // This should fail if any item was packed
    }

    return allTestsPassed;
  }

  /**
   * Tester method for the Packing.optimalPacking() method.
   * This tester should test the optimalPacking() method by
   * randomly generating at least TEN (10) different scenarios,
   * and randomly generating at least ONE-HUNDRED (100)
   * different packing solutions for EACH of the scenarios.
   * Each scenario should have at least FIVE (5) random items,
   * and the suitcases should be of size at least 5x5.
   * If any random solution is better than the optimal packing then
   * it is not actually optimal, so the method does not pass the test.
   * You should use the Utilities method to generate random lists of
   * items, and to randomly pack the suitcases.
   * @return true if all tests pass, false otherwise
   */
  public static boolean optimalPackingRandomTest() {

    boolean allPassed = true;
    int count = 0;// initial the count
    for (int i = 0; i < 20; i++) {// for loop
      // Generate random cases scnerio
      ArrayList<Item> randomly = Utilities.randomItems(6);// using the Utility function to randomize
      Suitcase suitcaseforRandom = new Suitcase(6, 6, randomly);// generate random suitcase

      Suitcase suitcaseOptimal = Packing.optimalPacking(suitcaseforRandom);// the suitcase that have the optimal ways arranging

      // Test 150 random packing solutions for each scenario cases
      for (int c = 0; c < 150; c++) {
        // first Random packed the suitcase
        Suitcase packRandomly = Utilities.randomlyPack(suitcaseforRandom);

        if (packRandomly.areaPacked() > suitcaseOptimal.areaPacked()) {// check whether the random case work better
          allPassed = false;// update the all_pased
          count ++;// update the counts
          break; // if test case failed, break
        }
      }
    }
    //System.out.println(count);// give the final count
    return allPassed;// return the test pass or not

  }

  public static void main(String[] args) {
    boolean allPass = true;
    String printFormat = "%-29s %s\n";

    boolean rushedBase = rushedPackingBaseTest();
    allPass &= rushedBase;
    System.out.printf(printFormat, "rushedPackingBaseTest():", rushedBase);

    boolean rushedRecur = rushedPackingRecursiveTest();
    allPass &= rushedRecur;
    System.out.printf(printFormat, "rushedPackingRecursiveTest():", rushedRecur);

    boolean greedyBase = greedyPackingBaseTest();
    allPass &= greedyBase;
    System.out.printf(printFormat, "greedyPackingBaseTest():", greedyBase);

    boolean greedyRecur = greedyPackingRecursiveTest();
    allPass &= greedyRecur;
    System.out.printf(printFormat, "greedyPackingRecursiveTest():", greedyRecur);

    boolean optimalRandom = optimalPackingRandomTest();
    allPass &= optimalRandom;
    System.out.printf(printFormat, "optimalPackingRandomTest():", optimalRandom);

    System.out.printf(printFormat, "All tests:", allPass);
  }
}