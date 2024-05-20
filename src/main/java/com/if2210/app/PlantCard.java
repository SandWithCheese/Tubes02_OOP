import java.util.ArrayList;
import ItemCard;

public class PlantCard {
    private String name;
    private String image;
    private int age;
    private ItemCard activeItem;
    private ArrayList<ItemCard> inventory;

    public PlantCard(String name, String image, int age, ItemCard activeItem) {
        this.name = name;
        this.image = image;
        this.age = age;
        this.activeItem = activeItem;
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ItemCard getActiveItem() {
        return activeItem;
    }
    public void setActiveItem(ItemCard activeItem) {
        this.activeItem = activeItem;
    }

    public ArrayList<ItemCard> getInventory() {
        return inventory;
    }

    public void addItemToInventory(ItemCard item) {
        this.inventory.add(item);
    }

    public void useItem() {
        if (this.activeItem != null) {
            System.out.println("Using item: " + this.activeItem.getName());
        } else {
            System.out.println("No active item.");
        }
    }
}
