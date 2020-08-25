package me.pljr.playtime.objects;

public class CorePlayer {
    private final long initialized;
    private long yesterday;
    private long daily;
    private long weekly;
    private long montly;
    private long all;
    private boolean afk;
    private long afkStart;
    private long afkTime;

    public CorePlayer(long yesterday, long daily, long weekly, long montly, long all){
        this.initialized = System.currentTimeMillis();
        this.yesterday = yesterday;
        this.daily = daily;
        this.weekly = weekly;
        this.montly = montly;
        this.all = all;
        this.afk = false;
        this.afkStart = 0;
        this.afkTime = 0;
    }

    public long getInitialized() {
        return initialized;
    }

    public long getYesterday() {
        return yesterday;
    }

    public void setYesterday(long yesterday) {
        this.yesterday = yesterday;
    }

    public long getDaily() {
        return daily;
    }

    public void setDaily(long daily) {
        this.daily = daily;
    }

    public long getWeekly() {
        return weekly;
    }

    public void setWeekly(long weekly) {
        this.weekly = weekly;
    }

    public long getMontly() {
        return montly;
    }

    public void setMontly(long montly) {
        this.montly = montly;
    }

    public long getAll() {
        return all;
    }

    public void setAll(long all) {
        this.all = all;
    }

    public boolean isAfk() {
        return afk;
    }

    public void setAfk(boolean afk) {
        this.afk = afk;
    }

    public long getAfkStart() {
        return afkStart;
    }

    public void setAfkStart(long afkStart) {
        this.afkStart = afkStart;
    }

    public long getAfkTime() {
        return afkTime;
    }

    public void setAfkTime(long afkTime) {
        this.afkTime = afkTime;
    }
}
