package edu.ucsd.cse110.habitizer.lib.util;

import  org.jetbrains.annotations.Nullable;
public interface Observer<T> {
    void onChange(@Nullable T value);
}
