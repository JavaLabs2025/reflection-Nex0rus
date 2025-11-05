package org.example.classes;

import com.google.common.base.MoreObjects;
import org.example.Generatable;

@Generatable
public class Example {
    int i;

    public Example(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("i", i)
                .toString();
    }
}
