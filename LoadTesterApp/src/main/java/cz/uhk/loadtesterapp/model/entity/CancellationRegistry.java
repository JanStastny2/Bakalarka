package cz.uhk.loadtesterapp.model.entity;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class CancellationRegistry {
    private final ConcurrentHashMap<Long, AtomicBoolean> map = new ConcurrentHashMap<>();

    public AtomicBoolean signalFor(Long id) {
        return map.computeIfAbsent(id, k -> new AtomicBoolean(false));
    }

    public void requestCancel(Long id) {
        signalFor(id).set(true);
    }

    public boolean isCancelRequested(Long id) {
        return signalFor(id).get();
    }

    public void clear(Long id) {
        map.remove(id);
    }

}
