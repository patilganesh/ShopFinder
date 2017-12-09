package com.gajananmotors.shopfinder.model;

/**
 * Created by Gajanan Motars on 12/9/2017.
 */


public class Model {


        public static final int ADVERTISEMENT_TYPE=0;
        public static final int IMAGE_TYPE=1;


        public int type;
        public int data;
        public String text;



        public Model(int type, String text, int data)
        {
            this.type=type;
            this.data=data;
            this.text=text;

        }
}
