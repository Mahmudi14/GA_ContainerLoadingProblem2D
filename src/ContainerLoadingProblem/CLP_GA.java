package ContainerLoadingProblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class CLP_GA {
    public static boolean cek(ArrayList<Barang> data,int pContainer,int lContaner){
        int ukuranTotalBarang =0;
        for (int i = 0; i < data.size(); i++) {
            Barang barang = data.get(i);
            int pnjng = barang.panjang;
            int lebar = barang.lebar;
            int luas = pnjng * lebar;
            ukuranTotalBarang+=luas;
        }
        int luasContainer = pContainer * lContaner;

        if (luasContainer >= ukuranTotalBarang){
            return true;
        }else{
            return false;
        }

    }
    public static void masukkanBarang(int[][] container, int[] barang, ArrayList<Barang> daftarBarang) {
        boolean cek = true;
        masuk :
        for (int j = 0; j < barang.length; j++) {
            int idBarang = barang[j]-1;
            Barang barang1 = daftarBarang.get(idBarang);
            int panjangBarang = barang1.panjang;
            int lebarBarang = barang1.lebar;

            selesai:
            for (int k = 0; k < container.length; k++) {
                for (int l = 0; l < container[0].length; l++) {

                    if (container[k][l] == 0) {
                        if ((k + panjangBarang ) < container.length && (l + lebarBarang) < container[0].length) {
                            boolean muat = false;
                            cekTempat:
                            for (int m = k; m <= (k + panjangBarang); m++) {
                                for (int n = l; n < (l + lebarBarang); n++) {
                                    if (container[m][n] == 0) {
                                        muat = true;
                                    } else {
                                        muat = false;
                                        break cekTempat;
                                    }
                                }
                            }
                            if (muat) {
                                for (int m = k; m <= (k + panjangBarang); m++) {
                                    for (int n = l; n < (l + lebarBarang); n++) {
                                        container[m][n] = idBarang+1;
                                    }
                                }
                                break selesai;
                            }
                        } else {
                            container[k][l] = -1;
                        }
                    }
                    if(k == container.length-1 && l == container[0].length-1){
                        cek = false;
                        break masuk;
                    }
                }
            }
        }
        if(!cek){
            for (int i = 0; i < container.length; i++) {
                for (int j = 0; j < container[i].length; j++) {
                    container[i][j] = 0;
                }
            }
        }
    }
    public static double hitungFitness(int[][] container) {
        int terluar = -1;
        int ruangkosong = 0;
        for (int i = 0; i < container.length; i++) {
            for (int j = 0; j < container[0].length; j++) {
                if (container[i][j] != 0 && j > terluar) {
                    terluar = i;
                }
            }
        }
        if(terluar>=0) {
            for (int i = 0; i < container.length; i++) {
                for (int j = 0; j <= terluar; j++) {
                    if (container[i][j] <= 0) {
                        ruangkosong++;
                    }
                }
            }
        }else{
            ruangkosong = container.length * container[0].length;
        }
        double fitness = 1.0 / ruangkosong;
        return fitness;
    }
    public static void urutkanFitness(double[][] data) {
        int indek_min;
        double tampungindex;
        double tampungfitnes;
        for (int i = 0; i < data.length; i++) {
            indek_min = i;
            for (int j = i + 1; j < data.length; j++) {
                if (data[j][1] > data[indek_min][1]) {
                    indek_min = j;
                }
            }
            if (indek_min != i) {
                tampungindex = data[indek_min][0];
                tampungfitnes = data[indek_min][1];

                data[indek_min][0] = data[i][0];
                data[indek_min][1] = data[i][1];

                data[i][0] = tampungindex;
                data[i][1] = tampungfitnes;
            }
        }
    }
    public static void tampilkanContainer(int panjang,int lebar,int [] individu,ArrayList<Barang> dataBarang){
        int [][] container = new int[panjang][lebar];
        masukkanBarang(container,individu,dataBarang);
        for (int i = 0; i < panjang; i++) {
            for (int j = 0; j < lebar; j++) {
                if(container[i][j]<0){
                    container[i][j]=0;
                }
                System.out.print(container[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        ArrayList<Barang> dataBarang = new ArrayList<>();
        dataBarang.add(new Barang(5, 5));
        dataBarang.add(new Barang(5, 10));
        dataBarang.add(new Barang(7, 7));
        dataBarang.add(new Barang(8, 8));
        dataBarang.add(new Barang(9, 2));


        int panjangContainer = 40;
        int lebarContainer = 20;

        int generasi = 20;
        int jumlahPopulasi = 20;
        int jumlahIndividuTerseleksi = 15;
        int panjangKromosom = dataBarang.size();
        double probMutasi = 0.5;
        int [] kromosomTerbaik = null;
        double fitnessterbaik = Double.MIN_VALUE;

        boolean bisa = cek(dataBarang,panjangContainer,lebarContainer);
        if(bisa) {
            int[][] populasiAwal = new int[jumlahPopulasi][panjangKromosom];
            //Membangkitkan Populasi Awal
            for (int i = 0; i < jumlahPopulasi; i++) {
                ArrayList<Integer> kromosom = new ArrayList<>();
                int index = 0;
                while (kromosom.size() < panjangKromosom) {
                    int nilai = ThreadLocalRandom.current().nextInt(0, panjangKromosom);

                    if (!kromosom.contains(nilai)) {
                        if (index >= panjangKromosom) {
                            index = 0;
                        }else {
                            kromosom.add(nilai);
                            populasiAwal[i][index] = nilai + 1;
                            index++;
                        }
                    }
                }
            }
            //Memasukkan Barang dan Menghitung Nilai fitness
            double[][] dataFitnessAwal = new double[jumlahPopulasi][2];
            for (int i = 0; i < jumlahPopulasi; i++) {
                int[][] container = new int[panjangContainer][lebarContainer];
                int[] individu = populasiAwal[i];
                masukkanBarang(container, individu, dataBarang);
                double fitness = hitungFitness(container);
                dataFitnessAwal[i][0] = i;
                dataFitnessAwal[i][1] = fitness;
            }
            //Mengurutkan Nilai Fitness
            urutkanFitness(dataFitnessAwal);
            int[][] populasi = new int[jumlahPopulasi][panjangKromosom];
            for (int i = 0; i < jumlahPopulasi; i++) {
                int index = (int) dataFitnessAwal[i][0];
                populasi[i] = populasiAwal[index];
            }

            //Proses elitism
            for (int i = 0; i < populasi.length; i++) {
                int[][] container = new int[panjangContainer][lebarContainer];
                int[] barang = populasi[i];
                masukkanBarang(container, barang, dataBarang);
                double fitness = hitungFitness(container);
                if (fitness > fitnessterbaik) {
                    kromosomTerbaik = barang;
                    fitnessterbaik = fitness;
                }
            }
            //Proses Evolusi
            for (int g = 1; g < generasi; g++) {
                int[][] populasiBaru = new int[jumlahPopulasi][panjangKromosom];

                //Seleksi Turnamen
                for (int i = 0; i < jumlahIndividuTerseleksi; i++) {
                    populasiBaru[i] = populasi[i];
                }
                //Melengkapi Populasi
                for (int i = jumlahIndividuTerseleksi; i < populasiBaru.length; i++) {
                    ArrayList<Integer> kromosom = new ArrayList<>();
                    int index = 0;
                    while (kromosom.size() < panjangKromosom) {
                        int nilai = ThreadLocalRandom.current().nextInt(0, panjangKromosom);
                        if (!kromosom.contains(nilai)) {
                            if (index >= panjangKromosom) {
                                index = 0;
                            }
                            if (index < panjangKromosom) {
                                kromosom.add(nilai);
                                populasiBaru[i][index] = nilai + 1;
                                index++;
                            }
                        }
                    }
                }

                //Mutasi
                double[][] dataFitnesBaru = new double[jumlahPopulasi][2];
                for (int i = 0; i < populasiBaru.length; i++) {
                    double random = Math.random();
                    if (random >= probMutasi) {
                        int index1 = ThreadLocalRandom.current().nextInt(0, panjangKromosom);
                        int index2 = ThreadLocalRandom.current().nextInt(0, panjangKromosom);
                        while (index1 == index2) {
                            int index = ThreadLocalRandom.current().nextInt(0, panjangKromosom);
                            if (index != index1) {
                                index2 = index;
                            }
                        }
                        //Swap Gen Yang Telah Dipilih
                        int[] individu = populasiBaru[i];
                        int temp = individu[index1];
                        individu[index1] = individu[index2];
                        individu[index2] = temp;
                    }
                    int[][] container = new int[panjangContainer][lebarContainer];
                    int[] barang = populasiBaru[i];
                    masukkanBarang(container, barang, dataBarang);
                    double fitness = hitungFitness(container);
                    dataFitnesBaru[i][0] = i;
                    dataFitnesBaru[i][1] = fitness;
                }

                urutkanFitness(dataFitnesBaru);//Mengurutkan Individu Berdasarkan Nilai Fitness

                //Mengurutkan Populasi Berdasarkan Urutan Data Nilai Fitness Yang telah Diurutkan
                populasi = new int[jumlahPopulasi][panjangKromosom];
                for (int i = 0; i < jumlahPopulasi; i++) {
                    int index = (int) dataFitnessAwal[i][0];
                    populasi[i] = populasiBaru[index];
                }

                for (int i = 0; i < populasi.length; i++) {
                    int[][] container = new int[panjangContainer][lebarContainer];
                    int[] barang = populasi[i];
                    masukkanBarang(container, barang, dataBarang);
                    double fitness = hitungFitness(container);
                    if (fitness > fitnessterbaik) {
                        kromosomTerbaik = barang;
                        fitnessterbaik = fitness;
                    }
                }
            }//End Of Evolusi
        }
        if(!bisa || fitnessterbaik == 1.0/(panjangContainer * lebarContainer)){
            System.out.println("Tidal Ada Solusi");
        }else if(bisa){
            System.out.println();
            System.out.println("=======================Solusi Terbaik Algoritma Genetika=======================");
            System.out.println("-------------------------------------------------------------------------------");
            tampilkanContainer(panjangContainer,lebarContainer,kromosomTerbaik,dataBarang);
            System.out.println("-------------------------------------------------------------------------------\n");
            System.out.println("Kromosom Terbaik \t: "+Arrays.toString(kromosomTerbaik));
            System.out.println("Fitness \t\t\t: "+fitnessterbaik);
        }



    }
}