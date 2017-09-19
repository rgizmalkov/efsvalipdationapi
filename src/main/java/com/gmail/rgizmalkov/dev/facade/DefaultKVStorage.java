package com.gmail.rgizmalkov.dev.facade;

import com.gmail.rgizmalkov.dev.ival.Format;
import com.gmail.rgizmalkov.dev.vdf.DefaultValues;

import java.io.InputStream;
import java.util.Properties;

import static com.gmail.rgizmalkov.dev.vdf.DefaultValues.*;

public abstract class DefaultKVStorage implements KVStorage  {

    Properties properties;

    protected DefaultKVStorage(){
        this.properties = new Properties();
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("validation.register.properties");
        try {
            properties.load(resourceAsStream);
        }catch (Exception ex){
            /*NuN*/
        }
        properties.put(DEFAULT_CRITICALITY.getKey(), DEFAULT_CRITICALITY.getValue());
        properties.put(DEFAULT_DISABLE.getKey(), DEFAULT_DISABLE.getValue());
    }

    public static void main(String[] args) {
        new DefaultKVStorage(){

            @Override
            public <V> V get(String key, Class<V> V) {
                return null;
            }

            @Override
            public <V> V getObject(String key, Class<V> V, Format format) {
                return null;
            }
        };
    }
}
