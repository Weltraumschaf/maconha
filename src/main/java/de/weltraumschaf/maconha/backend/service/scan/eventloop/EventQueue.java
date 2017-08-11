package de.weltraumschaf.maconha.backend.service.scan.eventloop;

/**
 * Implementors give access to the sored event queue.
 */
interface EventQueue extends EventEmitter {
    /**
     * Removes and returns the next event from the queue.
     *
     * @return may be {@code null}, if queue is empty
     */
    Event next();

    /**
     * Whether the queue is empty.
     *
     * @return {@code true} if there are no more events, else {@code false}
     */
    boolean isEmpty();
}
