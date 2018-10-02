package com.example.ramshark.tombradar05;

public class DataDead {





        public String fname;
        public String lname;
        public String bdate;
        public String ddate;
        public String mname;
        public String area;
        public String blk;
        public String lot;
        public String lat;
        public String lang;
        public String grave_status;

    public DataDead() {
        this.lname = lname;
        this.fname = fname;

    }
    public String getName() {
        return lname+fname;

    }
    public void setName(String lname,String fname) {
        this.lname = lname;
        this.fname = fname;
    }
}
