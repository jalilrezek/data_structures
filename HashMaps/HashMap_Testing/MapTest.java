package hw7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("All")
public abstract class MapTest {

  protected Map<String, String> map;

  @BeforeEach
  public void setup() {
    map = createMap();
  }

  protected abstract Map<String, String> createMap();

  @Test
  public void newMapIsEmpty() {
    assertEquals(0, map.size());
  }

  @Test
  public void insertOneElement() {
    map.insert("key1", "value1");
    assertEquals(1, map.size());
    assertTrue(map.has("key1"));
    assertEquals("value1", map.get("key1"));
  }

  @Test
  public void insertMultipleElement() {
    map.insert("key1", "value1");
    map.insert("key2", "value2");
    map.insert("key3", "value3");
    System.out.println("Right after inserting key3");
    assertEquals(3, map.size());
    assertTrue(map.has("key1"));
    assertTrue(map.has("key2"));
    assertTrue(map.has("key3"));
    assertEquals("value1", map.get("key1"));
    assertEquals("value2", map.get("key2"));
    assertEquals("value3", map.get("key3"));
  }

  @Test
  public void insertDuplicatedKey() {
    try {
      map.insert("key1", "value1");
      map.insert("key1", "value2");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void insertNullKey() {
    try {
      map.insert(null, "value1");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void insertDuplicatedValue() {
    map.insert("key1", "value1");
    map.insert("key2", "value1");
    assertEquals(2, map.size());
  }

  @Test
  public void insertNullValue() {
    map.insert("null", null);
    assertEquals(1, map.size());
  }

  @Test
  public void removeOneElement() {
    map.insert("key1", "value1");
    assertEquals("value1", map.remove("key1"));
    assertEquals(0, map.size());
  }

  @Test
  public void removeMultipleElements() {
    map.insert("key1", "value1");
    map.insert("key2", "value2");
    map.insert("key3", "value3");
    assertEquals("value1", map.remove("key1"));
    assertEquals("value3", map.remove("key3"));
    assertEquals(1, map.size());
    assertFalse(map.has("key1"));
    assertTrue(map.has("key2"));
    assertFalse(map.has("key3"));
    assertEquals("value2", map.get("key2"));
  }

  @Test
  public void removeNull() {
    try {
      map.remove(null);
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void removeNoSuchElement() {
    try {
      map.remove("key1");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void updateValue() {
    map.insert("key1", "value1");
    map.put("key1", "value2");
    assertEquals(1, map.size());
    assertEquals("value2", map.get("key1"));
  }

  @Test
  public void updateMultipleValues() {
    map.insert("key1", "value1");
    map.insert("key2", "value2");
    map.insert("key3", "value3");
    map.put("key1", "updated1");
    map.put("key3", "updated3");
    assertEquals(3, map.size());
    assertEquals("updated1", map.get("key1"));
    assertEquals("value2", map.get("key2"));
    assertEquals("updated3", map.get("key3"));
  }

  @Test
  public void updateMultipleTimes() {
    map.insert("key1", "value1");
    map.put("key1", "value2");
    map.put("key1", "value3");
    map.put("key1", "value4");
    assertEquals(1, map.size());
    assertEquals("value4", map.get("key1"));
  }

  @Test
  public void updateNullValue() {
    map.insert("key1", "value1");
    map.put("key1", null);
    assertEquals(1, map.size());
    assertNull(map.get("key1"));
  }

  @Test
  public void updateNullKey() {
    try {
      map.put(null, "value");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void updateKeyNotMapped() {
    try {
      map.put("key", "value");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void getKeyNull() {
    try {
      map.get(null);
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void iteratorEmptyMap() {
    for (String key : map) {
      fail("Empty map!");
    }
  }

  @Test
  public void iteratorMultipleElements() {
    map.insert("key1", "value1");
    map.insert("key2", "value2");
    map.insert("key3", "value3");
    int counter = 0;
    for (String key : map) {
      counter++;
      assertTrue(map.has(key));
    }
    assertEquals(3, counter);
  }

  // Ideally we should also check for "Keys must be immutable"
  // This is not trivial; check out
  // https://github.com/MutabilityDetector/MutabilityDetector



  ///////////////////////// MY TESTS /////////////////////////////
  @Test
  public void insertManyThenRemoveMany() {
    map.insert("key1", "value1");
    map.insert("key2", "value2");
    map.insert("key3", "value3");
    assertEquals(3, map.size());
    //assertEquals(5, map.totalSize());
    assertTrue(map.has("key1"));
    assertTrue(map.has("key2"));
    assertTrue(map.has("key3"));
    assertEquals("value1", map.get("key1"));
    assertEquals("value2", map.get("key2"));
    assertEquals("value3", map.get("key3"));
    for (int i = 0; i < 100; i++) {
      String str = Integer.toString(i);
      map.insert(str, "d");
    }
    map.remove("key1");
    assertEquals(102, map.size());
    for (int i = 99; i > 49; i--) {
      map.remove(Integer.toString(i));
    }
    assertEquals(52, map.size());
  }

  @Test
  public void hashCodeVals() {
    System.out.println("2".hashCode()); // 50
    System.out.println("3".hashCode()); // 51 --> consecutive
  }

  @Test
  public void insertFewElementsAllOfSameValue() {  // name used to be "insertFewElements" at time of submission
    for (int i = 0; i < 2; i++) { // they all appear in order in the debugger, but the hashCode values mean that will happen, so makes sense
      map.insert(Integer.toString(i), "v");
      if (!map.has(Integer.toString(i))) {
        System.out.println("Just inserted entry yet it's not found by has(): " + Integer.toString(i));
      }
    }
  }

  @Test
  public void insert11Elements() {
    for (int i = 0; i < 11; i++) { // they all appear in order in the debugger, but the hashCode values mean that will happen, so makes sense
      System.out.println("Want to add: " + i);
      map.insert(Integer.toString(i), "v");
      System.out.println("Successfully added: " + i);
    }
    assertEquals(11, map.size()); //
    assertTrue(map.has("10"));// this passes
    for (int i = 0; i < 11; i++) {
      if (!map.has(Integer.toString(i))) {
        System.out.println("Missing: " + i); // 10 is missing. Visualizer shows it's there. Problem is with the has() function?
      }
    }
  }

  @Test
  public void insert100Elements() {
    for (int i = 0; i < 100; i++) { // they all appear in order in the debugger, but the hashCode values mean that will happen, so makes sense
      map.insert(Integer.toString(i), "v");
      if (!map.has(Integer.toString(i))) {
        System.out.println("Just inserted entry yet it's not found by has(): " + Integer.toString(i));
      }
    }
    assertEquals(100, map.size()); // this passes
    for (int i = 0; i < 100; i++) {
      if (!map.has(Integer.toString(i))) { // 21 was NOT actually added. The rest that are missing were but has() fails to find them.
        System.out.println("Missing: " + i); // 30 - 40 (exclusive) are missing. Visualizer shows it's there. Problem is with the has() function?
      }
    }
    int i = 1;
  }

  @Test
  public void insert1000Elements() {
    for (int i = 0; i < 1000; i++) {
      map.insert(Integer.toString(i), "v");
    }
    assertEquals(1000, map.size()); // this passes
    for (int i = 0; i < 1000; i++) {
      if (!map.has(Integer.toString(i))) {
        System.out.println(i); // "quadratic" (not really) pattern of missing elements (1 missing, 10 missing, 50 missing)
      }
    }
    assertTrue(map.has("21"));
    for (int i = 0; i < 1000; i++) { // missing somewhere between 745-750; and missing #21
      //System.out.println(i);
      assertTrue(map.has(Integer.toString(i)));
    }
  }

  @Test
  public void insertFewThenRemoveAll() {
    //assertEquals(2, map.totalSize());
    map.insert("key1", "value1");
    //assertEquals(2, map.totalSize());
    map.insert("key2", "value2");
    //assertEquals(5, map.totalSize());

    map.remove("key1");
    //assertEquals(5, map.totalSize());

    map.remove("key2");
   // assertEquals(5, map.totalSize());

    assertEquals(0, map.size());

    map.insert("key3", "v");
    assertEquals(1, map.size());
    //assertEquals(5, map.totalSize());
    assertTrue(map.has("key3"));
  }

  @Test
  public void insertDeleteReInsert() {
    map.insert("key1", "v");
    map.remove("key1");
    map.insert("key1", "v");
    assertEquals(1, map.size());
  }

  @Test
  public void variousOps() {
    try {
      map.get("key1");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
    map.put("key1", "v");
    try {
      map.get("key2");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
    try {
      map.get(null);
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
    map.remove("key1");
    try {
      map.get("key1");
      fail("Failed to throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void insert1000Reverse() {
    for (int i = 999; i > -1; i--) {
      map.insert(Integer.toString(i), "v");
    }

    for (int i = 0; i < 1000; i++) {
      assertTrue(map.has(Integer.toString(i)));
      if (!map.has(Integer.toString(i))) {
        System.out.println(i);
      }
    }
  }

  @Test
  public void insert10000() {
    for (int i = 0; i < 10000; i++) {
      map.insert(Integer.toString(i), "v");
    }

    for (int i = 0; i < 10000; i++) {
      assertTrue(map.has(Integer.toString(i)));
    }
  }

  @Test
  public void insert10000Reverse() {
    for (int i = 9999; i > -1; i--) {
      map.insert(Integer.toString(i), "v");
    }

//    for (int i = 9999; i > -1; i--) {
//      assertTrue(map.has(Integer.toString(i)));
//      if (!map.has(Integer.toString(i))) {
//        System.out.println(i);
//      }
//    }
  }

  @Test
  public void insert100000() {
    for (int i = 0; i < 100000; i++) {
      map.insert(Integer.toString(i), "v");
    }

    for (int i = 0; i < 100000; i++) {
      assertTrue(map.has(Integer.toString(i)));
    }

  }

  @Test
  public void insert100000Reverse() {
    String[] vals = new String[100_000];
    for (int i = 99_999; i > -1; i--) {
      map.insert(Integer.toString(i), Integer.toString(i));
      vals[i] = Integer.toString(i); // vals stores the ints in reverse order.
    }

    for (int i = 0; i < 100000; i++) {
      assertTrue(map.has(Integer.toString(i)));
      assertTrue(map.get(Integer.toString(i)).equals(vals[i]));
    }

  }

  @Test
  public void insert10ReverseTestsHasAndGet() {
    String[] vals = new String[10];
    for (int i = 9; i > -1; i--) {
      map.insert(Integer.toString(i), Integer.toString(i));
      vals[i] = Integer.toString(i); // vals stores the ints in FORWARD order
    }
    for (int i = 0; i < 10; i++) {
      assertTrue(map.has(Integer.toString(i)));
      String curHashStr = map.get(Integer.toString(i));
      String curArrStr = vals[i];
      assertEquals(curHashStr, curArrStr);
    }
  }

  @Test
  public void insert100ReverseAndCheckValsTestsHasAndGet() {
    String[] vals = new String[100];
    for (int i = 99; i > -1; i--) {
      map.insert(Integer.toString(i), Integer.toString(i));
      vals[i] = Integer.toString(i); // vals stores the ints in FORWARD order
    }
    // vals lists them in ORDER

//    for (int i = 0; i < vals.length; i++) {
//      System.out.println(vals[i]);
//    }

    for (int i = 0; i < 100; i++) {
      assertTrue(map.has(Integer.toString(i)));
      String curHashStr = map.get(Integer.toString(i));
      String curArrStr = vals[i];
      assertEquals(curHashStr, curArrStr);
    }

  }

  @Test
  public void insert100_000RandomTestsHasAndGet() {
    // Create an ArrayList to store integers from 0 to 9
    List<Integer> randomIntegers = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      randomIntegers.add(i);
    }

    // Shuffle the ArrayList
    Collections.shuffle(randomIntegers);

    // Insert key-value pairs into the map in random order
    for (Integer i : randomIntegers) {
      map.insert(Integer.toString(i), Integer.toString(i));
    }

    // Verify the correctness of the inserted values
    for (Integer i : randomIntegers) {
      assertTrue(map.has(Integer.toString(i)));
      String curHashStr = map.get(Integer.toString(i));
      String curArrStr = Integer.toString(i);
      assertEquals(curHashStr, curArrStr);
    }
  }

  @Test
  public void insert100_000RandomTestsHasAndGetAndPut() { // same as before, but now "put" new values everywhere
    // Create an ArrayList to store integers from 0 to 9
    List<Integer> randomIntegers = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      randomIntegers.add(i);
    }

    // Shuffle the ArrayList
    Collections.shuffle(randomIntegers);

    // Insert key-value pairs into the map in random order
    for (Integer i : randomIntegers) {
      map.insert(Integer.toString(i), Integer.toString(i));
    }

    for (Integer i : randomIntegers) {
      map.put(Integer.toString(i), "value"); // testing put
    }

    // Verify the correctness of the inserted values
    for (Integer i : randomIntegers) {
      assertTrue(map.has(Integer.toString(i)));
      String curHashStr = map.get(Integer.toString(i));
      //String curArrStr = Integer.toString(i);
      String curArrStr = "value";
      assertEquals(curHashStr, curArrStr);
    }
  }


}