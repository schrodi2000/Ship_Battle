package com.schrodi;

public class Output {
    int mapSize;

    public Output(int mapSize) {
        this.mapSize = mapSize;
    }

    public void draw(Player player) {
        System.out.print("  ");
        for(int i = 1; i<=mapSize;i++){

            System.out.print(" " + i);
        }
        System.out.println("");
        for (int iy = 0; iy < mapSize; iy++) {
            char buchstabe = (char) ('A' + iy);
            System.out.print(buchstabe + " ");
            for (int ix = 0; ix < mapSize; ix++) {
                switch (player.getMapKarteTeil(ix,iy)){

                    case aliveShip -> {
                        System.out.print('#');
                    }
                    case deadShip -> {
                        System.out.print('X');
                    }
                    case water -> {
                        System.out.print('-');
                    }
                    case miss -> {
                        System.out.print('~');
                    }
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }
}