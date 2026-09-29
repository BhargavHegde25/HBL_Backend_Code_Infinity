package com.kony.dbp.queuemaster.utils;

public abstract class HaltableThread extends Thread {

  protected boolean haltRequested = false;
  protected boolean started = false;
  private boolean zzz = false;

  public HaltableThread() { }

  public HaltableThread(String name) {
    super(name);
  }

  public void haltThread() {
    this.haltRequested = true;
    wakeup();
  }

  public boolean snooze() {
    boolean interrupted = false;
    synchronized (this) {
      try {
        zzz = true;
        while (zzz) { // Guard against spurious wakeup.
          wait();
        }
      }
      catch (InterruptedException ie) {
        interrupted = true;
      }
    }
    return interrupted;
  }

  public boolean snooze(long snoozeMillis) {
    if (snoozeMillis <= 0) {
      return snooze();
    }
    boolean interrupted = false;
    long wakeupTime = System.currentTimeMillis() + snoozeMillis;
    synchronized (this) {
      try {
        zzz = true;
        while (zzz) { // Guard against spurious wakeup.
          wait(snoozeMillis);
          long remainingMillis = wakeupTime - System.currentTimeMillis();
          if (remainingMillis <= 0) {
            break;
          }
          snoozeMillis = remainingMillis;
        }
      }
      catch (InterruptedException ie) {
        interrupted = true;
      }
    }
    return interrupted;
  }

  @Override
  public final void run() {
    this.started = true;
    runThread();
  }

  public abstract void runThread();

  public void startAndWait() {
    start();
    while (!this.started) {
        Thread.yield();
    }
  }

  public final boolean waitForHalt() {
    return waitForHalt(0);
  }

  public final boolean waitForHalt(long timeoutMillis) {
    if (!this.haltRequested) {
      throw new IllegalStateException();
    }
    long startTime = System.currentTimeMillis();
    while (isAlive()) {
      if (timeoutMillis > 0 && System.currentTimeMillis()  > startTime + timeoutMillis) {
        return false;
      }
      Thread.yield();
    }
    return true;
  }

  public final void wakeup() {
    synchronized (this) {
      zzz = false;
      notifyAll();
    }
  }
}