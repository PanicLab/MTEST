package com.paniclab.services;

import java.util.Collection;

public interface TaskData<T> {
    Collection<T> getData();

    static <U> TaskData<U> get(Collection<U> data ) {
        return new TaskDataImpl<>(data);
    }
}
