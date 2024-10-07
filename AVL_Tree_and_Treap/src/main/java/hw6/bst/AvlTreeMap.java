package hw6.bst;

import hw6.OrderedMap;
import java.util.Iterator;
import java.util.Stack;

// I think all the core operations are O(lg n) because they all rely on recursive calls. So,
// they are only looking at the right node based on its size (comparisons, if less, go left, if
// more, go right etc)

/**
 * Map implemented as an AVL Tree.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class AvlTreeMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;
  private int size; // I added this

  /**
   * Complete a right rotation.
   * @param curRoot node to execute rotation around
   * @return rotated node
   **/
  private Node<K, V> rightRotation(Node<K, V> curRoot) {
    Node<K, V> child = curRoot.left;

    curRoot.left = child.right;
    child.right = curRoot;

    curRoot.height = setHeight(curRoot);
    child.height = setHeight(child);

    setBF(curRoot);
    setBF(child);
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
    child.left = curRoot;

    curRoot.height = setHeight(curRoot);
    child.height = setHeight(child);

    setBF(curRoot);
    setBF(child);

    curRoot = child;

    return curRoot;
  }

  /**
   * Complete a right-left rotation.
   * @param curRoot node to execute rotation around
   * @return rotated node
   **/
  private Node<K,V> rlRotation(Node<K, V> curRoot) {
    curRoot.right = rightRotation(curRoot.right);
    return leftRotation(curRoot); // would be called as "node = RLRotation(node)"
  }

  /**
   * Complete a left-right rotation.
   * @param curRoot node to execute rotation around
   * @return rotated node
   **/
  private Node<K,V> lrRotation(Node<K, V> curRoot) {
    curRoot.left = leftRotation(curRoot.left);
    return rightRotation(curRoot);
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
   * See which rotation to execute, if any, and refer to appropriate rotation function.
   * @param curRoot the node to test
   * @return rotated node, via rotation functions; or unchanged node if no rotation needed
   **/
  private Node<K,V> executeRotations(Node<K,V> curRoot) {
    //System.out.println("entered execute rotations");
    if (curRoot.bf == -2) { // must have a left child for this to be possible. Unbalanced to the left.
      if (curRoot.right.bf == -1 || curRoot.right.bf == 0) {
        return leftRotation(curRoot);
      } else  { // curRoot.left.bf must be +1
        return rlRotation(curRoot);
      }
    } else if (curRoot.bf == 2) { // must have a right child for this to be possible. Unbalanced to the right.
      if (curRoot.left.bf == 1 || curRoot.left.bf == 0) {
        return rightRotation(curRoot);
      } else { // curRoot.right.bf must be -1
        return lrRotation(curRoot);
      }
    } else { // no rotation needed
      return curRoot;
    }
  }

  /**
   * Set height and balance factor for the node.
   * @param n the node whose attributes to set
   **/
  private void setAttributes(Node<K,V> n) {
    n.height = setHeight(n);
    setBF(n);
  }

  // must set the heights then the balance factors first. Then, do any rotations.
  // Then, set the heights then the balance factors again.  -- well, actually, this may not be necessary.
  // The reason is any time we do a subsequent operation, the first thing done will be to update the height and BF,
  // so it takes care of that for us. After balancing, in theory the tree may have incorrect attributes, but it corrects
  // them before any subsequent operations, so it should preserve correct structure and operations.
  // Because after doing a remove() or insert(),
  // the node might have changed characteristics. Then, remove() or insert() will call this function,
  // and we need to account for those changes before we check for any necessary rotations. Otherwise, we might
  // miss the need for a rotation when it was actually necessary.

  /**
   * Preliminary checks for executing rotations (null, leaf, or otherwise).
   * @param curRoot node to examine for whether it must be pushed to execute_rotations for potential rotation
   * @return potentially rotated node through execute_rotations, or unchanged node if it's a leaf or null
   **/
  private Node<K,V> checkRotations(Node<K,V> curRoot) {

    if (curRoot == null) {
      return null;
    } else if (is_leaf(curRoot)) {
      setAttributes(curRoot);
      return curRoot;
    } else {
      setAttributes(curRoot);
      return executeRotations(curRoot);
    }

  }

  /**
   * Set height of node.
   * @param n the node whose height to set
   * @return int representing the new height
   **/
  private int setHeight(Node<K,V> n) {
    if (n == null) { // node is null. This happens when remove() calls setHeight() on subtreeRoot.child
      return -1; // via checkRotations()
    } else if (n.left == null && n.right != null) {
      return n.right.height + 1;
    } else if (n.left != null && n.right == null) {
      // System.out.println("SetHeight() right null left not null. Left height: " + n.left.height);
      return n.left.height + 1;
    } else if (n.left == null && n.right == null) { // is a leaf
      return 0;
    } else { // both left and right are not null
      return Math.max(n.left.height, n.right.height) + 1;
    }
  }

  /**
   * Insert given key and value into subtree rooted at given node.
   * @param n node being changed according to the insertion
   * @param k the key of the node to insert
   * @param v the value to insert
   * @return changed subtree with a new node added.
   **/
  private Node<K, V> insert(Node<K, V> n, K k, V v) throws IllegalArgumentException {

    if (n == null) {
      return new Node<>(k, v); // height and bf initialized to zero
    }

    int cmp = k.compareTo(n.key);
    if (cmp < 0) {
      n.left = insert(n.left, k, v);
      n = checkRotations(n);
    } else if (cmp > 0) { // recursive call, so this should work.
      n.right = insert(n.right, k, v);
      n = checkRotations(n);
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
    // TODO Implement Me! -- should be done
    if (k == null) {
      throw new IllegalArgumentException("cannot handle null key");
    }
    root = insert(root, k, v);
    size++;
  }

  /**
   * Remove a given node by its key.
   * @param k key of node to be removed
   * @return root of entire tree with the appropriate removal modification effected
   **/
  @Override
  public V remove(K k) throws IllegalArgumentException {
    // TODO Implement Me! -- should be done
    Node<K, V> node = findForSure(k);
    V value = node.value;
    root = remove(root, node);
    size--;
    return value;
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
      subtreeRoot = remove(subtreeRoot); // necessary if you try to remove root of entire tree.
      return checkRotations(subtreeRoot);
    } else if (cmp > 0) {
      subtreeRoot.left = remove(subtreeRoot.left, toRemove);

      subtreeRoot.left = checkRotations(subtreeRoot.left); // remember, after replacing with max in left if needed,
      // you recursively go back up and fix any errors. So, shouldn't need to check subtreeRoot.right
      subtreeRoot = checkRotations(subtreeRoot);
    } else {
      subtreeRoot.right = remove(subtreeRoot.right, toRemove);

      subtreeRoot.right = checkRotations(subtreeRoot.right); // per similar reasoning to the above, shouldn't need
      // to check subtreeRoot.left
      subtreeRoot = checkRotations(subtreeRoot);
    }

    return subtreeRoot;
  }

  /**
   * Remove a given node and return remaining tree (structural change).
   * @param node root of current subtree upon which removal is executed
   * @return modified subtree, or null if we've moved the node to the bottom until it became a leaf
   **/
  private Node<K, V> remove(Node<K, V> node) {
    // Easy if the node has 0 or 1 child.
    if (node.right == null) {
      return node.left;
    } else if (node.left == null) {
      return node.right;
    }

    // If it has two children, find the predecessor (max in left subtree),
    Node<K, V> toReplaceWith = max(node);
    // then copy its data to the given node (value change),
    node.key = toReplaceWith.key;
    node.value = toReplaceWith.value;
    // then remove the predecessor node (structural change).
    // toReplaceWith is "toRemove" in remove(subtreeRoot, toReplaceWith) function
    node.left = remove(node.left, toReplaceWith); // apply recursive function to the node's left,
    // because the node becomes max(node) (greatest in left subtree), meaning the original
    // node that was greatest in left subtree must be removed cuz it is now moved to where the
    // node we're removing to begin with, was.
    // The formula is applied to node.left because the greatest node in left subtree is in left
    // subtree.
    // It will return a left subtree with the necessary update, that the formerly greatest-node
    // in the left subtree is removed, with any structural changes needed having been applied by remove().

    return node;
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


  /**
   * Update the node with a given key to hold a new value.
   * @param k key value of node to be updated
   * @param v new value to add
   **/
  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    // TODO Implement Me! -- should be done
    Node<K, V> n = findForSure(k);
    n.value = v;
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

  /**
   * Obtain value held by a given node.
   * @param k key of node whose value to obtain
   * @return a value of desired node
   **/
  @Override
  public V get(K k) throws IllegalArgumentException {
    // TODO Implement Me! -- should be done
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
    // TODO Implement Me! -- should be done
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
    // TODO Implement Me! -- should be done
    return size;
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

  @Override
  public Iterator<K> iterator() {
    // TODO Implement Me! -- should be done
    return new InorderIterator();
  }

  /*** Do not change this function's name or modify its code. ***/
  @Override
  public String toString() {
    return BinaryTreePrinter.printBinaryTree(root);
  }

  private void setBF(Node<K,V> n) {
    if (n.left == null && n.right != null) {
      n.bf = -1 - n.right.height; // height of null is -1
    } else if (n.left != null && n.right == null) {
      n.bf = n.left.height - (-1);
    } else if (n.left == null && n.right == null) { // is a leaf
      n.bf = 0;
    } else {
      n.bf = n.left.height - n.right.height;
    }
  }

  /**
   * Feel free to add whatever you want to the Node class (e.g. new fields).
   * Just avoid changing any existing names, deleting any existing variables,
   * or modifying the overriding methods.
   *
   * <p>Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers.</p>
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;

    int height; // initialized to 0 by default which is what we want

    int bf; // initialized to 0 by default

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
    }

    @Override
    public String toString() {
      return key + ":" + value;
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

