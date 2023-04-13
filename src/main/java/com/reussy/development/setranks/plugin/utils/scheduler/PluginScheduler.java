package com.reussy.development.setranks.plugin.utils.scheduler;

public interface PluginScheduler {

    /**
     * Do a sync task.
     *
     * @param runnable The runnable.
     */
    void doSync(Runnable runnable);

    /**
     * Do sync task which will be executed after delay.
     *
     * @param runnable The runnable.
     * @param delay    The delay in ticks.
     */
    void doSyncLater(Runnable runnable, long delay);

    /**
     * Do sync task which will be executed after delay
     * and will be repeated every period.
     *
     * @param runnable The runnable.
     * @param delay    The delay in ticks.
     * @param period   The period in ticks.
     */
    void doSyncRepeating(Runnable runnable, long delay, long period);

    /**
     * Do an async task.
     *
     * @param runnable the runnable.
     */
    void doAsync(Runnable runnable);

    /**
     * Do async task which will be executed after delay.
     *
     * @param runnable The runnable.
     * @param delay    The delay in ticks.
     */
    void doAsyncLater(Runnable runnable, long delay);

    /**
     * Do async task which will be executed after delay
     * and will be repeated every period.
     *
     * @param runnable The runnable.
     * @param delay    The delay in ticks.
     * @param period   The period in ticks.
     */
    void doAsyncRepeating(Runnable runnable, long delay, long period);
}

