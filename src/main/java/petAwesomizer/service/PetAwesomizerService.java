package petAwesomizer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import petAwesomizer.mapper.PetAwesomizerMapper;
import petAwesomizer.model.*;
import petAwesomizer.model.Breeds.Breed;
import petAwesomizer.model.Breeds.BreedRoot;
import petAwesomizer.model.Random.Randyroot;

import java.util.ArrayList;


@Service
public class PetAwesomizerService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PetAwesomizerMapper petAwesomizerMapper;

    //maps the data from the petfinder api to an object
    public PetRoot searchPets(String location, String animal, String age, String sex, String count) {

        //mapping petfinder to PetRoot object and adding search parameters to url
        String webUrl = "http://api.petfinder.com/pet.find?key=9bce8b750600914be2415a1932012ee0&count=" + count + "&format=json&location=" + location + "&animal=" + animal + "&age=" + age + "&sex=" + sex;

        PetRoot pets = restTemplate.getForObject(webUrl, PetRoot.class);

        return pets;
    }


    //maps chuck norris fact to an object and replaces chuck norris with the pet's name
    public String getCNFact(String name) {
        String u = "http://api.icndb.com/jokes/random?exclude=[explicit]&escape=javascript&firstName=" + name;
        CNRoot cn = restTemplate.getForObject(u, CNRoot.class);


        //gets fact as a String from api
        String cnfact = cn.getValue().getJoke();

        //inserts the original fact into the temporary database in case fact needs to be reported
        CNRoot temp = cn;
        temp.getValue().setJoke(cn.getValue().getJoke().replaceAll(name, "Chuck"));
        petAwesomizerMapper.insertCNFact(temp.getValue());


        //checks database to see if fact has been reported and removes it if so
        ArrayList<RCNRoot> cnRoots = petAwesomizerMapper.getAllRCNFacts();

        boolean reported = false;

        for (RCNRoot c : cnRoots) {


            if (c.getJoke().contains(cnfact)) {
                System.out.println("reported fact caught");
                reported = true;

            }
        }
        if (reported) {
            String joke = name + " agrees with all of your political beliefs";
            return joke;
        } else {

            //removes "Norris from the text
            String joke = cnfact.replaceAll("Norris", "").replaceAll("^ +| +$|( )+", "$1");
            joke.replaceAll("(?i)roundhouse kick", "cuddle");
            return joke;
        }

    }

    //changes gender pronouns in description to match sex of pet
    public String changeGender(String text) {
        return text.replaceAll("(?i)(\\bhe\\b)", "she").replaceAll("(?i)(\\bhim\\b)", "her").replaceAll(
                "(?i)(\\bhis\\b)", "her").replaceAll("(?i)(\\bhimself\\b)", "herself");
    }

    //removes the section of the picture url that makes it small
    public String urlFormater(String url) {
        String remove = url.substring(url.length() - 35, url.length());

        return url.replaceFirst(remove, ".jpg");
    }

    //maps the petfinder data to a cleaner format and excludes unneeded data
    public ArrayList<PetSimplified> mapPets(String location, String animal, String age, String sex, String count) {
        //clears the temp database
        deleteTempTable();

        //maps pets
        Pet[] pet = searchPets(location, animal, age, sex, count).getPetfinder().getPets().getPet();

        //creates new Array list
        ArrayList<PetSimplified> objArray = new ArrayList();

        //testing pet count
        int petnum = 1;

        //loops over the pet array
        for (Pet p : pet) {
            //creates a new PetSimplified object, sets the instance variables from the pet array
            PetSimplified obj = new PetSimplified();

            //set pet name
            obj.setName(p.getName().get$t());

            //set pet id
            obj.setId(petnum);

            //if animal is a rabbit inserts rabbit name into the mysql database for Neural network project
            if (p.getAnimal().get$t().contentEquals("Rabbit")) {
                insertRN(obj.getName().replaceAll(" [^\\w].*", "").replaceAll(" [ at ].*", "").replaceAll("\\d.*", ""));
            }
            //set Animal, Sex and Age
            obj.setAnimal("Animal: " + p.getAnimal().get$t());
            obj.setSex("Sex: " + p.getSex().get$t());
            obj.setAge("Age : " + p.getAge().get$t());

            //puts city and state into one location instance variable;
            obj.setLocation("Location: " + p.getContact().getCity().get$t() + ", " + p.getContact().getState().get$t());

            //determines sex of pet then changes gender pronouns if it's female
            //maps altered chuck norris fact to pet description instance variable
            if (obj.getSex().contentEquals("Sex: F")) {
                obj.setDescription(changeGender(getCNFact(obj.getName())));

            } else {
                obj.setDescription(getCNFact(obj.getName()));
            }


            //sets a default photo if no image is available
            try {
                obj.setPhoto(urlFormater(p.getMedia().getPhotos().getPhoto()[0].get$t()));
            } catch (Exception e) {
                obj.setPhoto("https://cdn.shopify.com/s/files/1/0489/4081/products/cat-riding-a-fire-breathing-unicorn-decal_1024x1024.jpg?v=1407574957");
            }

            //Set Email
            obj.setEmail("Contact Email: " + p.getContact().getEmail().get$t());

            //adds each new object to the ArrayList
            objArray.add(obj);

            //increase id number
            petnum++;

        }
        return objArray;

    }


    //gets a random pet from the petfinder api and maps it to the PetSimplified object
    //user can limit result to animal type
    public PetSimplified getRandomPet(String animal, String breed) {
        //clears the temp database
        deleteTempTable();

        String webUrl = "http://api.petfinder.com/pet.getRandom?key=9bce8b750600914be2415a1932012ee0&output=basic&format=json" + "&animal=" + animal + "&breed=" + breed;

        Randyroot pet = restTemplate.getForObject(webUrl, Randyroot.class);
        PetSimplified obj = new PetSimplified();

        obj.setId(1);
        obj.setName(pet.getPetfinder().getPet().getName().get$t());
        obj.setAnimal(pet.getPetfinder().getPet().getAnimal().get$t());
        obj.setSex(pet.getPetfinder().getPet().getSex().get$t());
        obj.setAge(pet.getPetfinder().getPet().getAge().get$t());

        //puts city and state into one location instance variable;
        obj.setLocation(pet.getPetfinder().getPet().getContact().getCity().get$t() + ", " + pet.getPetfinder().getPet().getContact().getState().get$t());

        //determines sex of pet then changes gender pronouns if it's female
        //maps altered chuck norris fact to pet description instance variable
        if (obj.getSex().contentEquals("F")) {
            obj.setDescription(changeGender(getCNFact(obj.getName())));

        } else {
            obj.setDescription(getCNFact(obj.getName()));
        }

        //sets a default photo if no image is available
        try {
            obj.setPhoto(urlFormater(pet.getPetfinder().getPet().getMedia().getPhotos().getPhoto()[0].get$t()));
        } catch (Exception e) {
            obj.setPhoto("https://cdn.shopify.com/s/files/1/0489/4081/products/cat-riding-a-fire-breathing-unicorn-decal_1024x1024.jpg?v=1407574957");
        }
        //set email
        obj.setEmail(pet.getPetfinder().getPet().getContact().getEmail().get$t());

        //if animal is a rabbit inserts rabbit name into the mysql database for Neural network project
        if (obj.getAnimal().contentEquals("Rabbit")) {
            insertRN(obj.getName().replaceAll(" [^\\w].*", "").replaceAll(" [ at ].*", "").replaceAll("\\d.*", ""));
        }

        return obj;
    }

    //Breed List that fills with breed of animal chosen
    //can't get animal value from animal input in home form to send to breed list yet
    public ArrayList<String> getBreedList(String animal) {
        String webUrl = "http://api.petfinder.com/breed.list?key=9bce8b750600914be2415a1932012ee0&format=json&animal=" + animal;
        BreedRoot breeds = restTemplate.getForObject(webUrl, BreedRoot.class);
        Breed[] breed = breeds.getPetfinder().getBreeds().getBreed();
        ArrayList<String> breedList = new ArrayList<String>();
        for (Breed b : breed
                ) {
            breedList.add(b.get$t());
        }
        return breedList;
    }

    //inserts pets into the mysql database
    public void insertPets(ArrayList<PetSimplified> pets) {
        for (PetSimplified p : pets) {
            petAwesomizerMapper.insertPetsAll(p);
        }

    }

    //inserts rabbit name into the mysql database for Neural network project
    public void insertRN(String name) {
        petAwesomizerMapper.insertRabbitName(name);

    }

    //clears the temp table and resets the AI to 1
    public void deleteTempTable() {
        petAwesomizerMapper.deletetempcnfacts();
        petAwesomizerMapper.aiReset();
    }

    //inserts reported fact into reported facts database
    public void reportFact(int id) {
        RCNRoot fact = petAwesomizerMapper.getTemp(id);

        petAwesomizerMapper.insertRCNFact(fact.getJoke());


    }
}
