/*
 * Copyright 2016 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.idea.blaze.base.run;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.idea.blaze.base.dependencies.TargetInfo;
import com.google.idea.blaze.base.model.primitives.RuleType;
import com.google.idea.blaze.base.run.targetfinder.FuturesUtil;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Future;

/** Locates blaze rules which build a given source file. */
public interface SourceToTargetFinder {

  ExtensionPointName<SourceToTargetFinder> EP_NAME =
      ExtensionPointName.create("com.google.idea.blaze.SourceToTargetFinder");

  /**
   * Finds all rules of the given type 'reachable' from source file (i.e. with source included in
   * srcs, deps or runtime_deps).
   */
  Future<Collection<TargetInfo>> targetsForSourceFile(
      Project project, File sourceFile, Optional<RuleType> ruleType);

  /**
   * Iterates through the all {@link SourceToTargetFinder}'s, returning a {@link Future}
   * representing the first non-empty result.
   *
   * <p>Future returns null if there was no non-empty result found.
   */
  static ListenableFuture<Collection<TargetInfo>> findTargetInfoFuture(
      Project project, File sourceFile, Optional<RuleType> ruleType) {
    Iterable<Future<Collection<TargetInfo>>> futures =
        Iterables.transform(
            Arrays.asList(EP_NAME.getExtensions()),
            f -> f.targetsForSourceFile(project, sourceFile, ruleType));
    return FuturesUtil.getFirstFutureSatisfyingPredicate(futures, t -> t != null && !t.isEmpty());
  }

  /**
   * Iterates through all {@link SourceToTargetFinder}s, returning the first immediately available,
   * non-empty result.
   */
  static Collection<TargetInfo> findTargetsForSourceFile(
      Project project, File sourceFile, Optional<RuleType> ruleType) {
    Collection<TargetInfo> targets =
        FuturesUtil.getIgnoringErrors(findTargetInfoFuture(project, sourceFile, ruleType));
    return targets != null ? targets : ImmutableList.of();
  }
}