package ContainerLoadingProblem;

public class Barang {
    public int panjang;
    public int lebar;

    public Barang(int panjang,int lebar){
        this.panjang = panjang;
        this.lebar = lebar;
    }

    @Override
    public String toString() {
        return '[' + ", panjang : " + panjang +
                ", lebar : " + lebar + ']';
    }
}