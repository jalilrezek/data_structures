// numOccupiedArraySpots is reset to numElem after a rehashing operation, as appropriate.



package hw7.hashing;

import hw7.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashMap<K, V> implements Map<K, V> {
  private static final Pair<?, ?> TOMBSTONE = new Pair<>(null, null);
  private Pair[] table;
  private int numElem; // num elements. The "size" of our HashMap() (which doesn't count tombstones)

  private int numOccupiedArraySpots; // numElem plus num tombstones. This differs from "numElem" only in that
  // it is NOT decremented when we replace an entry with a tombstone.
  // this is TOTAL SIZE

  private int arrSize; // size of array

  private final double loadFactor = 0.67;

  private int[] primes = {5, 11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853,
      25717, 51437,102877, 205759, 411527, 823117, 1646237,3292489, 6584983, 13169977};

  private int primesIndex;


  /**
   * Constructor for an OpenAddressingHashMap.
   **/
  public OpenAddressingHashMap() {
    arrSize = primes[primesIndex];
    this.table = new Pair[arrSize]; // is the size up to me? TA said if you start with more than 5
    this.numOccupiedArraySpots = 0;
    this.numElem = 0; // you lose points. Growing is a big part of the assignment. Starting with large size
  } // reduces growth.

  /**
   * Get index for a key.
   * @param key the key whose index to calculate
   **/
  private int getIndex(K key) {
    int result = Math.abs(key.hashCode()) % arrSize;
    //System.out.println("In getIndex(). Result is: " + result + " M is: " + M);
    return result;
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

  private void rehashAndGrow() {
    resizingCheck();

    Pair[] tmp = new Pair[arrSize];

    for (int i = 0; i < table.length; i++) {
      if (table[i] != null && !table[i].equals(TOMBSTONE)) {
        K key = (K) (table[i].key);
        int indexInNewArray = getIndex(key);
        while (tmp[indexInNewArray] != null) { // can't have tombstones yet; it's newly made. So only check if valid entry is there
          indexInNewArray = (indexInNewArray + 1) % arrSize;
        }
        tmp[indexInNewArray] = table[i];
      }
    }
    table = tmp;
    numOccupiedArraySpots = numElem;
  }

  /**
   * Insert a new key value pair into the hash map.
   * @param k the key to insert
   * @param v the value to insert
   **/
  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null || has(k)) {
      throw new IllegalArgumentException();
    }
    numElem++;
    numOccupiedArraySpots++;

    if ((double) numOccupiedArraySpots / arrSize > loadFactor) {
      rehashAndGrow();
    }
    int index = getIndex(k);

    while (table[index] != null && !(table[index].equals(TOMBSTONE))) {
      index = (index + 1) % arrSize;
    }

    table[index] = new Pair<>(k, v);

  }

  /**
   * Remove a key from the hashmap.
   * @param k the key to remove
   **/
  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null || !has(k)) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);

    while (table[index] != null) {
      if (table[index] != TOMBSTONE && ((K) table[index].key).equals(k)) {
        // found it
        numElem--;
        V removedVal = (V) table[index].value;
        table[index] = TOMBSTONE;
        return removedVal;
      }
      index = (index + 1) % arrSize;
    }

    assert false; // shouldn't reach here
    return null;

    //throw new IllegalArgumentException(); // didn't have it.
  }

  /**
   * Update the value at some existing key.
   * @param k the key to search for
   * @param v the new value to put at that key
   **/
  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    if (k == null || !has(k)) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k); // element is in there, around this index
    for (int i = 0; i < arrSize; i++) {
      if (table[index] == null || table[index].equals(TOMBSTONE)) { // shouldn't be possible for it to be null
        // continue
      } else if (((K) table[index].key).equals(k)) {
        table[index] = new Pair<>(k, v);
        return;
      }
      index = (index + 1) % arrSize;
    }
  }

  /**
   * Get the value stored at some key.
   * @param k the key whose value to get
   **/
  @Override
  public V get(K k) throws IllegalArgumentException {
    if (k == null || !has(k)) {
      throw new IllegalArgumentException();
    }
    int index = getIndex(k);

    for (int i = 0; i < arrSize; i++) {
      if (table[index].equals(TOMBSTONE) || table[index] == null) { // shouldn't be possible for it to be null
        // continue
      } else if (((K) table[index].key).equals(k)) {
        return (V) table[index].value;
      }
      index = (index + 1) % arrSize;
    }
    assert false; // should not reach this.
    return null;
  }

  /**
   * See if the hashmap contains some key.
   * @param k key to search for
   **/
  @Override
  public boolean has(K k) {
    if (empty()) {
      return false;
    }
    if (k == null) {
      throw new IllegalArgumentException(); // unchecked
    }

    int index = getIndex(k);

    for (int i = 0; i < arrSize; i++) {
      if (table[index] == null) {
        return false;
      } else if (table[index].equals(TOMBSTONE)) {
        // key is null, so cannot do ".equals(k)". Continue
      } else if (((K) table[index].key).equals(k)) {
        return true;
      }
      index = (index + 1) % arrSize;
    }
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
      return this.key;
    }
  }

  @Override
  public Iterator<K> iterator() {
    // TODO Implement Me!
    return new OpenAddressingIterator<>();
  }

  private class OpenAddressingIterator<T> implements Iterator<T> {
    private int numValidEntriesVisited;
    private int numTotalIndicesVisited;

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

      while (table[numTotalIndicesVisited] == null || table[numTotalIndicesVisited].equals(TOMBSTONE)) {
        numTotalIndicesVisited++;
      }

      numValidEntriesVisited++;

      T returnVal = (T) table[numTotalIndicesVisited].getKey();
      numTotalIndicesVisited++; // so that we start the next round on the next index not on the one we just
      // returned from
      return returnVal;

    }
  }


}
