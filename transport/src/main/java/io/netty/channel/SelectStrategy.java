/*
 * Copyright 2016 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.channel;

import io.netty.util.IntSupplier;

/**
 *
 * Select strategy interface.
 *
 * 提供对 select loop 行为的控制策略， 例如： 如果有 events 要被立即执行， 一个阻塞的 select 操作会被延迟或者完全跳过
 *
 * Provides the ability to control the behavior of the select loop. For example a blocking select
 * operation can be delayed or skipped entirely if there are events to process immediately.
 */
public interface SelectStrategy {

    /**
     * 代表下一步应该 blocking select 操作
     *
     * Indicates a blocking select should follow.
     */
    int SELECT = -1;
    /**
     * 代表 IO loop 应该被重试， 下一步不会进行 blocking select 操作, 正常的 Nio 不会
     *
     * Indicates the IO loop should be retried, no blocking select to follow directly.
     */
    int CONTINUE = -2;
    /**
     * 代表在 IO loop 会在不阻塞的情况下 poll 新事件  NIO不支持
     *
     * Indicates the IO loop to poll for new events without blocking.
     */
    int BUSY_WAIT = -3;

    /**
     * The {@link SelectStrategy} can be used to steer the outcome of a potential select
     * call.
     *
     * @param selectSupplier The supplier with the result of a select result.
     * @param hasTasks true if tasks are waiting to be processed.
     * @return {@link #SELECT} if the next step should be blocking select {@link #CONTINUE} if
     *         the next step should be to not select but rather jump back to the IO loop and try
     *         again. Any value >= 0 is treated as an indicator that work needs to be done.
     */
    int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks) throws Exception;
}
