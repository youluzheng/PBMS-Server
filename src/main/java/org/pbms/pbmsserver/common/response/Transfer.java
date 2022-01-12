package org.pbms.pbmsserver.common.response;


@FunctionalInterface
public interface Transfer<V, T> {
    T to(V v);
}
