package eu.example.src.repository.file;

import eu.example.src.domain.Utilizator;
import eu.example.src.validators.Validator;

import java.util.Optional;

public class UtilizatorRepository extends AbstractFileRepository<Long, Utilizator>{

    public UtilizatorRepository(Validator<Utilizator> validator, String fileName) {
        super(validator, fileName);
    }



    @Override
    public Utilizator createEntity(String line) {
        String[] splited = line.split(";");
        Utilizator u = new Utilizator(splited[1], splited[2], splited[3], splited[4]);
        if(currentId!=null)
            currentId = Math.max(currentId, Long.parseLong(splited[0])+1);
        else
            currentId = Long.parseLong(splited[0])+1;
        System.out.println(currentId);
        u.setId(Long.parseLong(splited[0]));
        return u;
    }

    @Override
    public String saveEntity(Utilizator entity) {
        if(entity.getId()==null)
            entity.setId((long)currentId++);
        System.out.println(entity.getId());
        String s = entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
        return s;
    }


}
