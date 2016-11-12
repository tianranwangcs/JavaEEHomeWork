package test;

//@Component("Car")
public class Car {
    private String carId = "001";
    private String carColor = "red";

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String tostring() {
        return "a " + carColor + " car with " + carId;
    }
}
