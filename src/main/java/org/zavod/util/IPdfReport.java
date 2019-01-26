package org.zavod.util;

import java.io.IOException;
import java.io.InputStream;

public interface IPdfReport <T> {

    InputStream create(T t) throws IOException;

}
