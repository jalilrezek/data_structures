package hw6.bst;

import hw6.OrderedMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**
 * Map implemented as a Treap.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class TreapMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'rand'. ***/
  private static Random rand;
  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;

  private int size;

  /**
   * Make a TreapMap.
   */
  public TreapMap() {
    rand = new Random();
  }

  /**
  * Make a TreapMap with a set seed value for its Random attribute.
  * @param r Random object with a deliberate seed set (potentially)
  **/
  public TreapMap(Random r) {
    rand = r;
  } // overloaded constructor to set a specific random value.

  /**
   * Get random value corresponding to a priority.
   * @return next integer from random
   **/
  public int getRandVal() {
    return rand.nextInt();
  }

  /**
   * See if node is a leaf.
   * @param treeRoot the node to test
   * @return true if is a leaf, false if not
   **/
  private boolean is_leaf(Node<K,V> treeRoot) {
    return (treeRoot.left == null && treeRoot.right == null);
  }

  /**
   * Complete a right rotation.
   * @param curRoot node to execute rotation around
   * @return rotated node
   **/
  private Node<K, V> rightRotation(Node<K, V> curRoot) {
    //System.out.println("entered right rotation");
    Node<K, V> child = curRoot.left;

    curRoot.left = child.right;
    child.right = curRoot;

    curRoot = child;
    return curRoot;
  }

  /**
   * Complete a left rotation.
   * @param curRoot node to execute rotation around
   * @return rotated node
   **/
  private Node<K, V> leftRotation(Node<K, V> curRoot) {
    //System.out.println("\nEntered left rotation\n");
    Node<K, V> child = curRoot.right;
    curRoot.right = child.left;
    child.left = curRoot; // achieves "rotation" to the left

    curRoot = child;
    return curRoot;
  }


  /**
   * Insert given key and value into subtree rooted at given node.
   * @param n node being changed according to the insertion
   * @param k the key of the node to insert
   * @param v the value to insert
   * @return changed subtree with a new node added.
   **/
  private Node<K, V> insert(Node<K, V> n, K k, V v) throws IllegalArgumentException {
    // System.out.println("entered insert");
    if (n == null) {
      return new Node<>(k, v);
    }

    int cmp = k.compareTo(n.key);
    if (cmp < 0) {
      n.left = insert(n.left, k, v);
      if (n.left.priority < n.priority) { // min heap, so if child has lower priority, move it up
        n = rightRotation(n);
      }
    } else if (cmp > 0) {
      n.right = insert(n.right, k, v);
      if (n.right.priority < n.priority) {
        n = leftRotation(n);
      }
    } else {
      throw new IllegalArgumentException("duplicate key " + k);
    }

    return n;
  }

  /**
   * Insert a key-value pair into the map.
   * @param k key value to insert
   * @param v value to insert
   **/
  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    // TODO Implement Me!
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    root = insert(root, k, v);
    size++;
  }



  /**
   * Remove node with given key from subtree rooted at given node.
   * @param subtreeRoot root of subtree upon which removal is being executed
   * @param toRemove the node being removed
   * @return changed subtree with given key missing.
   **/
  private Node<K, V> remove(Node<K, V> subtreeRoot, Node<K, V> toRemove) {
    int cmp = subtreeRoot.key.compareTo(toRemove.key);
    if (cmp == 0) {
      return remove(subtreeRoot);
    } else if (cmp > 0) {
      subtreeRoot.left = remove(subtreeRoot.left, toRemove);
    } else {
      subtreeRoot.right = remove(subtreeRoot.right, toRemove);
    }

    return subtreeRoot;
  }

  /**
   * Remove a given node and return remaining tree (structural change).
   * @param node root of current subtree upon which removal is executed
   * @return modified subtree, or null if we've moved the node to the bottom until it became a leaf
   **/
  private Node<K, V> remove(Node<K, V> node) {

    if (is_leaf(node)) {
      return null;
    }

    //node.priority = -1; // flagged for destruction. But wait... do I really need to do this? No.
    boolean useLeftChild = false;
    //boolean useRightChild = false;


    if (node.left != null && node.right != null) {
      useLeftChild = node.left.priority <= node.right.priority ? true : false;
    } else if (node.right == null) {
      useLeftChild = true;
    }  // else, right child will be used

    if (useLeftChild) {
      node = rightRotation(node);
      node.right = remove(node.right);
    } else {
      node = leftRotation(node);
      node.left = remove(node.left);
    }

    return node;
  }

  /**
   * Remove a given node by its key.
   * @param k key of node to be removed
   * @return root of entire tree with the appropriate removal modification effected
   **/
  @Override
  public V remove(K k) throws IllegalArgumentException {
    // TODO Implement Me!
    Node<K, V> node = findForSure(k);
    V value = node.value;
    root = remove(root, node);
    size--;
    return value;
  }


  /**
   * Find the maximum-key node in the left subtree of a particular node.
   * @param node root of subtree in which to search for max key in left subtree
   * @return a node with maximum key in subtree rooted at given node.
   **/
  private Node<K, V> max(Node<K, V> node) {
    Node<K, V> curr = node.left;
    while (curr.right != null) {
      curr = curr.right;
    }
    return curr;
  }

  // Return node for given key,
  // throw an exception if the key is not in the tree.
  private Node<K, V> findForSure(K k) {
    Node<K, V> n = find(k);
    if (n == null) {
      throw new IllegalArgumentException("cannot find key " + k);
    }
    return n;
  }

  // Return node for given key.
  private Node<K, V> find(K k) {
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    Node<K, V> n = root;
    while (n != null) {
      int cmp = k.compareTo(n.key);
      if (cmp < 0) {
        n = n.left;
      } else if (cmp > 0) {
        n = n.right;
      } else {
        return n;
      }
    }
    return null;
  }

  /**
   * Update the node with a given key to hold a new value.
   * @param k key value of node to be updated
   * @param v new value to add
   **/
  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    // TODO Implement Me!
    Node<K, V> n = findForSure(k);
    n.value = v;
  }

  /**
   * Obtain value held by a given node.
   * @param k key of node whose value to obtain
   * @return a value of desired node
   **/
  @Override
  public V get(K k) throws IllegalArgumentException {
    // TODO Implement Me!
    Node<K, V> n = findForSure(k);
    return n.value;
  }

  /**
   * See if the tree contains a particular node.
   * @param k key to search for
   * @return true if tree contains node, false otherwise
   **/
  @Override
  public boolean has(K k) {
    // TODO Implement Me!
    if (k == null) {
      return false;
    }
    return find(k) != null;
  }

  /**
   * Find the size of the data structure (number of nodes it has).
   * @return number of key-value pairs in the data structure
   **/
  @Override
  public int size() {
    // TODO Implement Me!
    return size;
  }

  @Override
  public Iterator<K> iterator() {
    // TODO Implement Me!
    return new InorderIterator();
  }

  /*** Do not change this function's name or modify its code. ***/
  @Override
  public String toString() {
    return BinaryTreePrinter.printBinaryTree(root);
  }


  /**
   * Feel free to add whatever you want to the Node class (e.g. new fields).
   * Just avoid changing any existing names, deleting any existing variables,
   * or modifying the overriding methods.
   * Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers. Since this is
   * a node class for a Treap we also include a priority field.
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int priority;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      priority = generateRandomInteger();
    }


    // Use this function to generate random values
    // to use as node priorities as you insert new
    // nodes into your TreapMap.
    private int generateRandomInteger() {
      // Note: do not change this function!
      return rand.nextInt(); // if have a set seed value, then the nodes generated will have predictable priorities
    }

    @Override
    public String toString() {
      return key + ":" + value + ":" + priority;
    }

    @Override
    public BinaryTreeNode getLeftChild() {
      return left;
    }

    @Override
    public BinaryTreeNode getRightChild() {
      return right;
    }
  }

  // Iterative in-order traversal over the keys
  private class InorderIterator implements Iterator<K> {
    private final Stack<Node<K, V>> stack;

    InorderIterator() {
      stack = new Stack<>();
      pushLeft(root);
    }

    private void pushLeft(Node<K, V> curr) {
      while (curr != null) {
        stack.push(curr);
        curr = curr.left;
      }
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public K next() {
      Node<K, V> top = stack.pop();
      pushLeft(top.right);
      return top.key;
    }
  }
}
