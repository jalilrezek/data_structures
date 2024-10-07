package hw7.hashing;

import hw7.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ChainingHashMap<K, V> implements Map<K, V> {

  private ArrayList<Pair<K, V>>[] buckets;

  private int numElem;

  private int arrSize;

  private final double loadFactor = 0.67;

  private int[] primes = {5, 11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853,
      25717, 51437,102877, 205759, 411527, 823117, 1646237,3292489, 6584983, 13169977};

  private int primesIndex;

  /**
   * Constructor for an OpenAddressingHashMap.
   **/
  public ChainingHashMap() {
    arrSize = primes[primesIndex];
    this.buckets = (ArrayList<Pair<K,V>>[]) new ArrayList[arrSize];
    for (int i = 0; i < arrSize; i++) {
      buckets[i] = new ArrayList<>(); // diamond inference - equivalent to saying ArrayList<Pair<K,V>>();
    }
    this.numElem = 0;
  }

  /**
   * Get index for a key.
   * @param k the key whose index to calculate
   **/
  private int getIndex(K k) {
    return Math.abs(k.hashCode()) % arrSize;
  }

  /**
   * Initial check in rehash() to determine what new size to give the array.
   **/
  private void resizingCheck() {
    primesIndex++;
    if (primesIndex < primes.length) {
      arrSize = primes[primesIndex];
    } else {
      arrSize *= 2;
    }
  }

  /**
   * Reassign the elements of the hash map to a new array resized accordingly.
   **/
  private void rehash() {
    resizingCheck();

    ArrayList<Pair<K,V>>[] tmp =  new ArrayList[arrSize]; // alt: tmp = (ArrayList<Pair<K,V>>[]) new ArrayList[M];
    for (int i = 0; i < tmp.length; i++) {
      tmp[i] = new ArrayList<>();
    }
    ArrayList<Pair<K,V>>[] orig = buckets;
    buckets = tmp;

    for (int i = 0; i < orig.length; i++) {
      if (!orig[i].isEmpty()) { // if empty, skip over it
        for (Pair<K, V> element : orig[i]) {
          K curKey = element.getKey();
          V curVal = element.getValue();
          insertHelper(curKey, curVal);
        }
      }
    }

  }

  /**
   * Insert a new key value pair into the hash map.
   * pre: k is not null and hashmap does not already have k
   * @param k the key to insert
   * @param v the value to insert
   **/

  private void insertHelper(K k, V v) {
    int index = getIndex(k);
    Pair pairToAdd = new Pair<>(k, v);
    buckets[index].add(pairToAdd);


  }

  /**
   * Remove a key from the hashmap.
   * @param k the key to remove
   **/
  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    // TODO Implement Me!
    if (k == null || has(k)) {
      throw new IllegalArgumentException();
    }

    int futureNumElem = numElem + 1;

    if (((double) futureNumElem) / arrSize > loadFactor) {
      rehash();
    }
    insertHelper(k, v);
    numElem++;

  }

  /**
   * Remove a key from the hashmap.
   * @param k the key to remove
   **/
  @Override
  public V remove(K k) throws IllegalArgumentException {
    // TODO Implement Me!
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);

    for (Pair<K,V> pair : buckets[index]) {
      if (pair.getKey().equals(k)) { // duplicates possible? Insert() should prevent it.
        buckets[index].remove(pair);
        numElem--;
        return pair.getValue();
      }
    }

    throw new IllegalArgumentException(); // doesn't have it

    //    assert false; // shouldn't reach here.
    //    return null;

    //throw new IllegalArgumentException(); // didn't have it. No need to check has()

  }

  /**
   * Update the value at some existing key.
   * @param k the key to search for
   * @param v the new value to put at that key
   **/
  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    // TODO Implement Me!
    if (k == null || !has(k)) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k); // element is in there, around this index

    for (Pair<K,V> pair : buckets[index]) {
      if (pair.getKey().equals(k)) {
        pair.updateVal(v); // "pair" was in grey when I just did "pair = new Pair<>(k, v)". It wouldn't have worked
        return;
      }
    }

  }

  /**
   * Get the value stored at some key.
   * @param k the key whose value to get
   **/
  @Override
  public V get(K k) throws IllegalArgumentException {
    // TODO Implement Me!
    if (k == null || !has(k)) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);

    for (Pair<K,V> pair : buckets[index]) {
      if (pair.getKey().equals(k)) {
        return pair.getValue();
      }
    }

    throw new IllegalArgumentException(); //  shouldn't be able to reach this because has() was checked already

  }

  /**
   * See if the hashmap contains some key.
   * @param k key to search for
   **/
  @Override
  public boolean has(K k) {
    // TODO Implement Me!
    if (empty()) {
      return false;
    }
    if (k == null) {
      throw new IllegalArgumentException(); // unchecked
    }

    int index = getIndex(k);

    for (Pair<K,V> pair : buckets[index]) {
      if (pair.getKey().equals(k)) {
        return true;
      }
    }
    // reached end of corresponding bucket & didn't find it.
    return false;
  }

  /**
   * Return the number of valid entries in the array (i.e. not including tombstones)
   * @return number of valid entries
   **/
  @Override
  public int size() {
    // TODO Implement Me!
    return numElem;
  }

  /**
   * See if the hashmap has no valid entries.
   * @return true if number of valid entries is zero, otherwise false
   **/
  public boolean empty() {
    return numElem == 0;
  }

  /**
   * A class to store key-value pairs.
   **/
  private static class Pair<K,V> {
    K key;
    V value;

    Pair(K k, V v) {
      this.key = k;
      this.value = v;
    }

    /**
     * Get the key of this pair.
     * @return key of the pair
     **/
    public K getKey() {
      return key;
    }

    /**
     * Get the value of this pair.
     * @return value of the pair
     **/
    public V getValue() {
      return value;
    }


    public void updateVal(V v) {
      this.value = v;
    }

  }

  @Override
  public Iterator<K> iterator() {
    return new ChainingIterator();
  }

  private class ChainingIterator<T> implements Iterator<T> {
    private int numValidEntriesVisited;

    private int numBucketsTraversed;

    private int numWithinBucket;

    /**
     * Some relevant checks and updating relevant indices when moving to a new bucket (ArrayList).
     **/
    private void newBucketTasks() {
      if (numBucketsTraversed >= buckets.length) { // hasNext() should check for this, but just in case
        throw new NoSuchElementException(); // but do not check after already incrementing numTraversed or will
      } // get NoSuchElementException
      numBucketsTraversed++;
      numWithinBucket = 0; // reset for new bucket.
    }

    /**
     * Test if the hashmap has a next valid entry.
     * @return true if has another valid entry, else false
     **/
    @Override
    public boolean hasNext() {
      return numValidEntriesVisited < numElem;
    }

    /**
     * Return the next valid entry's key if it exists.
     * @return key of the next entry in the map
     **/
    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      while (buckets[numBucketsTraversed].isEmpty()) {
        newBucketTasks();
      }
      T returnVal = (T) buckets[numBucketsTraversed].get(numWithinBucket).getKey();
      numWithinBucket++;
      numValidEntriesVisited++;
      T checkStyleCompliantReturnVal = returnVal; // can't have >3 lines before first usage...
      // want to access indices before incrementation.

      if (numWithinBucket >= buckets[numBucketsTraversed].size()) { // goes at end?
        newBucketTasks();
      }

      return checkStyleCompliantReturnVal;

    }
  }

}
