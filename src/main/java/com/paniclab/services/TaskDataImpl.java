package com.paniclab.services;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class TaskDataImpl<T> implements TaskData<T> {
    private final List<T> data;

    public TaskDataImpl(Collection<T> collection) {
        data = Collections.unmodifiableList(new ArrayList<>(collection));
    }

    @Override
    public Collection<T> getData() {
        return data;
    }
}

