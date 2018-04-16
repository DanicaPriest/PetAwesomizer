package petAwesomizer.model;

public class FormCommand {
    String locationField;
    String animalValue;
    String ageValue;
    String sexValue;

    public String getAgeValue() {
        return ageValue;
    }

    public void setAgeValue(String ageValue) {
        this.ageValue = ageValue;
    }

    public String getSexValue() {
        return sexValue;
    }

    public void setSexValue(String sexValue) {
        this.sexValue = sexValue;
    }

    public String getLocationField() {
        return locationField;
    }

    public void setLocationField(String locationField) {
        this.locationField = locationField;
    }

    public String getAnimalValue() {
        return animalValue;
    }

    public void setAnimalValue(String animalValue) {
        this.animalValue = animalValue;
    }

}
