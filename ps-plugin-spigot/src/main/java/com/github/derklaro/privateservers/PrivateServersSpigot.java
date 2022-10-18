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

package com.github.derklaro.privateservers;

import com.github.derklaro.privateservers.api.Plugin;
import com.github.derklaro.privateservers.api.cloud.CloudDetector;
import com.github.derklaro.privateservers.api.cloud.CloudSystem;
import com.github.derklaro.privateservers.api.module.ModuleLoader;
import com.github.derklaro.privateservers.api.task.TaskManager;
import com.github.derklaro.privateservers.api.translation.TranslationManager;
import com.github.derklaro.privateservers.commands.PrivateServerSystemInfoCommand;
import com.github.derklaro.privateservers.common.cloud.DefaultCloudSystemDetector;
import com.github.derklaro.privateservers.common.module.DefaultModuleLoader;
import com.github.derklaro.privateservers.event.BukkitEventUtil;
import com.github.derklaro.privateservers.event.CloudSystemPickedEvent;
import com.github.derklaro.privateservers.event.TranslationManagerSetupEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PrivateServersSpigot extends JavaPlugin implements Plugin {

  private static PrivateServersSpigot instance;

  public static PrivateServersSpigot getInstance() {
    return instance;
  }

  @Override
  public void onLoad() {
    instance = this;

    this.getModuleLoader().detectModules();
  }

  @Override
  public void onEnable() {
    this.getModuleLoader().loadModules(this, this.getClassLoader());
    this.getCloudSystemDetector().detectCloudSystem();

    CloudSystem cloudSystem = this.getCloudSystemDetector().getDetectedCloudSystem().orElse(null);
    if (cloudSystem == null) {
      this.getLogger().info("No cloud system detected, disabling plugin. Ensure you have a cloud system module added");
      this.getPluginLoader().disablePlugin(this);
      return;
    }

    TranslationManager translationManager = BukkitEventUtil.fireEvent(new TranslationManagerSetupEvent(
      new TranslationManager())).getTranslationManager();
    translationManager.initialize();

    PluginCommand systemInfoCommand = this.getCommand("pssinfo");
    if (systemInfoCommand != null) {
      PrivateServerSystemInfoCommand command = new PrivateServerSystemInfoCommand(this.getModuleLoader(), cloudSystem,
        this.getDescription());
      systemInfoCommand.setExecutor(command);
      systemInfoCommand.setTabCompleter(command);
    }

    Bukkit.getPluginManager().callEvent(new CloudSystemPickedEvent(cloudSystem));
  }

  @Override
  public void onDisable() {
    this.getModuleLoader().disableModules();
  }

  @Override
  public @NotNull TaskManager getTaskManager() {
    return SpigotTaskManager.INSTANCE;
  }

  @Override
  public @NotNull CloudDetector getCloudSystemDetector() {
    return DefaultCloudSystemDetector.DEFAULT_INSTANCE;
  }

  @Override
  public @NotNull ModuleLoader getModuleLoader() {
    return DefaultModuleLoader.INSTANCE;
  }
}
