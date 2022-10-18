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

package com.github.derklaro.privateservers.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * Holds some constants which may be removed or change from version to version. Don't feel save if you use this
 * class for your plugin!
 */
@ApiStatus.Internal
public final class Constants {

  public static final String WHITELIST_JOIN_PERM = "ps.whitelist.bypass";
  public static final String WHITELIST_COMMAND_USE_PERM = "ps.command.whitelist";
  public static final String VISIBILITY_COMMAND_USE_PERM = "ps.command.visibility";
  public static final String SERVER_INFO_COMMAND_USE_PERM = "ps.command.server.info";
  public static final String SYSTEM_INFO_COMMAND_USE_PERM = "ps.command.system.info";

  private Constants() {
    throw new UnsupportedOperationException();
  }
}
