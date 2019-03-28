package api.storage.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "storage", schema = "public", catalog = "StorageApiDb")
public class StorageEntity {
    private int id;
    private String name;
    private int amount;
    private double price;
    private Date date;
    private ProductNamesEntity productNamesByName;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "amount", nullable = false)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "price", nullable = false)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageEntity that = (StorageEntity) o;
        return id == that.id &&
                amount == that.amount &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount, price, date);
    }

    @ManyToOne
    @JoinColumn(name = "name", referencedColumnName = "name", nullable = false)
    public ProductNamesEntity getProductNamesByName() {
        return productNamesByName;
    }

    public void setProductNamesByName(ProductNamesEntity productNamesByName) {
        this.productNamesByName = productNamesByName;
    }
}
