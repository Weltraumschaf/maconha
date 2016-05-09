package de.weltraumschaf.maconha.job;

import java.util.concurrent.Callable;

/**
 * @param <V>
 */
public interface Job<V> extends Callable<V>, MessageProducer{

}
