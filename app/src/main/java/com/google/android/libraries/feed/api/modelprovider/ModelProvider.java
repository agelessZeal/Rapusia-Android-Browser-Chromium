// Copyright 2018 The Feed Authors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.libraries.feed.api.modelprovider;

import android.support.annotation.IntDef;
import com.google.android.libraries.feed.api.common.MutationContext;
import com.google.search.now.feed.client.StreamDataProto.StreamSharedState;
import com.google.search.now.wire.feed.ContentIdProto.ContentId;
import java.util.List;

/**
 * A ModelProvider provides access to the Stream Model for the UI layer. It is populated by the
 * Session Manager. The ModelProvider is backed by a Session instance, there is a one-to-one
 * relationship between the ModelProvider and Session implementation. The Stream Library uses the
 * model to build the UI displayed to the user.
 */
public interface ModelProvider {
  /**
   * Provides the contentId of the lowest child of the root viewed by the user, or {@code null} if
   * no content with a ContentId has been seen.
   */
  interface ViewDepthProvider {
    /** Returns a contentId of the lowest root child seen by the user. */
    /*@Nullable*/
    String getChildViewDepth();
  }

  /** Returns a Mutator used to change the model. */
  ModelMutation edit();

  /**
   * This is called to invalidate the model. The SessionManager calls this to free all resources
   * held by this ModelProvider instance.
   */
  void invalidate();

  /**
   * This is called to detach the ModelProvider from the SessionManager. The Session will continue
   * to be persisted, but the SessionManager will drop connections to this ModelProvider. The
   * ModelProvider will enter a {@code State.INVALIDATED}. This will not call the {@link
   * ModelProviderObserver#onSessionFinished()}.
   */
  void detachModelProvider();

  /** Used by the session to raises a {@link ModelError} with the ModelProvider. */
  void raiseError(ModelError error);

  /**
   * Returns a {@code ModelFeature} which represents the root of the stream tree. Returns {@code
   * null} if the stream is empty.
   */
  /*@Nullable*/
  ModelFeature getRootFeature();

  /** Return a ModelChild for a String ContentId */
  /*@Nullable*/
  ModelChild getModelChild(String contentId);

  /**
   * Returns a {@code StreamSharedState} containing shared state such as the Piet shard state.
   * Returns {@code null} if the shared state is not found.
   */
  /*@Nullable*/
  StreamSharedState getSharedState(ContentId contentId);

  /**
   * Handle the processing of a {@code ModelToken}. For example start a request for the next page of
   * content. The results of handling the token will be available through Observers on the
   * ModelToken.
   */
  void handleToken(ModelToken modelToken);

  /**
   * Allow the stream to force a refresh. This will result in the current model being invalidated if
   * the requested refresh is successful.
   */
  void triggerRefresh();

  /** Defines the Lifecycle of the ModelProvider */
  @IntDef({State.INITIALIZING, State.READY, State.INVALIDATED})
  @interface State {
    /**
     * State of the Model Provider before it has been fully initialized. The model shouldn't be
     * accessed before it enters the {@code READY} state. You should register an Observer to receive
     * an event when the model is ready.
     */
    int INITIALIZING = 0;
    /** State of the Model Provider when it is ready for normal use. */
    int READY = 1;
    /**
     * State of the Model Provider when it has been invalidated. In this mode, the Model is no
     * longer valid and methods will fail.
     */
    int INVALIDATED = 2;
  }

  /** Returns the current state of the ModelProvider */
  @State
  int getCurrentState();

  /** A String which represents the session bound to the ModelProvider. */
  /*@Nullable*/
  String getSessionToken();

  /**
   * Returns a List of ModelChild for the root. These children may not be bound. This is not
   * intended to be used by the Stream to access children directly. Instead the ModelCursor should
   * be used.
   */
  List<ModelChild> getAllRootChildren();

  /** Allow the Stream to provide a RemoveTracking based upon mutation context. */
  interface RemoveTrackingFactory<T> {

    /**
     * Returns the {@link RemoveTracking}, if this returns {@code null} then no remove tracking will
     * be preformed on this ModelProvider mutation.
     */
    /*@Nullable*/
    RemoveTracking<T> create(MutationContext mutationContext);
  }

  /** Called by the stream to set the {@link RemoveTrackingFactory}. */
  void enableRemoveTracking(RemoveTrackingFactory<?> removeTrackingFactory);

  /**
   * Register a {@link ModelProviderObserver} for changes on this container. The types of changes
   * would include adding or removing children or updates to the metadata payload.
   */
  void registerObserver(ModelProviderObserver observer);

  /** Remove a registered observer */
  void unregisterObserver(ModelProviderObserver observer);
}
