package org.pbms.pbmsserver.common.vo;


@FunctionalInterface
public interface Transfer<V, T> {
    T to(V v);
}
