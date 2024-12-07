package eu.example.src.services;

import eu.example.src.domain.Messages;
import eu.example.src.repository.Repository;
import eu.example.src.validators.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class MessagesService extends AbstractService<Long, Messages> {
    private List<BiConsumer<Long, Long>> specificChangeListeners = new ArrayList<>();
    public MessagesService(Repository<Long, Messages> repository, Validator<Messages> validator) {
        super(repository, validator);
    }

    public Iterable<Messages> getMessagesBetween(Long from, Long to) {
        return StreamSupport.stream(repo.findAll().spliterator(), false)
                .filter(x -> (x.getFrom().equals(from) && x.getTo().equals(to)) || (x.getFrom().equals(to) && x.getTo().equals(from)))
                .collect(Collectors.toList());

    }

    public void addChangeListener(BiConsumer<Long, Long> listener) {
        specificChangeListeners.add(listener);
    }

    private void notifyListeners(Long id1, Long id2) {
        for (BiConsumer<Long, Long> listener : specificChangeListeners) {
            listener.accept(id1, id2);
        }
    }

    @Override
    public void add(Object entity) {
        super.add(entity);
        Messages m= (Messages) entity;
        notifyListeners(m.getFrom(), m.getTo());
    }

    @Override
    boolean isType(Object o) {
        return true;
    }

    @Override
    boolean isTypeOfID(Object o) {
        return true;
    }
}
