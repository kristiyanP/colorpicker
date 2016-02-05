package petrov.kristiyan.colorpicker;


public class ColorPal {
    public int color;
    public boolean check;

    public ColorPal(int color, boolean check) {
        this.color = color;
        this.check = check;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ColorPal){
            return ((ColorPal) o).color == color;
        }
        return false;
    }
}