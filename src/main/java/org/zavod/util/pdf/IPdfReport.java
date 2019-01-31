package org.zavod.util.pdf;

public interface IPdfReport <T> {

    byte[] create(T t);

}
