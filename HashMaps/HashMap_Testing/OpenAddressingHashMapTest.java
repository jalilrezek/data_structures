package hw7;

import hw7.hashing.OpenAddressingHashMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenAddressingHashMapTest extends MapTest {
  @Override
  protected Map<String, String> createMap() {
    return new OpenAddressingHashMap<>();
  }

  // All tests are mine


}