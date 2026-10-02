package com.gathertocraft.ironcore;

import java.util.Set;

/**
 * The shared material ladder every Iron container climbs.
 *
 * <p>Each tier declares the tiers it can be reached from directly — upgrades are strict, but merge
 * points accept several sources (crystal from diamond or emerald, netherite from diamond, emerald
 * or obsidian).
 */
public enum MaterialTier {
  WOOD(Set.of()),
  COPPER(Set.of(WOOD)),
  IRON(Set.of(COPPER)),
  GOLD(Set.of(IRON)),
  DIAMOND(Set.of(GOLD)),
  EMERALD(Set.of(GOLD)),
  CRYSTAL(Set.of(DIAMOND, EMERALD)),
  OBSIDIAN(Set.of(DIAMOND, EMERALD)),
  NETHERITE(Set.of(DIAMOND, EMERALD, OBSIDIAN));

  private final Set<MaterialTier> sources;

  MaterialTier(Set<MaterialTier> sources) {
    this.sources = sources;
  }

  public Set<MaterialTier> sources() {
    return this.sources;
  }
}
