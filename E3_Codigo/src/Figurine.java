public class Figurine extends Product{
    private double height;
    private double width;
    private double depth;
    private String material;
    private String franchise;

    public Figurine(double height, double width, double depth, String material, String franchise) {
        if (height < 0 || width < 0 || depth < 0) {
            throw new IllegalArgumentException("Argumentos invalidos");
        }

        this.height = height;
        this.width = width;
        this.depth = depth;
        this.material = material;
        this.franchise = franchise;
    }
}
