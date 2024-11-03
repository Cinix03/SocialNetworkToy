package eu.example.src.services;

import eu.example.src.Utils.Graph;
import eu.example.src.domain.Friendship;
import eu.example.src.domain.Tuple;
import eu.example.src.domain.Utilizator;
import eu.example.src.repository.Repository;
import eu.example.src.validators.Validator;

import java.util.List;

public class FriendshipService extends AbstractService<Tuple<Long, Long>, Friendship>{
    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> repo, Validator<Friendship> validator) {
        super(repo, validator);
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
}
