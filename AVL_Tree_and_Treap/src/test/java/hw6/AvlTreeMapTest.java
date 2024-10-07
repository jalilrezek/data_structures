package hw6;

import hw6.bst.AvlTreeMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to AVL Tree.
 * Testing tool used: https://www.cs.usfca.edu/~galles/visualization/AVLtree.html
 */
@SuppressWarnings("All")
public class AvlTreeMapTest extends BinarySearchTreeMapTest {

  @Override
  protected Map<String, String> createMap() {
    return new AvlTreeMap<>();
  }

  // removal tests are in BSTMapTest.java, but need to test it remains balanced.



  @Test
  public void insertLeftRotation() {
    map.insert("1", "a");
    // System.out.println(avl.toString());
    // must print
    /*
        1:a
     */

    map.insert("2", "b");
    // System.out.println(avl.toString());
    // must print
    /*
        1:a,
        null 2:b
     */

    map.insert("3", "c"); // it must do a left rotation here!
    // System.out.println(avl.toString());
    // must print
    /*
        2:b,
        1:a 3:c
     */

    String[] expected = new String[]{
        "2:b",
        "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  // TODO Add more tests

  @Test
  public void updateCausesNoRotations() {
    map.insert("M", "a");
    map.insert("D", "d");
    map.insert("P", "p");

    map.insert("A", "b");

    map.put("M", "q"); // should not cause rotations. Updating the root in particular

    String[] expected = new String[]{
            "M:q\n" +
                    "D:d P:p\n" +
                    "A:b null null null"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertLeafCausesNoRotations() {
    map.insert("M", "a");
    map.insert("D", "d");
    map.insert("P", "p");

    map.insert("A", "b"); // should not cause any rotation

    String[] expected = new String[]{
            "M:a\n" +
                    "D:d P:p\n" +
                    "A:b null null null"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertRightRotation() {
    map.insert("3", "c");
    map.insert("2", "b");
    map.insert("1", "a");
   // map.insert("0", "d");

    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertRLRotation() {
    map.insert("1", "a");
    map.insert("3", "c");
    map.insert("2", "b");

    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertLRRotation() {
    map.insert("3", "c");
    //System.out.println("made it here");
    map.insert("1", "a");
    //System.out.println("made it here");
    map.insert("2", "b");
    //System.out.println("made it here");

    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void insertTwoLeftRotations() {
    map.insert("A", "a");
    map.insert("B", "b");
    map.insert("C", "c");
    map.insert("D", "d");

    String[] expected = new String[]{ // not sure if nulls should be there but looks similar to
            "B:b\n" + // online tool
                    "A:a C:c\n" +
                    "null null null D:d"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());

  }

  @Test
  public void insertTwoRightRotations() {
    map.insert("A", "a");
    map.insert("B", "b");
    map.insert("C", "c");
    map.insert("D", "d");

    String[] expected = new String[]{ // not sure if nulls should be there but looks similar to
            "B:b\n" + // online tool
                    "A:a C:c\n" +
                    "null null null D:d"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());

  }

  @Test
  public void insertRightThenLeftRotation() {
    map.insert("M", "a");
    map.insert("J", "b");
    map.insert("E", "c"); // right rotation should occur

    map.insert("Q", "d");
    map.insert("Z", "e"); // left rotation should occur

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "J:b\n" + // to online tool.
                    "E:c Q:d\n" +
                    "null null M:a Z:e"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());

  }

  @Test
  public void insertLeftThenRightRotation() {
    map.insert("J", "a");
    map.insert("M", "b");
    map.insert("Q", "c"); // left rotation should occur

    map.insert("E", "d");
    map.insert("A", "e"); // right rotation should occur

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "M:b\n" + // to the online tool.
                    "E:d Q:c\n" +
                    "A:e J:a null null"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());

  }

  @Test
  public void insertRightRotationThenRLRotation() {
    map.insert("J", "a");
    map.insert("M", "b");
    map.insert("Q", "c"); // left rotation triggered

    map.insert("A", "d");
    map.insert("D", "e"); // right-left triggered

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
                    "M:b\n" + // to online tool
                    "D:e Q:c\n" +
                    "A:d J:a null null"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }


  @Test
  public void insertLeftRotationThenRLRotation() {
    map.insert("M", "a");
    map.insert("Q", "b");
    map.insert("U", "c"); // left rotation triggered

    map.insert("Z", "d");
    map.insert("V", "e"); // right-left triggered

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "Q:b\n" + // to online tool
                    "M:a V:e\n" +
                    "null null U:c Z:d"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  // I couldn't find a way to test insertRightRotationWhenNodeHasOneChild(). No matter how you add
  // maybe you can't get to such a situation as to trigger a rotation when the node already has a child
  // because if adding the child to cause the rotation causes a rotation, then adding the other
  // child it already has would have caused a rotation already, and it no longer can cause a rotation

  @Test
  public void removeRootWhenRootIsOnlyNode() {
   // System.out.println(map.toString());
    map.insert("M", "a");
    map.remove("M");
   // System.out.println(map.toString());
    assertEquals("", map.toString());
  }


  @Test
  public void removeLeafCausesNoRotations() {
    map.insert("M", "a");
    map.insert("D", "b");
    map.insert("T", "c");
    map.insert("B", "d");

    map.remove("B");

    String[] expected = new String[]{
            "M:a\n" +
                    "D:b T:c"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeLeafCausesRightRotation() {
    map.insert("M", "a");
    map.insert("D", "b");
    map.insert("T", "c");
    map.insert("B", "d");

    map.remove("T"); // should trigger right rotation

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "D:b\n" + // to online tool.
                    "B:d M:a"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeLeafCausesLeftRotation() {
    map.insert("M", "a");
    map.insert("D", "b");
    map.insert("T", "c");
    map.insert("Z", "d");

    map.remove("D"); // should trigger right rotation

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "T:c\n" + // to online tool
                    "M:a Z:d"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeLeafCausesRLRotation() {
    map.insert("T", "a");
    map.insert("M", "b");
    map.insert("Z", "c");
    map.insert("P", "d");

    map.remove("Z");

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "P:d\n" + // to online tool
                    "M:b T:a"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeLeafCausesLRRotation() {
    map.insert("P", "a");
    map.insert("M", "b");
    map.insert("T", "c");
    map.insert("R", "d");

    map.remove("M");

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "R:d\n" + // to online tool
                    "P:a T:c"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeNodeWithOneChildCausesNoRotations() {
    map.insert("M", "a");
    map.insert("D", "b");
    map.insert("T", "c");


    map.insert("A", "d");
    map.insert("E", "d");
    map.insert("P", "d");

    map.remove("M"); // remove the root which has two children. No rotation caused

    String[] expected = new String[]{
            "E:d\n" +
                    "D:b T:c\n" +
                    "A:d null P:d null"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeNodeWithOneChildCausesRightLeftRotation() {
    map.insert("M", "a");
    map.insert("D", "b");
    map.insert("T", "c");


    map.insert("C", "d");
    map.insert("P", "d");
    map.insert("Z", "d");

    map.insert("Pa", "a");

    map.remove("D"); // this is a node with just one child. Its removal causes a  right-left rotation.

    String[] expected = new String[]{
            "P:d\n" +
                    "M:a T:c\n" +
                    "C:d null Pa:a Z:d"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeNodeWithOneChildCausesSingleLeftRotation() {
    map.insert("M", "a");
    map.insert("D", "b");
    map.insert("T", "c");


    map.insert("C", "d");
    map.insert("P", "d");
    map.insert("Z", "d");

    map.insert("Za", "a");

    map.remove("D"); // this is a node with just one child. Its removal causes a  right-left rotation.

    String[] expected = new String[]{
            "T:c\n" +
                    "M:a Z:d\n" +
                    "C:d P:d null Za:a"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());


  }

  // this covers two things. Firstly, removing a node with two children causes no rotations when it shouldn't.
  // secondly, this works when the node being removed is the root in particular.
  @Test
  public void removeRootWithTwoChildrenCausesNoRotations() { // no rotation will be needed
    map.insert("M", "a"); // if removing the root, because the heights of the children
    map.insert("D", "b"); // do not change enough or at all, not sure which.
    map.insert("T", "c");
    map.insert("B", "d");

    map.remove("M");

    String[] expected = new String[]{
            "D:b\n" +
                    "B:d T:c"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void removeNodeWithTwoChildrenCausesSingleLeftRotation() {
    map.insert("M", "a");
    map.insert("B", "b");
    map.insert("T", "c");

    map.insert("A", "a");
    map.insert("Da", "b");
    map.insert("Z", "c");

    map.insert("Db", "a"); // this puts us in a scenario where removing B would cause one left rotation

    map.remove("B"); // causes single left rotation

    String[] expected = new String[]{
            "M:a\n" +
                    "Da:b T:c\n" +
                    "A:a Db:a null Z:c"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }


  @Test
  public void removeNodeWithTwoChildrenCausesRLRotation() {
    map.insert("M", "a");
    map.insert("B", "b");
    map.insert("T", "c");

    map.insert("A", "a");
    map.insert("Da", "b");
    map.insert("Z", "c");

    map.insert("D", "a");

    map.remove("B"); // causes right-left rotation

    String[] expected = new String[]{
            "M:a\n" +
                    "D:a T:c\n" +
                    "A:a Da:b null Z:c"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }


  @Test
  public void insertRightRotationThenRemoveRightRotationComplex() {
    map.insert("M", "a");
    map.insert("E", "d");
    map.insert("Y", "b");
    map.insert("Z", "c");

    map.insert("J", "e");
    map.insert("C", "f");
    map.insert("A", "g"); // causes a single-right rotation. Map was fine up to here.

    map.remove("J"); // single right rotation is caused in right subtree of root

    String[] expected = new String[]{
            "M:a\n" +
                    "C:f Y:b\n" +
                    "A:g E:d null Z:c"};

    assertEquals((String.join("\n", expected) + "\n"), map.toString());

  }

  @Test
  public void insertLeftRotationThenRemoveLeftRotationComplex() {
    map.insert("M", "a");
    map.insert("P", "d");
    map.insert("B", "b");
    map.insert("A", "c");

    map.insert("N", "e");
    map.insert("U", "f");
    map.insert("V", "g"); // causes a single-left rotation. Map was fine up to here, when I was
    map.insert("W", "h"); // initially debugging; now, it does indeed match the online tool
    // even after removal.

    map.remove("P"); // single left rotation is caused in right subtree of root

    String[] expected = new String[]{
            "M:a\n" +
                    "B:b V:g\n" +
                    "A:c null N:e W:h\n" +
                    "null null null null null U:f null null"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void manyInsertionsTriggerLeftRightRotation() { // I think it's an LR check for sure
    map.insert("A", "a");
    map.insert("B", "a");
    map.insert("C", "a");
    map.insert("D", "a");
    map.insert("E", "a");
    map.insert("F", "a");
    map.insert("G", "a");
    map.insert("H", "a");
    map.insert("I", "a");
    map.insert("J", "a");
    map.insert("K", "a");

    map.insert("Ea", "a"); // triggers double rotation

    String[] expected = new String[]{
            "F:a\n" +
                    "D:a H:a\n" +
                    "B:a E:a G:a J:a\n" +
                    "A:a C:a null Ea:a null null I:a K:a"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  public void rightRotationThenLeftRotationFromRemovalOfRoot() {
    // we insert many elements, then remove the root of the entire tree. This removal causes a rotation in a subtree,
    // but also, by the time we recur up to the root, we need to check the root as well because it will need to be
    // rotated.
    map.insert("M", "a");
    map.insert("E", "a");
    map.insert("T", "a");
    map.insert("D", "a");
    map.insert("J", "a");
    map.insert("P", "a");
    map.insert("V", "a");

    map.insert("B", "a");
    map.insert("O", "a");
    map.insert("U", "a");
    map.insert("X", "a");
    map.insert("Y", "a");

    map.remove("M"); // a right rotation is triggered in the left subtree after deleting max node in left subtree,
    // and then a left rotation is triggered.
    // Based on online tool, it looks like my tree stops after just the first rotation and leaves it unbalanced.

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "T:a\n" +
                    "J:a V:a\n" +
                    "D:a P:a U:a X:a\n" +
                    "B:a E:a O:a null null null null Y:a"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());


  }

  @Test
  public void lrRotationThenLeftRotationFromRemovalOfRoot() {
    map.insert("M", "a");
    map.insert("E", "a");
    map.insert("T", "a");
    map.insert("D", "a");
    map.insert("J", "a");
    map.insert("P", "a");
    map.insert("V", "a");

    map.insert("Da", "a");
    map.insert("O", "a");
    map.insert("U", "a");
    map.insert("X", "a");
    map.insert("Y", "a");

    map.remove("M"); // a right rotation is triggered in the left subtree after deleting max node in left subtree,
    // and then a left rotation is triggered.
    // Based on online tool, it looks like my tree stops after just the first rotation and leaves it unbalanced.

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "T:a\n" +
                    "J:a V:a\n" +
                    "Da:a P:a U:a X:a\n" +
                    "D:a E:a O:a null null null null Y:a"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());



  }

  @Test
  public void rightRotationThenRLRotationFromRemovalOfRoot() {
    map.insert("M", "a");
    map.insert("E", "a");
    map.insert("T", "a");
    map.insert("D", "a");
    map.insert("J", "a");
    map.insert("P", "a");
    map.insert("V", "a");

    map.insert("B", "a");
    map.insert("O", "a");
    map.insert("Q", "a");
    map.insert("U", "a");

    map.insert("Oa", "a");

    map.remove("M"); // a right rotation is triggered in the left subtree after deleting max node in left subtree,
    // and then a left rotation is triggered.
    // Based on online tool, it looks like my tree stops after just the first rotation and leaves it unbalanced.

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "P:a\n" +
                    "J:a T:a\n" +
                    "D:a O:a Q:a V:a\n" +
                    "B:a E:a null Oa:a null null U:a null"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());

  }

  @Test
  public void lrRotationThenRLRotationFromRemovalOfRoot() { // two double-rotations occur.
    map.insert("M", "a");
    map.insert("E", "a");
    map.insert("T", "a");
    map.insert("D", "a");
    map.insert("J", "a");
    map.insert("P", "a");
    map.insert("V", "a");

    map.insert("Da", "a");
    map.insert("O", "a");
    map.insert("Pa", "a");
    map.insert("U", "a");
    map.insert("X", "a");

    map.insert("Oa", "a");

    map.remove("M"); // a right rotation is triggered in the left subtree after deleting max node in left subtree,
    // and then a left rotation is triggered.
    // Based on online tool, it looks like my tree stops after just the first rotation and leaves it unbalanced.

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "P:a\n" +
                    "J:a T:a\n" +
                    "Da:a O:a Pa:a V:a\n" +
                    "D:a E:a null Oa:a null null U:a X:a"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());

  }

  @Test
  public void complexManyOperations() {
    map.insert("M", "a");
    map.insert("B", "b");
    map.insert("T", "c");
    map.insert("J", "d");

    // map.remove("F"); // not present
    map.remove("J");

    map.insert("Q", "e");
    map.insert("S", "f"); // rotation

    map.remove("B"); // rotation
    map.remove("M");

    //map.insert("Q", "g"); // already present
    map.insert("Aa", "h");
    map.insert("B", "i"); // rotation
    map.insert("P", "j"); // rotation
    //map.insert("Q", "k");
    map.insert("R", "l"); // maybe rotation
    map.insert("C", "dk");
    map.insert("c", "di");

    map.remove("B");

    map.insert("r", "d"); // rotation
    map.insert("e", "f"); // rotation
    map.insert("A", "g");

    map.remove("A");
    // map.remove("z"); // not present
    map.remove("Q");

    map.insert("Q", "f"); // rotation

    //map.insert("e", "f"); // already present
    map.insert("z", "f");
    map.insert("Z", "f");

    map.remove("Q");

    String[] expected = new String[]{ // not sure if the "nulls" should be there but looks similar
            "S:f\n" +
                    "P:j c:di\n" +
                    "C:dk R:l T:c r:d\n" +
                    "Aa:h null null null null Z:f e:f z:f"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());

  }



}
