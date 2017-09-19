package com.gmail.rgizmalkov.dev.ival;

import java.util.List;

public interface ValidationMachine {
    List<ValidationResponse> validation(Object o);
}
