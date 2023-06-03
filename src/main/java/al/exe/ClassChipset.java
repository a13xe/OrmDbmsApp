package al.exe;

import javax.persistence.*;

@Entity
@Table(name = "chipset")
public class ClassChipset 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;

    // Default constructor
    public ClassChipset() 
    {
    }
    
    // Parameterized constructor
    public ClassChipset(String name) 
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
