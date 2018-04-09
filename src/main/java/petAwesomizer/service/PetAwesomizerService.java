package petAwesomizer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import petAwesomizer.model.CNRoot;
import petAwesomizer.model.Pet;
import petAwesomizer.model.PetRoot;
import petAwesomizer.model.PetSimplified;

import java.util.ArrayList;




@Service
public class PetAwesomizerService {
    @Autowired
    RestTemplate restTemplate;

//maps the data from the petfinder api to an object
    public PetRoot searchPets(String location) {
        String webUrl = "http://api.petfinder.com/pet.find?key=9bce8b750600914be2415a1932012ee0&format=json&location=" + location;

         PetRoot pets = restTemplate.getForObject(webUrl, PetRoot.class);


        return pets;
    }
//maps chuck norris fact to an object and replaces chuck norris with the pet's name
    public String getCNFact(String name){
        String u = "http://api.icndb.com/jokes/random?exclude=[explicit]&escape=javascript&firstName=" + name;
        CNRoot cn = restTemplate.getForObject(u, CNRoot.class);
        //removes "Norris from the text
        String joke = cn.getValue().getJoke().replaceAll("Norris", "").replaceAll("^ +| +$|( )+", "$1");

        return joke ;
    }

    //changes gender pronouns in description to match sex of pet
    public String changeGender(String text){
        return text.replaceAll("\\bhe\\b", "she").replaceAll("\\bhim\\b", "her").replaceAll("\\bhis\\b","her").replaceAll("\\bhimself\\b", "herself");
    }

    //removes the section of the picture url that makes it small
    public String urlFormater(String url){
        String remove = url.substring(url.indexOf("?"), url.length());
        return url.replaceFirst(remove, ".jpg");
    }

    //maps the petfinder data to a cleaner format and excludes unneeded data
    public ArrayList<PetSimplified> mapPets(String location){
        Pet[] pet = searchPets(location).getPetfinder().getPets().getPet();
        ArrayList<PetSimplified> objArray = new ArrayList();

        for (Pet p : pet) {
            PetSimplified obj = new PetSimplified();

            obj.setName(p.getName().get$t());
            obj.setAnimal(p.getAnimal().get$t());
            obj.setSex(p.getSex().get$t());
            obj.setAge(p.getAge().get$t());
            //puts city and state into one location instance variable;
            obj.setLocation(p.getContact().getCity().get$t() + ", " + p.getContact().getState().get$t());
            //determines sex of pet then changes gender pronouns if it's female
            //maps altered chuck norris fact to pet description instance variable
            if (obj.getSex().contentEquals("M")){
            obj.setDescription(getCNFact(obj.getName()));}
            else{
                obj.setDescription(changeGender(getCNFact(obj.getName())));
            }

            obj.setPhoto(p.getMedia().getPhotos().getPhoto()[0].get$t());
            obj.setEmail(p.getContact().getEmail().get$t());

            objArray.add(obj);
        }
        return objArray;

    }
}
