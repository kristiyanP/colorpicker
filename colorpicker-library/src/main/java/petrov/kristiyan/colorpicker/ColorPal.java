package petrov.kristiyan.colorpicker;


public class ColorPal {
    private int color;
    private boolean check;

    public ColorPal(int color, boolean check) {
        this.color = color;
        this.check = check;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ColorPal) {
            return ((ColorPal) o).color == color;
        }
        return false;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}