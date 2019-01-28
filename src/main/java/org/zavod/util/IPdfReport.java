package org.zavod.util;

public interface IPdfReport <T> {

    byte[] create(T t);

}
