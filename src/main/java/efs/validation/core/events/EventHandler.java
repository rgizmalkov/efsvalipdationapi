package efs.validation.core.events;

import java.lang.annotation.Annotation;

public interface EventHandler<H, A extends Annotation> {
    void action(H hobj, A aev);
}
