package petAwesomizer.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import petAwesomizer.model.PetSimplified;

@Mapper
public interface PetAwesomizerMapper {

    String INSERT_PETS = "INSERT INTO `pet_awesomizer`.pets (name, animal, sex, age, location, description, photo, email) " +
            "VALUES (#{name}, #{animal}, #{sex}, #{age}, #{location}, #{description}, #{photo}, #{email})";
    String INSERT_RABBIT_NAME = "INSERT INTO `nnproject`.rabbits (name) " +
            "VALUES (#{name})";

   @Insert(INSERT_PETS)
    public int insertPetsAll(PetSimplified petSimplified);

    @Insert(INSERT_RABBIT_NAME)
    public int insertRabbitName(String rabbit);


}
