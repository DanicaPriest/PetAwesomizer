package petAwesomizer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import petAwesomizer.model.PetSimplified;
import petAwesomizer.service.PetAwesomizerService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


@RestController
@RequestMapping("/petawesomizer")
public class PetAwesomizerController {

    @Autowired
    PetAwesomizerService petAwesomizerService;



    @RequestMapping("/")
    public ArrayList<PetSimplified> mapPet(@RequestParam(value = "location", defaultValue = "virginia") String location,
                                           @RequestParam(value = "animal", required = false, defaultValue = "") String animal) {

        return petAwesomizerService.mapPets(location, animal);
    }
    @RequestMapping("/search")
    public ModelAndView searchResults(@RequestParam(value = "location", defaultValue = "virginia") String location,
                                      @RequestParam(value = "animal", required = false, defaultValue = "") String animal ) throws IOException {

        String newSearch = petAwesomizerService.htmlBuilder( location, animal);
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/danica/Documents/CodingNomads/Labs/spring/PetAwesomizer/src/main/resources/templates/search.html"));
        writer.write(newSearch);
        writer.close();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("animal", animal + "s");
        modelAndView.addObject("location", location);
        modelAndView.setViewName("search");
        return modelAndView;
    }

    @RequestMapping(value= "/random", method = RequestMethod.GET)
    public ModelAndView getRandom(
            @RequestParam (value="animal", required= false, defaultValue = "") String animal,
             @RequestParam (value="breed", required= false, defaultValue = "") String breed
    ) {
        ModelAndView modelAndView = new ModelAndView();
        PetSimplified pet = petAwesomizerService.getRandomPet(animal, breed);

        modelAndView.addObject("petName", pet.getName());
        modelAndView.addObject("petPhoto", pet.getPhoto());
        modelAndView.addObject("animal", "Animal: " + pet.getAnimal());
        modelAndView.addObject("sex", "Sex: " + pet.getSex());
        modelAndView.addObject("age", "Age: " + pet.getAge());
        modelAndView.addObject("location", "Location: " + pet.getLocation());
        modelAndView.addObject("email", "Contact email: " + pet.getEmail());
        modelAndView.addObject("petFact", pet.getDescription());
        modelAndView.setViewName("random");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/load")
    public ArrayList<PetSimplified> loadPets(@RequestParam(value = "location", defaultValue = "virginia") String location,
                                             @RequestParam(value = "animal", required = false, defaultValue = "") String animal) {
        ArrayList<PetSimplified> pets = petAwesomizerService.mapPets(location, animal);
        petAwesomizerService.insertPets(pets);

        return pets;
    }

}
