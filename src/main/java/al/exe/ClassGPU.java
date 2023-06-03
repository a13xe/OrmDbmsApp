package al.exe;

import javax.persistence.*;

@Entity
@Table(name = "gpu")
public class ClassGPU 
{
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

    // Default constructor
    public ClassGPU() 
    {
    }

    // Parameterized constructor
    public ClassGPU(String model, double price, int cores, int memory, int frequency, ClassBrand brand) {
        this.model = model;
        this.price = price;
        this.cores = cores;
        this.memory = memory;
        this.frequency = frequency;
        this.brand = brand;
    }

    // Getters
    public int getId() 
    {
        return id;
    }
    public String getModel() 
    {
        return model;
    }
    public double getPrice() 
    {
        return price;
    }
    public int getCores() 
    {
        return cores;
    }
    public int getMemory() 
    {
        return memory;
    }
    public int getFrequency() 
    {
        return frequency;
    }
    public ClassBrand getBrand() 
    {
        return brand;
    }

    // Setters
    public void setId(int id) 
    {
        this.id = id;
    }
    public void setModel(String model) 
    {
        this.model = model;
    }
    public void setPrice(double price) 
    {
        this.price = price;
    }
    public void setCores(int cores) 
    {
        this.cores = cores;
    }
    public void setMemory(int memory) 
    {
        this.memory = memory;
    }
    public void setFrequency(int frequency) 
    {
        this.frequency = frequency;
    }
    public void setBrand(ClassBrand brand) 
    {
        this.brand = brand;
    }
}
