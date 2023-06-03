package al.exe;

import javax.persistence.*;

@Entity
@Table(name = "brand")
public class ClassBrand 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    // Default constructor
    public ClassBrand() 
    {
    }
    
    // Parameterized constructor
    public ClassBrand(String name) 
    {
        this.name = name;
    }

    // Getters
    public int getId() 
    {
        return id;
    }
    public String getName() 
    {
        return name;
    }

    // Setters
    public void setId(int id) 
    {
        this.id = id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }
}
