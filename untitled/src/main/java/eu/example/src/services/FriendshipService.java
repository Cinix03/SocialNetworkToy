package eu.example.src.services;

import eu.example.src.Utils.Graph;
import eu.example.src.domain.Friendship;
import eu.example.src.domain.Tuple;
import eu.example.src.domain.Utilizator;
import eu.example.src.repository.Repository;
import eu.example.src.validators.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class FriendshipService extends AbstractService<Tuple<Long, Long>, Friendship>{
    private List<Runnable> listeners = new ArrayList<>();
    private List<BiConsumer<Long, String>> specificChangeListeners = new ArrayList<>();


    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> repo, Validator<Friendship> validator) {
        super(repo, validator);
    }

    // Metodă pentru a adăuga un listener
    public void addChangeListener(Runnable listener) {
        listeners.add(listener);
    }

    public void addSpecificChangeListener(BiConsumer<Long, String> listener) {
        specificChangeListeners.add(listener);
    }

    public void notifySpecificListeners(Long id, String stat) {
        for (BiConsumer listener : specificChangeListeners) {
            listener.accept(id, stat);
        }
    }

    // Metodă pentru a notifica toți listenerii
    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }

    @Override
    public void add(Object entity) {
        super.add(entity);
        notifyListeners();
        Friendship f = (Friendship) entity;
        notifySpecificListeners(f.getReceiver(), f.getStatus());
    }

    @Override
    public void delete(Object o) {
        super.delete(o);
        notifyListeners();
    }

    @Override
    boolean isType(Object o) {
        if(o==null)
            return false;
        return o instanceof Friendship;
    }

    public int nrComunitati(int cati, Iterable<Utilizator> all) {
        //create an iterable for all ids from user
        Graph g = new Graph(repo.findAll(), cati, all);
        return g.nrComunitati();
    }

    public List<Integer> ceiMaiprietenosi(int cati, Iterable<Utilizator> all) {

        Graph g = new Graph(repo.findAll(), cati, all);
        return g.getCelMaiLungDrum();
    }
    @Override
    boolean isTypeOfID(Object o) {
        if(o==null)
            return false;
        return o instanceof Tuple && ((Tuple)o).getFirst() instanceof Long && ((Tuple)o).getSecond() instanceof Long;
    }

    public Iterable<Friendship> findAllPaginated(int page, int size, int idCautat) {
        return repo.findAllPaginated(page, size, idCautat);
    }

}
