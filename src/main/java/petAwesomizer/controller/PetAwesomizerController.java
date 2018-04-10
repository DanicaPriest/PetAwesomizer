package petAwesomizer.controller;


import org.springframework.web.bind.annotation.*;
import petAwesomizer.model.PetRoot;
import org.springframework.beans.factory.annotation.Autowired;
import petAwesomizer.model.PetSimplified;
import petAwesomizer.service.PetAwesomizerService;

import java.util.ArrayList;


@RestController
@RequestMapping("/petawesomizer")
public class PetAwesomizerController {

    @Autowired
    PetAwesomizerService petAwesomizerService;

    @RequestMapping("/search")
    public PetRoot searchPet(@RequestParam (value="location", defaultValue = "virginia")String location,
                             @RequestParam (value="animal", required= false, defaultValue = "") String animal) {

        return petAwesomizerService.searchPets(location, animal); }

    @RequestMapping("/")
    public ArrayList<PetSimplified> mapPet(@RequestParam (value="location", defaultValue = "virginia")String location,
                                           @RequestParam (value="animal", required= false, defaultValue = "") String animal) {

        return petAwesomizerService.mapPets(location, animal); }

    @RequestMapping("/random")
    public PetSimplified randomPet(
            @RequestParam (value="animal", required= false, defaultValue = "") String animal,
            @RequestParam (value="breed", required= false, defaultValue = "") String breed) {

        return petAwesomizerService.getRandomPet(animal, breed); }

    @RequestMapping(method = RequestMethod.PUT, value = "/load")
    public ArrayList<PetSimplified> loadPets(@RequestParam (value="location", defaultValue = "virginia")String location,
                                             @RequestParam (value="animal", required= false, defaultValue = "") String animal) {
        ArrayList<PetSimplified> pets = petAwesomizerService.mapPets(location,animal);
        petAwesomizerService.insertPets(pets);

        return pets; }

}
