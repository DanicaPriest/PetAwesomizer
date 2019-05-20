package petAwesomizer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import petAwesomizer.exceptions.ControllerAdviceClass;
import petAwesomizer.exceptions.CustomException;
import petAwesomizer.model.FormCommand;
import petAwesomizer.model.PetSimplified;
import petAwesomizer.service.PetAwesomizerService;

import java.util.ArrayList;


@RestController
@RequestMapping("/petawesomizer")
public class PetAwesomizerController {

    @Autowired
    PetAwesomizerService petAwesomizerService;

    @Autowired
    ControllerAdviceClass controllerAdviceClass;

    //home page that takes in pet search parameters from user
    @RequestMapping("/")
    public ModelAndView home(@ModelAttribute FormCommand formCommand) {
        ModelAndView modelAndView = new ModelAndView();

        //Breed List that fills with breed of animal chosen
        //can't get animal value from animal input to send to breed list yet
        //ArrayList<String> breedList = petAwesomizerService.getBreedList(formCommand.getAnimalValue());
        // modelAndView.addObject("breedList", breedList );

        modelAndView.setViewName("home");

        return modelAndView;
    }


    //searches the petfinder database by location and animal and returns the nearest results to search.html
    @PostMapping("/search")
    public ModelAndView searchResults(@ModelAttribute FormCommand formCommand) throws HttpMessageNotReadableException {
        ModelAndView modelAndView = new ModelAndView();
        try {

            ArrayList<PetSimplified> pets = petAwesomizerService.mapPets(formCommand.getLocationField(), formCommand.getAnimalValue(), formCommand.getAgeValue(), formCommand.getSexValue(), formCommand.getCount());
            modelAndView.addObject("pets", pets);

            //checks if optional animal parameter is blank
            if (formCommand.getAnimalValue().equals("")) {
                //changes animal value to "pets" for the title
                modelAndView.addObject("animal", "pets");
            } else {
                //replaces animal variable in title with animal parameter
                modelAndView.addObject("animal", formCommand.getAnimalValue() + "s");
            }

            //adds location to title (default is currently virginia)
            modelAndView.addObject("location", formCommand.getLocationField());
            modelAndView.setViewName("search");
        } catch (NullPointerException ne) {
            CustomException error = controllerAdviceClass.handle411(ne);
            modelAndView.addObject("reason", ne.getMessage());
            modelAndView.addObject("message", error.getMessage());
            modelAndView.setViewName("error");
            return modelAndView;
        }

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

//reports inappropriate content
    @RequestMapping("/report")
    public ModelAndView report() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("reason", "Thank you for reporting inappropriate content");
        modelAndView.addObject("message", "This has now been removed from the database and will not appear again");
        modelAndView.setViewName("error");
        return modelAndView;
    }

    //loads pets into mysql database
    @RequestMapping(method = RequestMethod.PUT, value = "/load")
    public ArrayList<PetSimplified> loadPets(@RequestParam(value = "location", defaultValue = "virginia") String location,
                                             @RequestParam(value = "animal", required = false, defaultValue = "") String animal) {

        ArrayList<PetSimplified> pets = petAwesomizerService.mapPets(location, animal, "", "", "50");
        petAwesomizerService.insertPets(pets);

        return pets;
    }

    //original json from petfinderapi
    @RequestMapping("/json")
    public ArrayList<PetSimplified> mapPet(@RequestParam(value = "location", defaultValue = "virginia") String location,
                                           @RequestParam(value = "animal", required = false, defaultValue = "") String animal) {

        return petAwesomizerService.mapPets(location, animal, "", "", "50");
    }
}
