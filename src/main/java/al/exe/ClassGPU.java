package al.exe;

import javax.persistence.*;

@Entity
@Table(name = "gpu")
public class ClassGPU {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "model")
    private String model;

    @Column(name = "price")
    private double price;

    @Column(name = "cores")
    private int cores;

    @Column(name = "memory")
    private int memory;

    @Column(name = "frequency")
    private int frequency;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private ClassBrand brand;

    public ClassGPU() {
    }

    public ClassGPU(String model, double price, int cores, int memory, int frequency, ClassBrand brand) {
        this.model = model;
        this.price = price;
        this.cores = cores;
        this.memory = memory;
        this.frequency = frequency;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public ClassBrand getBrand() {
        return brand;
    }

    public void setBrand(ClassBrand brand) {
        this.brand = brand;
    }
}
