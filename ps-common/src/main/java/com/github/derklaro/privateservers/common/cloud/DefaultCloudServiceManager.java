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

package com.github.derklaro.privateservers.common.cloud;

import com.github.derklaro.privateservers.api.cloud.CloudServiceManager;
import com.github.derklaro.privateservers.api.cloud.service.CloudService;
import com.github.derklaro.privateservers.api.cloud.service.ServiceListener;
import com.github.derklaro.privateservers.common.util.Iterables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class DefaultCloudServiceManager implements CloudServiceManager, CloudServiceManager.Unsafe {

  protected final Set<CloudService> cloudServices = new CopyOnWriteArraySet<>();
  protected final Set<ServiceListener> serviceListeners = new CopyOnWriteArraySet<>();

  public DefaultCloudServiceManager() {
    this.cloudServices.addAll(this.getAllCurrentlyRunningPrivateServersFromCloudSystem());
  }

  @Override
  public @NotNull Unsafe getUnsafe() {
    return this;
  }

  @Override
  public @NotNull Optional<CloudService> getCurrentCloudService() {
    return this.getCloudServiceByUniqueId(this.getCurrentServiceUniqueID());
  }

  @Override
  public @NotNull Optional<CloudService> getCloudServiceByUniqueId(@NotNull UUID uniqueId) {
    return Iterables.first(this.cloudServices, cloudService -> cloudService.getServiceUniqueId().equals(uniqueId));
  }

  @Override
  public @NotNull Optional<CloudService> getCloudServiceByName(@NotNull String name) {
    return Iterables.first(this.cloudServices, cloudService -> cloudService.getName().equals(name));
  }

  @Override
  public @NotNull Optional<CloudService> getCloudServiceByOwnerUniqueId(@NotNull UUID ownerUniqueId) {
    return Iterables.first(this.cloudServices, cloudService -> cloudService.getOwnerUniqueId().equals(ownerUniqueId));
  }

  @Override
  public @NotNull Optional<CloudService> getCloudServiceByOwnerName(@NotNull String ownerName) {
    return Iterables.first(this.cloudServices, cloudService -> cloudService.getOwnerName().equals(ownerName));
  }

  @Override
  public @NotNull @UnmodifiableView Collection<CloudService> getCloudServices() {
    return Collections.unmodifiableSet(this.cloudServices);
  }

  @Override
  public void registerServiceListener(@NotNull ServiceListener listener) {
    this.serviceListeners.add(listener);
  }

  @Override
  public void unregisterServiceListener(@NotNull ServiceListener listener) {
    this.serviceListeners.remove(listener);
  }

  @Override
  public @NotNull @UnmodifiableView Collection<ServiceListener> getServiceListeners() {
    return Collections.unmodifiableSet(this.serviceListeners);
  }

  // CloudServiceManager.Unsafe

  @Override
  public void handleCloudServiceStart(@NotNull CloudService cloudService) {
    this.cloudServices.removeIf(service -> service.getServiceUniqueId().equals(cloudService.getServiceUniqueId()));
    this.cloudServices.add(cloudService);

    this.serviceListeners.forEach(listener -> listener.handleServiceRegister(cloudService));
  }

  @Override
  public void handleCloudServiceUpdate(@NotNull CloudService cloudService) {
    this.cloudServices.removeIf(service -> service.getServiceUniqueId().equals(cloudService.getServiceUniqueId()));
    this.cloudServices.add(cloudService);

    this.serviceListeners.forEach(listener -> listener.handleServerUpdate(cloudService));
  }

  @Override
  public void handleCloudServiceStop(@NotNull CloudService cloudService) {
    this.cloudServices.removeIf(service -> service.getServiceUniqueId().equals(cloudService.getServiceUniqueId()));
    this.serviceListeners.forEach(listener -> listener.handleServiceUnregister(cloudService));
  }

  // abstract methods

  @NotNull
  public abstract Collection<CloudService> getAllCurrentlyRunningPrivateServersFromCloudSystem();

  @NotNull
  public abstract UUID getCurrentServiceUniqueID();
}
