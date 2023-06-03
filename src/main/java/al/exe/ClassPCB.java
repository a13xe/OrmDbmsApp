package al.exe;

import javax.persistence.*;

@Entity
@Table(name = "mbrd")
public class ClassPCB 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "model")
    private String model;
    @Column(name = "price")
    private double price;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private ClassBrand brand;
    @ManyToOne
    @JoinColumn(name = "socket_id")
    private ClassSocket socket;
    @ManyToOne
    @JoinColumn(name = "chipset_id")
    private ClassChipset chipset;
    
    // Default constructor
    public ClassPCB() 
    {
    }

    // Parameterized constructor
    public ClassPCB(String model, double price, ClassBrand brand, ClassSocket socket, ClassChipset chipset) 
    {
        this.model = model;
        this.price = price;
        this.brand = brand;
        this.socket = socket;
        this.chipset = chipset;
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
    public ClassBrand getBrand() 
    {
        return brand;
    }
    public ClassSocket getSocket() 
    {
        return socket;
    }
    public ClassChipset getChipset() 
    {
        return chipset;
    }
    
    // Setters
    public void setModel(String model) 
    {
        this.model = model;
    }
    public void setPrice(double price) 
    {
        this.price = price;
    }
    public void setBrand(ClassBrand brand) 
    {
        this.brand = brand;
    }
    public void setSocket(ClassSocket socket) 
    {
        this.socket = socket;
    }
    public void setChipset(ClassChipset chipset) 
    {
        this.chipset = chipset;
    }
}
