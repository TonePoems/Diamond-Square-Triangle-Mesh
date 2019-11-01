package TriangleMesh;

/**
 *
 * @author Anthony
 */
public class DSAlgorithm {

    //variables to create the DS map
    //EXP: Preferred Pixelsize
    // 1: >55
    // 2: >33
    // 3: >20
    // 4: >10
    // 5: >5
    // 6: 
    // 7: 
    // 8: 
    // 9: Out of heap memory
    private int PIXELSIZE = 4; //size of each pixel on the map                                                       ~

    private int EXP = 8; // 1 - 10, to change for the size                                                            ~
    //                                            \/ this is the number, 1 - 9, to change for the size
    private int MAPSIZE = (int) Math.pow(2, EXP) + 1; //size of the map, sideLength is 2^x + 1                        

    /*
            1 2
            3 4
     */
    // 256 / 2 for flat gray seeds
    private double SEED1 = 256 / 2; //seeds for each corner                                                              ~
    private double SEED2 = 256 / 2; //seeds for each corner                                                              ~
    private double SEED3 = 256 / 2; //seeds for each corner                                                              ~
    private double SEED4 = 256 / 2; //seeds for each corner                                                              ~

    private double h; //roughness of the map, 0<h<1                                                                       ~

    //a,b get multiplied by h in each refinement step
    private double a; //initial min bound for the random range      
    private double b; //initial max bound for the random range                                                             ~

    private double[][] map; //create an array to hold the heights, 0 - 255

    public DSAlgorithm() {
        h = .5; //roughness of the map, 0<h<1                                                                       ~

        //a,b get multiplied by h in each refinement step
        a = -150; //initial min bound for the random range      
        b = 150; //initial max bound for the random range                                                             ~

        this.map = new double[MAPSIZE][MAPSIZE];

        map[0][0] = SEED1; //set the starting seeds
        map[0][MAPSIZE - 1] = SEED2; //set the starting seeds
        map[MAPSIZE - 1][0] = SEED3; //set the starting seeds
        map[MAPSIZE - 1][MAPSIZE - 1] = SEED4; //set the starting seeds
    }
    
    //overloaded constructor
    public DSAlgorithm(int a, int b, double h, int i, int pixelsize, double seed1, double seed2, double seed3, double seed4) {
        this.h = h; //roughness of the map, 0<h<1                                                                       ~

        //a,b get multiplied by h in each refinement step
        this.a = a; //initial min bound for the random range      
        this.b = b; //initial max bound for the random range    
        
        this.EXP = i;
        MAPSIZE = (int) Math.pow(2, EXP) + 1;
        
        this.PIXELSIZE = pixelsize;
        
        this.SEED1 = seed1;
        this.SEED2 = seed2;
        this.SEED3 = seed3;
        this.SEED4 = seed4;

        this.map = new double[MAPSIZE][MAPSIZE];

        map[0][0] = SEED1; //set the starting seeds
        map[0][MAPSIZE - 1] = SEED2; //set the starting seeds
        map[MAPSIZE - 1][0] = SEED3; //set the starting seeds
        map[MAPSIZE - 1][MAPSIZE - 1] = SEED4; //set the starting seeds
    }

    /**
     * Recursive function to fill in the height map
     *
     * @param TX
     * @param BX
     * @param TY
     * @param BY
     * @param a
     * @param b
     */
    public void fillMap(int TX, int BX, int TY, int BY, double a, double b) {
        //TL: Top Left
        //BR: Bottom Right
        //Top Y
        //Bottom Y

        //test the disply
//        for (int r = 0; r < MAPSIZE; r++) {
//            for (int c = 0; c < MAPSIZE; c++) {
//                map[r][c] = Math.random() * 255; //select random color
//            }
//        }

        if (BX - TX == 2) { //base case for this recursive function...
            //perform square step
            map[TY + 1][TX + 1] = (map[TY][TX] + map[TY][BX] + map[BY][TX] + map[BY][BX]) / 4 + (Math.random() * ((b - a) + 1)) + a;

            a = a * h; //refine range
            b = b * h; //refine range

            //perform diamond steps
            map[TY + 1][TX] = (map[TY][TX] + map[TY + 1][TX + 1] + map[BY][TX]) / 3 + (Math.random() * ((b - a) + 1)) + a;
            map[TY][TX + 1] = (map[TY][TX] + map[TY + 1][TX + 1] + map[TY][BX]) / 3 + (Math.random() * ((b - a) + 1)) + a;
            map[TY + 1][BX] = (map[TY][BX] + map[TY + 1][TX + 1] + map[BY][BX]) / 3 + (Math.random() * ((b - a) + 1)) + a;
            map[BY][TX + 1] = (map[BY][TX] + map[TY + 1][TX + 1] + map[BY][BX]) / 3 + (Math.random() * ((b - a) + 1)) + a;
        }
        else { //all other cases
            //perform square steps
            map[(TY + BY) / 2][(TX + BX) / 2] = (map[TY][TX] + map[TY][BX] + map[BY][TX] + map[BY][BX]) / 4 + (Math.random() * ((b - a) + 1)) + a;
            //System.out.println(map[(TL + BR) / 2][(TL + BR) / 2]);
            
            a = a * h; //refine range
            b = b * h; //refine range
            
            //perform diamond steps
            if (map[(TY + BY) / 2][TX] == 0)
                map[(TY + BY) / 2][TX] = (map[TY][TX] + map[(TY + BY) / 2][(TX + BX) / 2] + map[BY][TX]) / 3 + (Math.random() * ((b - a) + 1)) + a;
            //System.out.println(map[(TL + BR) / 2][TL]);
            if (map[TY][(TX + BX) / 2] == 0)
                map[TY][(TX + BX) / 2] = (map[TY][TX] + map[(TY + BY) / 2][(TX + BX) / 2] + map[TY][BX]) / 3 + (Math.random() * ((b - a) + 1)) + a;
            //System.out.println(map[TL][BR / 2]);
            if (map[(TY + BY) / 2][BX] == 0)
                map[(TY + BY) / 2][BX] = (map[TY][BX] + map[(TY + BY) / 2][(TX + BX) / 2] + map[BY][BX]) / 3 + (Math.random() * ((b - a) + 1)) + a;
            //System.out.println(map[(TL + BR) / 2][BR]);
            if (map[BY][(TX + BX) / 2] == 0)
                map[BY][(TX + BX) / 2] = (map[BY][TX] + map[(TY + BY) / 2][(TX + BX) / 2] + map[BY][BX]) / 3 + (Math.random() * ((b - a) + 1)) + a;
            //System.out.println(map[BR][(TL + BR) / 2]);
            
            a = a * h; //refine range
            b = b * h; //refine range
            
            //call with 4 smaller segments
            fillMap(TX, (TX + BX) / 2, TY, (TY + BY) / 2, a, b);
            fillMap((TX + BX) / 2, BX, TY, (TY + BY) / 2, a, b);
            fillMap((TX + BX) / 2, BX, (TY + BY) / 2, BY, a, b);
            fillMap(TX, (TX + BX)  / 2, (TY + BY)  / 2, BY, a, b);
        }
    }
    


    //getters
    public double[][] getMap() {
        return map;
    }

    public int getPIXELSIZE() {
        return PIXELSIZE;
    }

    public int getMAPSIZE() {
        return MAPSIZE;
    }

    public double getH() {
        return h;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getSEED(int a) {
        switch (a) {
            case 1:
                return SEED1;
            case 2:
                return SEED2;
            case 3:
                return SEED3;
            case 4:
                return SEED4;
        }
        return 0;
    }
    
    public int getEXP() {
        return EXP;
    }
}

/*
random colo generate.exe
*/