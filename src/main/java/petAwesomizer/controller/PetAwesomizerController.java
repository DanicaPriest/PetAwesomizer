package petAwesomizer.controller;


import org.springframework.web.bind.annotation.RequestParam;
import petAwesomizer.model.PetRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import petAwesomizer.model.PetSimplified;
import petAwesomizer.service.PetAwesomizerService;

import java.util.ArrayList;


@RestController
@RequestMapping("/petawesomizer")
public class PetAwesomizerController {

    @Autowired
    PetAwesomizerService petAwesomizerService;

    @RequestMapping("/search")
    public PetRoot searchPet(@RequestParam (value="location", defaultValue = "virginia")String location) {
        return petAwesomizerService.searchPets(location);

    }

    @RequestMapping("/")
    public ArrayList<PetSimplified> mapPet(@RequestParam (value="location", defaultValue = "virginia")String location) {
        return petAwesomizerService.mapPets(location);

    }

}
