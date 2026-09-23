package anner.ironchest.blocks;

public interface TierSpec {
  int size();

  int rowLength();

  default int rowCount() {
    return size() / rowLength();
  }
}
