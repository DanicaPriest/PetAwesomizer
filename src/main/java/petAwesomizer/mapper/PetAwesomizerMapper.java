package petAwesomizer.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import petAwesomizer.model.CNRoot;
import petAwesomizer.model.PetSimplified;
import petAwesomizer.model.RCNRoot;

import java.util.ArrayList;

@Mapper
public interface PetAwesomizerMapper {

    String INSERT_PETS = "INSERT INTO `pet_awesomizer`.pets (name, animal, sex, age, location, description, photo, email) " +
            "VALUES (#{name}, #{animal}, #{sex}, #{age}, #{location}, #{description}, #{photo}, #{email})";
    String INSERT_RABBIT_NAME = "INSERT INTO `nnproject`.rabbits (name) " +
            "VALUES (#{name})";

    String INSERT_RCNFACT = "INSERT INTO `pet_awesomizer`.reportedCNfacts (reportedCNFact) " +
            "VALUES (#{reportedCNFact})";
    String GET_RCNFACT = "SELECT * FROM `pet_awesomizer`.reportedCNfacts where reportedCNFact = #{reportedCNFact}";
    String GET_ALL__RCNFACTS = "SELECT * FROM `pet_awesomizer`.reportedCNfacts";

   @Insert(INSERT_PETS)
    public int insertPetsAll(PetSimplified petSimplified);

    @Insert(INSERT_RABBIT_NAME)
    public int insertRabbitName(String rabbit);

    @Insert(INSERT_RCNFACT)
    public int insertRCNFact(CNRoot cnRoot);

    @Select(GET_RCNFACT)
    public CNRoot getRCNFact(String cnFact);

    @Select(GET_ALL__RCNFACTS)
    public ArrayList<RCNRoot> getAllRCNFacts();

}
