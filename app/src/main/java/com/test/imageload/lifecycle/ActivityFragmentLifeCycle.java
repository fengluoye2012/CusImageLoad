package com.test.imageload.lifecycle;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;

public class ActivityFragmentLifeCycle implements LifeCycle {

    private boolean isStarted;
    private boolean isDestroyed;

    private Set<LifeCycleListener> lifeCycleListeners = new HashSet<>();

    @Override
    public void addListener(@NonNull LifeCycleListener listener) {
        lifeCycleListeners.add(listener);

        if (isDestroyed) {
            listener.onDestroy();
        } else if (isStarted) {
            listener.onStart();
        } else {
            listener.onStop();
        }
    }

    @Override
    public void removeListener(@NonNull LifeCycleListener listener) {
        lifeCycleListeners.remove(listener);
    }

    public void onStart() {
        isStarted = true;
        for (LifeCycleListener listener : lifeCycleListeners) {
            listener.onStart();
        }
    }

    public void onStop() {
        isStarted = false;
        for (LifeCycleListener listener : lifeCycleListeners) {
            listener.onStop();
        }
    }

    public void onDestroy() {
        isDestroyed = true;
        for (LifeCycleListener listener : lifeCycleListeners) {
            listener.onDestroy();
        }
    }
}
