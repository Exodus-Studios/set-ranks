package com.reussy.development.setranks.plugin.utils.scheduler;

import com.reussy.development.setranks.plugin.SetRanksPlugin;

public class SchedulerWrapper implements PluginScheduler {

    private final SetRanksPlugin plugin;

    public SchedulerWrapper(SetRanksPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Do a sync task.
     *
     * @param runnable the runnable.
     */
    @Override
    public void doSync(Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    /**
     * Do sync task which will be executed after delay.
     *
     * @param runnable The runnable.
     * @param delay    The delay in ticks.
     */
    @Override
    public void doSyncLater(Runnable runnable, long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    /**
     * Do sync task which will be executed after delay
     * and will be repeated every period.
     *
     * @param runnable The runnable.
     * @param delay    The delay in ticks.
     * @param period   The period in ticks.
     */
    @Override
    public void doSyncRepeating(Runnable runnable, long delay, long period) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }

    /**
     * Do an async task.
     *
     * @param runnable the runnable.
     */
    @Override
    public void doAsync(Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    /**
     * Do async task which will be executed after delay.
     *
     * @param runnable The runnable.
     * @param delay    The delay in ticks.
     */
    @Override
    public void doAsyncLater(Runnable runnable, long delay) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    /**
     * Do async task which will be executed after delay
     * and will be repeated every period.
     *
     * @param runnable The runnable.
     * @param delay    The delay in ticks.
     * @param period   The period in ticks.
     */
    @Override
    public void doAsyncRepeating(Runnable runnable, long delay, long period) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period);
    }
}
