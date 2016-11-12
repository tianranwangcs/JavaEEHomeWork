package test;

public class Boss {
    private Office office;
    private Car car;

    //we must need a constructor with no parameters
    //http://hellsing42.iteye.com/blog/137202
    public Boss()
    {
    }

    //@Autowired
    public Boss(Car car) {
        this.car = car;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }


    public String tostring() {
        return "This Boss has " + car.tostring() + ", now he is in " + office.tostring()+".";
    }
}
