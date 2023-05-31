package al.exe;

import javax.persistence.*;

@Entity
@Table(name = "socket_to_chipset")
public class ClassSocketToChipset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "socket_id")
    private ClassSocket socket;

    @ManyToOne
    @JoinColumn(name = "chipset_id")
    private ClassChipset chipset;

    public ClassSocketToChipset() {
    }

    public ClassSocketToChipset(ClassSocket socket, ClassChipset chipset) {
        this.socket = socket;
        this.chipset = chipset;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ClassSocket getSocket() {
        return socket;
    }

    public void setSocket(ClassSocket socket) {
        this.socket = socket;
    }

    public ClassChipset getChipset() {
        return chipset;
    }

    public void setChipset(ClassChipset chipset) {
        this.chipset = chipset;
    }
}
