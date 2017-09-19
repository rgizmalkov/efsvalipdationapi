package com.gmail.rgizmalkov.dev.facade;

import com.gmail.rgizmalkov.dev.ival.Format;

public interface KVStorage {

    <V> V get(String key, Class<V> V);
    <V> V getObject(String key, Class<V> V, Format format);

}
