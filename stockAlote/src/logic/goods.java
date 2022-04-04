package logic;

public class goods {

    // Atributes
    private int id;
    private String name;
    private int qty;
    private int weight_pu;
    private int volume_pu;
    private boolean fragile;


    // Getters
    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getQty() {
        return qty;
    }
    public int getWeight_pu() {
        return weight_pu;
    }
    public int getVolume_pu() {
        return volume_pu;
    }
    public boolean getFragility() {
        return fragile;
    }

    // Setters
    public void setID(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
    public void setWeight_pu(int weight_pu) {
        this.weight_pu = weight_pu;
    }
    public void setVolume_pu(int volume) {
        this.volume_pu = volume;
    }
    public void setFragility(boolean fragile) {
        this.fragile = fragile;
    }



}
