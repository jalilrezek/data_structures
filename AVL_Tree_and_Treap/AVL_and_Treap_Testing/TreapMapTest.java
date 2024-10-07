package hw6;

import hw6.bst.TreapMap;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to Treap.
 */
@SuppressWarnings("All")
public class TreapMapTest extends BinarySearchTreeMapTest {

  @Override
  protected Map<String, String> createMap() {
    return new TreapMap<>();
  }

  // TODO Add tests
  //  (think about how you might write tests while randomness is involved in TreapMap implementation!)

  @Test
  public  void getRandValsFromVariousSeeds() {
    Random ra = new Random(8);
    TreapMap<String, String> myMap = new TreapMap<>(ra); // you feed it the Random object as a parameter and can choose seed

//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());
//    System.out.println(myMap.getRandVal());

    /* Results for seed value 0:
 -1155484576
-723955400
1033096058
-1690734402
-1557280266
1327362106
-1930858313
502539523
-1728529858
-938301587
1431162155
1085665355
1654374947
-1661998771
-65105105
     */

//    results for seed value -2: -- this is good for testing one single rotation caused by insert, remove, etc
//    1155484575
//    732419875
//            -675488479
//            -1086884149
//    394222435
//            -542838123
//            -1571278228
//            -612342734
//    1958313011
//    546887676
//    571825337
//    1760109434
//    2054170244
//            -2048340904
//    546951751

  }

  @Test
  public void insertCausesThreeLeftRotations() {

    // I traced out the results by hand
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("A", "a");
    myMap.insert("B", "b");
    myMap.insert("C", "c");
    myMap.insert("D", "b");



    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "D:b:-1690734402\n" +
                    "A:a:-1155484576 null\n" +
                    "null B:b:-723955400 null null\n" +
                    "null null null C:c:1033096058 null null null null"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
  }

  @Test
  public void insertCausesThreeLeftRotationsThenRemoveOne() {

    // I traced out the results by hand, and also traced out the removal by hand. It's the same as the previous
    // test except the removal was also done. I just wanted to specify that for one test, it was just insertions,
    // and for this one, we are testing removal as well.
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("A", "a");
    myMap.insert("B", "b");
    myMap.insert("C", "c");
    myMap.insert("D", "b");

    myMap.remove("D");


    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "A:a:-1155484576\n" +
                    "null B:b:-723955400\n" +
                    "null null null C:c:1033096058"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
  }


  @Test
  public void insertOneRightRotation() {
    Random ra = new Random(-2);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("W", "c");
    myMap.insert("V", "d");
  }

  @Test
  public void insertOneLeftRotation() {
    Random ra = new Random(-2);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("M", "a");
    //myMap.insert("W", "c");
    myMap.insert("V", "d");
  }


  @Test
  public void insertionsCauseTwoRightRotations() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("M", "m");
    myMap.insert("L", "b");
    myMap.insert("R", "b");
    myMap.insert("B", "b");


    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "B:b:-1690734402\n" +
                    "null M:m:-1155484576\n" +
                    "null null L:b:-723955400 R:b:1033096058"
    };
    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());

  }

  // this tests two things: removing a node and causing one right rotation;
  // and removing a node which has exactly one child
  @Test
  public void removeNodeWithOneChildCausesOneRightRotation() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("Z", "a");
    myMap.insert("Y", "b");
    myMap.insert("C", "c");

//    Now looks like this. Y has one child.
//    Z:a:-1155484576
//    Y:b:-723955400 null
//    C:c:1033096058 null null null

    myMap.remove("Y");


    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "Z:a:-1155484576\n" +
                    "C:c:1033096058 null"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
  }

  // this tests two things: removing a node and causing one left rotation;
  // and removing a node which has exactly one child
  @Test
  public void removeNodeWithOneChildCausesOneLeftRotation() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("A", "a");
    myMap.insert("B", "b");
    myMap.insert("C", "c");

//    Now looks like this. B has one child.
//    A:a:-1155484576
//    null B:b:-723955400
//    null null null C:c:1033096058

    myMap.remove("B");


    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "A:a:-1155484576\n" +
                    "null C:c:1033096058"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
  }

  // this tests two things: removing a node and causing two separate rotations;
  // and removing a node which has exactly two children
  @Test
  public void removeNodeWithTwoChildrenCausesOneLeftThenOneRightRotation() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("Y", "a");
    myMap.insert("Z", "b");
    myMap.insert("C", "c");


    myMap.remove("Y");


    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "Z:b:-723955400\n" +
                    "C:c:1033096058 null"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
  }

  // this tests two things: removing a node and causing two separate rotations;
  // and removing a node which has exactly two children
  // Note: I thought I might want to test just one kind of rotation upon removal of a node with two children,
  // but based on how treap sinks the removed node down to the bottom, it will inevitably cause at least two rotations
  // one will be a right rotation and the other must be a left rotation.
  @Test
  public void removeNodeWithTwoChildrenCausesOneRightThenOneLeftRotation() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("Y", "a");

    myMap.insert("Z", "b");

    myMap.insert("C", "c");


    myMap.remove("Y");


    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "Z:b:-723955400\n" +
                    "C:c:1033096058 null"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
  }

  @Test
  public void updateCausesNoStructuralChange() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("Y", "a");

    myMap.insert("Z", "b");
    //System.out.println(myMap.toString());

    myMap.insert("C", "c");
    //System.out.println(myMap.toString());

    String orig = myMap.toString();


    myMap.put("C", "z");


    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "Y:a:-1155484576\n" +
                    "C:z:1033096058 Z:b:-723955400"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
  }

  // none of the insertions done here should cause any structural change to happen
  @Test
  public void insertCausesNoStructuralChange() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("Y", "a");

    myMap.insert("Z", "b");

    myMap.insert("C", "c");



    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "Y:a:-1155484576\n" +
                    "C:c:1033096058 Z:b:-723955400"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
  }

  // for the treap, when removing, to preserve the min heap property, the only rotations that are ever done, if any,
  // are done as we sink the deleted node down successive levels until it's a leaf. There are no other rotations
  // necessary as we recurse back up towards the root - this is unlike the case for the AVL Tree.
  // So, the only removal that will not cause a structural change is the removal of a leaf.
  @Test
  public void removeLeafCausesNoStructuralChange() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("Y", "a");

    myMap.insert("Z", "b");

    myMap.insert("C", "c");

    myMap.remove("Z"); // remove this leaf.



    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "Y:a:-1155484576\n" +
                    "C:c:1033096058 null"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
  }

  // this test demonstrates that removing a node which has one child does not have lead to structural rotations
  // higher up in the tree
  @Test
  public void removeNodeWithOneChildDoesNotAffectStructure() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("Y", "a");

    myMap.insert("Z", "b");
    //System.out.println(myMap.toString());

    myMap.insert("C", "c");
    //System.out.println(myMap.toString());

    myMap.insert("a", "dj");
    myMap.insert("A", "b");
    myMap.insert("CA", "d");
    myMap.insert("Zb", "a");
    myMap.insert("Yb", "d"); // "C" has only one child, "CA".

//    Looks like below. Now demonstrate that removing C doesn't change the rest of the sturcture at all besides
    // replacing it with its child, "CA"
//    Zb:a:-1930858313
//    A:b:-1557280266 a:dj:-1690734402
//    null Y:a:-1155484576 null null
//    null null C:c:1033096058 Z:b:-723955400 null null null null
//    null null null null null CA:d:1327362106 Yb:d:502539523 null null null null null null null null null

    //System.out.println(myMap.toString());

    myMap.remove("C"); // this node has only one child. Removing it should replace it with its child, but because
    // of the continual maintenance of the min heap property, it should not result in any other structural changes
    // leaving the rest of the tree unchanged.



    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "Zb:a:-1930858313\n" +
                    "A:b:-1557280266 a:dj:-1690734402\n" +
                    "null Y:a:-1155484576 null null\n" +
                    "null null CA:d:1327362106 Z:b:-723955400 null null null null\n" +
                    "null null null null null null Yb:d:502539523 null null null null null null null null null"
    };

    assertEquals((String.join("\n", expected) + "\n"), myMap.toString());
    // as desired, the resultant string shown above as "expected" is the exact same structurally, other than having
    // replaced "C" with its only child, "CA"
  }


  @Test
  public void insertLeftRotationThenRightRotation() {
    Random ra = new Random(0);
    TreapMap<String, String> myMap = new TreapMap<>(ra);

    myMap.insert("M", "m");
    myMap.insert("B", "b");
    myMap.insert("R", "r");
    myMap.insert("C", "b");

  }



}