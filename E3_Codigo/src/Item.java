public abstract class Item{
    private String name;
    private String description;
    private double price;
    private String picturePath;
    
    public Item(String name, String description, double price, String picturePath){
        this.name = name;
        this.description = description;
        
        /* si el precio introducido es menor a 0 se pone el valor por defecto 1 */
        if (price < 0){
            this.price = 1;
        } else {
            this.price = price;
        }
        this.picturePath = picturePath;
    }
}