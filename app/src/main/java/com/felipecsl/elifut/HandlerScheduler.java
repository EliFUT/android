package com.felipecsl.elifut;

import android.os.Handler;

import java.util.concurrent.TimeUnit;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.ScheduledAction;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public final class HandlerScheduler extends Scheduler {
  private final Handler handler;

  public static HandlerScheduler from(Handler handler) {
    if(handler == null) {
      throw new NullPointerException("handler == null");
    } else {
      return new HandlerScheduler(handler);
    }
  }

  HandlerScheduler(Handler handler) {
    this.handler = handler;
  }

  public Worker createWorker() {
    return new HandlerScheduler.HandlerWorker(this.handler);
  }

  static class HandlerWorker extends Worker {
    private final Handler handler;
    private final CompositeSubscription compositeSubscription = new CompositeSubscription();

    HandlerWorker(Handler handler) {
      this.handler = handler;
    }

    public void unsubscribe() {
      this.compositeSubscription.unsubscribe();
    }

    public boolean isUnsubscribed() {
      return this.compositeSubscription.isUnsubscribed();
    }

    public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
      final ScheduledAction scheduledAction = new ScheduledAction(action);
      scheduledAction.add(Subscriptions.create(new Action0() {
        public void call() {
          HandlerWorker.this.handler.removeCallbacks(scheduledAction);
        }
      }));
      scheduledAction.addParent(this.compositeSubscription);
      this.compositeSubscription.add(scheduledAction);
      this.handler.postDelayed(scheduledAction, unit.toMillis(delayTime));
      return scheduledAction;
    }

    public Subscription schedule(Action0 action) {
      return this.schedule(action, 0L, TimeUnit.MILLISECONDS);
    }
  }
}
