package petAwesomizer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import petAwesomizer.model.PetSimplified;
import petAwesomizer.service.PetAwesomizerService;

import java.io.IOException;
import java.util.ArrayList;


@RestController
@RequestMapping("/petawesomizer")
public class PetAwesomizerController {

    @Autowired
    PetAwesomizerService petAwesomizerService;

    //home page that takes in pet search parameters from user
@RequestMapping(value={"/", "/home"}, method = RequestMethod.GET)
public ModelAndView home(){
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("home");

    return modelAndView;
}


    //searches the petfinder database by location and animal and returns the nearest results to search.html
    @RequestMapping("/search")
    public ModelAndView searchResults(@RequestParam(value = "location", defaultValue = "virginia") String location,
                                      @RequestParam(value = "animal", required = false, defaultValue = "") String animal) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        ArrayList<PetSimplified> pets = petAwesomizerService.mapPets(location, animal);
        modelAndView.addObject("pets", pets);

        //checks if optional animal parameter is blank
        if (animal.equals("")) {
            //changes animal value to "pets" for the title
            modelAndView.addObject("animal", "pets");
        } else {
            //replaces animal variable in title with animal parameter
            modelAndView.addObject("animal", animal + "s");
        }
        //adds location to title (default is currently virginia)
        modelAndView.addObject("location", location);
        modelAndView.setViewName("search");

        return modelAndView;
    }

    //maps a random animal from petfinder to random.html
    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public ModelAndView getRandom(
            @RequestParam(value = "animal", required = false, defaultValue = "") String animal,
            @RequestParam(value = "breed", required = false, defaultValue = "") String breed
    ) {
        ModelAndView modelAndView = new ModelAndView();
        PetSimplified pet = petAwesomizerService.getRandomPet(animal, breed);

        //setting thymeleaf object variables
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

    @RequestMapping("/json")
    public ArrayList<PetSimplified> mapPet(@RequestParam(value = "location", defaultValue = "virginia") String location,
                                           @RequestParam(value = "animal", required = false, defaultValue = "") String animal) {

        return petAwesomizerService.mapPets(location, animal);
    }
}
