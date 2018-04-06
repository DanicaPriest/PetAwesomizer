package mybatis.controller;



import model.PetRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.PetAwesomizerService;


@RestController
@RequestMapping("/petawesomizer")
public class PetAwesomizerController {

    @Autowired
    PetAwesomizerService petAwesomizerService;

    @RequestMapping("/")
    public PetRoot getPet() {
        return petAwesomizerService.searchPets();

    }

}
