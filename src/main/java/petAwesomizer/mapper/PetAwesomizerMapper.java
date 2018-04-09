package petAwesomizer.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import petAwesomizer.model.PetSimplified;

@Mapper
public interface PetAwesomizerMapper {

    String INSERT_PETS = "INSERT INTO `pet_awesomizer`.pets (name, animal, sex, age, location, description, photo, email) " +
            "VALUES (#{name}, #{animal}, #{sex}, #{age}, #{location}, #{description}, #{photo}, #{email})";


   @Insert(INSERT_PETS)
    public int insertPetsAll(PetSimplified petSimplified);



}
