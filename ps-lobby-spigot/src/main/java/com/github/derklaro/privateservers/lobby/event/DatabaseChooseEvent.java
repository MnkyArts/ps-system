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

package com.github.derklaro.privateservers.lobby.event;

import com.github.derklaro.privateservers.lobby.npc.database.NpcDatabase;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DatabaseChooseEvent extends Event {

  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final String databaseName;
  private NpcDatabase database;

  public DatabaseChooseEvent(NpcDatabase database, String databaseName) {
    this.database = database;
    this.databaseName = databaseName;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public NpcDatabase getDatabase() {
    return this.database;
  }

  public void setDatabase(NpcDatabase database) {
    this.database = database;
  }

  public String getDatabaseName() {
    return this.databaseName;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
