package com.gathertocraft.ironcore;

public interface TierSpec {
  int size();

  int rowLength();

  default int rowCount() {
    return size() / rowLength();
  }
}
