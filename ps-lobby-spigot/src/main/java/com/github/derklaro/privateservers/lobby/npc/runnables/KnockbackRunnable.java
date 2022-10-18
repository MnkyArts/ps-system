/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020-2022 Pasqual K. and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.derklaro.privateservers.lobby.npc.runnables;

import com.github.derklaro.privateservers.api.Plugin;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.derklaro.privateservers.api.configuration.NpcConfiguration;
import com.github.derklaro.privateservers.lobby.npc.DefaultNpcManager;
import com.github.juliarn.npc.NPC;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KnockbackRunnable {

  private final DefaultNpcManager npcManager;

  public KnockbackRunnable(Configuration configuration, DefaultNpcManager npcManager, Plugin plugin) {
    this.npcManager = npcManager;
    this.schedule(configuration.getNpcConfiguration().getKnockbackConfiguration(), plugin);
  }

  protected void schedule(@NotNull NpcConfiguration.KnockbackConfiguration configuration, @NotNull Plugin plugin) {
    if (configuration.isEnabled() && configuration.getKnockbackDistance() > 0 && configuration.getKnockbackStrength() > 0) {
      double distance = configuration.getKnockbackDistance();
      double strength = configuration.getKnockbackStrength();

      plugin.getTaskManager().scheduleSyncRepeatingTask(() -> {
        for (NPC npc : this.npcManager.getNpcPool().getNPCs()) {
          npc.getLocation().getWorld()
            .getNearbyEntities(npc.getLocation(), distance, distance, distance)
            .stream()
            .filter(entity -> entity instanceof Player && !entity.hasPermission(configuration.getBypassPermission()))
            .filter(entity -> npc.isShownFor((Player) entity))
            .forEach(entity -> entity.setVelocity(entity.getLocation().toVector()
              .subtract(npc.getLocation().toVector())
              .normalize()
              .multiply(strength)
              .setY(0.2D))
            );
        }
      }, 20, 5);
    }
  }
}
