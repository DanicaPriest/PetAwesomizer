package petAwesomizer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import petAwesomizer.mapper.PetAwesomizerMapper;
import petAwesomizer.model.CNRoot;
import petAwesomizer.model.Pet;
import petAwesomizer.model.PetRoot;
import petAwesomizer.model.PetSimplified;
import petAwesomizer.model.Random.Randyroot;

import java.util.ArrayList;


@Service
public class PetAwesomizerService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PetAwesomizerMapper petAwesomizerMapper;

    //maps the data from the petfinder api to an object
    public PetRoot searchPets(String location, String animal) {
        String webUrl = "http://api.petfinder.com/pet.find?key=9bce8b750600914be2415a1932012ee0&count=50&format=json&location=" + location + "&animal=" + animal;

        PetRoot pets = restTemplate.getForObject(webUrl, PetRoot.class);

        return pets;
    }

    //maps chuck norris fact to an object and replaces chuck norris with the pet's name
    public String getCNFact(String name) {
        String u = "http://api.icndb.com/jokes/random?exclude=[explicit]&escape=javascript&firstName=" + name;
        CNRoot cn = restTemplate.getForObject(u, CNRoot.class);
        //removes "Norris from the text
        String joke = cn.getValue().getJoke().replaceAll("Norris", "").replaceAll("^ +| +$|( )+", "$1");

        return joke;
    }

    //changes gender pronouns in description to match sex of pet
    public String changeGender(String text) {
        return text.replaceAll("\\bhe\\b", "she").replaceAll("\\bhim\\b", "her").replaceAll(
                "\\bhis\\b", "her").replaceAll("\\bhimself\\b", "herself").replaceAll(
                        "\\bHe\\b", "She").replaceAll("\\bHis\\b", "Her");
    }

    //removes the section of the picture url that makes it small
    public String urlFormater(String url) {
        String remove = url.substring(url.length() - 35, url.length());

        return url.replaceFirst(remove, ".jpg");
    }

    //maps the petfinder data to a cleaner format and excludes unneeded data
    public ArrayList<PetSimplified> mapPets(String location, String animal) {
        Pet[] pet = searchPets(location, animal).getPetfinder().getPets().getPet();
        ArrayList<PetSimplified> objArray = new ArrayList();

        //loops over the pet array
        for (Pet p : pet) {
            //creates a new PetSimplified object, sets the instance variables from the pet array
            PetSimplified obj = new PetSimplified();

            obj.setName(p.getName().get$t());
            obj.setAnimal("Animal: " + p.getAnimal().get$t());
            obj.setSex("Sex: " + p.getSex().get$t());
            obj.setAge("Age : "+ p.getAge().get$t());

            //puts city and state into one location instance variable;
            obj.setLocation("Location: " + p.getContact().getCity().get$t() + ", " + p.getContact().getState().get$t());

            //determines sex of pet then changes gender pronouns if it's female
            //maps altered chuck norris fact to pet description instance variable
            if (obj.getSex().contentEquals("F")) {
                obj.setDescription(changeGender(getCNFact(obj.getName())));
            } else {
                obj.setDescription(getCNFact(obj.getName()));
            }
            try {
                obj.setPhoto(urlFormater(p.getMedia().getPhotos().getPhoto()[0].get$t()));
            } catch (Exception e) {
                obj.setPhoto("https://cdn.shopify.com/s/files/1/0489/4081/products/cat-riding-a-fire-breathing-unicorn-decal_1024x1024.jpg?v=1407574957");
            }
            obj.setEmail("Contact Email: " + p.getContact().getEmail().get$t());

            //adds each new object to the ArrayList
            objArray.add(obj);
        }
        return objArray;

    }

    //gets a random pet from the petfinder api and maps it to the PetSimplified object
    //user can limit result to animal type
    public PetSimplified getRandomPet(String animal, String breed) {
        String webUrl = "http://api.petfinder.com/pet.getRandom?key=9bce8b750600914be2415a1932012ee0&output=basic&format=json" + "&animal=" + animal + "&breed=" + breed;

        Randyroot pet = restTemplate.getForObject(webUrl, Randyroot.class);
        PetSimplified obj = new PetSimplified();

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
        try {
            obj.setPhoto(urlFormater(pet.getPetfinder().getPet().getMedia().getPhotos().getPhoto()[0].get$t()));
        } catch (Exception e) {
            obj.setPhoto("https://cdn.shopify.com/s/files/1/0489/4081/products/cat-riding-a-fire-breathing-unicorn-decal_1024x1024.jpg?v=1407574957");
        }
        obj.setEmail(pet.getPetfinder().getPet().getContact().getEmail().get$t());

        return obj;
    }

    //inserts pets into the mysql database
    public void insertPets(ArrayList<PetSimplified> pets) {
        for (PetSimplified p : pets) {
            petAwesomizerMapper.insertPetsAll(p);
        }

    }

}
