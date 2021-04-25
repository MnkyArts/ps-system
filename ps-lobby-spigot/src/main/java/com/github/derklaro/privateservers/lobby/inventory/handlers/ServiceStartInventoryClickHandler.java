/*
 * This file is part of ps-system, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2020 - 2021 Pasqual Koschmieder and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.derklaro.privateservers.lobby.inventory.handlers;

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.configuration.CloudServiceConfiguration;
import com.github.derklaro.privateservers.api.configuration.Configuration;
import com.github.derklaro.privateservers.api.configuration.InventoryConfiguration;
import com.github.derklaro.privateservers.lobby.inventory.ClickHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ServiceStartInventoryClickHandler implements ClickHandler {

  private final CloudServiceManager cloudServiceManager;
  private final InventoryConfiguration.ServiceTypeStartInventory startConfiguration;

  public ServiceStartInventoryClickHandler(CloudServiceManager cloudServiceManager, Configuration configuration) {
    this.cloudServiceManager = cloudServiceManager;
    this.startConfiguration = configuration.getServiceTemplateStartItems();
  }

  @Override
  public boolean handleClick(@NotNull Player player, @NotNull Inventory inventory, @NotNull ItemStack itemStack, int slot) {
    for (InventoryConfiguration.ServiceItemMapping serviceItem : this.startConfiguration.getServiceItems()) {
      if (serviceItem.getItemLayout().getSlot() == slot) {
        this.startService(player, serviceItem);
        break;
      }
    }

    return true;
  }

  private void startService(@NotNull Player player, @NotNull InventoryConfiguration.ServiceItemMapping mapping) {
    if (HandlerUtils.canUse(player, mapping.getItemLayout())) {
      player.closeInventory();

      this.cloudServiceManager.createCloudService(
        mapping.getGroupName(),
        mapping.getTemplateName(),
        mapping.getTemplateBackend(),
        new CloudServiceConfiguration(
          false,
          mapping.isCopyAfterStop(),
          new ArrayList<>(),
          player.getUniqueId(),
          player.getName(),
          mapping.getGroupName(),
          mapping.getTemplateName(),
          mapping.getTemplateBackend(),
          false,
          false
        )
      ).thenRun(() -> player.sendMessage("Your service was created, you will be connected shortly.."));
      player.sendMessage("Your service got created, please wait a moment...");
    } else {
      HandlerUtils.notifyNotAllowed(player);
    }
  }
}