package mybatis.service;

import mybatis.mapper.PetAwesomizerMapper;
import mybatis.model.PetRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PetAwesomizerService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PetAwesomizerMapper petAwesomizerMapper;

    public PetRoot searchPets() {
        String webUrl = "http://api.petfinder.com/pet.find?key=9bce8b750600914be2415a1932012ee0&output=basic&format=json&location=22308";

         PetRoot pets = restTemplate.getForObject(webUrl, PetRoot.class);


        return pets;
    }
}
